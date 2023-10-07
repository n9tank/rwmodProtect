package rust;
import java.io.File;
import java.io.IOException;
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
 public static HashMap wmap;
 public static loder get(String str){
  str=str.replaceFirst("^/+","");
  HashMap map=wmap;
  Object o=map.get(str);
  str=str.toLowerCase();
  if (o == null) {
   o=map.get(str);
  }
  if(o==null&&!str.endsWith("/")){
   o=map.get(str.concat("/"));
  }
  return (loder)o;
 }
 public static void init(File file) {
  if (!file.exists())return;
  ui ur=ui.def;
  rwmodLib rw=new rwmodLib(file, ur);
  HashMap<String,loder> ini=rw.iniMap;
  ini.putAll(rw.iniHide);
  wmap=ini;
 }
 rwmodLib(){}
 public rwmodLib(File file, ui ui){
  Ui = ui;
  HashMap inihide=new HashMap();
  iniHide = inihide;
  HashMap inimap=new HashMap();
  iniMap = inimap;
  Buff = new StringBuilder();
  try{
  ZipFile zip=new ZipFile(file);
  Zip = zip;
  Enumeration<? extends ZipEntry> ent=zip.entries();
  try {
   while (ent.hasMoreElements()) {
    ZipEntry zipEntry=ent.nextElement();
     String fileName=zipEntry.getName().toLowerCase();
     loder lod=new loder(zip.getInputStream(zipEntry));
     if (isini(fileName)&&!dontlod(lod)) {
      inimap.put(fileName,lod);
     } else {
      inihide.put(fileName,lod);
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
   }
  } catch (Exception e) {
   ui.fali(e);
  }
  zip.close();
  }catch(Exception e){
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
 public loder getlod(String str) {
  str=str.toLowerCase();
  Object o=iniMap.get(str);
  if(o==null){
   o=iniHide.get(str);
  }
  loder lod=(loder)o;
  if (lod.str==null) {
   lod.str = "";
   lodAllCopy(lod, str);
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
 public void lodAllCopy(loder lod, String file) {
  file=loder.getSuperPath(file);
  HashMap ini=lod.ini;
  Object o=ini.get("core");
  HashMap put=null;
  if (o != null) {
   HashMap map=(HashMap)o;
   o = map.get("copyFrom");
   if (o != null/* && (str = (String)o).length() > 0 && !str.equals("IGNORE")*/) {
    String str=(String)o;
    put=new HashMap();
    String list[]=str.split(",");
    int i=0,l=list.length;
    do{
     str = list[i];
     str = loder.getPath(str,file);
     loder loder=getlod(str);
     loder.put(put,loder.put,null,false);
    }while(++i < l);
   }
  }
  if(put!=null){
  loder.put(put,ini,null,false);
  }else put=ini;
  lod.put=put;
  lod.ini=null;
 }
}
