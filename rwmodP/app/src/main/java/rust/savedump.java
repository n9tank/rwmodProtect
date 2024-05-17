package rust;

import carsh.log;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

public class savedump implements Runnable {
 File in;
 File ou;
 ui ui;
 public savedump(File i, File o, ui u) {
  in = i;
  ou = o;
  ui = u;
 }
 public static int indexOf(byte arr[], byte find[], int off, int len) {
  int l=find.length;
  for (int i=0;i < len;++i) {
   if (arr[i] == find[off]) {
	++off;
	if (off == l)return i - l;
   } else off = 0;
  }
  return -off - 1;
 }
 public void run() {
  Throwable ex=null;
  try {
   FileInputStream f=new FileInputStream(in);
   FileChannel c=f.getChannel();
   try {
	byte[] brr=new byte[8200];
	f.read(brr, 0, 200);
	int i=indexOf(brr, new byte[]{(byte)0x1f,(byte)0x8b}, 0, 200);
	if (i > 0) {
	 c.position(i);
	 ByteBuffer by=ByteBuffer.wrap(brr);
	 by.limit(8192);
	 ReadableByteChannel gz=Channels.newChannel(new GZIPInputStream(f));
	 byte[] finds=new byte[]{(byte)'<',(byte)'x',(byte)'m',(byte)'l'};
	 int l;
	 while ((l = gz.read(by)) > 0) {
	  i = indexOf(brr, finds, i < 0 ?-i: 0, l);
	  int size=by.getInt(i - 4);
	  if (i >= 0) {
	   RandomAccessFile o=new RandomAccessFile(ou, "rw");
	   FileChannel oc=o.getChannel();
	   try {
		o.setLength(size);
		int n=by.position() - i;
		o.write(brr, i, n);
		size -= n;
		if (size > 0)oc.transferFrom(gz, n, size);
	   } finally {
		oc.close();
	   }
	   break;
	  } else if (i++ < 0) {
	   int n=i + 4;
	   System.arraycopy(brr, brr.length - n, brr, 0, n);
	   by.clear();
	   by.position(n);
	   by.limit(n + 8192);
	  }
	 }
	}
   } finally {
	c.close();
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
}
