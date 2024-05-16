package rust;

import carsh.log;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;

public class zipmodify implements Runnable {
 File f;
 ui ui;
 boolean pack;
 public zipmodify(File in, ui u, boolean p) {
  f = in;
  ui = u;
  pack = p;
 }
 public void run() {
  Throwable ex=null;
  try {
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
	  if (!he.isDirectory() && !name.regionMatches(true, n, ".ogg", 0, 4) && !name.regionMatches(true, n, ".wav", 0, 4)) {
	   he.setUncompressedSize(-1);
	   he.setDirectory(true);
	   map.put(name, name);
	  }
	 } else {
	  long g;
	  if (he.isDirectory() && (g = he.getCompressedSize()) != 0) {
	   he.setUncompressedSize(g);
	   he.setDirectory(false);
	   map.put(name, name.substring(0, le - 1));
	  }
	 }
	}
	zip.renameFiles(map);
   } finally {
	zip.close();
   }
  } catch (Throwable e) {
   log.e(this, e);
   ex = e;
  }
  ui.end(ex);
 }
}
