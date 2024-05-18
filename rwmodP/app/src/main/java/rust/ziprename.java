package rust;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import net.lingala.zip4j.model.FileHeader;
import carsh.log;

public class ziprename implements Runnable {
 File in;
 ui ui;
 public ziprename(File i, ui u) {
  in = i;
  ui = u;
 }
 public void run() {
  Throwable ex=null;
  try {
   net.lingala.zip4j.ZipFile zip=new net.lingala.zip4j.ZipFile(in);
   try {
	zip.setRunInThread(false);
	List<FileHeader> list=zip.getFileHeaders();
	HashMap msp=new HashMap();
	for (FileHeader he:list) {
	 long g=he.getCompressedSize();
	 if (g != 0) {
	  String name=he.getFileName();
	  he.setUncompressedSize(g);
	  String str=name;
	  if (he.isDirectory()) {
	   he.setDirectory(false);
	   str = name.substring(0, name.length() - 1);
	  }
	  msp.put(name, str);
	 }
	}
	zip.renameFiles(msp);
   } finally {
	zip.close();
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  ui.end(ex);
 }
}
