/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.compress.archivers.zip;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ByteUtils;
import org.apache.commons.io.Charsets;

/**
 * Reimplementation of {@link java.util.zip.ZipOutputStream java.util.zip.ZipOutputStream} to handle the extended functionality of this package, especially
 * internal/external file attributes and extra fields with different layouts for local file data and central directory entries.
 * <p>
 * This class will try to use {@link java.nio.channels.SeekableByteChannel} when it knows that the output is going to go to a file and no split archive shall be
 * created.
 * </p>
 * <p>
 * If SeekableByteChannel cannot be used, this implementation will use a Data Descriptor to store size and CRC information for {@link #DEFLATED DEFLATED}
 * entries, you don't need to calculate them yourself. Unfortunately, this is not possible for the {@link #STORED STORED} method, where setting the CRC and
 * uncompressed size information is required before {@link #putArchiveEntry(ZipArchiveEntry)} can be called.
 * </p>
 * <p>
 * As of Apache Commons Compress 1.3, the class transparently supports Zip64 extensions and thus individual entries and archives larger than 4 GB or with more
 * than 65,536 entries in most cases but explicit control is provided via {@link #setUseZip64}. If the stream can not use SeekableByteChannel and you try to
 * write a ZipArchiveEntry of unknown size, then Zip64 extensions will be disabled by default.
 * </p>
 *
 * @NotThreadSafe
 */
public class ZipArchiveOutputStream extends ArchiveOutputStream<ZipArchiveEntry> {

    /**
     * Structure collecting information for the entry that is currently being written.
     */

    /**
     * enum that represents the possible policies for creating Unicode extra fields.
     */
    static final int BUFFER_SIZE = 512;
    private static final int LFH_SIG_OFFSET = 0;
    private static final int LFH_VERSION_NEEDED_OFFSET = 4;
    private static final int LFH_GPB_OFFSET = 6;
    private static final int LFH_METHOD_OFFSET = 8;
    private static final int LFH_TIME_OFFSET = 10;
    private static final int LFH_CRC_OFFSET = 14;
    private static final int LFH_COMPRESSED_SIZE_OFFSET = 18;
    private static final int LFH_ORIGINAL_SIZE_OFFSET = 22;
    private static final int LFH_FILENAME_LENGTH_OFFSET = 26;
    private static final int LFH_EXTRA_LENGTH_OFFSET = 28;
    private static final int LFH_FILENAME_OFFSET = 30;
    private static final int CFH_SIG_OFFSET = 0;
    private static final int CFH_VERSION_MADE_BY_OFFSET = 4;
    private static final int CFH_VERSION_NEEDED_OFFSET = 6;
    private static final int CFH_GPB_OFFSET = 8;
    private static final int CFH_METHOD_OFFSET = 10;
    private static final int CFH_TIME_OFFSET = 12;
    private static final int CFH_CRC_OFFSET = 16;
    private static final int CFH_COMPRESSED_SIZE_OFFSET = 20;
    private static final int CFH_ORIGINAL_SIZE_OFFSET = 24;
    private static final int CFH_FILENAME_LENGTH_OFFSET = 28;
    private static final int CFH_EXTRA_LENGTH_OFFSET = 30;
    private static final int CFH_COMMENT_LENGTH_OFFSET = 32;
    private static final int CFH_DISK_NUMBER_OFFSET = 34;
    private static final int CFH_INTERNAL_ATTRIBUTES_OFFSET = 36;

    private static final int CFH_EXTERNAL_ATTRIBUTES_OFFSET = 38;

    private static final int CFH_LFH_OFFSET = 42;

    private static final int CFH_FILENAME_OFFSET = 46;

    /**
     * Compression method for deflated entries.
     */
    public static final int DEFLATED = java.util.zip.ZipEntry.DEFLATED;

    /**
     * Default compression level for deflated entries.
     */
    public static final int DEFAULT_COMPRESSION = Deflater.DEFAULT_COMPRESSION;

    /**
     * Compression method for stored entries.
     */
    public static final int STORED = java.util.zip.ZipEntry.STORED;

