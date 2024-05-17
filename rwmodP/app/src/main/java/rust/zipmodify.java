package rust;

import java.io.File;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import carsh.log;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import java.util.zip.ZipEntry;

public class zipmodify implements Runnable {
 File in;
 File ou;
 ui ui;
 public boolean raw;
 boolean pack;
 public zipmodify(File i, File o, ui u, boolean pc, boolean rw) {
  in = i;
  ou = o;
  ui = u;
  pack = pc;
  raw = rw;
 }
 public void run() {
  Throwable ex=null;
  try {
   ZipFile zip=new ZipFile(in);
   ZipArchiveOutputStream zipout=pack ?new zipout(ou): new ZipArchiveOutputStream(ou);
   zipout.setLevel(9);
   ParallelScatterZipCreator cr=null;
   if (!raw)cr = new ParallelScatterZipCreator();
   try {
	Enumeration<ZipArchiveEntry> all=zip.getEntries();
	while (all.hasMoreElements()) {
	 ZipArchiveEntry en=all.nextElement();
	 String name=en.getName();
	 int n;
	 if (pack) {
	  if (!en.isDirectory()) {
	   if (!name.regionMatches(true, n = name.length() - 4, ".ogg", 0, 4) && !name.regionMatches(true, n, ".wav", 0, 4))name = name.concat("/");
	  } else name = null;
	 } else {
	  if (en.getCompressedSize() != 0) {
	   if (en.isDirectory())name = name.substring(0, name.length() - 1);
	  } else name = null;
	 }
	 if (name != null) {
	  ZipArchiveEntry out=lib.getArc(name);
	  if (raw) {
	   if (!pack)out.setSize(en.getCompressedSize());
	   zipout.addRawArchiveEntry(out, zip.getRawInputStream(en));
	  } else cr.addArchiveEntry(out, new inputsu(zip, en));
	 }
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
