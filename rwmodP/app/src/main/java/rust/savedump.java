package rust;

import carsh.log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import java.io.BufferedOutputStream;

public class savedump implements Runnable {
 File in;
 File ou;
 ui ui;
 public savedump(File i, File o, ui u) {
  in = i;
  ou = o;
  ui = u;
 }
 public static int indexOf(byte arr[], byte find[], int off, int start, int len) {
  int l=find.length;
  byte b=find[off];
  for (int i=start;i < len;++i) {
   if (arr[i] == b) {
	if (++off == l)return ++i - l;
	b = find[off];
   } else if (off > 0) {
	i--;
	b = find[off = 0];
   }
  }
  return -off - 1;
 }
 public void run() {
  Throwable ex=null;
  try {
   BufferedInputStream buff=new BufferedInputStream(new FileInputStream(in));
   try {
	byte[] brr=new byte[8199];
	buff.mark(0);
	buff.read(brr, 0, 200);
	int i=indexOf(brr, new byte[]{(byte)0x1f,(byte)0x8b}, 0, 0, 200);
	if (i > 0) {
	 buff.reset();
	 buff.skip(i);
	 GzipCompressorInputStream gz=new GzipCompressorInputStream(buff);
	 int l,n=0;
	 i = 0;
	 byte[] finds=new byte[]{(byte)'<',(byte)'?',(byte)'x',(byte)'m'};
	// byte[] finds2=new byte[]{(byte)'<',(byte)'m',(byte)'a',(byte)'p'};
	 try {
	  while ((l = gz.read(brr, n, 8192)) > 0) {
	   i = indexOf(brr, finds, i, n, l + n);
	   if (i >= 0) {
		ByteBuffer by=ByteBuffer.wrap(brr);
		int size=by.getInt(i - 4);
		FileOutputStream o=new FileOutputStream(ou);
		FileChannel oc=o.getChannel();
		try {
		 oc.write(ByteBuffer.allocateDirect(1), size);
		 n = l + n;
		 o.write(brr, i, n -= i);
		 size -= n;
		 if (size > 0) {
		  ReadableByteChannel gzc = Channels.newChannel(gz);
		  try {
		   oc.transferFrom(gzc, n, size);
		  } finally {
		   gzc.close();
		  }
		 }
		} finally {
		 oc.close();
		}
		break;
	   } else {
		int j=n;
		i = -(++i);
		n = i + 4;
		System.arraycopy(brr, l + j - n, brr, 0, n);
	   }
	  }
	 } finally {
	  gz.close();
	 }
	}
   } finally {
	buff.close();
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
}
