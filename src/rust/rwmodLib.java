package rust;
import java.util.HashMap;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.Iterator;
import java.util.Map;

public class rwmodLib {
 public StringBuilder Buff;
 public ZipFile Zip;
 public HashMap iniMap;
 public HashMap iniHide;
 public ui Ui;
 public static rwmodLib lib;
 public static void init(String str, ui ui) {
  rwmodLib rw=new rwmodLib(str, ui);
  HashMap ini=rw.iniMap;
  ini.putAll(rw.iniHide);
  rw.iniHide = null;
  lib = rw;
 }
 public rwmodLib() {}
 public rwmodLib(String file, ui ui) {
  Ui=ui;
  HashMap inihide=new HashMap();
  iniHide = inihide;
  HashMap inimap=new HashMap();
  iniMap = inimap;
  Buff = new StringBuilder();
  try {
   ZipFile zip=new ZipFile(file);
   Zip = zip;
   Enumeration<? extends ZipEntry> ent=zip.entries();
   while (ent.hasMoreElements()) {
    ZipEntry zipEntry=ent.nextElement();
    if (!zipEntry.isDirectory()) {
     String fileName=zipEntry.getName();
     loder lod=new loder(zip.getInputStream(zipEntry));
     if (isini(fileName) && dontlod(lod)) {
      inimap.put(fileName, lod);
     } else {
      inihide.put(fileName, lod);
     }
    }
   }
   Iterator ite=inimap.entrySet().iterator();
   while (ite.hasNext()) {
    Map.Entry en=(Map.Entry)ite.next();
    String key=(String)en.getKey();
    loder lod=(loder)en.getValue();
    if (lod.str == null) {
     lodAllCopy(lod, key, true);
    }
   }
  } catch (Exception e) {
   ui.fali(e);
  }
  ui.finsh();
 }
 public static boolean dontlod(loder lod) {
  Object o=lod.ini.get("core");
  if (o != null) {
   HashMap map=(HashMap)o;
   o = map.get("dont_load");
   if (o != null) {
    String str=(String)o;
    map.remove("dont_load");
    return "1".equals(str) || "true".equalsIgnoreCase(str);
   }
  }
  return false;
 }
 public String toPath(String str) {
  str = str.replaceAll("/{2}", "/").replaceAll("^/", "");
  if (!str.endsWith("/")) {
   ZipEntry en=Zip.getEntry(str);
   if (en == null) {
    str = str.concat("/");
   }
  }
  return str;
 }
 public loder replace(String str, boolean isini) {
  loder lod;
  str = toPath(str);
  Object o;
  HashMap<String, loder> map=isini ?iniMap: iniHide;
  if ((o = map.get(str)) == null) {
   try {
    ZipFile zip=Zip;
    lod = new loder(zip.getInputStream(zip.getEntry(str)));
    map.put(str, lod);
   } catch (Exception e) {
    Ui.fali(e);
    lod = null;
   }
  } else lod = (loder)o;
  if (lod.str == null) {
   lod.str = str;
   lodAllCopy(lod, str, isini(str));
  }
  return lod;
 }
 public boolean isini(String file) {
  int i=file.length();
  if (file.endsWith("/"))--i;
  i -= 4;
  if (file.startsWith(".ini", i) && !iniHide.containsKey(file)) {
   return true;
  } else return false;
 }
 public void lodAllCopy(loder lod, String file, boolean isini) {
  String str=loder.getSuperPath(lod.str);
  if (isini) {
   loder put=getSpuerAll(file, Buff);
   if (put != null)loder.put(lod.put, put.put, false);
  }
  Object o=lod.ini.get("core");
  if (o != null) {
   HashMap map=(HashMap)o;
   o = map.get("copyFrom");
   if (o != null && (str = (String)o).length() > 0 && str.equals("IGNORE")) {
    String list[]=str.split(",");
    int i=0,l=list.length;
    do{
     str = list[i];
     str = loder.getPath(str, file);
     loder ini=replace(str, isini(str));
     loder.put(lod.put, ini.put, true);
    }while(++i < l);
   }
  }
  loder.put(lod.put, lod.ini, false);
 }
 public loder getSpuerAll(String str, StringBuilder buff) {
  int i=str.length();
  buff.setLength(0);
  buff.append(str);
  HashMap inihide=iniHide;
  do{
   i = str.lastIndexOf("/", --i);
   buff.setLength(i + 1);
   buff.append("all-units.template");
   str = buff.toString();
   if (inihide.containsKey(str) || inihide.containsKey(str = str.concat("/"))) {
    buff.setLength(0);
    return replace(str, false);
   }
  }while(i > 0);
  buff.setLength(0);
  return null;
 }
}