    /**
     * Default encoding for file names and comment.
     */
    static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * General purpose flag, which indicates that file names are written in UTF-8.
     *
     * @deprecated use {@link GeneralPurposeBit#UFT8_NAMES_FLAG} instead
     */
    @Deprecated
    public static final int EFS_FLAG = GeneralPurposeBit.UFT8_NAMES_FLAG;

    /**
     * Helper, a 0 as ZipShort.
     */
    private static final byte[] ZERO = { 0, 0 };

    /**
     * Helper, a 0 as ZipLong.
     */
    private static final byte[] LZERO = { 0, 0, 0, 0 };

    private static final byte[] ONE = ZipLong.getBytes(1L);

    /*
     * Various ZIP constants shared between this class, ZipArchiveInputStream and ZipFile
     */
    /**
     * local file header signature
     */
    static final byte[] LFH_SIG = ZipLong.LFH_SIG.getBytes(); // NOSONAR

    /**
     * data descriptor signature
     */
    static final byte[] DD_SIG = ZipLong.DD_SIG.getBytes(); // NOSONAR

    /**
     * central file header signature
     */
    static final byte[] CFH_SIG = ZipLong.CFH_SIG.getBytes(); // NOSONAR

    /**
     * end of central dir signature
     */
    static final byte[] EOCD_SIG = ZipLong.getBytes(0X06054B50L); // NOSONAR

    /**
     * ZIP64 end of central dir signature
     */
    static final byte[] ZIP64_EOCD_SIG = ZipLong.getBytes(0X06064B50L); // NOSONAR

    /**
     * ZIP64 end of central dir locator signature
     */
    static final byte[] ZIP64_EOCD_LOC_SIG = ZipLong.getBytes(0X07064B50L); // NOSONAR

    /** Indicates if this archive is finished. protected for use in Jar implementation */
    protected boolean finished;

    /**
     * Current entry.
     */
    public ZipArchiveEntry entry;

    /**
     * The file comment.
     */
    private String comment = "";

    /**
     * Compression level for next entry.
     */
    private int level = DEFAULT_COMPRESSION;

    /**
     * Has the compression level changed when compared to the last entry?
     */
    private boolean hasCompressionLevelChanged;

    /**
     * Default compression method for next entry.
     */
    private int method = java.util.zip.ZipEntry.DEFLATED;

    /**
     * List of ZipArchiveEntries written so far.
     */
    private final List<ZipArchiveEntry> entries = new LinkedList<>();

    public final StreamCompressor streamCompressor;

    /**
     * Start of central directory.
     */
    private long cdOffset;

    /**
     * Length of central directory.
     */
    private long cdLength;

    /**
     * Disk number start of central directory.
     */
    private long cdDiskNumberStart;

    /**
     * Length of end of central directory
     */
    private long eocdLength;

    /**
     * Holds some book-keeping data for each entry.
     */
     /**
     * The encoding to use for file names and the file comment.
     *
     * <p>
     * For a list of possible values see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">Supported Encodings</a>.
     * Defaults to UTF-8.
     * </p>
     */
    private Charset charset = DEFAULT_CHARSET;

    /**
     * The ZIP encoding to use for file names and the file comment.
     *
     * This field is of internal use and will be set in {@link #setEncoding(String)}.
     */
    private CharsetEncoder zipEncoding = DEFAULT_CHARSET.newEncoder();//ZipEncodingHelper.getZipEncoding(DEFAULT_CHARSET);

    /**
     * This Deflater object is used for output.
     */
    protected final Deflater def;

    private final OutputStream outputStream;

    /**
     * whether to use the general purpose bit flag when writing UTF-8 file names or not.
     */
    private boolean useUTF8Flag = true;

    /**
     * Whether to encode non-encodable file names as UTF-8.
     */
    private final boolean fallbackToUTF8= true;

    /**
     * whether to create UnicodePathExtraField-s for each entry.
     */
    /**
     * Whether anything inside this archive has used a ZIP64 feature.
     *
     * @since 1.3
     */
    private boolean hasUsedZip64;

    private Zip64Mode zip64Mode = Zip64Mode.Never;
    //Zip64Mode.AsNeeded;

    public final byte[] copyBuffer = new byte[32768];

    /**
     * Whether we are creating a split zip
     */
    private final boolean isSplitZip;

    /**
     * Holds the number of Central Directories on each disk, this is used when writing Zip64 End Of Central Directory and End Of Central Directory
     */
    private final Map<Integer, Integer> numberOfCDInDiskData = new HashMap<>();

    /**
     * Creates a new ZIP OutputStream writing to a File. Will use random access if possible.
     *
     * @param file the file to ZIP to
     * @throws IOException on error
     */
    public ZipArchiveOutputStream(final File file) throws Exception {
        this(new BufferedOutputStream(new FileOutputStream(file)));
    }

    /**
     * Creates a split ZIP Archive.
     *
     * <p>
     * The files making up the archive will use Z01, Z02, ... extensions and the last part of it will be the given {@code
     * file}.
     * </p>
     *
     * <p>
     * Even though the stream writes to a file this stream will behave as if no random access was possible. This means the sizes of stored entries need to be
     * known before the actual entry data is written.
     * </p>
     *
     * @param file         the file that will become the last part of the split archive
     * @param zipSplitSize maximum size of a single part of the split archive created by this stream. Must be between 64kB and about 4GB.
     *
     * @throws IOException              on error
     * @throws IllegalArgumentException if zipSplitSize is not in the required range
     * @since 1.20
     
    public ZipArchiveOutputStream(final File file, final long zipSplitSize) throws IOException {
        this(file.toPath(), zipSplitSize);
    }

    /**
     * Creates a new ZIP OutputStream filtering the underlying stream.
     *
     * @param out the outputstream to zip
     */
    public ZipArchiveOutputStream(final OutputStream out) {
        this.outputStream = out;
        this.def = new Deflater(level, true);
        this.streamCompressor = StreamCompressor.create(out, def);
        this.isSplitZip = false;
    }

    /**
     * Creates a split ZIP Archive.
     * <p>
     * The files making up the archive will use Z01, Z02, ... extensions and the last part of it will be the given {@code
     * file}.
     * </p>
     * <p>
     * Even though the stream writes to a file this stream will behave as if no random access was possible. This means the sizes of stored entries need to be
     * known before the actual entry data is written.
     * </p>
     *
     * @param path         the path to the file that will become the last part of the split archive
     * @param zipSplitSize maximum size of a single part of the split archive created by this stream. Must be between 64kB and about 4GB.
     * @throws IOException              on error
     * @throws IllegalArgumentException if zipSplitSize is not in the required range
     * @since 1.22
     
    public ZipArchiveOutputStream(final Path path, final long zipSplitSize) throws IOException {
        this.def = new Deflater(level, true);
        this.outputStream = new ZipSplitOutputStream(path, zipSplitSize);
        this.streamCompressor = StreamCompressor.create(this.outputStream, def);
        this.isSplitZip = true;
    }

    /**
     * Creates a new ZIP OutputStream writing to a Path. Will use random access if possible.
     *
     * @param file    the file to ZIP to
     * @param options options specifying how the file is opened.
     * @throws IOException on error
     * @since 1.21
    public ZipArchiveOutputStream(final Path file, final OpenOption... options) throws IOException {
        this.def = new Deflater(level, true);
        this.outputStream = options.length == 0 ? new FileRandomAccessOutputStream(file) : new FileRandomAccessOutputStream(file, options);
        this.streamCompressor = StreamCompressor.create(outputStream, def);
        this.isSplitZip = false;
    }

    *
     * Creates a new ZIP OutputStream writing to a SeekableByteChannel.
     *
     * <p>
     * {@link org.apache.commons.compress.utils.SeekableInMemoryByteChannel} allows you to write to an in-memory archive using random access.
     * </p>
     *
     * @param channel the channel to ZIP to
     * @since 1.13
     
    public ZipArchiveOutputStream(final SeekableByteChannel channel) {
        this.outputStream = new SeekableChannelRandomAccessOutputStream(channel);
        this.def = new Deflater(level, true);
        this.streamCompressor = StreamCompressor.create(outputStream, def);
        this.isSplitZip = false;
    }

    /**
     * Adds an archive entry with a raw input stream.
     * <p>
     * If crc, size and compressed size are supplied on the entry, these values will be used as-is. Zip64 status is re-established based on the settings in this
     * stream, and the supplied value is ignored.
     * </p>
     * <p>
     * The entry is put and closed immediately.
     * </p>
     *
     * @param entry     The archive entry to add
     * @param rawStream The raw input stream of a different entry. May be compressed/encrypted.
     * @throws IOException If copying fails
     */
    public void addRawArchiveEntry(ZipArchiveEntry ae,InputStream raw) throws Exception{
    int size=raw.read(copyBuffer);
    if(size>0)addRawArchiveEntry(ae,raw,size,Integer.MAX_VALUE);
    }
    public void addRawArchiveEntry(final ZipArchiveEntry ae, final InputStream rawStream,int length,int boomlen)throws Exception{
        /*if (hasZip64Extra(ae)) {
            // Will be re-added as required. this may make the file generated with this method
            // somewhat smaller than standard mode,
            // since standard mode is unable to remove the ZIP 64 header.
            ae.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        }*/
        /*ae.getCrc() != ZipArchiveEntry.CRC_UNKNOWN && ae.getSize() != ArchiveEntry.SIZE_UNKNOWN
                && ae.getCompressedSize() != ArchiveEntry.SIZE_UNKNOWN;*/
        int size=0;
        boolean store=ae.getMethod()==0&&!(this instanceof zipOut);
        byte arr[]=copyBuffer;
        putArchiveEntry(ae);
        try{
        do{
        size+=length;
        if(!store){
        streamCompressor.writeCounted(arr, 0, length);
        count(length);
        }else write(arr,0,length);
        length=0;
        }while ((length=rawStream.read(arr))>0&&size<boomlen);
        }catch(Exception e){
        if(length>0)throw e;//如果出现写出错误，那么抛出。
        }
        if(store)closeArchiveEntry();
    }

    /**
     * Adds UnicodeExtra fields for name and file comment if mode is ALWAYS or the data cannot be encoded using the configured encoding.
     */
     /*
    private void addUnicodeExtraFields(final ZipArchiveEntry ze, final boolean encodable, final ByteBuffer name) throws IOException {
        if (!encodable) {
            ze.addExtraField(new UnicodePathExtraField(ze.getName(), name.array(), name.arrayOffset(), name.limit() - name.position()));
        }

       /* final String comm = ze.getComment();
        if (comm != null && !comm.isEmpty()) {

            final boolean commentEncodable = zipEncoding.canEncode(comm);

            if (createUnicodeExtraFields == UnicodeExtraFieldPolicy.ALWAYS || !commentEncodable) {
                final ByteBuffer commentB = getEntryEncoding(ze).encode(comm);
                ze.addExtraField(new UnicodeCommentExtraField(comm, commentB.array(), commentB.arrayOffset(), commentB.limit() - commentB.position()));
            }
        }
    }*/

    /**
     * Whether this stream is able to write the given entry.
     * <p>
     * May return false if it is set up to use encryption or a compression method that hasn't been implemented yet.
     * </p>
     *
     * @since 1.1
     */
    @Override
   public boolean canWriteEntryData(final ArchiveEntry ae) {
        if (ae instanceof ZipArchiveEntry) {
            final ZipArchiveEntry zae = (ZipArchiveEntry) ae;
            return zae.getMethod() != ZipMethod.IMPLODING.getCode() && zae.getMethod() != ZipMethod.UNSHRINKING.getCode() && ZipUtil.canHandleEntryData(zae);
        }
        return false;
    }

    /**
     * Verifies the sizes aren't too big in the Zip64Mode.Never case and returns whether the entry would require a Zip64 extra field.
     */
    private boolean checkIfNeedsZip64(final Zip64Mode effectiveMode) throws ZipException {
        final boolean actuallyNeedsZip64 = isZip64Required(entry, effectiveMode);
        if (actuallyNeedsZip64 && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(entry));
        }
        return actuallyNeedsZip64;
    }

    /**
     * Closes this output stream and releases any system resources associated with the stream.
     *
     * @throws IOException            if an I/O error occurs.
     * @throws Zip64RequiredException if the archive's size exceeds 4 GByte or there are more than 65535 entries inside the archive and {@link #setUseZip64} is
     *                                {@link Zip64Mode#Never}.
     */
    @Override
    public void close() throws IOException {
        try {
            if (!finished) {
                finish();
            }
        } finally {
            destroy();
        }
    }

    /**
     * Writes all necessary data for this entry.
     *
     * @throws IOException            on error
     * @throws Zip64RequiredException if the entry's uncompressed or compressed size exceeds 4 GByte and {@link #setUseZip64} is {@link Zip64Mode#Never}.
     */
    @Override
    public void closeArchiveEntry() throws IOException {
       // preClose();

        flushDeflater();

        final long bytesWritten = streamCompressor.getTotalBytesWritten() - entry.getDataOffset();
        final long realCrc = streamCompressor.getCrc32();
        final boolean actuallyNeedsZip64 = handleSizesAndCrc(bytesWritten, realCrc);
        streamCompressor.reset();
    }

