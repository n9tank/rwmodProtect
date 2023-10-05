package rust;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class rwmodLib {
 public StringBuilder Buff;
 public ZipFile Zip;
 public HashMap iniMap;
 public HashMap iniHide;
 public ui Ui;
 public HashMap<String,ZipEntry> low;
 public static rwmodLib lib;
 public static void init(File str) {
  if(!str.exists())return;
  rwmodLib rw=new rwmodLib(str,ui.def);
  HashMap ini=rw.iniMap;
  ini.putAll(rw.iniHide);
  rw.iniHide = null;
  lib = rw;
 }
 rwmodLib(){}
 public rwmodLib(File file, ui ui) {
  Ui=ui;
  HashMap inihide=new HashMap();
  iniHide = inihide;
  HashMap inimap=new HashMap();
  iniMap = inimap;
  HashMap lows=new HashMap();
  low=lows;
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
     if (isini(fileName)&&dontlod(lod)) {
      inimap.put(fileName, lod);
     } else {
      inihide.put(fileName, lod);
     }
     String lowr=fileName.toLowerCase();
     if(!lows.containsKey(lowr)){
     lows.put(lowr,zipEntry);
     }
    }
   }
   Iterator ite=inimap.entrySet().iterator();
   while (ite.hasNext()) {
    Map.Entry en=(Map.Entry)ite.next();
    String key=(String)en.getKey();
    loder lod=(loder)en.getValue();
    if (lod.str == null) {
     lod.str="";
     lodAllCopy(lod, key, true);
    }
   }
  } catch (Exception e) {
   ui.fali(e);
  }
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
 public ZipEntry toPath(String str) {
  str = str.replaceFirst("^/+","");
  HashMap<String,ZipEntry> lowm=low;
  ZipFile zip=Zip;
  ZipEntry en=Zip.getEntry(str);
  String lows=str.toLowerCase();
  if (en == null) {
  ZipEntry r=lowm.get(lows);
  if(r!=null)return r;
  }else return en;
  if(!str.endsWith("/")) {
   str=str.concat("/");
   if((en=zip.getEntry(str))==null){
   return lowm.get(lows.concat("/"));
   }
  }
  return en;
 }
 public loder replace(String str, boolean isini) {
  loder lod;
  ZipEntry en =toPath(str);
  Object o;
  HashMap<String, loder> map=isini ?iniMap: iniHide;
  if ((o = map.get(str)) == null) {
   try {
    ZipFile zip=Zip;
    lod = new loder(zip.getInputStream(en));
    map.put(str, lod);
   } catch (Exception e) {
    Ui.fali(e);
    lod = null;
   }
  } else lod = (loder)o;
  if (lod.str == null) {
   lod.str ="";
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
  String str=loder.getSuperPath(file);
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
 public void write(loder lod,String str,boolean isini,StringBuilder buff){
 lod.str="";
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
   loder lod;
   ZipEntry en=toPath(str);
   if (en!=null) {
    lod=(loder)iniHide.get(str=en.getName());
    buff.setLength(0);
    if(lod.str==null)write(lod,str,false,new StringBuilder());
    return lod;
   }
  }while(i > 0);
  buff.setLength(0);
  return null;
 }
}
