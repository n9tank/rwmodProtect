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
import java.util.regex.Pattern;
class loder {
 HashMap ini;
 HashMap put;
 HashMap all;
 String str;
 static int max;
 static int vlmax;
 static HashSet vlset;
 static final Pattern fin=Pattern.compile("^(?:SHADOW:)?(?:CORE|SHARED):");
 static HashMap<String,HashSet> line=new HashMap();
 loder(InputStreamReader input, StringBuilder buff)throws Exception {
  BufferedReader in=new BufferedReader(input);
  try {
   init(in, buff);
  } finally {
   in.close();
  }
 }
 loder(BufferedReader in, StringBuilder buff)throws Exception {
  init(in, buff);
 }
 static void writeKeys(HashMap map, boolean hasNext, OutputStreamWriter out)throws Exception {
  Iterator<Map.Entry> ite=map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en = ite.next();
   out.write((String)en.getKey());
   out.write(':');
   out.write((String)en.getValue());
   if (hasNext || ite.hasNext())out.write('\n');
  }
 }
 static void write(Iterator<Map.Entry<String,HashMap>> ite, OutputStreamWriter out) throws Exception {
  Map.Entry<String,HashMap> en=ite.next();
  HashMap map=en.getValue();
  if (map.size() > 0) {
   out.write('[');
   out.write(en.getKey());
   out.write("]\n");
   writeKeys(map, ite.hasNext(), out);
  }
 }
 void write(OutputStreamWriter out) throws Exception {
  HashMap map=ini;
  HashMap gloab=(HashMap)ini.get("");
  ini.remove("");
  boolean size=gloab.size() > 0;
  Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
   if (ite.hasNext()) {
    write(ite, out);
   } else if (size) {
    out.write("[]\n");
   }
   if (size)writeKeys(gloab, ite.hasNext(), out);
   while (ite.hasNext())write(ite, out);
   out.flush();
 }
 static String wh(String str, HashSet set, int m) {
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
 static Object wh(String str, HashMap map, int m) {
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
 static void put(HashMap map, HashMap map2) {
  Iterator ite=map2.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   Object o2=en.getValue();
   if (o2 instanceof HashMap) {
    HashMap hash=(HashMap)o2;
    if (o == null) {
     map.put(key, hash.clone());
    } else {
     HashMap set=(HashMap)o;
     set.putAll(hash);
    }
   } else {
    cpys cpy=(cpys)o2;
    map.put(key, (cpy.is ?cpy.skip: cpy.m).clone());
   }
  }
 }
 static void putAnd(HashMap map, HashMap map2, HashMap cou, byte is) {
  Iterator ite=map2.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   Object o=map.get(key);
   Object o2=en.getValue();
   cpys cp2=null;
   HashMap hash;
   cpys cpy=null;
   boolean si=false;
   HashMap set=null;
   if (o2 instanceof HashMap) {
    hash = (HashMap)o2;
   } else {
    cp2 = (cpys)o2;
    hash = cp2.m;
   }
   tag: {
    if (o == null) {
     if (cp2 != null) {
      si = cp2.is;
      set = (HashMap)hash.clone();
     } else {
      o = hash.clone();
      map.put(key, hash.clone());
      break tag;
     }
    } else if (o instanceof HashMap) {
     set = (HashMap)o;
     set.putAll(hash);
    } else {
     cpy = (cpys)o;
     set = cpy.m;
     set.putAll(hash);
    }
    String str=null;
    if (cp2 == null) {
     str = (String)hash.get("@copyFrom_skipThisSection");
     si = str != null && (str.equals("1") || str.equalsIgnoreCase("true"));
    }
    if (si) {
     if (cpy == null) {
      cpy = new cpys();
      map.put(key, cpy);
      cpy.m = set;
     }
     cpy.skip = (HashMap)(cp2 == null ?hash: cp2.skip).clone();
    } else {
     if (cpy != null) {
      cpy.skip.putAll(hash);
     } else if (cp2 != null) {
      cpy = new cpys();
      map.put(key, cpy);
      cpy.m = set;
      cpy.skip = (HashMap)hash.clone();
     }
    }
    if (str != null && cpy != null)cpy.is = si;
   }
   if (cou != null) {
    o = wh(key, rwmodProtect.Res, rwmodProtect.max);
    if (o != null) {
     HashMap find=(HashMap)o;
     HashMap list2=(HashMap)cou.get(key);
     if (list2 == null) {
      list2 = new HashMap();
      cou.put(key, list2);
     }
     Iterator ite2=hash.keySet().iterator();
     while (ite2.hasNext()) {
      key = (String)ite2.next();
      o = find.get(key);
      if (is == 2 && !si)continue;
      if (o != null) {
       list2.put(key, (is & 1) == 0);
      }
     }
    }
   }
  }
 }
 static String getRoot(String str) {
  int i=str.indexOf("/");
  if (i >= 0) {
   return str.substring(0, ++i);
  }
  return "";
 }
 static String getName(String file) {
  int i=file.length();
  if (file.endsWith("/")) {
   file = file.substring(0, --i);
  }
  i = file.lastIndexOf("/", --i);
  if (i > 0)file = file.substring(++i);
  return file;
 }
 static String getSuperPath(String str) {
  int i=str.length();
  if (str.endsWith("/"))--i;
  i = str.lastIndexOf("/", --i);
  if (i > 0) {
   return str.substring(0, ++i);
  }
  return "";
 }
 static String get(String str, HashMap map, HashMap loc, StringBuilder buff) {
  int i=0,j=0;
  buff.setLength(0);
  do{
   i = str.indexOf("${", i);
   if (i >= 0) {
    buff.append(str, j, i);
    j = i;
    int n=str.indexOf("}", i += 2);
    if (n > 0) {
     String key=str.substring(i, n).trim();
     if (key.length() > 0) {
      if (!off.off(map, loc, key, buff))return null;
     }
     j = i = ++n;
    } else break;
   } else break;
  }while(true);
  buff.append(str, j, str.length());
  return buff.toString();
 }
 void init(BufferedReader buff, StringBuilder bf)throws Exception {
  HashMap global=new HashMap();
  String str;
  HashMap list=null;
  HashMap lines=line;
  HashMap table=new LinkedHashMap();
  table.put("", global);
  ini = table;
  HashSet hashset=null;
  while ((str = buff.readLine()) != null) {
   str = str.trim();
   if (str.startsWith("#"))continue;
   if (str.startsWith("[") && str.endsWith("]")) {
    str = str.substring(1, str.length() - 1).trim();
    if (str.contains("]"))continue;
    if (str.startsWith("comment_")) {
     list = null;
    } else {
     Object o=wh(str, lines, max);
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
     if (key.startsWith("@global ")) {
      if (value.equals("IGNORE"))continue;
      global.put(key, set);
     } else {
      if (hashset != null && hashset.contains(key)) {
       String with;
       if (set.startsWith(with = "\"\"\"") || set.startsWith(with = "\'\'\'")) {
        set = set.substring(3);
        bf.setLength(0);
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
 }
 HashMap getPut() {
  HashMap pu=put;
  HashMap coe=new HashMap();
  put(coe, pu);
  as(coe);
  return coe;
 }
 HashMap getAs(HashMap cou) {
  HashMap pu=put;
  HashMap hash=new HashMap();
  putAnd(pu, ini, cou, (byte)0);
  put(hash, pu);
  as(hash);
  return hash;
 }
 void put(HashMap as, ArrayList need) {
  HashMap hash=as;
  HashMap put=ini;
  int l=need.size();
  while (--l >= 0) {
   String str=(String)need.get(l);
   HashMap map=(HashMap)hash.get(str);
   Object o=put.get(str);
   HashMap putall;
   if (o == null) {
    putall = new HashMap();
    put.put(str, putall);
   } else putall = (HashMap)o;
   ArrayList list=(ArrayList)need.get(--l);
   int i=list.size();
   while (--i >= 0) {
    str = (String)list.get(i);
    putall.put(str, map.get(str));
   }
  }
 }
 ArrayList find(HashMap cous, HashMap coe, HashMap hash, StringBuilder buff) {
  ArrayList need=new ArrayList();
  HashMap def=rwmodProtect.Res;
  HashMap re=ini;
  Iterator ite = hash.entrySet().iterator();
  Pattern fud=fin;
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String ac=(String)en.getKey();
   HashMap tr=(HashMap)wh(ac, def, rwmodProtect.max);
   ArrayList arr=new ArrayList();
   HashMap map=(HashMap)cous.get(ac);
   HashMap list=(HashMap)en.getValue();
   HashMap list2=(HashMap)coe.get(ac);
   HashMap list3=(HashMap)re.get(ac);
   Iterator ite2=list.entrySet().iterator();
   while (ite2.hasNext()) {
    en = (Map.Entry) ite2.next();
    String key=(String)en.getKey();
    Object o;
    if (map != null)o = map.get(key);
    else o = key;
    String str,vl,v=(String)en.getValue();
    vl = get(v, hash, list, buff);
    boolean eq=true;
    if (o == true || list2 == null || (str = (String)list2.get(key)) == null || (eq = vl == null ?!v.equals(str): !vl.equals(get(str, coe, list2, buff)))) {
     if (vl != null && tr != null && tr.get(key) != null && !fud.matcher(vl).find()) {
      arr.add(key);
     }
    } else if (list3 != null && !eq) {
     //去重复键 opt
     list3.remove(key);
    }
    if (arr.size() > 0) {
     need.add(arr);
     need.add(ac);
    }
   }
  }
  return need;
 }
 static void as(HashMap map) {
  Iterator ite = map.entrySet().iterator();
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