    /**
     * Writes all necessary data for this entry.
     *
     * @param phased This entry is second phase of a 2-phase ZIP creation, size, compressed size and crc are known in ZipArchiveEntry
     * @throws IOException            on error
     * @throws Zip64RequiredException if the entry's uncompressed or compressed size exceeds 4 GByte and {@link #setUseZip64} is {@link Zip64Mode#Never}.
     */
    public void closeCopiedEntry(final boolean phased) throws IOException {
       // preClose();
      /*  final Zip64Mode effectiveMode = getEffectiveZip64Mode(entry);
        final boolean actuallyNeedsZip64 = checkIfNeedsZip64(effectiveMode);*/
        closeEntry(false, phased);
    }

    public void closeEntry(final boolean actuallyNeedsZip64, final boolean phased) throws IOException {
       /* if (!phased && outputStream instanceof RandomAccessOutputStream) {
            rewriteSizesAndCrc(actuallyNeedsZip64);
        }

      if (!phased) {
            writeDataDescriptor(entry);
        }*/
        entry = null;
    }

    /**
     * Creates a new ZIP entry taking some information from the given file and using the provided name.
     * <p>
     * The name will be adjusted to end with a forward slash "/" if the file is a directory. If the file is not a directory a potential trailing forward slash
     * will be stripped from the entry name.
     * </p>
     * <p>
     * Must not be used if the stream has already been closed.
     * </p>
     */
    @Override
    public ZipArchiveEntry createArchiveEntry(final File inputFile, final String entryName) throws IOException {
       /* if (finished) {
            throw new IOException("Stream has already been finished");
        }*/
        return new ZipArchiveEntry(inputFile, entryName);
    }

    /**
     * Creates a new ZIP entry taking some information from the given file and using the provided name.
     * <p>
     * The name will be adjusted to end with a forward slash "/" if the file is a directory. If the file is not a directory a potential trailing forward slash
     * will be stripped from the entry name.
     * </p>
     * <p>
     * Must not be used if the stream has already been closed.
     * </p>
     *
     * @param inputPath path to create the entry from.
     * @param entryName name of the entry.
     * @param options   options indicating how symbolic links are handled.
     * @return a new instance.
     * @throws IOException if an I/O error occurs.
     * @since 1.21
     */
    @Override
    public ZipArchiveEntry createArchiveEntry(final Path inputPath, final String entryName, final LinkOption... options) throws IOException {
      /*  if (finished) {
            throw new IOException("Stream has already been finished");
        }*/
        return new ZipArchiveEntry(inputPath, entryName);
    }

    private byte[] createCentralFileHeader(final ZipArchiveEntry ze) throws IOException {

       /*    final boolean needsZip64Extra = hasZip64Extra(ze) || ze.getCompressedSize() >= ZipConstants.ZIP64_MAGIC || ze.getSize() >= ZipConstants.ZIP64_MAGIC
                || entryMetaData.offset >= ZipConstants.ZIP64_MAGIC || ze.getDiskNumberStart() >= ZipConstants.ZIP64_MAGIC_SHORT
                || zip64Mode == Zip64Mode.Always || zip64Mode == Zip64Mode.AlwaysWithCompatibility;

        if (needsZip64Extra && zip64Mode == Zip64Mode.Never) {
            // must be the offset that is too big, otherwise an
            // exception would have been throw in putArchiveEntry or
            // closeArchiveEntry
            throw new Zip64RequiredException(Zip64RequiredException.ARCHIVE_TOO_BIG_MESSAGE);
        }

        handleZip64Extra(ze, entryMetaData.offset, needsZip64Extra);*/

        return createCentralFileHeader(ze, getName(ze), false);
    }

    /**
     * Writes the central file header entry.
     *
     * @param ze            the entry to write
     * @param name          The encoded name
     * @param entryMetaData meta data for this file
     * @throws IOException on error
     */
    private byte[] createCentralFileHeader(final ZipArchiveEntry ze, final ByteBuffer name, final boolean needsZip64Extra)
            throws IOException {
       /* if (isSplitZip) {
            // calculate the disk number for every central file header,
            // this will be used in writing End Of Central Directory and Zip64 End Of Central Directory
            final int currentSplitSegment = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex();
            if (numberOfCDInDiskData.get(currentSplitSegment) == null) {
                numberOfCDInDiskData.put(currentSplitSegment, 1);
            } else {
                final int originalNumberOfCD = numberOfCDInDiskData.get(currentSplitSegment);
                numberOfCDInDiskData.put(currentSplitSegment, originalNumberOfCD + 1);
            }
        }*/

        final byte[] extra = new byte[0];
        final int extraLength = extra.length;

        /* file comment length
        String comm = ze.getComment();
        if (comm == null) {
            comm = "";
        }*/

        final ByteBuffer commentB = em_name;
        final int nameLen = name.limit() - name.position();
        final int commentLen = 0;
        final int len = CFH_FILENAME_OFFSET + nameLen + extraLength + commentLen;
        final byte[] buf = new byte[len];

        System.arraycopy(CFH_SIG, 0, buf, CFH_SIG_OFFSET, ZipConstants.WORD);

        // version made by
        // CheckStyle:MagicNumber OFF
        ZipShort.putShort(ze.getPlatform() << 8 | (!hasUsedZip64 ? ZipConstants.DATA_DESCRIPTOR_MIN_VERSION : ZipConstants.ZIP64_MIN_VERSION), buf,
                CFH_VERSION_MADE_BY_OFFSET);

        final int zipMethod = ze.getMethod();
        final boolean encodable = zipEncoding.canEncode(ze.getName());
        ZipShort.putShort(versionNeededToExtract(zipMethod, needsZip64Extra, false), buf, CFH_VERSION_NEEDED_OFFSET);
        getGeneralPurposeBits(!encodable,false/*&& fallbackToUTF8,entryMetaData.usesDataDescriptor*/).encode(buf, CFH_GPB_OFFSET);

        // compression method
        ZipShort.putShort(zipMethod, buf, CFH_METHOD_OFFSET);

        // last mod. time and date
        ZipUtil.toDosTime(ze.getTime(), buf, CFH_TIME_OFFSET);

        // CRC
        // compressed length
        // uncompressed length
        ZipLong.putLong(ze.getCrc(), buf, CFH_CRC_OFFSET);
        if (ze.getCompressedSize() >= ZipConstants.ZIP64_MAGIC || ze.getSize() >= ZipConstants.ZIP64_MAGIC || zip64Mode == Zip64Mode.Always
                || zip64Mode == Zip64Mode.AlwaysWithCompatibility) {
            ZipLong.ZIP64_MAGIC.putLong(buf, CFH_COMPRESSED_SIZE_OFFSET);
            ZipLong.ZIP64_MAGIC.putLong(buf, CFH_ORIGINAL_SIZE_OFFSET);
        } else {
            ZipLong.putLong(ze.getCompressedSize(), buf, CFH_COMPRESSED_SIZE_OFFSET);
            ZipLong.putLong(ze.getSize(), buf, CFH_ORIGINAL_SIZE_OFFSET);
        }

        ZipShort.putShort(nameLen, buf, CFH_FILENAME_LENGTH_OFFSET);

        // extra field length
        ZipShort.putShort(extraLength, buf, CFH_EXTRA_LENGTH_OFFSET);

        ZipShort.putShort(commentLen, buf, CFH_COMMENT_LENGTH_OFFSET);

        // disk number start
      /*  if (isSplitZip) {
            if (ze.getDiskNumberStart() >= ZipConstants.ZIP64_MAGIC_SHORT || zip64Mode == Zip64Mode.Always) {
                ZipShort.putShort(ZipConstants.ZIP64_MAGIC_SHORT, buf, CFH_DISK_NUMBER_OFFSET);
            } else {
                ZipShort.putShort((int) ze.getDiskNumberStart(), buf, CFH_DISK_NUMBER_OFFSET);
            }
        } else {*/
            System.arraycopy(ZERO, 0, buf, CFH_DISK_NUMBER_OFFSET, ZipConstants.SHORT);
      //  }

        // internal file attributes
        ZipShort.putShort(ze.getInternalAttributes(), buf, CFH_INTERNAL_ATTRIBUTES_OFFSET);

        // external file attributes
        ZipLong.putLong(ze.getExternalAttributes(), buf, CFH_EXTERNAL_ATTRIBUTES_OFFSET);

        // relative offset of LFH
       /* if (entryMetaData.offset >= ZipConstants.ZIP64_MAGIC || zip64Mode == Zip64Mode.Always) {
            ZipLong.putLong(ZipConstants.ZIP64_MAGIC, buf, CFH_LFH_OFFSET);
        } else {*/
            ZipLong.putLong(Math.min(ze.getLocalHeaderOffset()-headOff, ZipConstants.ZIP64_MAGIC), buf, CFH_LFH_OFFSET);
     //   }

        // file name
        System.arraycopy(name.array(), name.arrayOffset(), buf, CFH_FILENAME_OFFSET, nameLen);

        final int extraStart = CFH_FILENAME_OFFSET + nameLen;
        System.arraycopy(extra, 0, buf, extraStart, extraLength);

        final int commentStart = extraStart + extraLength;

        // file comment
        System.arraycopy(commentB.array(), commentB.arrayOffset(), buf, commentStart, commentLen);
        return buf;
    }

