package rust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
class loder {
 HashMap ini;
 HashMap put;
 HashMap all;
 String str;
 loder(InputStreamReader input, StringBuilder buff)throws IOException {
  BufferedReader in=new BufferedReader(input);
  try {
   init(in, buff);
  } finally {
   in.close();
  }
 }
 loder(BufferedReader in, StringBuilder buff)throws IOException {
  init(in, buff);
 }
 static void write(loder ini, ZipOutputStream zip, OutputStreamWriter out) throws IOException {
  zip.putNextEntry(new ZipEntry(ini.str));
  HashMap map=ini.ini;
  HashMap gloab=(HashMap)map.get("");
  map.remove("");
  Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
  boolean wt=false;
  if (ite.hasNext()) {
   Map.Entry<String,HashMap> en = ite.next();
   map = en.getValue();
   boolean size=map.size() > 0;
   while (true) {
    if (size) {
     wt = true;
     out.write('[');
     out.write(en.getKey());
     out.write("]\n");
     loder.writeKeys(map, out);
    }
    if (!ite.hasNext())break;
    en = ite.next();
    map = en.getValue();
    size = map.size() > 0;
    if (size)out.write('\n');
   }
  }
  if (gloab.size() > 0) {
   if (!wt)out.write("[]");
   out.write('\n');
   loder.writeKeys(gloab, out);
  }
  out.flush();
  zip.closeEntry();
 }
 static void writeKeys(HashMap map, OutputStreamWriter out)throws IOException {
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
 static void putAnd(HashMap map, HashMap map2, HashMap cou, boolean is) {
  Iterator ite=map2.entrySet().iterator();
  HashMap<String, HashMap> res=rwmodProtect.Res;
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
   String str=null;
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
    } else {
     cpy = (cpys)o;
     set = cpy.m;
    }
    if (cp2 == null) {
     str = (String)hash.get("@copyFrom_skipThisSection");
     si = str != null && (str.equals("1") || str.equalsIgnoreCase("true"));
    }
    if (o != null)set.putAll(hash);
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
    o = wh(key, res, rwmodProtect.max);
    if (o != null) {
     HashMap fid=(HashMap)o;
     o = cou.get(key);
     cpy = null;
     HashMap put;
     if (o == null) {
      put = new HashMap();
      cou.put(key, put);
     } else if (o instanceof HashMap) {
      put = (HashMap)o;
     } else {
      cpy = (cpys)o;
      put = cpy.m;
     }
     Iterator ite2=hash.entrySet().iterator();
     HashMap now=new HashMap();
     while (ite2.hasNext()) {
      en = (Map.Entry)ite2.next();
      key = (String)en.getKey();
      if (fid.containsKey(key)) {
       now.put(key, is);
      }
     }
     put.putAll(now);
     if (si) {
      if (cpy == null) {
       cpy = new cpys();
       cou.put(key, cpy);
      }
      cpy.skip = now;
     } else {
      if (cpy != null) {
       cpy.skip.putAll(now);
      }
     }
     if (str != null && cpy != null)cpy.is = si;
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
 static String get(String str, String eqz, HashMap map, HashMap loc, StringBuilder buff) {
  int i=0,j=0;
  buff.setLength(0);
  HashMap gl=(HashMap)map.get("");
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
         if (keyv.equals("section") || keyv.equals(eqz))locv = loc;
         else {
          locv = (HashMap)map.get(keyv);
          if (locv == null)return null;
         }
         group = (String)locv.get(list[1]);
        } else {
         o = loc.get("@define ".concat(keyv));
         if (o == null) {
          o = gl.get("@global ".concat(keyv));
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
 void init(BufferedReader buff, StringBuilder bf)throws IOException {
  HashMap global=new HashMap();
  String str;
  HashMap list=null;
  HashMap table=new LinkedHashMap();
  table.put("", global);
  ini = table;
  while ((str = buff.readLine()) != null) {
   str = str.trim();
   if (str.startsWith("#"))continue;
   String with;
   if (str.startsWith(with = "\"\"\"") || str.startsWith(with = "'''")) {
    int len=str.length();
    if (len > 3)len = -3;
    while (!str.startsWith(with, len)) {
     str = buff.readLine().trim();
     len = str.length() - 3;
    }
   }
   if (str.startsWith("[") && str.endsWith("]")) {
    str = str.substring(1, str.length() - 1).trim();
    if (str.contains("]"))continue;
    if (str.startsWith("comment_")) {
     list = null;
    } else {
     Object o=table.get(str);
     if (o == null) {
      list = new HashMap();
      table.put(str, list);
     } else list = (HashMap)o;
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
      list.put(key, set);
     }
    }
   }
  }
 }
 static HashMap asFor(HashMap map, Object o, String key) {
  HashMap hash;
  if (o instanceof HashMap) {
   HashMap mapput=new HashMap();
   hash = (HashMap)o;
   cpys cpy=new cpys();
   o = hash.remove("@copyFromSection");
   if (o != null && !o.equals("IGNORE")) {
    map.put(key, cpy);
    key = (String)o;
    String list[]=key.split(",");
    int i = 0;
    int l=list.length;
    while (i < l) {
     String vl=list[i++].trim();
     Object set=map.get(vl);
     HashMap as;
     if (set != null && (as = asFor(map, set, vl)) != null) {
      mapput.putAll(as);
     }
    }
   }
   HashMap put2=null;
   if (!key.startsWith("te")) {
    int i=key.indexOf("_");
    if (i > 0) {
     o = hash.get("copyFrom");
     if (o == null)o = mapput.get("copyFrom");
     if (o != null && !o.equals("IGNORE")) {
      if (mapput.size() == 0)map.put(key, cpy);
      String vl=key.substring(0, ++i).concat((String)o);
      Object set=map.get(vl);
      HashMap as;
      if (set != null && (as = asFor(map, set, vl)) != null) {
       put2 = new HashMap();
       put2.putAll(as);
       put2.putAll(mapput);
      }
     }
    }
   }
   cpy.hash = hash;
   if (put2 == null)put2 = mapput;
   if (put2.size() > 0) {
    cpy.skip = (HashMap)put2.clone();
    put2.putAll(hash);
    hash = put2;
   }
   cpy.m = hash;
  } else {
   cpys cp2=(cpys)o;
   hash = cp2.m;
  }
  return hash;
 }
 static void as(HashMap map) {
  Iterator ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   String key=(String)en.getKey();
   asFor(map, en.getValue(), key);
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
 static final Pattern find=Pattern.compile("[aA-zZ_][aA0-zZ9_.]*");;
 static final Pattern find2=Pattern.compile("[-+/*^%()]");
 static final Pattern pt=Pattern.compile("^(?:SHADOW:)?(?:CORE|SHARED):");
 static boolean isV(String str, String s, int i) {
  if (str.equalsIgnoreCase("none") || str.equals("IGNORE") || (str.equalsIgnoreCase("auto") && s.equals("image_shadow")))
   return true;
  String list[]=null;
  Pattern upt=pt;
  if (i >= 0) {
   list = str.split("\\,");
  }
  if (i == 0) {
   int l=list.length;
   while (--l >= 0) {
    str = list[l].trim();
    int r=str.indexOf("*");
    if (r > 0)str = str.substring(0, r);
    if (!upt.matcher(str).find())return false;
   }
  } else if (i < 0) {
   return upt.matcher(str).find();
  } else {
   int l=list.length;
   while (--l >= 0) {
    str = list[l].trim();
    if (str.startsWith("ROOT:")||ismusic(str))return false;
   }
  }
  return true;
 }
 public static boolean ismusic(String str) {
  int i=str.length() - 4;
  return str.regionMatches(true, i, ".ogg", 0, 4) || str.regionMatches(true, i, ".wav", 0, 4);
 }
}
