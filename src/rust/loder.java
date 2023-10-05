package rust;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class loder {
 final static HashSet em=new HashSet();
 public HashMap ini;
 public HashMap put;
 public HashMap as;
 public String str;
 public HashMap cou;
 public int end;
 public static int max;
 public static int vlmax;
 public static HashSet vlset;
 public static HashMap<String,HashSet> line=new HashMap();
 public loder(InputStream input) throws Exception {
  this(new InputStreamReader(input));
 }
 public static void writeKeys(HashMap map, boolean hasNext, OutputStreamWriter out)throws Exception {
  Iterator<Map.Entry> ite=map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en = ite.next();
   out.write((String)en.getKey());
   out.write(':');
   out.write((String)en.getValue());
   if (hasNext || ite.hasNext())out.write('\n');
  }
 }
 public static void write(Iterator<Map.Entry<String,HashMap>> ite, OutputStreamWriter out) throws Exception {
  Map.Entry<String,HashMap> en=ite.next();
  HashMap map=en.getValue();
  if (map.size() > 0) {
   out.write('[');
   out.write(en.getKey());
   out.write("]\n");
   writeKeys(map, ite.hasNext(), out);
  }
 }
 public void write(OutputStreamWriter out) throws Exception {
  HashMap map=ini;
  HashMap gloab=(HashMap)ini.get("");
  ini.remove("");
  boolean size=gloab.size() > 0;
  Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
  try{
  if (ite.hasNext()) {
   write(ite, out);
  } else if (size) {
   out.write("[]\n");
  }
  if (size)writeKeys(gloab, ite.hasNext(), out);
  while (ite.hasNext())write(ite, out);
  }finally{
  out.flush();
  }
 }
 public static String wh(String str, HashSet set, int m) {
  int i=0;
  String vl=str;
  do{
   if (set.contains(vl)) {
    return vl;
   }
   i = str.indexOf("_", ++i);
   if (i > 0) {
    vl = str.substring(0, i + 1);
   } else break;
  }while(--m >= 0);
  return null;
 }
 public static Object wh(String str, HashMap map, int m) {
  int i=0;
  String vl=str;
  do{
   Object o=map.get(vl);
   if (o != null)return o;
   i = str.indexOf("_", ++i);
   if (i > 0) {
    vl = str.substring(0, i + 1);
   } else break;
  }while(--m >= 0 && i > 0);
  return null;
 }
 public void put(loder lod,boolean skip){
  int ed=end;
  HashMap map=ini;
  HashMap map2=lod.put;
  HashMap cous=cou;
  Iterator ite=map2.entrySet().iterator();
  HashMap<String, HashMap> res=rwmodProtect.Res;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   Object hash=en.getValue();
   HashMap map3;
   if (o == null) {
    map3=(HashMap)hash;
    map.put(key,map3.clone());
   } else {
    HashMap set=(HashMap)o;
    String k;
    if (skip||(o = set.get("@copyFrom_skipThisSection")) == null || (!(k = (String)o).equals("1") && !k.equalsIgnoreCase("true"))) {
     map3=(HashMap)hash;
     set.putAll(map3);
    }else map3=null;
   }
   if (map3!=null) {
    o=wh(key,res,rwmodProtect.max);
    if(o!=null){
     HashMap find=(HashMap)o;
     HashMap list=(HashMap)cous.get(key);
     if(list==null){
     list=new HashMap();
     cous.put(key,list);
     }
     Iterator ite2=map3.keySet().iterator();
     while(ite2.hasNext()){
     key=(String) ite2.next();
     if(find.get(key)!=null){
     Object r=list.get(key);
     if (r != null) {
     ((IntOff)r).off=ed;
     }else{
     list.put(key,new IntOff());
     }
    }
    }
   }
   }
  }
  end=++ed;
 }
 public void put(HashMap need) {
  HashMap hash=as;
  HashMap put=ini;
  Iterator ite=need.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String str=(String)en.getKey();
   HashMap map=(HashMap)hash.get(str);
   Object o=put.get(str);
   HashMap putall;
   if (o == null) {
    putall = new HashMap();
    put.put(str, putall);
   } else putall = (HashMap)o;
   ArrayList list=(ArrayList)en.getValue();
   int i=list.size();
   while (--i >= 0) {
    str = (String)list.get(i);
    putall.put(str, map.get(str));
   }
  }
 }
 public HashMap<String,ArrayList> eqz() {
  HashMap pu=put;
  HashMap re=ini;
  HashMap map=new HashMap();
  put(map, pu, true);
  as(map);
  put(pu, re, false);
  HashMap hash=as;
  put(hash,pu,true);
  as(hash);
  HashMap need=new HashMap();
  HashMap def=rwmodProtect.Res;
  Iterator ite = hash.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=wh(key,def,rwmodProtect.max);
   if (o != null) {
    ArrayList needL=new ArrayList();
    HashMap <String, Integer> list=(HashMap)o;
    HashMap<String,String> list2=(HashMap)en.getValue();
    HashMap list3=(HashMap)map.get(key);
    HashMap list4=(HashMap)re.get(key);
    Iterator<Map.Entry<String, String>> ite2=list2.entrySet().iterator();
    while (ite2.hasNext()) {
     Map.Entry<String, String> en2=ite2.next();
     String ke = en2.getKey();
     String value=en2.getValue();
     String ov=null;
     boolean eqz=true;
     if (list.get(ke) != null && (list3 == null || (ov = (String)list3.get(ke)) == null || (eqz = !get(value, hash, list2).equals(get(ov, map, list3))))) {
      needL.add(ke);
     } else if (list4 != null && !eqz) {
      list4.remove(ke);
     }
    }
    if (needL.size() > 0)need.put(key, needL);
   }
  }
  return need;
 }
 public static void as(HashMap map) {
  Iterator ite=map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   HashMap hash=(HashMap)en.getValue();
   String vl;
   HashMap mapput=new HashMap();
   if ((vl = wh(key, vlset, vlmax)) != null) {
    Object o=hash.get("copyFrom");
    if (o != null) {
     key = (String)o;
     HashMap set=(HashMap)map.get(vl.concat(key));
     mapput.putAll(set);
    }
   }
   Object o=hash.get("@copyFromSection");
   if (o != null) {
    key = (String)o;
    HashMap set=(HashMap)map.get(key);
    mapput.putAll(set);
   }
   if(mapput.size()>0){
    mapput.putAll(hash);
    en.setValue(mapput);
   }
  }
 }
 public static void put(HashMap map, HashMap map2, boolean skip) {
  Iterator ite=map2.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   Object hash=en.getValue();
   if (o == null) {
    map.put(key, ((HashMap)hash).clone());
   } else {
    HashMap set=(HashMap)o;
    String k;
    if (skip || (o = set.get("@copyFrom_skipThisSection")) == null || (!(k = (String)o).equals("1") && !k.equalsIgnoreCase("true"))) {
     set.putAll((HashMap)hash);
    }
   }
  }
 }
 public static String getRoot(String str){
  int i=str.indexOf("/");
  if(i>=0){
   return str.substring(0,++i);
  }
  return "";
 }
 public static String getName(String file) {
  int i=file.length();
  if (file.endsWith("/")) {
   file = file.substring(0, --i);
  }
  i = file.lastIndexOf("/", --i);
  if (i > 0)file = file.substring(++i);
  return file;
 }
 public static String getSuperPath(String str) {
  int i=str.length();
  if (str.endsWith("/"))--i;
  i = str.lastIndexOf("/", --i);
  if (i > 0) {
   return str.substring(0, ++i);
  }
  return "";
 }
 public static String getImagePath(String str, String path, StringBuilder buff) {
  boolean shadow=false;
  boolean root=false;
  if (str.startsWith("SHADOW:")) {
   shadow = true;
   str = str.substring(7);
  }
  if (str.startsWith("CORE:") || str.startsWith("SHARED:")) {
   return null;
  }
  if (str.startsWith("ROOT:")) {
   root = true;
   str = str.substring(5);
  }
  if (str.startsWith("SHADOW:")) {
   shadow = true;
   str = str.substring(7);
  }
  if (!root && path.length() > 0) {
   str = path.concat(str);
  }
  if (shadow) {
   buff.append("SHADOW:");
  }
  return str;
 }
 public static String get(String str, HashMap map, HashMap loc) {
  int i=0,j=0;
  StringBuilder buff=new StringBuilder();
  do{
   i = str.indexOf("${", i);
   if (i >= 0) {
    buff.append(str, j, i);
    j = i;
    int n=str.indexOf("}", i += 2);
    if (n > 0) {
     String key=str.substring(i, n).trim();
     if (key.length() > 0) {
      key = off.off(map, loc, key);
     }
     buff.append(key);
     j = i = ++n;
    } else break;
   } else break;
  }while(true);
  buff.append(str, j, str.length());
  return buff.toString();
 }
 public static String getPath(String str, String path) {
  String s;
  if (str.startsWith(s = "CORE:")) {
   str = null;
  } else if (str.startsWith("ROOT:")) {
   str = str.substring(5);
  } else if (path.length() > 0) {
   str = path.concat(str);
  }
  return str;
 }
 public loder(InputStreamReader input)throws Exception {
  HashMap map=new HashMap();
  HashMap global=new HashMap();
  put = map;
  cou=new HashMap();
  BufferedReader buff=new BufferedReader(input);
  String str;
  HashMap list=null;
  HashMap table=new LinkedHashMap();
  table.put("",global);
  ini = table;
  HashSet hashset=em;
  try {
   while ((str = buff.readLine()) != null) {
    str = str.trim();
    if (str.startsWith("#"))continue;
    if (str.startsWith("[")&&str.endsWith("]")) {
     str = str.substring(1, str.length() - 1).trim();
     if (str.contains("]"))continue;
     if (str.startsWith("comment_")) {
      list = null;
     } else {
      Object o=wh(str, line, max);
      if (o != null) {
       hashset = (HashSet)o;
      } else hashset = em;
      list = new HashMap();
      table.put(str, list);
     }
    } else if (list != null) {
     String value[]=str.split("[=:]", 2);
     if (value.length > 1) {
      String key=value[0].trim();
      String set=value[1].trim();
      if (key.startsWith("@gloabl ")) {
       if (value.equals("IGNORE"))continue;
       global.put(key, value);
      } else {
       if (hashset.contains(key)) {
        String with;
        if (set.startsWith(with = "\"\"\"") || set.startsWith(with = "\'\'\'")) {
         set = set.substring(3);
         StringBuilder bf=new StringBuilder();
         do{
          set = set.trim();
          if (set.endsWith(with)) {
           bf.append(set, 0, set.length() - 3);
           break;
          }
          bf.append(set);
         }while((set = buff.readLine()) != null);
         set = bf.toString();
        }
       }
       list.put(key, set);
      }
     } else continue;
    }
   }
  }finally {
   buff.close();
  }
 }
}
