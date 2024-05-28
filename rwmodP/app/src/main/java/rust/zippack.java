package rust;

import carsh.log;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class zippack implements Runnable {
 File in;
 File ou;
 ui ui;
 public boolean raw;
 public zippack(File i, File o, boolean rw, ui u) {
  in = i;
  ou = o;
  ui = u;
  raw = rw;
 }
 public void run() {
  Throwable ex=null;
  try {
   ZipFile zip= new ZipFile(in);
   ZipArchiveOutputStream zipout=new zipout(ou);
   ParallelScatterZipCreator cr=null;
   if (!raw)cr = lib.prc(9);
   try {
	Enumeration all=zip.getEntries();
	while (all.hasMoreElements()) {
	 ZipArchiveEntry en=(ZipArchiveEntry)all.nextElement();
	 String name=en.getName();
	 int n;
	 if (!en.isDirectory()) {
	  if (!name.regionMatches(true, n = name.length() - 4, ".ogg", 0, 4) && !name.regionMatches(true, n, ".wav", 0, 4))name = name.concat("/");
	 } else name = null;
	 if (name != null) {
	  ZipArchiveEntry out=lib.getArc(name);
	  if (raw) {
	   InputStream io= zip.getRawInputStream(en);
	   try {
		zipout.addRawArchiveEntry(out, io);
	   } finally {
		io.close();
	   }
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
