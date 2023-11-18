package rust;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class lib implements Runnable,ui {
 InputStream inp;
 File In;
 File Ou;
 ui Ui;
 ZipFile Zip;
 HashMap iniMap;
 static HashMap libMap;
 static TaskWait close=new TaskWait(null);
 lib(File ou, ui ui) {
  Ou = ou;
  Ui = ui;
  TaskWait task=close;
  task.down(task.cancel);
  task.back = this;
  task.err = null;
 }
 public static void exec(InputStream in, File ou, ui ui) {
  lib lib=new lib(ou, ui);
  lib.inp = in;
  close.addN(lib);
 }
 public static void exec(File in, File ou, ui ui) {
  lib lib=new lib(ou, ui);
  lib.In = in;
  close.addN(lib);
 }
 loder getlod(String str, String su) {
  str = str.toLowerCase();
  Object o=iniMap.get(str);
  loder lod=(loder)o;
  if (!lod.use) {
   lod.use = true;
   lodAllCopy(lod, su);
  }
  return lod;
 }
 void lodAllCopy(loder lod, String file) {
  HashMap ini=lod.ini;
  Object o=ini.get("core");
  HashMap put=null;
  if (o != null) {
   HashMap map=(HashMap)o;
   o = map.get("copyFrom");
   if (o != null) {
    String str=(String)o;
    put = new HashMap();
    String list[]=str.split(",");
    int i=0,l=list.length;
    do{
     str = list[i];
     loder loder=getlod(file.concat(str), file);
     loder.putAnd(put, loder.put, null, null);
    }while(++i < l);
   }
  }
  if (put != null) {
   loder.putAnd(put, ini, null, null);
  } else put = ini;
  lod.put = put;
  lod.ini = null;
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
  } else {
   try {
    HashMap inimap=iniMap;
    Iterator ite=inimap.entrySet().iterator();
    while (ite.hasNext()) {
     Map.Entry en=(Map.Entry)ite.next();
     String key=(String)en.getKey();
     loder lod=(loder)en.getValue();
     if (!lod.use) {
      lod.use = true;
      lodAllCopy(lod, loder.getSuperPath(key));
     }
    }
    libMap = inimap;
   } catch (Throwable e2) {
    if (e == null)e = e2;
    else e.addSuppressed(e2);
   }
  }
  if (!(e instanceof InterruptedException))Ui.end(e);
 }
 public void run() {
  HashMap inimap=new HashMap();
  iniMap = inimap;
  File ou=Ou;
  ZipFile zip=null;
  TaskWait task=close;
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
   zip = new ZipFile(red);
   Enumeration<? extends ZipArchiveEntry> ens=zip.getEntries();
   if (ou != null) {
    ParallelScatterZipCreator cre=null;
    ZipArchiveOutputStream out=null;
    try {
     out = new ZipArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(ou)));
     out.setLevel(9);
     cre = new ParallelScatterZipCreator();
     while (ens.hasMoreElements()) {
      ZipArchiveEntry zipe=ens.nextElement();
      String name;
      if (!zipe.isDirectory() && (name = zipe.getName()).endsWith("i") && name.charAt(7) == 'u') {
       loder loder=new loder(new inputsu(zip, zipe));
       name = name.substring(13).toLowerCase();
       inimap.put(name, loder);
       inputsu ins=new inputsu(loder);
       cre.addArchiveEntry(getArc(name), ins);
      }
     }
    } finally {
     if (out != null) {
      cre.writeTo(out);
      out.close();
     }
    }
    end(null);
   } else {
    while (ens.hasMoreElements()) {
     ZipArchiveEntry zipe= ens.nextElement();
     String name=zipe.getName();
     loder loder=new loder(new inputsu(zip, zipe));
     loder.task = task;
     name = name.toLowerCase();
     inimap.put(name, loder);
     task.add(loder);
    }
    task.end();
   }
  } catch (Throwable e) {
   task.down(e);
  }
 }
}