    private byte[] createLocalFileHeader(final ZipArchiveEntry ze, final ByteBuffer name, final boolean encodable, final boolean phased,
            final long archiveOffset) {
      /*  final ZipExtraField oldEx = ze.getExtraField(ResourceAlignmentExtraField.ID);
        if (oldEx != null) {
            ze.removeExtraField(ResourceAlignmentExtraField.ID);
        }
        final ResourceAlignmentExtraField oldAlignmentEx = oldEx instanceof ResourceAlignmentExtraField ? (ResourceAlignmentExtraField) oldEx : null;

        int alignment = ze.getAlignment();
        if (alignment <= 0 && oldAlignmentEx != null) {
            alignment = oldAlignmentEx.getAlignment();
        }

        if (alignment > 1 || oldAlignmentEx != null && !oldAlignmentEx.allowMethodChange()) {
            final int oldLength = LFH_FILENAME_OFFSET + name.limit() - name.position() + ze.getLocalFileDataExtra().length;

            final int padding = (int) (-archiveOffset - oldLength - ZipExtraField.EXTRAFIELD_HEADER_SIZE - ResourceAlignmentExtraField.BASE_SIZE
                    & alignment - 1);
            ze.addExtraField(new ResourceAlignmentExtraField(alignment, oldAlignmentEx != null && oldAlignmentEx.allowMethodChange(), padding));
        }
*/
        final byte[] extra = new byte[0];
        final int nameLen = name.limit() - name.position();
        final int len = LFH_FILENAME_OFFSET + nameLen + extra.length;
        final byte[] buf = new byte[len];

        System.arraycopy(LFH_SIG, 0, buf, LFH_SIG_OFFSET, ZipConstants.WORD);

        // store method in local variable to prevent multiple method calls
        final int zipMethod = ze.getMethod();
        final boolean dataDescriptor = false;
        //usesDataDescriptor(zipMethod, phased);

        ZipShort.putShort(versionNeededToExtractMethod(zipMethod), buf, LFH_VERSION_NEEDED_OFFSET);

        final GeneralPurposeBit generalPurposeBit = getGeneralPurposeBits(!encodable,false/* && fallbackToUTF8, dataDescriptor*/);
        generalPurposeBit.useEncryption(true);
        generalPurposeBit.encode(buf, LFH_GPB_OFFSET);

        // compression method
        ZipShort.putShort(zipMethod, buf, LFH_METHOD_OFFSET);

        ZipLong.putLong(0, buf, LFH_TIME_OFFSET);

        // CRC
        ZipLong.putLong(0, buf, LFH_CRC_OFFSET);
        /*
        if (phased || !(zipMethod == DEFLATED || outputStream instanceof RandomAccessOutputStream)) {
            ZipLong.putLong(ze.getCrc(), buf, LFH_CRC_OFFSET);
        } else {
            System.arraycopy(LZERO, 0, buf, LFH_CRC_OFFSET, ZipConstants.WORD);
        }*/

        // compressed length
        // uncompressed length
       /* if (hasZip64Extra(entry)) {
            // point to ZIP64 extended information extra field for
            // sizes, may get rewritten once sizes are known if
            // stream is seekable
            ZipLong.ZIP64_MAGIC.putLong(buf, LFH_COMPRESSED_SIZE_OFFSET);
            ZipLong.ZIP64_MAGIC.putLong(buf, LFH_ORIGINAL_SIZE_OFFSET);
        } else if (phased) {*/
            ZipLong.putLong(0, buf, LFH_COMPRESSED_SIZE_OFFSET);
            ZipLong.putLong(0, buf, LFH_ORIGINAL_SIZE_OFFSET);
        /*} else if (zipMethod == DEFLATED || outputStream instanceof RandomAccessOutputStream) {
            System.arraycopy(LZERO, 0, buf, LFH_COMPRESSED_SIZE_OFFSET, ZipConstants.WORD);
            System.arraycopy(LZERO, 0, buf, LFH_ORIGINAL_SIZE_OFFSET, ZipConstants.WORD);
        } else { // Stored
            ZipLong.putLong(ze.getSize(), buf, LFH_COMPRESSED_SIZE_OFFSET);
            ZipLong.putLong(ze.getSize(), buf, LFH_ORIGINAL_SIZE_OFFSET);
        }*/
        // file name length
        ZipShort.putShort(nameLen, buf, LFH_FILENAME_LENGTH_OFFSET);

        // extra field length
        ZipShort.putShort(extra.length, buf, LFH_EXTRA_LENGTH_OFFSET);

        // file name
        System.arraycopy(name.array(), name.arrayOffset(), buf, LFH_FILENAME_OFFSET, nameLen);

        // extra fields
        System.arraycopy(extra, 0, buf, LFH_FILENAME_OFFSET + nameLen, extra.length);

        return buf;
    }

    /**
     * Writes next block of compressed data to the output stream.
     *
     * @throws IOException on error
     */
    protected final void deflate() throws IOException {
        streamCompressor.deflate();
    }

