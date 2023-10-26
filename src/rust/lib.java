package rust;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;

public class lib implements Runnable {
 InputStream inp;
 File In;
 File Ou;
 ui Ui;
 HashMap iniMap;
 static HashMap libMap;
 static Future fu;
 public static void exec(File in, File ou, ui ui) {
  if (fu != null)fu.cancel(true);
  lib lib=new lib();
  lib.In = in;
  lib.Ou = ou;
  lib.Ui = ui;
  fu = ui.pool.submit(lib);
 }
 loder getlod(String str) {
  str = str.toLowerCase();
  Object o=iniMap.get(str);
  loder lod=(loder)o;
  if (lod.str == null) {
   lod.str = "";
   lodAllCopy(lod, str);
  }
  return lod;
 }
 void lodAllCopy(loder lod, String file) {
  file = loder.getSuperPath(file);
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
     loder loder=getlod(file.concat(str));
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
 public void run() {
  ui ui=Ui;
  HashMap inimap=new HashMap();
  iniMap = inimap;
  File ou=Ou;
  try {
   ZipFile zip=new ZipFile(In);
   Enumeration<? extends ZipArchiveEntry> ens=zip.getEntries();
   try {
    if (ou != null) {
     ou.getParentFile().mkdirs();
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
      //compress关我线程池，我日你妈
     } finally {
      if (out != null) {
       cre.writeTo(out);
       out.close();
      }
     }
    } else {
     TaskWait task=new TaskWait();
     while (ens.hasMoreElements()) {
      ZipArchiveEntry zipe= ens.nextElement();
      String name;
      if (!zipe.isDirectory() && (name = zipe.getName()).endsWith("i") && name.charAt(7) == 'u') {
       loder loder=new loder(new inputsu(zip, zipe));
       name = name.substring(13).toLowerCase();
       inimap.put(name, loder);
       task.add(loder);
      }
     }
     task.lock();
    }
   } finally {
    zip.close();
   }
   Iterator ite=inimap.entrySet().iterator();
   while (ite.hasNext()) {
    Map.Entry en=(Map.Entry)ite.next();
    String key=(String)en.getKey();
    loder lod=(loder)en.getValue();
    if (lod.str == null) {
     lod.str = "";
     lodAllCopy(lod, key);
    }
   }
   libMap = inimap;
   ui.end(null);
  } catch (Throwable e) {
   if (ou != null)ou.delete();
   if (!(e instanceof InterruptedException)) {
    ui.end(e);
   }
  }
 }
}
