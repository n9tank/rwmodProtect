package rust;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.concurrent.Future;
import java.io.InterruptedIOException;

public class lib implements Runnable {
 InputStream inp;
 File In;
 File Ou;
 ui Ui;
 HashMap iniMap;
 static HashMap libMap;
 public static Future exec(InputStream in, ui ui) {
  lib lib=new lib();
  lib.inp = in;
  lib.Ui = ui;
  return ui.pool.submit(lib);
 }
 public static Future exec(File in, File ou, ui ui) {
  lib lib=new lib();
  lib.In = in;
  lib.Ou = ou;
  lib.Ui = ui;
  return ui.pool.submit(lib);
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
 public void run() {
  ui ui=Ui;
  HashMap inimap=new HashMap();
  iniMap = inimap;
  StringBuilder buf=new StringBuilder();
  File ou=Ou;
  int index=0;
  int now=0;
  int size=0;
  try {
   if (ou == null) {
    BufferedInputStream buff=new BufferedInputStream(inp);
    size = buff.available();
    ZipInputStream zip=new ZipInputStream(buff);
    BufferedReader red=new BufferedReader(new InputStreamReader(zip));
    try {
     ZipEntry zipEntry;
     while ((zipEntry = zip.getNextEntry()) != null) {
      String fileName=zipEntry.getName().toLowerCase();
      loder lod=new loder(red, buf);
      zip.closeEntry();
      inimap.put(fileName, lod);
      index = (size - buff.available()) * 90;
      int ov=index / size;
      if (ov != now)ui.poss(now = ov);
     }
    } finally {
     red.close();
    }
    size = inimap.size();
    index = size * 900;
    size *= 10;
   } else {
    ZipFile zip=new ZipFile(In);
    BufferedWriter wt =null;
    try {
     ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(ou)));
     out.setLevel(9);
     wt = new BufferedWriter(new OutputStreamWriter(out));
     StringBuilder def= new StringBuilder();
     size = zip.size();
     Enumeration<? extends ZipEntry> ens=zip.entries();
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
    } finally {
     if (wt != null)wt.close();
     zip.close();
    }
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
    index += 100;
    int ov;
    if ((ov = index / size) != now) {
     ui.poss(now = ov);
    }
   }
   libMap = inimap;
   ui.end(null);
  } catch (Throwable e) {
   if(ou!=null)ou.delete();
   if(!(e instanceof InterruptedException)){
   ui.end(e);
   }
  }
 }
}