    /**
     * Closes the underlying stream/file without finishing the archive, the result will likely be a corrupt archive.
     * <p>
     * This method only exists to support tests that generate corrupt archives so they can clean up any temporary files.
     * </p>
     */
    void destroy() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws Zip64RequiredException if the archive's size exceeds 4 GByte or there are more than 65535 entries inside the archive and {@link #setUseZip64} is
     *                                {@link Zip64Mode#Never}.
     */
    @Override
    public void finish() throws IOException {
      /*  if (finished) {
            throw new IOException("This archive has already been finished");
        }

        if (entry != null) {
            throw new IOException("This archive contains unclosed entries.");
        }*/

        long cdOverallOffset = streamCompressor.getTotalBytesWritten();
        cdOffset = cdOverallOffset-headOff;
        /*if (isSplitZip) {
            // when creating a split zip, the offset should be
            // the offset to the corresponding segment disk
            final ZipSplitOutputStream zipSplitOutputStream = (ZipSplitOutputStream) this.outputStream;
            cdOffset = zipSplitOutputStream.getCurrentSplitSegmentBytesWritten();
            cdDiskNumberStart = zipSplitOutputStream.getCurrentSplitSegmentIndex();
        }*/
        writeCentralDirectoryInChunks();

        cdLength = streamCompressor.getTotalBytesWritten() - cdOverallOffset;
        // calculate the length of end of central directory, as it may be used in writeZip64CentralDirectory
        final ByteBuffer commentData = DEFAULT_CHARSET.encode(comment);
        final long commentLength = (long) commentData.limit() - commentData.position();
        eocdLength = ZipConstants.WORD /* length of EOCD_SIG */
                + ZipConstants.SHORT /* number of this disk */
                + ZipConstants.SHORT /* disk number of start of central directory */
                + ZipConstants.SHORT /* total number of entries on this disk */
                + ZipConstants.SHORT /* total number of entries */
                + ZipConstants.WORD /* size of central directory */
                + ZipConstants.WORD /* offset of start of central directory */
                + ZipConstants.SHORT /* ZIP comment length */
                + commentLength /* ZIP comment */;

        //writeZip64CentralDirectory();
        writeCentralDirectoryEnd();
        entries.clear();
        streamCompressor.close();
      /*  if (isSplitZip) {
            // trigger the ZipSplitOutputStream to write the final split segment
            outputStream.close();
        }*/
        finished = true;
    }

    /**
     * Flushes this output stream and forces any buffered output bytes to be written out to the stream.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
        }
    }

    /**
     * Ensures all bytes sent to the deflater are written to the stream.
     */
    private void flushDeflater() throws IOException {
        if (entry.getMethod() == DEFLATED) {
            streamCompressor.flushDeflater();
        }
    }

    /**
     * Returns the total number of bytes written to this stream.
     *
     * @return the number of written bytes
     * @since 1.22
     */
    @Override
    public long getBytesWritten() {
        return streamCompressor.getTotalBytesWritten();
    }

    /**
     * If the mode is AsNeeded and the entry is a compressed entry of unknown size that gets written to a non-seekable stream then change the default to Never.
     *
     * @since 1.3
     */
    private Zip64Mode getEffectiveZip64Mode(final ZipArchiveEntry ze) {
        if (zip64Mode != Zip64Mode.AsNeeded || outputStream instanceof RandomAccessOutputStream ||
                ze.getMethod() != DEFLATED || ze.getSize() != ArchiveEntry.SIZE_UNKNOWN) {
            return zip64Mode;
        }
        return Zip64Mode.Never;
    }

    /**
     * The encoding to use for file names and the file comment.
     *
     * @return null if using the platform's default character encoding.
     */
  public int headOff;
    public String getEncoding() {
        return charset != null ? charset.name() : null;
    }
    private Charset getEntryEncoding(boolean can) {
        return can?charset:DEFAULT_CHARSET;
    }

    private GeneralPurposeBit getGeneralPurposeBits(final boolean utfFallback, final boolean usesDataDescriptor) {
        final GeneralPurposeBit b = new GeneralPurposeBit();
        b.useUTF8ForNames(useUTF8Flag || utfFallback);
       /* if (usesDataDescriptor) {
            b.useDataDescriptor(true);
        }*/
        return b;
    }

    private ByteBuffer getName(final ZipArchiveEntry ze) throws IOException {
    String str=ze.getName();
    if(zipEncoding.canEncode(str))return charset.encode(str);
    return DEFAULT_CHARSET.encode(str);
    }

    /**
     * Gets the existing ZIP64 extended information extra field or create a new one and add it to the entry.
     *
     * @since 1.3
     */
    private Zip64ExtendedInformationExtraField getZip64Extra(final ZipArchiveEntry ze) {
      /*  if (entry != null) {
            entry.causedUseOfZip64 = !hasUsedZip64;
        }*/
        hasUsedZip64 = true;
        final ZipExtraField extra = ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
        Zip64ExtendedInformationExtraField z64 = extra instanceof Zip64ExtendedInformationExtraField ? (Zip64ExtendedInformationExtraField) extra : null;
        if (z64 == null) {
            /*
             * System.err.println("Adding z64 for " + ze.getName() + ", method: " + ze.getMethod() + " (" + (ze.getMethod() == STORED) + ")" + ", channel: " +
             * (channel != null));
             */
            z64 = new Zip64ExtendedInformationExtraField();
        }

        // even if the field is there already, make sure it is the first one
        ze.addAsFirstExtraField(z64);

        return z64;
    }

    /**
     * Ensures the current entry's size and CRC information is set to the values just written, verifies it isn't too big in the Zip64Mode.Never case and returns
     * whether the entry would require a Zip64 extra field.
     */
    private boolean handleSizesAndCrc(final long bytesWritten, final long crc){
      /*  if (entry.getMethod() == DEFLATED) {
            /*
             * It turns out def.getBytesRead() returns wrong values if the size exceeds 4 GB on Java < Java7 entry.setSize(def.getBytesRead());
            entry.setSize(entry.bytesRead);
            entry.setCompressedSize(bytesWritten);
            entry.setCrc(crc);

        } else if (!(outputStream instanceof RandomAccessOutputStream)) {
            if (entry.getCrc() != crc) {
                throw new ZipException("Bad CRC checksum for entry " + entry.getName() + ": " + Long.toHexString(entry.getCrc()) + " instead of "
                        + Long.toHexString(crc));
            }

            if (entry.getSize() != bytesWritten) {
                throw new ZipException("Bad size for entry " + entry.getName() + ": " + entry.getSize() + " instead of " + bytesWritten);
            }
        } else { method is STORED and we used SeekableByteChannel */
            entry.setSize(bytesWritten);
            entry.setCompressedSize(bytesWritten);
            entry.setCrc(crc);
       // }

        return false;
        //checkIfNeedsZip64(effectiveMode);
    }

    /**
     * If the entry needs Zip64 extra information inside the central directory then configure its data.
     */
    private void handleZip64Extra(final ZipArchiveEntry ze, final long lfhOffset, final boolean needsZip64Extra) {
        if (needsZip64Extra) {
            final Zip64ExtendedInformationExtraField z64 = getZip64Extra(ze);
            if (ze.getCompressedSize() >= ZipConstants.ZIP64_MAGIC || ze.getSize() >= ZipConstants.ZIP64_MAGIC || zip64Mode == Zip64Mode.Always
                    || zip64Mode == Zip64Mode.AlwaysWithCompatibility) {
                z64.setCompressedSize(new ZipEightByteInteger(ze.getCompressedSize()));
                z64.setSize(new ZipEightByteInteger(ze.getSize()));
            } else {
                // reset value that may have been set for LFH
                z64.setCompressedSize(null);
                z64.setSize(null);
            }

            final boolean needsToEncodeLfhOffset = lfhOffset >= ZipConstants.ZIP64_MAGIC || zip64Mode == Zip64Mode.Always;
            final boolean needsToEncodeDiskNumberStart = ze.getDiskNumberStart() >= ZipConstants.ZIP64_MAGIC_SHORT || zip64Mode == Zip64Mode.Always;

            if (needsToEncodeLfhOffset || needsToEncodeDiskNumberStart) {
                z64.setRelativeHeaderOffset(new ZipEightByteInteger(lfhOffset));
            }
            if (needsToEncodeDiskNumberStart) {
                z64.setDiskStartNumber(new ZipLong(ze.getDiskNumberStart()));
            }
            ze.setExtra();
        }
    }

    /**
     * Is there a ZIP64 extended information extra field for the entry?
     *
     * @since 1.3
     */
    private boolean hasZip64Extra(final ZipArchiveEntry ze) {
    return false;
       // return ze.getExtraField(Zip64ExtendedInformationExtraField.HEADER_ID) instanceof Zip64ExtendedInformationExtraField;
    }

    /**
     * This method indicates whether this archive is writing to a seekable stream (i.e., to a random access file).
     * <p>
     * For seekable streams, you don't need to calculate the CRC or uncompressed size for {@link #STORED} entries before invoking
     * {@link #putArchiveEntry(ZipArchiveEntry)}.
     * </p>
     *
     * @return true if seekable
     */
    public boolean isSeekable() {
        return outputStream instanceof RandomAccessOutputStream;
    }

    private boolean isTooLargeForZip32(final ZipArchiveEntry zipArchiveEntry) {
        return zipArchiveEntry.getSize() >= ZipConstants.ZIP64_MAGIC || zipArchiveEntry.getCompressedSize() >= ZipConstants.ZIP64_MAGIC;
    }

    private boolean isZip64Required(final ZipArchiveEntry entry1, final Zip64Mode requestedMode) {
        return requestedMode == Zip64Mode.Always || requestedMode == Zip64Mode.AlwaysWithCompatibility || isTooLargeForZip32(entry1);
    }

    private void preClose() throws IOException {
       /* if (finished) {
            throw new IOException("Stream has already been finished");
        }

        if (entry == null) {
            throw new IOException("No current entry to close");
        }*/

      /*  if (!entry.hasWritten) {
            write(ByteUtils.EMPTY_BYTE_ARRAY, 0, 0);
        }*/
    }

    /**
     * {@inheritDoc}
     *
     * @throws ClassCastException     if entry is not an instance of ZipArchiveEntry
     * @throws Zip64RequiredException if the entry's uncompressed or compressed size is known to exceed 4 GByte and {@link #setUseZip64} is
     *                                {@link Zip64Mode#Never}.
     */
    @Override
    public void putArchiveEntry(final ZipArchiveEntry archiveEntry) throws IOException {
        putArchiveEntry(archiveEntry, true);
    }

    /**
     * Writes the headers for an archive entry to the output stream. The caller must then write the content to the stream and call {@link #closeArchiveEntry()}
     * to complete the process.
     *
     * @param archiveEntry The archiveEntry
     * @param phased       If true size, compressedSize and crc required to be known up-front in the archiveEntry
     * @throws ClassCastException     if entry is not an instance of ZipArchiveEntry
     * @throws Zip64RequiredException if the entry's uncompressed or compressed size is known to exceed 4 GByte and {@link #setUseZip64} is
     *                                {@link Zip64Mode#Never}.
     */
    private void putArchiveEntry(final ZipArchiveEntry archiveEntry, final boolean phased) throws IOException {
       /* if (finished) {
            throw new IOException("Stream has already been finished");
        }*/

        entry = archiveEntry;
        entries.add(entry);

        //setDefaults(entry);

       /* final Zip64Mode effectiveMode = getEffectiveZip64Mode(entry);
        //validateSizeInformation(effectiveMode);

        if (shouldAddZip64Extra(entry, effectiveMode)) {

            final Zip64ExtendedInformationExtraField z64 = getZip64Extra(entry);

            final ZipEightByteInteger size;
            final ZipEightByteInteger compressedSize;
            if (phased) {
                // sizes are already known
                size = new ZipEightByteInteger(entry.getSize());
                compressedSize = new ZipEightByteInteger(entry.getCompressedSize());
            } else if (entry.getMethod() == STORED && entry.getSize() != ArchiveEntry.SIZE_UNKNOWN) {
                // actually, we already know the sizes
                compressedSize = size = new ZipEightByteInteger(entry.getSize());
            } else {
                // just a placeholder, real data will be in data
                // descriptor or inserted later via SeekableByteChannel
                compressedSize = size = ZipEightByteInteger.ZERO;
            }
            z64.setSize(size);
            z64.setCompressedSize(compressedSize);
            entry.setExtra();
        }*/

        if (entry.getMethod() == DEFLATED /*&& hasCompressionLevelChanged*/) {
            def.setLevel(level);
           // hasCompressionLevelChanged = false;
        }
        writeLocalFileHeader(archiveEntry, phased);
    }

    /**
     * When using random access output, write the local file header and potentially the ZIP64 extra containing the correct CRC and compressed/uncompressed
     * sizes.
     */
    private void rewriteSizesAndCrc(final boolean actuallyNeedsZip64) throws IOException {
      /*  final RandomAccessOutputStream randomStream = (RandomAccessOutputStream) outputStream;
        long dataStart = entry.localDataStart;
        if (randomStream instanceof ZipSplitOutputStream) {
            dataStart = ((ZipSplitOutputStream) randomStream).calculateDiskPosition(entry.getDiskNumberStart(), dataStart);
        }

        long position = dataStart;
        randomStream.writeFully(ZipLong.getBytes(entry.getCrc()), position); position += ZipConstants.WORD;
        if (!hasZip64Extra(entry) || !actuallyNeedsZip64) {
            randomStream.writeFully(ZipLong.getBytes(entry.getCompressedSize()), position); position += ZipConstants.WORD;
            randomStream.writeFully(ZipLong.getBytes(entry.getSize()), position); position += ZipConstants.WORD;
        } else {
            randomStream.writeFully(ZipLong.ZIP64_MAGIC.getBytes(), position); position += ZipConstants.WORD;
            randomStream.writeFully(ZipLong.ZIP64_MAGIC.getBytes(), position); position += ZipConstants.WORD;
        }

       if (hasZip64Extra(entry)) {
            final ByteBuffer name = getName(entry);
            final int nameLen = name.limit() - name.position();
            // seek to ZIP64 extra, skip header and size information
            position = dataStart + 3 * ZipConstants.WORD + 2 * ZipConstants.SHORT + nameLen + 2 * ZipConstants.SHORT;
            // inside the ZIP64 extra uncompressed size comes
            // first, unlike the LFH, CD or data descriptor
            randomStream.writeFully(ZipEightByteInteger.getBytes(entry.getSize()), position); position += ZipConstants.DWORD;
            randomStream.writeFully(ZipEightByteInteger.getBytes(entry.getCompressedSize()), position); position += ZipConstants.DWORD;

            if (!actuallyNeedsZip64) {
                // do some cleanup:
                // * rewrite version needed to extract
                position = dataStart - 5 * ZipConstants.SHORT;
                randomStream.writeFully(ZipShort.getBytes(versionNeededToExtractMethod(entry.getMethod())), position);
                position += ZipConstants.SHORT;

                // * remove ZIP64 extra, so it doesn't get written
                // to the central directory
                entry.removeExtraField(Zip64ExtendedInformationExtraField.HEADER_ID);
                entry.setExtra();

                // * reset hasUsedZip64 if it has been set because
                // of this entry
                if (entry.causedUseOfZip64) {
                    hasUsedZip64 = false;
                }
            }
        }*/
    }

    /**
     * Sets the file comment.
     *
     * @param comment the comment
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * Whether to create Unicode Extra Fields.
     * <p>
     * Defaults to NEVER.
     * </p>
     *
     * @param b whether to create Unicode Extra Fields.
     */
     /*
    public void setCreateUnicodeExtraFields(final UnicodeExtraFieldPolicy b) {
        createUnicodeExtraFields = b;
    }*/

    /**
     * Provides default values for compression method and last modification time.
     */
    private void setDefaults(final ZipArchiveEntry entry) {
        if (entry.getMethod() == -1) { // not specified
            entry.setMethod(method);
        }

        if (entry.getTime() == -1) { // not specified
            entry.setTime(System.currentTimeMillis());
        }
    }

    public void setEncoding(final Charset encoding) {
        this.charset = encoding;
        this.zipEncoding=encoding.newEncoder();
        if (useUTF8Flag && !encoding.name().equalsIgnoreCase("utf-8")) {
        useUTF8Flag = false;
        //createUnicodeExtraFields=UnicodeExtraFieldPolicy.NOT_ENCODEABLE;    
        }
      }

    /**
     * The encoding to use for file names and the file comment.
     * <p>
     * For a list of possible values see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html">Supported Encodings</a>.
     * Defaults to UTF-8.
     * </p>
     *
     * @param encoding the encoding to use for file names, use null for the platform's default encoding
     */
    public void setEncoding(final String encoding) {
        setEncoding(Charsets.toCharset(encoding));
    }

    /**
     * Whether to fall back to UTF and the language encoding flag if the file name cannot be encoded using the specified encoding.
     * <p>
     * Defaults to false.
     * </p>
     *
     * @param b whether to fall back to UTF and the language encoding flag if the file name cannot be encoded using the specified encoding.
    
    public void setFallbackToUTF8(final boolean b) {
        fallbackToUTF8 = b;
    }

    /**
     * Sets the compression level for subsequent entries.
     * <p>
     * Default is Deflater.DEFAULT_COMPRESSION.
     * </p>
     *
     * @param level the compression level.
     * @throws IllegalArgumentException if an invalid compression level is specified.
     */
    public void setLevel(final int level) {
      /*  if (level < Deflater.DEFAULT_COMPRESSION || level > Deflater.BEST_COMPRESSION) {
            throw new IllegalArgumentException("Invalid compression level: " + level);
        }*/
        if (this.level == level) {
            return;
        }
        //hasCompressionLevelChanged = true;
        this.level = level;
    }

    /**
     * Sets the default compression method for subsequent entries.
     * <p>
     * Default is DEFLATED.
     * </p>
     *
     * @param method an {@code int} from java.util.zip.ZipEntry
     */
    public void setMethod(final int method) {
        this.method = method;
    }

    /**
     * Whether to set the language encoding flag if the file name encoding is UTF-8.
     * <p>
     * Defaults to true.
     * </p>
     *
     * @param b whether to set the language encoding flag if the file name encoding is UTF-8
     */
    public void setUseLanguageEncodingFlag(final boolean b) {
        useUTF8Flag = b && ZipEncodingHelper.isUTF8(charset);
    }

    /**
     * Whether Zip64 extensions will be used.
     * <p>
     * When setting the mode to {@link Zip64Mode#Never Never}, {@link #putArchiveEntry}, {@link #closeArchiveEntry}, {@link #finish} or {@link #close} may throw
     * a {@link Zip64RequiredException} if the entry's size or the total size of the archive exceeds 4GB or there are more than 65,536 entries inside the
     * archive. Any archive created in this mode will be readable by implementations that don't support Zip64.
     * </p>
     * <p>
     * When setting the mode to {@link Zip64Mode#Always Always}, Zip64 extensions will be used for all entries. Any archive created in this mode may be
     * unreadable by implementations that don't support Zip64 even if all its contents would be.
     * </p>
     * <p>
     * When setting the mode to {@link Zip64Mode#AsNeeded AsNeeded}, Zip64 extensions will transparently be used for those entries that require them. This mode
     * can only be used if the uncompressed size of the {@link ZipArchiveEntry} is known when calling {@link #putArchiveEntry} or the archive is written to a
     * seekable output (i.e. you have used the {@link #ZipArchiveOutputStream(java.io.File) File-arg constructor}) - this mode is not valid when the output
     * stream is not seekable and the uncompressed size is unknown when {@link #putArchiveEntry} is called.
     * </p>
     * <p>
     * If no entry inside the resulting archive requires Zip64 extensions then {@link Zip64Mode#Never Never} will create the smallest archive.
     * {@link Zip64Mode#AsNeeded AsNeeded} will create a slightly bigger archive if the uncompressed size of any entry has initially been unknown and create an
     * archive identical to {@link Zip64Mode#Never Never} otherwise. {@link Zip64Mode#Always Always} will create an archive that is at least 24 bytes per entry
     * bigger than the one {@link Zip64Mode#Never Never} would create.
     * </p>
     * <p>
     * Defaults to {@link Zip64Mode#AsNeeded AsNeeded} unless {@link #putArchiveEntry} is called with an entry of unknown size and data is written to a
     * non-seekable stream - in this case the default is {@link Zip64Mode#Never Never}.
     * </p>
     *
     * @since 1.3
     * @param mode Whether Zip64 extensions will be used.
     */
    public void setUseZip64(final Zip64Mode mode) {
        zip64Mode = mode;
    }

    /**
     * Whether to add a Zip64 extended information extra field to the local file header.
     * <p>
     * Returns true if
     * </p>
     * <ul>
     * <li>mode is Always</li>
     * <li>or we already know it is going to be needed</li>
     * <li>or the size is unknown and we can ensure it won't hurt other implementations if we add it (i.e. we can erase its usage</li>
     * </ul>
     */
    private boolean shouldAddZip64Extra(final ZipArchiveEntry entry, final Zip64Mode mode) {
        return mode == Zip64Mode.Always || mode == Zip64Mode.AlwaysWithCompatibility || entry.getSize() >= ZipConstants.ZIP64_MAGIC
                || entry.getCompressedSize() >= ZipConstants.ZIP64_MAGIC
                || entry.getSize() == ArchiveEntry.SIZE_UNKNOWN && outputStream instanceof RandomAccessOutputStream && mode != Zip64Mode.Never;
    }

    /**
     * 4.4.1.4 If one of the fields in the end of central directory record is too small to hold required data, the field SHOULD be set to -1 (0xFFFF or
     * 0xFFFFFFFF) and the ZIP64 format record SHOULD be created.
     *
     * @return true if zip64 End Of Central Directory is needed
     */
    private boolean shouldUseZip64EOCD() {
        int numberOfThisDisk = 0;
        if (isSplitZip) {
            numberOfThisDisk = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex();
        }
        final int numOfEntriesOnThisDisk = numberOfCDInDiskData.getOrDefault(numberOfThisDisk, 0);
        return numberOfThisDisk >= ZipConstants.ZIP64_MAGIC_SHORT /* number of this disk */
                || cdDiskNumberStart >= ZipConstants.ZIP64_MAGIC_SHORT /* number of the disk with the start of the central directory */
                || numOfEntriesOnThisDisk >= ZipConstants.ZIP64_MAGIC_SHORT /* total number of entries in the central directory on this disk */
                || entries.size() >= ZipConstants.ZIP64_MAGIC_SHORT /* total number of entries in the central directory */
                || cdLength >= ZipConstants.ZIP64_MAGIC /* size of the central directory */
                || cdOffset >= ZipConstants.ZIP64_MAGIC; /*
                                                          * offset of start of central directory with respect to the starting disk number
                                                          */
    }

    private boolean usesDataDescriptor(final int zipMethod, final boolean phased) {
    return false;
       // return !phased && zipMethod == DEFLATED && !(outputStream instanceof RandomAccessOutputStream);
    }

    /**
     * If the Zip64 mode is set to never, then all the data in End Of Central Directory should not exceed their limits.
     *
     * @throws Zip64RequiredException if Zip64 is actually needed
     */
    private void validateIfZip64IsNeededInEOCD() throws Zip64RequiredException {
        // exception will only be thrown if the Zip64 mode is never while Zip64 is actually needed
        if (zip64Mode != Zip64Mode.Never) {
            return;
        }

        int numberOfThisDisk = 0;
        if (isSplitZip) {
            numberOfThisDisk = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex();
        }
        if (numberOfThisDisk >= ZipConstants.ZIP64_MAGIC_SHORT) {
            throw new Zip64RequiredException(Zip64RequiredException.DISK_NUMBER_TOO_BIG_MESSAGE);
        }

        if (cdDiskNumberStart >= ZipConstants.ZIP64_MAGIC_SHORT) {
            throw new Zip64RequiredException(Zip64RequiredException.CENTRAL_DIRECTORY_DISK_NUMBER_TOO_BIG_MESSAGE);
        }

        final int numOfEntriesOnThisDisk = numberOfCDInDiskData.getOrDefault(numberOfThisDisk, 0);
        if (numOfEntriesOnThisDisk >= ZipConstants.ZIP64_MAGIC_SHORT) {
            throw new Zip64RequiredException(Zip64RequiredException.TOO_MANY_ENTRIES_ON_DISK_MESSAGE);
        }

        // number of entries
        if (entries.size() >= ZipConstants.ZIP64_MAGIC_SHORT) {
            throw new Zip64RequiredException(Zip64RequiredException.TOO_MANY_ENTRIES_MESSAGE);
        }

        if (cdLength >= ZipConstants.ZIP64_MAGIC) {
            throw new Zip64RequiredException(Zip64RequiredException.CENTRAL_DIRECTORY_SIZE_TOO_BIG_MESSAGE);
        }

        if (cdOffset >= ZipConstants.ZIP64_MAGIC) {
            throw new Zip64RequiredException(Zip64RequiredException.ARCHIVE_TOO_BIG_MESSAGE);
        }
    }

    /**
     * Throws an exception if the size is unknown for a stored entry that is written to a non-seekable output or the entry is too big to be written without
     * Zip64 extra but the mode has been set to Never.
     */
    private void validateSizeInformation(final Zip64Mode effectiveMode) throws ZipException {
        // Size/CRC not required if SeekableByteChannel is used
        if (entry.getMethod() == STORED && !(outputStream instanceof RandomAccessOutputStream)) {
           if (entry.getSize() == ArchiveEntry.SIZE_UNKNOWN) {
                throw new ZipException("Uncompressed size is required for" + " STORED method when not writing to a" + " file");
            }
            if (entry.getCrc() == ZipArchiveEntry.CRC_UNKNOWN) {
                throw new ZipException("CRC checksum is required for STORED" + " method when not writing to a file");
            }
            entry.setCompressedSize(entry.getSize());
        }

        if ((entry.getSize() >= ZipConstants.ZIP64_MAGIC || entry.getCompressedSize() >= ZipConstants.ZIP64_MAGIC)
                && effectiveMode == Zip64Mode.Never) {
            throw new Zip64RequiredException(Zip64RequiredException.getEntryTooBigMessage(entry));
        }
    }

    private int versionNeededToExtract(final int zipMethod, final boolean zip64, final boolean usedDataDescriptor) {
        if (zip64) {
            return ZipConstants.ZIP64_MIN_VERSION;
        }
        if (usedDataDescriptor) {
            return ZipConstants.DATA_DESCRIPTOR_MIN_VERSION;
        }
        return versionNeededToExtractMethod(zipMethod);
    }

    private int versionNeededToExtractMethod(final int zipMethod) {
        return zipMethod == DEFLATED ? ZipConstants.DEFLATE_MIN_VERSION : ZipConstants.INITIAL_VERSION;
    }

    /**
     * Writes bytes to ZIP entry.
     *
     * @param b      the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     */
    @Override
    public void write(final byte[] b, final int offset, final int length) throws IOException {
       /*if (entry == null) {
            throw new IllegalStateException("No current entry");
        }
        ZipUtil.checkRequestedFeatures(entry);*/
        final long writtenThisTime = streamCompressor.write(b, offset, length, entry.getMethod());
        count(writtenThisTime);
    }

    /**
     * Writes the &quot;End of central dir record&quot;.
     *
     * @throws IOException            on error
     * @throws Zip64RequiredException if the archive's size exceeds 4 GByte or there are more than 65535 entries inside the archive and
     *                                {@link #setUseZip64(Zip64Mode)} is {@link Zip64Mode#Never}.
     */
    protected void writeCentralDirectoryEnd() throws IOException {
      /*  if (!hasUsedZip64 && isSplitZip) {
            ((ZipSplitOutputStream) this.outputStream).prepareToWriteUnsplittableContent(eocdLength);
        }*/

        //validateIfZip64IsNeededInEOCD();

        writeCounted(EOCD_SIG);

        // number of this disk
        int numberOfThisDisk = 0;
       /* if (isSplitZip) {
            numberOfThisDisk = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex();
        }*/
        writeCounted(ZipShort.getBytes(numberOfThisDisk));

        // disk number of the start of central directory
        writeCounted(ZipShort.getBytes((int) cdDiskNumberStart));

        // number of entries
        final int numberOfEntries = entries.size();

        // total number of entries in the central directory on this disk
        final int numOfEntriesOnThisDisk = isSplitZip ? numberOfCDInDiskData.getOrDefault(numberOfThisDisk, 0) : numberOfEntries;
        final byte[] numOfEntriesOnThisDiskData = ZipShort.getBytes(Math.min(numOfEntriesOnThisDisk, ZipConstants.ZIP64_MAGIC_SHORT));
        writeCounted(numOfEntriesOnThisDiskData);

        // number of entries
        final byte[] num = ZipShort.getBytes(Math.min(numberOfEntries, ZipConstants.ZIP64_MAGIC_SHORT));
        writeCounted(num);

        // length and location of CD
        writeCounted(ZipLong.getBytes(Math.min(cdLength, ZipConstants.ZIP64_MAGIC)));
        writeCounted(ZipLong.getBytes(Math.min(cdOffset, ZipConstants.ZIP64_MAGIC)));

        // ZIP file comment
        
       /* final ByteBuffer data = DEFAULT_CHARSET.encode(comment);
        final int dataLen = data.limit() - data.position();*/
        writeCounted(ZipShort.getBytes(0));
       /* streamCompressor.writeCounted(data.array(), data.arrayOffset(), dataLen);*/
    }

    private void writeCentralDirectoryInChunks() throws IOException {
        for (final ZipArchiveEntry ze : entries)
         writeCounted(createCentralFileHeader(ze));
    }

    /**
     * Writes the central file header entry.
     *
     * @param ze the entry to write
     * @throws IOException            on error
     * @throws Zip64RequiredException if the archive's size exceeds 4 GByte and {@link #setUseZip64(Zip64Mode)} is {@link Zip64Mode#Never}.
     */
    protected void writeCentralFileHeader(final ZipArchiveEntry ze) throws IOException {
        final byte[] centralFileHeader = createCentralFileHeader(ze);
        writeCounted(centralFileHeader);
    }

    /**
     * Write bytes to output or random access file.
     *
     * @param data the byte array to write
     * @throws IOException on error
     */
    public void writeCounted(final byte[] data) throws IOException {
        streamCompressor.writeCounted(data);
    }

    /**
     * Writes the data descriptor entry.
     *
     * @param ze the entry to write
     * @throws IOException on error
     */
    protected void writeDataDescriptor(final ZipArchiveEntry ze) throws IOException {
        if (!usesDataDescriptor(ze.getMethod(), false)) {
            return;
        }
        writeCounted(DD_SIG);
        writeCounted(ZipLong.getBytes(ze.getCrc()));
        if (!hasZip64Extra(ze)) {
            writeCounted(ZipLong.getBytes(ze.getCompressedSize()));
            writeCounted(ZipLong.getBytes(ze.getSize()));
        } else {
            writeCounted(ZipEightByteInteger.getBytes(ze.getCompressedSize()));
            writeCounted(ZipEightByteInteger.getBytes(ze.getSize()));
        }
    }

    /**
     * Writes the local file header entry
     *
     * @param ze the entry to write
     * @throws IOException on error
     */
    protected void writeLocalFileHeader(final ZipArchiveEntry ze) throws IOException {
        writeLocalFileHeader(ze, false);
    }
    static ByteBuffer em_name=ByteBuffer.allocate(0);
    private void writeLocalFileHeader(final ZipArchiveEntry ze, final boolean phased) throws IOException {
        final boolean encodable =true;
    // zipEncoding.canEncode(ze.getName());
        final ByteBuffer name = em_name;/*:getName(ze);*/

      /*  if (createUnicodeExtraFields != UnicodeExtraFieldPolicy.NEVER) {
            addUnicodeExtraFields(ze, encodable, DEFAULT_CHARSET.encode(ze.getName()));
        }*/

        long localHeaderStart = streamCompressor.getTotalBytesWritten();
       /* if (isSplitZip) {
            // when creating a split zip, the offset should be
            // the offset to the corresponding segment disk
            final ZipSplitOutputStream splitOutputStream = (ZipSplitOutputStream) this.outputStream;
            ze.setDiskNumberStart(splitOutputStream.getCurrentSplitSegmentIndex());
            localHeaderStart = splitOutputStream.getCurrentSplitSegmentBytesWritten();
        }*/

        final byte[] localHeader = createLocalFileHeader(ze, name, encodable, phased, localHeaderStart);
        ze.setLocalHeaderOffset(localHeaderStart); // At crc offset
        writeCounted(localHeader);
        ze.setDataOffset(streamCompressor.getTotalBytesWritten());
    }

    /**
     * Write bytes to output or random access file.
     *
     * @param data the byte array to write
     * @throws IOException on error
     */
    protected final void writeOut(final byte[] data) throws IOException {
        streamCompressor.writeOut(data, 0, data.length);
    }

    /**
     * Write bytes to output or random access file.
     *
     * @param data   the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @throws IOException on error
     */
    protected final void writeOut(final byte[] data, final int offset, final int length) throws IOException {
        streamCompressor.writeOut(data, offset, length);
    }

    /**
     * Write preamble data. For most of the time, this is used to make self-extracting zips.
     *
     * @param preamble data to write
     * @throws IOException if an entry already exists
     * @since 1.21
     */
    public void writePreamble(final byte[] preamble) throws IOException {
        writePreamble(preamble, 0, preamble.length);
    }

    /**
     * Write preamble data. For most of the time, this is used to make self-extracting zips.
     *
     * @param preamble data to write
     * @param offset   the start offset in the data
     * @param length   the number of bytes to write
     * @throws IOException if an entry already exists
     * @since 1.21
     */
    public void writePreamble(final byte[] preamble, final int offset, final int length) throws IOException {
        this.streamCompressor.writeCounted(preamble, offset, length);
    }

    /**
     * Writes the &quot;ZIP64 End of central dir record&quot; and &quot;ZIP64 End of central dir locator&quot;.
     *
     * @throws IOException on error
     * @since 1.3
     */
    protected void writeZip64CentralDirectory() throws IOException {
        if (zip64Mode == Zip64Mode.Never) {
            return;
        }

        if (!hasUsedZip64 && shouldUseZip64EOCD()) {
            // actually "will use"
            hasUsedZip64 = true;
        }

        if (!hasUsedZip64) {
            return;
        }

        long offset = streamCompressor.getTotalBytesWritten();
        long diskNumberStart = 0L;
        if (isSplitZip) {
            // when creating a split zip, the offset of should be
            // the offset to the corresponding segment disk
            final ZipSplitOutputStream zipSplitOutputStream = (ZipSplitOutputStream) this.outputStream;
            offset = zipSplitOutputStream.getCurrentSplitSegmentBytesWritten();
            diskNumberStart = zipSplitOutputStream.getCurrentSplitSegmentIndex();
        }

        writeOut(ZIP64_EOCD_SIG);
        // size of zip64 end of central directory, we don't have any variable length
        // as we don't support the extensible data sector, yet
        writeOut(ZipEightByteInteger.getBytes(ZipConstants.SHORT /* version made by */
                + ZipConstants.SHORT /* version needed to extract */
                + ZipConstants.WORD /* disk number */
                + ZipConstants.WORD /* disk with central directory */
                + ZipConstants.DWORD /* number of entries in CD on this disk */
                + ZipConstants.DWORD /* total number of entries */
                + ZipConstants.DWORD /* size of CD */
                + (long) ZipConstants.DWORD /* offset of CD */
        ));

        // version made by and version needed to extract
        writeOut(ZipShort.getBytes(ZipConstants.ZIP64_MIN_VERSION));
        writeOut(ZipShort.getBytes(ZipConstants.ZIP64_MIN_VERSION));

        // number of this disk
        int numberOfThisDisk = 0;
        if (isSplitZip) {
            numberOfThisDisk = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex();
        }
        writeOut(ZipLong.getBytes(numberOfThisDisk));

        // disk number of the start of central directory
        writeOut(ZipLong.getBytes(cdDiskNumberStart));

        // total number of entries in the central directory on this disk
        final int numOfEntriesOnThisDisk = isSplitZip ? numberOfCDInDiskData.getOrDefault(numberOfThisDisk, 0) : entries.size();
        final byte[] numOfEntriesOnThisDiskData = ZipEightByteInteger.getBytes(numOfEntriesOnThisDisk);
        writeOut(numOfEntriesOnThisDiskData);

        // number of entries
        final byte[] num = ZipEightByteInteger.getBytes(entries.size());
        writeOut(num);

        // length and location of CD
        writeOut(ZipEightByteInteger.getBytes(cdLength));
        writeOut(ZipEightByteInteger.getBytes(cdOffset));

        // no "zip64 extensible data sector" for now

        if (isSplitZip) {
            // based on the ZIP specification, the End Of Central Directory record and
            // the Zip64 End Of Central Directory locator record must be on the same segment
            final int zip64EOCDLOCLength = ZipConstants.WORD /* length of ZIP64_EOCD_LOC_SIG */
                    + ZipConstants.WORD /* disk number of ZIP64_EOCD_SIG */
                    + ZipConstants.DWORD /* offset of ZIP64_EOCD_SIG */
                    + ZipConstants.WORD /* total number of disks */;

            final long unsplittableContentSize = zip64EOCDLOCLength + eocdLength;
            ((ZipSplitOutputStream) this.outputStream).prepareToWriteUnsplittableContent(unsplittableContentSize);
        }

        // and now the "ZIP64 end of central directory locator"
        writeOut(ZIP64_EOCD_LOC_SIG);

        // disk number holding the ZIP64 EOCD record
        writeOut(ZipLong.getBytes(diskNumberStart));
        // relative offset of ZIP64 EOCD record
        writeOut(ZipEightByteInteger.getBytes(offset));
        // total number of disks
        if (isSplitZip) {
            // the Zip64 End Of Central Directory Locator and the End Of Central Directory must be
            // in the same split disk, it means they must be located in the last disk
            final int totalNumberOfDisks = ((ZipSplitOutputStream) this.outputStream).getCurrentSplitSegmentIndex() + 1;
            writeOut(ZipLong.getBytes(totalNumberOfDisks));
        } else {
            writeOut(ONE);
        }
    }
}
