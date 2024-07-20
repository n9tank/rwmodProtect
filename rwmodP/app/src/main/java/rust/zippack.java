package rust;

import android.util.Log;
import carsh.log;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.zipOut;
import rust.zipunpack;

public class zippack implements Runnable {
 File in;
 File ou;
 ui ui;
 public static boolean keepUnSize; 
 //如果你是性能敏感的可以打开它，这可以稍微提升解压性能。 
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
   zipOut zipout=new zipOut(ou);
   ParallelScatterZipCreator cr=null;
   if (!raw)cr = lib.prc(9);
   try {
	Enumeration all=zip.entries();
	while (all.hasMoreElements()) {
	 ZipEntry en=(ZipEntry)all.nextElement();
	 String name=en.getName();
     int mode=ZipEntry.DEFLATED;         
	 if (!en.isDirectory()) {
      int n=name.length() - 4;          
      if (name.regionMatches(true, n, ".png", 0, 4))mode = 0;    
	  if (!name.regionMatches(true, n, ".ogg", 0, 4) && !name.regionMatches(true, n, ".wav", 0, 4))name = name.concat("/");
	 } else name = null;
	 if (name != null) {
	  ZipArchiveEntry out=lib.getArc(name);
      out.setMethod(mode);    
      out.setSize(en.getSize());
      if(keepUnSize)out.setCompressedSize(en.getCompressedSize());      
      if (raw || mode == 0) {
       try(InputStream io=zip.getInputStream(en)){  
         zipout.addRawArchiveEntry(out, zipunpack.getRaw(io, en, out.getMethod()));
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
