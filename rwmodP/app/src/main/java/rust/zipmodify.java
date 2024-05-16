package rust;

import carsh.log;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;

public class zipmodify implements Runnable {
 File f;
 ui ui;
 public boolean fast;
 boolean pack;
 public zipmodify(File in, ui u, boolean p) {
  f = in;
  ui = u;
  pack = p;
 }
 public void fzp() throws Throwable {
  FileChannel rf=new RandomAccessFile(f, "rw").getChannel();
  try {
   int fsize=(int)rf.size();
   ByteBuffer rw=rf.map(FileChannel.MapMode.READ_WRITE, 0, fsize);
   rw.order(ByteOrder.LITTLE_ENDIAN);
   int i=2;
   byte put,eqz;
   if (pack) {
	put = '/';
	eqz = '+';
   } else {
	eqz = '/';
	put = '+';
   }
   do{
	int size,j,k;
	i += 16;
	size = rw.getInt(i);
	i += 8;
	j = rw.getShort(i);
	i += 2;
	k = rw.getShort(i);
	i += j + 1;
	byte c2=rw.get(i);
	if (size != 0 && (c2 == eqz)) {
	 rw.put(i, c2 = put);
	}
	i += size + k + 1;
	if (rw.get(i) == (byte)2) {
	 i += 4;
	} else i += 2;
   }while(rw.get(i) != (byte)1);
   i += 22;
   do{
	int size=rw.getInt(i);
	i += 4;
	int j=rw.getShort(i);
	i += 2;
	int k=rw.getShort(i);
	i += 15 + j;
	byte c2=rw.get(i);
	if (size != 0 && (c2 == eqz)) {
	 rw.put(i, c2 = put);
	}
	i += k + 25;
   }while(i + 4 < fsize);
  } finally {
   rf.close();
  }
 }
 public void wzip() throws Throwable {
  ZipFile zip=new ZipFile(f);
  zip.setRunInThread(false);
  HashMap map=new HashMap();
  try {
   List<FileHeader> ha=zip.getFileHeaders();
   int i=ha.size();
   while (--i >= 0) {
	FileHeader he=ha.get(i);
	String name=he.getFileName();
	int le=name.length();
	if (pack) {
	 int n=le - 4;
	 if (!name.regionMatches(true, n, ".ogg", 0, le) && !name.regionMatches(true, n, ".wav", 0, le)) {
	  he.setDirectory(true);
	  map.put(name,name);
	 }
	} else {
	 if (he.isDirectory() && he.getUncompressedSize() != 0) {
	  he.setDirectory(false);
	  map.put(name, name.substring(0, le - 1));
	 }
	}
   }
   zip.renameFiles(map);
  } finally {
   zip.close();
  }
 }
 public void run() {
  Throwable ex=null;
  try {
   if (fast)fzp();
   else wzip();
  } catch (Throwable e) {
   log.e(this, e);
   ex = e;
  }
  ui.end(ex);
 }
}
