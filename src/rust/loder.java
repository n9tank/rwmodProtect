package rust;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class loder {
 HashMap ini;
 HashMap put;
 HashMap all;
 String str;
 static int max;
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
 static void writeKeys(HashMap map, OutputStreamWriter out)throws Exception {
  Iterator<Map.Entry> ite=map.entrySet().iterator();
  boolean nx=ite.hasNext();
  while (nx) {
   Map.Entry en = ite.next();
   out.write((String)en.getKey());
   out.write(':');
   out.write((String)en.getValue());
   nx = ite.hasNext();
   if (nx)out.write('\n');
  }
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
      boolean isNumber=find2.matcher(key).find();
      HashSet sset=set;
      Matcher matcher=find.matcher(key);
      int q=0,st=buff.length();
      while (matcher.find()) {
       buff.append(key, q, matcher.start());
       q = matcher.end();
       String group = matcher.group(0);
       if (!sset.contains(group)) {
        String list[]=group.split("\\.", 2);
        String keyv=list[0];
        Object o;
        tag:
        if (list.length > 1) {
         HashMap locv;
         if (keyv.equals("section"))locv = loc;
         else {
          locv = (HashMap)map.get(keyv);
          if (locv == null)return null;
         }
         group = (String)locv.get(list[1]);
        } else {
         o = loc.get("@define ".concat(keyv));
         if (o == null) {
          o = ((HashMap)map.get("")).get("@global ".concat(keyv));
         }
         group = (String)o;
        }
       }
       if (group == null)return null;
       buff.append(group);
      }
      int m = key.length();
      if (m > q)buff.append(key, q, m);
      key = buff.substring(st, buff.length());
      if (isNumber) {
       buff.setLength(st);
       cmp cVar = new cmp(key);
       cVar.a();
       double b = cVar.b();
       int intd=(int)b;
       if (intd == b) {
        key = String.valueOf(intd);
       } else {
        key = String.valueOf(b);
       }
       buff.append(key);
      }
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
    }
   }
  }
 }
 static void as(HashMap map) {
  Iterator ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   HashMap hash=(HashMap)en.getValue();
   HashMap mapput=new HashMap();
   int i=key.indexOf("_");
   if (i > 0) {
    Object o=hash.get("copyFrom");
    if (o != null) {
     String vl=key.substring(0, ++i);
     HashMap set=(HashMap)map.get(vl.concat((String)o));
     if (set != null)mapput.putAll(set);
    }
   }
   Object o=hash.get("@copyFromSection");
   if (o != null) {
    key = (String)o;
    String list[]=key.split(",");
    i = 0;
    int l=list.length;
    while (i < l) {
     HashMap set=(HashMap)map.get(list[i++].trim());
     if (set != null)mapput.putAll(set);
    }
   }
   if (mapput.size() > 0) {
    mapput.putAll(hash);
    en.setValue(mapput);
   }
  }
 }
 static final HashSet set;
 static{
  HashSet sset=new HashSet();
  set = sset;
  sset.add("int");
  sset.add("cos");
  sset.add("sin");
  sset.add("sqrt");
 }
 static final Pattern find=Pattern.compile("[aA-zZ_][aA-zZ_.0-9]*");;
 static final Pattern find2=Pattern.compile("[-+/*^%()]");
}
