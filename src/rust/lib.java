package rust;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class lib implements Runnable {
 File In;
 File Ou;
 ui Ui;
 public static void exec(File in, File ou, ui ui) {
  lib lib=new lib();
  lib.In = in;
  lib.Ou = ou;
  lib.Ui = ui;
  ui.pool.execute(lib);
 }
 static loder getlod(String str, HashMap iniMap) {
  str = str.toLowerCase();
  Object o=iniMap.get(str);
  loder lod=(loder)o;
  if (lod.str == null) {
   lod.str = "";
   lodAllCopy(lod, str, iniMap);
  }
  return lod;
 }
 static void lodAllCopy(loder lod, String file, HashMap iniMap) {
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
     loder loder=getlod(file.concat(str), iniMap);
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
 public void run() {
  ui ui=Ui;
  HashMap inimap=new HashMap();
  StringBuilder buf=new StringBuilder();
  File in=In;
  File ou=Ou;
  int index=0;
  int now=0;
  try {
   ZipFile zip=new ZipFile(in);
   File tmp=null;
   BufferedWriter wt=null;
   int size=zip.size();
   Enumeration<? extends ZipEntry> ens=zip.entries();
   try {
    if (ou == null) {
     size <<= 1;
     while (ens.hasMoreElements()) {
      ZipEntry zipEntry=ens.nextElement();
      String fileName=zipEntry.getName().toLowerCase();
      loder lod=new loder(new InputStreamReader(zip.getInputStream(zipEntry)), buf);
      inimap.put(fileName, lod);
      index += 100;
      int ov=index * 10 / size;
      if (ov != now)ui.poss(now = ov);
     }
    } else {
     tmp = new File(ou.getParent(), "tmp");
     ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tmp)));
     out.setLevel(9);
     wt = new BufferedWriter(new OutputStreamWriter(out));
     StringBuilder def= new StringBuilder();
     try {
      while (ens.hasMoreElements()) {
       ZipEntry zipe=ens.nextElement();
       String name;
       if (!zipe.isDirectory() && (name = zipe.getName()).endsWith("i") && name.charAt(7) == 'u') {
        loder loder=new loder(new InputStreamReader(zip.getInputStream(zipe)), def);
        name = name.substring(13).toLowerCase();
        inimap.put(name, loder);
        ++size;
        loder.write(loder, name, out, wt);
       }
       index += 100;
       int ov;
       if ((ov = index / size) != now)ui.poss(now = ov);
      }
      tmp.renameTo(ou);
     } finally {
      tmp.delete();
      wt.close();
     }
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
     lodAllCopy(lod, key, inimap);
    }
    index += 100;
    int ov;
    if ((ov = index / size) != now) {
     ui.poss(now = ov);
    }
   }
   rwmodProtect.wmap = inimap;
   ui.end(null);
  } catch (Throwable e) {
   ui.end(e);
  }
 }
}
