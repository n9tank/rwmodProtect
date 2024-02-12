package rust;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class iniobj {
 public boolean unclone;//不要制作副本
 public HashMap put;
 public Map gl;
 public loder all;
 public iniobj() {
  put = new HashMap();
 }
 public static HashMap clone(HashMap map) {
  HashMap put=new HashMap();
  for (Map.Entry<String,cpys> en:(Set<Map.Entry<String,cpys>>)map.entrySet()) {
   cpys cp=new cpys();
   cp.m = (HashMap)en.getValue().m.clone();
   put.put(en.getKey(), cp);
  }
  return put;
 }
 public iniobj(HashMap map, loder path) {
  put = map;
  for (cpys cpy:(Collection<cpys>)map.values()) {
   HashMap m=cpy.m;
   HashMap coe;
   cpy.coe = coe = new HashMap();
   for (Map.Entry<String,String> en:(Set<Map.Entry<String,String>>)m.entrySet()) {
    String key= en.getKey();
    if (rwmodProtect.Res.containsKey(key))coe.put(key, path);
   }
  }
 }
 public void put(iniobj drc, loder path) {
  HashMap src=put;
  for (Map.Entry<String,cpys>en:(Set<Map.Entry>)drc.put.entrySet()) {
   String ac=en.getKey();
   HashMap list=null;
   HashMap skip=null;
   HashMap skipdrc=null;
   cpys cpy = (cpys)src.get(ac);
   if (cpy != null) {
    list = cpy.m;
    skip = cpy.skip;
   } else {
    cpy = new cpys();
    src.put(ac, cpy);
   }
   String str=list == null ?null: (String)list.get("@copyFrom_skipThisSection");
   boolean has="1".equals(str) || "true".equals(str);
   cpys cp = en.getValue();
   HashMap listdrc=cp.m;
   HashMap cpcoe=cp.coe;
   HashMap cpskip = cp.skip;
   if (cpskip == null)cpskip = listdrc;
   if (str != null && !has) {
    listdrc = cpskip;
    HashMap v = cp.coeskip;
    if (v != null)cpcoe = v;
   } else if (has)skipdrc = cpskip;
   HashMap coe=cpy.coe;
   if (!has) {
    if (list == null) {
     cpy.m = (HashMap)listdrc.clone();
     if (path == null && cpcoe != null) {
      coe = (HashMap)cpcoe.clone();
      cpcoe = null;
     } else coe = new HashMap();
     cpy.coe = coe ;
    } else {
     for (Map.Entry en2:(Set<Map.Entry>)listdrc.entrySet())
      list.putIfAbsent(en2.getKey(), en2.getValue());
    }
    if (cpcoe != null) {
     if (path != null) {
      for (Object s:(Set)cpcoe.keySet())
       coe.putIfAbsent(s, path);
     } else {
      for (Map.Entry s:(Set<Map.Entry>)cpcoe.entrySet())
       coe.putIfAbsent(s.getKey(), s.getValue());
     }
    }
   }
   if (skipdrc != null) {
    HashMap coeskip=cpy.coeskip;
    HashMap cpcoeskip=cp.coeskip;
    if (cpcoeskip == null)cpcoeskip = cpcoe;
    if (skip == null) {
     cpy.skip = skip = (HashMap)list.clone();
     cpy.coeskip = coeskip = (HashMap)coe.clone();
    }
    for (Map.Entry en2:(Set<Map.Entry>)skipdrc.entrySet())
     skip.putIfAbsent(en2.getKey(), en2.getValue());
    if (path != null) {
     for (Object s:(Set)cpcoeskip.keySet())
      coeskip.putIfAbsent(s, path);
    } else {
     for (Map.Entry s:(Set<Map.Entry>)cpcoeskip.entrySet())
      coeskip.putIfAbsent(s.getKey(), s.getValue());
    }
   }
  }
 }
 cpys asFor(cpys cpy, String key) {
  HashMap map=put;
  HashMap hash=cpy.m;
  String str = (String)hash.remove("@copyFromSection");
  if (str != null && !str.equals("IGNORE")) {
   HashMap mapput = unclone ?hash: (HashMap)hash.clone();
   cpy.m = mapput;
   String list[]=str.split(",");
   int l=list.length;
   while (--l >= 0) {
    String vl=list[l].trim();
    cpys set=(cpys)map.get(vl);
    HashMap as;
    if (set != null && (as = asFor(set, vl).m) != null) {
     for (Map.Entry<String,String> en:(Set<Map.Entry<String,String>>)as.entrySet())
      mapput.putIfAbsent(en.getKey(), en.getValue());
    }
   }
  }
  return cpy;
 }
 cpys ascopy(cpys cpy, String key) {
  int i;
  if (!key.startsWith("te") && (i = key.indexOf('_')) > 0) {
   HashMap map=put;
   HashMap m=cpy.m;
   String str=(String)m.remove("copyFrom");
   if (str != null && !str.equals("IGNORE")) {
    str = get(str, key, cpy);
    str = key.substring(0, ++i).concat(str);
    cpys cp=(cpys)map.get(str);
    if (cp != null && (cp = ascopy(cp, str)) != null) {
     HashMap it = cp.m;
     for (Map.Entry<String,String> en:(Set<Map.Entry>)it.entrySet())
      m.putIfAbsent(en.getKey(), en.getValue());
    }
   }
  }
  return cpy;
 }
 public void as() {
  HashMap gl= new HashMap();
  this.gl = gl;
  HashMap map=put;
  for (cpys cpy:(Collection<cpys>)map.values()) {
   Iterator<Map.Entry<String,String>> ite2=cpy.m.entrySet().iterator();
   cpy.skip = cpy.m;
   while (ite2.hasNext()) {
    Map.Entry<String,String> en2=ite2.next();
    String key=en2.getKey();
    if (key.startsWith("@global ")) {
     gl.put(key.substring(8), en2.getValue());
     ite2.remove();
    }
   }
  }
  Set<Map.Entry> se=(Set<Map.Entry>)map.entrySet();
  for (Map.Entry<String,Object> en2:se)
   asFor((cpys)en2.getValue(), (String)en2.getKey());
  for (Map.Entry<String,Object> en2:se)
   ascopy((cpys)en2.getValue(), en2.getKey());
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
 String get(String str, String eqz, cpys cpy) {
  HashMap map=put;
  Map gl=this.gl;
  StringBuilder buff=new StringBuilder();
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
       Object o=null;
       if (!sset.contains(group)) {
        String list[]=group.split("\\.", 2);
        String keyv=list[0];
        if (list.length > 1) {
         if (!keyv.equals("section") && !key.equals(eqz)) {
          cpy = (cpys)map.get(keyv);
          if (cpy == null)return null;
         }
         o = cpy.m.get(list[1]);
        } else {
         o = cpy.m.get("@define ".concat(keyv));
         if (o == null)o = gl.get(keyv);
        }
        if (o == null)return null;
       }
       buff.append((String)o);
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
