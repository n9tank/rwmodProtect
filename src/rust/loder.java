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
 public HashMap ini;
 public HashMap put;
 public String str;
 public HashMap all;
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
  try {
   if (ite.hasNext()) {
    write(ite, out);
   } else if (size) {
    out.write("[]\n");
   }
   if (size)writeKeys(gloab, ite.hasNext(), out);
   while (ite.hasNext())write(ite, out);
  } finally {
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
 public boolean checkAll(HashMap map) {
  HashMap alls=all;
  if(alls==null)return true;
  Iterator ite=all.entrySet().iterator();
  String str;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   if (o != null) {
    HashMap list=(HashMap)o;
    HashMap find=(HashMap)en.getValue();
    o = list.get("@copyFrom_skipThisSection");
    if (o != null && ((str = (String)o).equals("1") || str.equalsIgnoreCase("true"))) {
     o = find.get("@copyFrom_skipThisSection");
     if (o == null || !str.equalsIgnoreCase((String)o)) {
      Iterator ite2=find.keySet().iterator();
      while (ite2.hasNext()) {
       str = (String)ite2.next();
       if (!list.containsKey(str))return false;
      }
     }
    }
   }
  }
  return true;
 }
 public static void put(HashMap map, HashMap map2) {
  Iterator ite=map2.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   HashMap hash=(HashMap)en.getValue();
   if (o == null) {
    map.put(key, hash.clone());
   } else {
    HashMap set=(HashMap)o;
    set.putAll(hash);
   }
  }
 }
 public static String getRoot(String str) {
  int i=str.indexOf("/");
  if (i >= 0) {
   return str.substring(0, ++i);
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
      if (key == null)return key;
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
  if (str.startsWith("CORE:")) {
   str = null;
  } else if (str.startsWith("ROOT:")) {
   str = str.substring(5);
  } else if (path.length() > 0) {
   str = path.concat(str);
  }
  return str;
 }
 public loder(InputStreamReader input)throws Exception {
  HashMap global=new HashMap();
  BufferedReader buff=new BufferedReader(input);
  String str;
  HashMap list=null;
  HashMap table=new LinkedHashMap();
  table.put("", global);
  ini = table;
  HashSet hashset=null;
  try {
   while ((str = buff.readLine()) != null) {
    str = str.trim();
    if (str.startsWith("#"))continue;
    if (str.startsWith("[") && str.endsWith("]")) {
     str = str.substring(1, str.length() - 1).trim();
     if (str.contains("]"))continue;
     if (str.startsWith("comment_")) {
      list = null;
     } else {
      Object o=wh(str, line, max);
      if (o != null)hashset = (HashSet)o;
      else hashset = null;
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
       if (hashset != null && hashset.contains(key)) {
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
  } finally {
   buff.close();
  }
 }
 public void putoff(HashMap map, HashMap map2, HashMap cous, boolean is) {
  Iterator ite=map2.entrySet().iterator();
  HashMap<String, HashMap> res=rwmodProtect.Res;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   HashMap map3=(HashMap)en.getValue();
   if (o == null) {
    map.put(key, map3.clone());
   } else {
    HashMap set=(HashMap)o;
    set.putAll(map3);
   }
   if (map3 != null)off(key, map3, cous, is);
  }
 }
 public static void off(String key, HashMap list, HashMap cou, boolean is) {
  Object o = wh(key, rwmodProtect.Res, rwmodProtect.max);
  if (o != null) {
   HashMap find=(HashMap)o;
   HashMap list2=(HashMap)cou.get(key);
   if (list2 == null) {
    list2 = new HashMap();
    cou.put(key, list2);
   }
   Iterator ite2=list.keySet().iterator();
   while (ite2.hasNext()) {
    key = (String)ite2.next();
    o = find.get(key);
    if (o != null) {
     list2.put(key, is);
    }
   }
  }
 }
 public HashMap getPut() {
  HashMap pu=put;
  HashMap coe=new HashMap();
  put(coe, pu);
  as(coe);
  return coe;
 }
 public HashMap getAs(HashMap cou) {
  HashMap re=ini;
  HashMap pu=put;
  HashMap hash=new HashMap();
  put(pu,re);
  put(hash, pu);
  Iterator ite=hash.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=re.get(key);
   HashMap list=null;
   HashMap map=(HashMap)en.getValue();
   if (o != null) {
    list = (HashMap)o;
    o = list.get("@copyFrom_skipThisSection");
   }
   if (o == null) {
    o = map.get("@copyFrom_skipThisSection");
   }
   String str;
   if (o != null && ((str = (String)o).equals("1") || str.equalsIgnoreCase("true"))) {
    if (list == null)ite.remove();
    else en.setValue(list);
    continue;
   }
   if (list == null)continue;
   map.putAll(list);
   off(key, list, cou, true);
  }
  return hash;
 }
 public void put(HashMap as, ArrayList need) {
  HashMap hash=as;
  HashMap put=ini;
  int l=need.size();
  while (--l >= 0) {
   Map.Entry en=(Map.Entry)need.get(l);
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
 public ArrayList find(HashMap cous, HashMap coe, HashMap hash) {
  ArrayList<Map.Entry> need=new ArrayList();
  HashMap def=rwmodProtect.Res;
  Iterator ite = hash.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   Object o=wh(ac, def, rwmodProtect.max);
   if (o != null) {
    ArrayList arr=new ArrayList();
    HashMap map=(HashMap)cous.get(ac);
    HashMap list=(HashMap)en.getValue();
    HashMap list2=(HashMap)coe.get(ac);
    HashMap tr=(HashMap)o;
    Iterator ite2=list.entrySet().iterator();
    while (ite2.hasNext()) {
     en = (Map.Entry) ite2.next();
     String key=(String)en.getKey();
     if (tr.get(key) == null)continue;
     o = map.get(key);
     String str;
     if (o == null || o == true || list2 == null || (str = (String)list2.get(key)) == null || !loder.get((String)en.getValue(), hash, list).equals(loder.get(str, coe, list2))) {
      arr.add(key);
     }
    }
    if (arr.size() > 0) {
     need.add(Map.entry(ac, arr));
    }
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
     if (set != null)mapput.putAll(set);
    }
   }
   Object o=hash.get("@copyFromSection");
   if (o != null) {
    key = (String)o;
    HashMap set=(HashMap)map.get(key);
    if (set != null)mapput.putAll(set);
   }
   if (mapput.size() > 0) {
    mapput.putAll(hash);
    en.setValue(mapput);
   }
  }
 }
}
