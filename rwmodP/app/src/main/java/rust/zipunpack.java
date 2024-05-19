package rust;

import carsh.log;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.parallel.InputStreamSupplier;

public class zipunpack implements Runnable {
 public static class get implements InputStreamSupplier {
  ZipFile zip;
  ZipEntry en;
  public InputStream get() {
   try {
	return zip.getInputStream(en);
   } catch (Throwable e) {
	log.e(this, e);
   }
   return null;
  }
 }
 File in;
 File ou;
 ui ui;
 public zipunpack(File i, File o, ui u) {
  in = i;
  ou = o;
  ui = u;
 }
 public void run() {
  Throwable ex=null;
  try {
   ZipFile zip= new ZipFile(in);
   ZipArchiveOutputStream zipout=new ZipArchiveOutputStream(ou);
   ParallelScatterZipCreator cr = lib.prc(1);
   try {
	Enumeration all=zip.entries();
	while (all.hasMoreElements()) {
	 ZipEntry en=(ZipEntry)all.nextElement();
	 String name=en.getName();
	 if (en.isDirectory())name = name.substring(0, name.length() - 1);
	 ZipArchiveEntry out=lib.getArc(name);
	 zipunpack.get get= new get();
	 get.zip = zip;
	 get.en = en;
	 cr.addArchiveEntry(out, get);
	}
   } finally {
	if (zipout != null) {
	 if (cr != null) {
	  try {
	   cr.writeTo(zipout);
	  } catch (Throwable e) {
	   log.e(this, ex = e);
	  }
	 }
	 zipout.close();
	}
	zip.close();
   }
  } catch (Throwable e) {
   log.e(this, ex = e);
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
}
