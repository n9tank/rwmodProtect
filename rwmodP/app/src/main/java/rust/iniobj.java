package rust;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class iniobj {
 public HashMap put;
 public HashMap gl;
 public StringBuilder mbuff;
 public iniobj() {
  put = new HashMap();
 }
 public void put(HashMap drc, HashMap coe, String path) {
  HashMap src=put;
  for (Map.Entry<String,HashMap<String,String>>en:(Set<Map.Entry>)drc.entrySet()) {
   String ac=en.getKey();
   Object o=src.get(ac);
   HashMap list;
   cpys cpy=null;
   HashMap skip=null;
   HashMap skipdrc=null;
   if (o instanceof cpys) {
    cpy = (cpys)o;
    list = cpy.m;
    skip = cpy.skip;
   } else list = (HashMap)src.get(ac);
   String str=list == null ?null: (String)list.get("@copyFrom_skipThisSection");
   boolean has="1".equals(str) || "true".equals(str);
   boolean nil=str == null || str.equals("IGNORE");
   HashMap listdrc=null;
   o = en.getValue();
   if (o instanceof HashMap)listdrc = (HashMap)o;
   else {
    cpys cp = ((cpys)o);
    if (!nil && !has)listdrc = cp.skip;
    if (nil) {
     if (cpy == null) {
      src.put(ac, cpy = new cpys());
      cpy.m = list;
      cpy.skip = skip = (HashMap)cp.skip.clone();
     } else skipdrc = cp.skip;
     listdrc = cp.m;
    }
   }
   if (nil && cpy != null && skipdrc == null)skipdrc = listdrc;
   if (skipdrc != null) {
    for (Map.Entry en2:(Set<Map.Entry>)skipdrc.entrySet()) {
     Object key=en2.getKey();
     o = en2.getValue();
     if (skip.putIfAbsent(key, o) == null && coe != null && rwmodProtect.Res.containsKey(key))
      ;
    }
   }
   if (has) {
    if (cpy == null) {
     cpy = new cpys();
     src.put(ac, cpy);
     cpy.m = list;
     cpy.skip = list = (HashMap)list.clone();
    } else list = cpy.skip;
   }
   HashMap coelist=null;
   if (coe != null) {
    coelist = (HashMap)coe.get(ac);
    if (coe == null)coe.put(ac, coelist = new HashMap());
   }
   if (list == null) {
    src.put(ac, list = (HashMap)listdrc.clone());
   } else {
    for (Map.Entry en2:(Set<Map.Entry>)listdrc.entrySet()) {
     Object key=en2.getKey();
     o = en2.getValue();
     list.putIfAbsent(key, o);
     //路径追踪不知道怎么写了
     //if (list.putIfAbsent(key, o) == null && coe != null && rwmodProtect.Res.containsKey(key))coelist.put(key, path);
    }
   }
  }
 }
 HashMap asFor(Object o, String key) {
  HashMap map=put;
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
     if (set != null && (as = asFor(set, vl)) != null) {
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
 Object ascopy(Object o, String key) {
  HashMap map=put;
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
     str = get(str, key, m);
     StringBuilder buff=mbuff;
     buff.setLength(0);
     buff.append(key, 0, ++i);
     buff.append(str);
     str = buff.toString();
     o = map.get(str);
     if (o != null && (o = ascopy(o, str)) != null) {
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
 void as() {
  HashMap map=put;
  HashMap gl= new HashMap();
  this.gl = gl;
  for (HashMap<String,String> list:(Collection<HashMap>)map.values()) {
   for (Map.Entry<String,String> en:(Set<Map.Entry<String,String>>)list.entrySet()) {
    String key=en.getKey();
    if (key.startsWith("@gloabl "))gl.put(key.substring(8), en.getValue());
   }
  }
  Iterator ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   Object o=en.getValue();
   if (o instanceof cpys) {
    cpys cpy=(cpys)o;
    en.setValue((cpy.m).clone());
   }
  }
  ite = map.entrySet().iterator();
  while (ite.hasNext()) {
   Map.Entry en=(Map.Entry)ite.next();
   asFor(en.getValue(), (String)en.getKey());
  }
  for (Map.Entry<String,Object> en:(Set<Map.Entry>)map.entrySet()) {
   ascopy(en.getValue(), en.getKey());
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
 String get(String str, String eqz, Object loc) {
  HashMap map=put;
  HashMap gl=this.gl;
  StringBuilder buff=mbuff;
  int i=0,j=0;
  buff.setLength(0);
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
         if (o == null)o = gl.get(keyv);
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
       cmpU cmp = new cmpU();
       cmp.m = key;
       cmp.t = key.charAt(0);
       double b = cmp.cmp();
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
}
