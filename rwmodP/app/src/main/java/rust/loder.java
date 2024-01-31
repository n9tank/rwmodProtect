package rust;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.compress.parallel.InputStreamSupplier;
class loder implements Callable,InputStreamSupplier {
 public InputStream get() {
  try {
   ByteArrayOutputStream rus = new ByteArrayOutputStream();
   BufferedWriter out=new BufferedWriter(new OutputStreamWriter(rus));
   if (ini == null)call();
   try {
    HashMap map=ini;
    Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
    boolean st=false;
    while (ite.hasNext()) {
     Map.Entry en = ite.next();
     map = (HashMap)en.getValue();
     if (map.size() > 0) {
      if (st)out.write('\n');
      st = true;
      String key=(String)en.getKey();
      out.write('[');
      out.write(key);
      out.write("]\n");
      Iterator<Map.Entry> ite2=map.entrySet().iterator();
      boolean nx=ite2.hasNext();
      while (nx) {
       en = ite2.next();
       out.write((String)en.getKey());
       out.write(':');
       out.write((String)en.getValue());
       nx = ite2.hasNext();
       if (nx)out.write('\n');
      }
     }
    }
    out.flush();
    return new ByteArrayInputStream(rus.toByteArray());
   } finally {
    out.close();
   }
  } catch (Throwable e) {
  }
  return null;
 }
 static HashSet glr;
 public Object call() throws Exception {
  Reader red=new InputStreamReader(read);
  BufferedReader buff=new BufferedReader(red);
  StringBuilder bf=new StringBuilder();
  Exception ex=null;
  try {
   try {
    HashMap core=new HashMap();
    HashMap bad=new HashMap();
    String str;
    boolean last=false;
    HashMap list=null;
    HashMap table=new LinkedHashMap();
    ini = table;
    table.put("", bad);
    table.put("core", core);
    wh:
    while ((str = buff.readLine()) != null) {
     str = str.trim();
     int i=str.length();
     if (i == 0 || str.startsWith("#"))continue;
     String with;
     if (str.startsWith(with = "\"\"\"") || str.startsWith(with = "'''")) {
      int len=str.length();
      if (len <= 6)len = 3;else len -= 3;
      while (true) {
       if (str.startsWith(with, len))continue wh;
       str = buff.readLine();
       if (str == null)return null;
       str = str.trim();
       len = str.length() - 3;
      }
     }
     if (str.startsWith("[") && str.indexOf(']', 1) == --i) {
      str = str.substring(1, i).trim();
      last = str.indexOf('_') < 0;
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
        if (last)core.put(key, set);
        else bad.put(key, set);
       } else {
        if (set.startsWith(with = "\"\"\"") || set.startsWith(with = "\'\'\'")) {
         bf.setLength(0);
         int len=set.length();
         int ed=len;
         int st=3;
         if (ed <= 6)ed = 3;else ed -= 3;
         while (true) {
          boolean now;
          if (now = set.startsWith(with, ed))len = ed;
          bf.append(set, st, len);
          if (now)break;
          st = 0;
          set = buff.readLine();
          if (set == null)return null;
          set = set.trim();
          len = set.length();
          ed = len - 3;
         }
         set = bf.toString();
        }
        list.put(key, set);
       }
      }
     }
    }
    boolean istm=isini;
    Object o = core.remove("dont_load");
    if (o != null) {
     str = (String)o;
     istm &= !("1".equals(str) || "true".equalsIgnoreCase(str));
    }
    isini = istm;
   } finally {
    if (red != null)buff.close();
   }
  } catch (InterruptedIOException e) {
  } catch (Exception e) {
   ex = e;
  }
  TaskWait tas=task;
  if (tas != null)task.down(ex);
  if (ex != null)throw ex;
  return null;
 }
 HashMap cou;
 HashMap ini;
 HashMap put;
 HashMap old;
 String src;
 loder all;
 String str;
 String allD;
 int allindex;
 InputStream read;
 TaskWait task;
 boolean isini;
 boolean use;
 Object copy[];
 loder(InputStream inp) {
  read = inp;
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
     map.put(key, hash);
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
 static void putAnd(HashMap map, HashMap map2, HashMap cou, String path, HashMap ini) {
  HashMap bad=(HashMap)map.get("");
  HashMap bad2=null;
  if (bad != null) {
   bad2 = (HashMap)map2.remove("");
   if (bad2 != null) {
    HashMap sr=ini == null ?null: (HashMap)ini.get("");
    for (Map.Entry<String,String> en:(Set<Map.Entry>)bad2.entrySet()) {
     String key=en.getKey();
     if (bad.putIfAbsent(key, en.getValue()) != null && sr != null)
      sr.remove(key);
    }
   }
  }
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
    o = cou.get(key);
    HashMap now;
    if (o == null) {
     now = new HashMap();
     cou.put(key, now);
    } else now = (HashMap)o;
    Iterator ite2=hash.entrySet().iterator();
    while (ite2.hasNext()) {
     en = (Map.Entry)ite2.next();
     key = (String)en.getKey();
     if (res.containsKey(key)) {
      now.put(key, path);
     }
    }
   }
  }
  if (bad2 != null)map2.put("", bad2);
 }
 static String getName(String file) {
  int len=file.length();
  if (file.endsWith("/"))--len;
  int i=file.lastIndexOf("/", len - 1);
  return file.substring(++i, len);
 }
 static String getSuperPath(String str) {
  int i=str.lastIndexOf('/', str.length() - 2);
  if (i > 0)return str.substring(0, i + 1);
  return "";
 }
 static String get(String str, String eqz, HashMap map, Object loc, StringBuilder buff) {
  int i=0,j=0;
  buff.setLength(0);
  HashMap bad=(HashMap)map.get("");
  HashMap gl=(HashMap)map.get("core");
  Pattern find0=find;
  Pattern find1=find2;
  do{
   i = str.indexOf("${", i);
   if (i >= 0) {
    buff.append(str, j, i);
    j = i;
    int n=str.indexOf("}", i += 2);
    if (n > 0) {
     String key=str.substring(i, n).trim();
     if (key.length() > 0) {
      boolean isNumber=find1.matcher(key).find();
      HashSet sset=set;
      Matcher matcher=find0.matcher(key);
      int q=0,st=buff.length();
      while (matcher.find()) {
       buff.append(key, q, matcher.start());
       q = matcher.end();
       String group = matcher.group(0);
       if (!sset.contains(group)) {
        String list[]=group.split("\\.", 2);
        String keyv=list[0];
        HashMap locv;
        if (loc instanceof HashMap) {
         locv = (HashMap)loc;
        } else {
         cpys cpy = (cpys)loc;
         locv = cpy.m;
        }
        Object o=null;
        if (list.length > 1) {
         HashMap or=null;
         if (!(keyv.equals("section") || key.equals(eqz))) {
          loc = map.get(keyv);
          if (loc == null)return null;
          if (loc instanceof HashMap)locv = (HashMap)loc;
          else or = ((cpys)loc).copy;
         }
         String vl=list[1];
         if (or != null)o = or.get(vl);
         if (o == null) o = locv.get(vl);
        } else {
         o = locv.get("@define ".concat(keyv));
         String gs="@global ".concat(keyv);
         if (o == null)o = bad.get(gs);
         if (o == null)o = gl.get(gs);
        }
        group = (String)o;
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
       if (intd == b) buff.append(intd);
       else buff.append(b);
      }
     }
     j = i = ++n;
    } else break;
   } else break;
  }while(true);
  buff.append(str, j, str.length());
  return buff.toString();
 }
 static HashMap asFor(HashMap map, Object o, String key) {
  HashMap hash;
  if (o instanceof HashMap) {
   HashMap mapput=new HashMap();
   hash = (HashMap)o;
   cpys cpy=new cpys();
   o = hash.remove("@copyFromSection");
   if (o != null && !o.equals("IGNORE")) {
    if (mapput.size() == 0)map.put(key, cpy);
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
   cpy.hash = hash;
   if (mapput.size() > 0) {
    cpy.skip = (HashMap)mapput.clone();
    mapput.putAll(hash);
    hash = mapput;
   }
   cpy.m = hash;
  } else {
   cpys cp2=(cpys)o;
   hash = cp2.m;
  }
  return hash;
 }
 static Object ascopy(HashMap map, Object o, String key, StringBuilder buff) {
  cpys cpy=null;
  HashMap cop=null;
  HashMap m=null;
  HashMap mp=null;
  if (o instanceof HashMap) m = (HashMap)o;
  else {
   cpy = (cpys)o;
   m = cpy.m;
   mp = cpy.skip;
   cop = cpy.copy;
  }
  if (cop == null) {
   int i;
   if (!key.startsWith("te") && (i = key.indexOf('_')) > 0) {
    String str=(String)m.remove("copyFrom");
    if (str != null) {
     str = get(str, key, map, m, buff);
     buff.setLength(0);
     buff.append(key, 0, ++i);
     buff.append(str);
     str = buff.toString();
     o = map.get(str);
     if (o != null && (o = ascopy(map, o, str, buff)) != null) {
      if (cpy == null) {
       cpy = new cpys();
       cpy.m = m;
       map.put(key, cpy);
      }
      if (mp == null) {
       mp = new HashMap();
       cpy.skip = mp;
      }
      HashMap it;
      if (o instanceof HashMap) {
       it = (HashMap)o;
       cop = new HashMap();
      } else {
       cpys cp=(cpys)o;
       it = cp.m;
       cop = (HashMap)cp.copy.clone();
      }
      HashMap res=rwmodProtect.Res;
      cpy.copy = cop;
      for (Map.Entry<String,String> en:(Set<Map.Entry>)it.entrySet()) {
       String k= en.getKey();
       if (res.containsKey(k)) {
        String v=en.getValue();
        if ((o = m.putIfAbsent(k, v)) != null) {
         mp.put(k, v);
         cop.put(k, o);
        }
       }
      }
     }
    }
   }
  }
  return cpy == null ?m: cpy;
 }
 static void as(HashMap map, StringBuilder buff) {
  Iterator ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   Object o=en.getValue();
   if (o instanceof cpys) {
    cpys cpy=(cpys)o;
    en.setValue((cpy.is ?cpy.skip: cpy.m).clone());
   }
  }
  ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   asFor(map, en.getValue(), (String)en.getKey());
  }
  for (Map.Entry<String,Object> en:(Set<Map.Entry>)map.entrySet()) {
   ascopy(map, en.getValue(), en.getKey(), buff);
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
 static final Pattern find=Pattern.compile("[aA-zZ_][aA0-zZ9_.]*");
 static final Pattern find2=Pattern.compile("[-+/*^%()]");
}
