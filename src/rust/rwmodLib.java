package rust;
import java.util.HashMap;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

public class rwmodLib{
public ZipFile Zip;
public HashMap iniMap;
public HashMap iniHide;
public static HashMap lib;
public static void init(String str){
 rwmodLib rw=new rwmodLib(str);
 HashMap ini=rw.iniMap;
 ini.putAll(rw.iniHide);
 lib=ini;
}
public rwmodLib(String file) {
  HashMap inihide=new HashMap();
  iniHide=inihide;
  HashMap inimap=new HashMap();
  iniMap=inimap;
  StringBuilder buff=new StringBuilder();
  try {
   ZipFile zip=new ZipFile(file);
   Zip=zip;
   Enumeration<? extends ZipEntry> ent=zip.entries();
   while(ent.hasMoreElements()){
    ZipEntry zipEntry=ent.nextElement();
    if(!zipEntry.isDirectory()){
    String fileName=zipEntry.getName();
    loder lod=new loder(zip.getInputStream(zipEntry));
    if(isini(fileName)&&!dontlod(lod)){
     inimap.put(fileName,lod);
    }else{
    inihide.put(fileName,lod);
    }
    }
   }
  } catch (Exception e) {
  }
 }
 public static boolean dontlod(loder lod){
  HashMap map=lod.ini;
  Object o=map.get("core");
  if (o != null) {
   map=(HashMap)o;
   o=map.get("dont_load");
   if(o!=null){
   String str=(String)o;
   map.remove("dont_load");
   return "1".equals(str) ||"true".equalsIgnoreCase(str);
   }
  }
  return false;
 }
 public String toPath(String str) {
  str = str.replaceAll("//+", "/").replaceAll("^//", "");
  if (!str.endsWith("/")) {
   ZipEntry en=Zip.getEntry(str);
   if (en == null) {
    str = str.concat("/");
   }
  }
  return str;
 }
 public loder replace(String str,boolean isini) {
  loder lod;
  str = toPath(str);
  Object o;
  HashMap<String, loder> map=isini?iniMap:iniHide;
  if ((o = map.get(str)) == null) {
   try{
    ZipFile zip=Zip;
    lod=new loder(zip.getInputStream(zip.getEntry(str)));
    map.put(str,lod);
    } catch (Exception e) {
   lod=null;
   }
  }else lod=(loder)o;
  if(lod.str==null){
  lod.str=str;
  replaceCopy(lod,str,isini(str),new StringBuilder());
  }
  return lod;
 }
 public boolean isini(String file){
  int i=file.length();
  if(file.endsWith("/"))--i;
  i-=4;
  if(file.startsWith(".ini",i)&&!iniHide.containsKey(file)){
   return true;
  }else return false;
 }
 public void replaceCopy(loder lod,String file,boolean isini,StringBuilder buff){
  String str=loder.getSuperPath(lod.str);
  if(isini){
   loder put=getSpuerAll(file, buff);
   if(put!=null)loder.put(lod.put, put.put, false);
  }
  Object o=lod.ini.get("core");
  if(o!=null){
   HashMap map=(HashMap)o;
   o=map.get("copyFrom");
   if((str=skip(o,""))!=null){
    String list[]=str.split(",");
    int i=0,l=list.length;
    do{
    str=list[i];
    str=loder.getPath(str,file);
    loder ini=replace(str, isini(str));
    loder.put(lod.put,ini.put,true);
    }while(++i < l);
   }
  }
 loder.put(lod.put,lod.ini,false);
 }
 public static String skip(Object o,String c){
  if(o==null)return null;
  String str=(String)o;
  if(str.equals(c)||str.equals("IGNORE"))return null;
  return str;
 }
 public loder getSpuerAll(String str,StringBuilder buff){
   int i=str.length()-1;
   buff.setLength(0);
   buff.append(str);
   HashMap inihide=iniHide;
   do{
    buff.setLength(i + 1);
    buff.append("all-units.template");
    str = buff.toString();
    if (inihide.containsKey(str)) {
     buff.setLength(0);
     return replace(str,false);
    }
   i = str.lastIndexOf("/", --i);
   }while(i > 0);
   buff.setLength(0);
   return null;
 }
}
