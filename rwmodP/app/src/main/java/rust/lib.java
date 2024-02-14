package rust;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class lib extends TaskWait {
 InputStream inp;
 static Map libMap;
 static lib close;
 public lib(File in, File ou, ui ui) {
  super(in, ou, ui);
  lib task=close;
  if (task != null)task.down(TaskWait.cancel);
  close = this;
 }
 public loder getLoder(String str) throws Throwable {
  ZipArchiveEntry za=Zip.getEntry(str);
  if (Ou != null)str = str.substring(13);
  str = str.toLowerCase();
  return addLoder(za, str, false);
 }
 public static void exec(InputStream in, File ou, ui ui) {
  lib lib=new lib(null, ou, ui);
  lib.inp = in;
 }
 public static ZipArchiveEntry getArc(String str) {
  ZipArchiveEntry en=new ZipArchiveEntry(str);
  en.setMethod(en.DEFLATED);
  return en;
 }
 public void end(Throwable e) {
  close = null;
  File ou=Ou;
  if (e == null) {
   if (ou != null) {
    Collection<loder> vl=Zipmap.values();
    for (loder lod:vl) {
     lod.task = null;
     lod.src = "//";//CORE://
    }
    try {
     ZipArchiveOutputStream out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(ou)));
     out.setLevel(9);
     ParallelScatterZipCreator cre = new ParallelScatterZipCreator();
     try {
      for (loder lod:vl) {
       cre.addArchiveEntry(getArc(lod.str), lod);
      }
     } finally {
      cre.writeTo(out);
      out.close();
     }
    } catch (Throwable e2) {
     e = e2;
    }
   }
   libMap = Zipmap;
  }
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
    Ou = ou = null;
    try {
     ch.transferFrom(Channels.newChannel(in), 0L, Long.MAX_VALUE);
    } finally {
     ch.close();
    }
   } else red = In;
   ZipFile zip = new ZipFile(red);
   Zip = zip;
   Enumeration<? extends ZipArchiveEntry> ens=zip.getEntries();
   while (ens.hasMoreElements()) {
    ZipArchiveEntry zipe=ens.nextElement();
    String name = zipe.getName();
    if (ou == null || (name.endsWith("i") && name.charAt(7) == 'u')) {
     if (ou != null)name = name.substring(13);
     name = name.toLowerCase();
     loder lod=addLoder(zipe, name, false);
     lod.str = name;
    }
   }
   ato.decrement();
  } catch (Throwable e) {
   down(e);
  }
 }
}
