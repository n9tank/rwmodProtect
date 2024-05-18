package rust;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import java.util.zip.ZipEntry;
import org.apache.commons.compress.archivers.zip.GeneralPurposeBit;
import java.nio.file.attribute.FileTime;

public class zipout extends ZipArchiveOutputStream {
 public zipout(File path) throws IOException {
  super(path);
 }
 public void addRawArchiveEntry(ZipArchiveEntry entry, InputStream rawStream) throws IOException {
  entry.setSize(0);
  entry.setCrc(1);
  entry.setTime(347155200000L);
  entry.setCompressedSize(-1);
  super.addRawArchiveEntry(entry, rawStream);
 } 
}
