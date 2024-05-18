package rust;

import carsh.log;
import java.io.File;
import net.lingala.zip4j.ZipFile;
import java.util.List;
import net.lingala.zip4j.model.FileHeader;
import java.util.HashMap;


public class zipdump implements Runnable {
 File in;
 ui ui;
 public zipdump(File i, ui u) {
  in = i;
  ui = u;
 }
 public void run() {
  Throwable ex=null;
  try {
   ZipFile zip=new ZipFile(in);
   try {
	List<FileHeader> list=zip.getFileHeaders();
	HashMap rm=new HashMap();
	for (FileHeader he:list) {
	 String name=he.getFileName();
	 if (he.isDirectory()) {
	  he.setDirectory(false);
	  String str = name.substring(0, name.length() - 1);
	  int i=0;
	  String out=str;
	  while (zip.getFileHeader(out) != null)out = str.concat(String.valueOf(i++));
	  rm.put(name, out);
	 }
	}
	zip.renameFiles(rm);
   } finally {
	zip.close();
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  ui.end(ex);
 }
}

