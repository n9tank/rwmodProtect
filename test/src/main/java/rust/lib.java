package rust;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class lib extends TaskWait implements Runnable {
 InputStream inp;
 File In;
 File Ou;
 static Map libMap;
 static lib close;
 lib(File ou, ui ui) {
  super(ui);
  Ou = ou;
  lib task=close;
  if (task != null)task.down(TaskWait.cancel);
  close = this;
 }
 public static void exec(InputStream in, File ou, ui ui) {
  lib lib=new lib(ou, ui);
  lib.inp = in;
 }
 public static void exec(File in, File ou, ui ui) {
  lib lib=new lib(ou, ui);
  lib.In = in;
 }
 public static ZipArchiveEntry getArc(String str) {
  ZipArchiveEntry en=new ZipArchiveEntry(str);
  en.setMethod(en.DEFLATED);
  return en;
 }
 public void end(Throwable e) {
  ZipFile zip=Zip;
  if (zip != null) {
   try {
    zip.close();
   } catch (IOException e2) {}
  }
  if (e != null) {
   File ou=Ou;
   if (ou != null)ou.delete();
  } else libMap = Zipmap;
  if (!(e instanceof InterruptedException))back.end(e);
 }
 public void run() {
  File ou=Ou;
  try {
   File red;
   InputStream in=inp;
   if (ou != null)ou.getParentFile().mkdirs();
   if (in != null) {
    red = ou;
    FileChannel ch=new FileOutputStream(ou).getChannel();
    ou = null;
    try {
     ch.transferFrom(Channels.newChannel(in), 0L, Long.MAX_VALUE);
    } finally {
     ch.close();
    }
   } else red = In;
   ZipFile zip = new ZipFile(red);
   Zip = zip;
   Enumeration<? extends ZipArchiveEntry> ens=zip.getEntries();
   ZipArchiveOutputStream out=null;
   ParallelScatterZipCreator cre=null;
   if (ou != null) {
    rootPath = "";
    out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(ou)));
    out.setLevel(9);
    cre = new ParallelScatterZipCreator();
   } else rootPath = "assets/units/";
   try {
    while (ens.hasMoreElements()) {
     ZipArchiveEntry zipe=ens.nextElement();
     String name;
     if (!zipe.isDirectory() && (name = zipe.getName()).endsWith("i") && (ou == null || name.charAt(7) == 'u')) {
      loder loder=new loder(zip.getInputStream(zipe));
      loder.task = this;
      loder.src = name;
      if (ou != null)name = name.substring(13);
      loder.str = name;
      name = name.toLowerCase();
      Zipmap.put(name, loder);
      if (ou == null)add(loder);
      else {
       ato.increment();
       cre.addArchiveEntry(getArc(name), loder);
      }
     }
    }
   } finally {
    if (cre != null) {
     cre.writeTo(out);
     out.close();
    }
   }
  } catch (Throwable e) {
   down(e);
  }
 }
}
