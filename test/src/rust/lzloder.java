
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class lzloder {
 public HashMap ini;
 public HashMap out;
 public lzloder all;
 public lzloder[] copy;
 public void forEach(lzloder lz) {
  for (Map.Entry<String,String> en:(Set)ini.entrySet()) {
   String ac=en.getKey();
   forEach(lz, ac);
  }
  for (lzloder to:copy) {
   to.forEach(lz);
  }
  if (all != null)all.forEach(lz);
 }
 public void forEach(lzloder lz, String ac) {
  HashMap res=null;
  HashMap tr=(HashMap)res.get(ac);
  HashMap src=lz.out;
  if (tr != null && !src.containsKey(ac)) {
   src.put(ac, null);
   for (Map.Entry<String,String> en2:(Set)tr.entrySet()) {
    String v=lz.getReal(ac, en2.getKey(), false, true,false);
    if (v != null) {

    }
   }
  }
 }
 public void run() {
  HashMap res=null;
  HashMap skip=null;
  for (Map.Entry<String,HashMap<String,String>> en:(Set)ini.entrySet()) {
   String ac=en.getKey();
   HashMap tr=(HashMap)res.get(ac);
   Iterator<Map.Entry<String, String>> ite=en.getValue().entrySet().iterator();
   while (ite.hasNext()) {
    Map.Entry < String,String > en2=ite.next();
    String key=en2.getKey();
    if (skip.containsKey(key))continue;
    if (en2.getValue().equals(getReal(ac, key, false, tr.containsKey(key),false))) {
     ite.remove();
    }
   }
  }
  forEach(this);
 }
 public Object getCopy(String key, String value, boolean next, boolean stack) {
  Object str=null;
  for (lzloder lz:copy) {
   if ((str = lz.get(key, value, next, stack)) != null) {
    return str;
   }
  }
  if (all != null)str = all.get(key, value, next, stack);
  return str;
 }
 public Object get(String key, String value, boolean next, boolean stack) {
  HashMap mmp=(HashMap)ini.get(key);
  Object str=null;
  if (mmp == null || (str = (String)mmp.get(value)) == null) {
   if (mmp != null && (next || ((str = (String)mmp.get("@copyFrom_skipThisSection")) == null || (next = !("1".equals(str) || "true".equalsIgnoreCase(str))))))
    str = getCopy(key, value, next, stack);
  }
  if (stack && str != null) {
   if (str instanceof String) {
    lzstack lzstack=new lzstack();
    lzstack.str = (String)str;
    lzstack.stack = this;
    return lzstack;
   }
  }
  return str;
 }
 public Object getOr(String key, String value, boolean next, boolean use, boolean stack) {
  if (use)return getCopy(key, value, next, stack);
  else return get(key, value, next, stack);
 }
 public Object getReal(String key, String value, boolean use, boolean img, boolean stack) {
  String str=(String)getOr(key, "@copyFrom_skipThisSection", false, use, false);
  boolean skip=!("1".equals(str) || "true".equalsIgnoreCase(str));
  Object obj = getOr(key, value, skip, use, stack);
  int i;
  if (str == null && (i = key.lastIndexOf('_')) > 0) {
   String link=(String)getOr(key, "@copyFromSection", skip, use, false);
   if (link != null && !link.equals("IGNORE")) {
    for (String next:link.split(",")) {
     next = next.trim();
     if ((obj = getReal(next, value, use, img, stack)) != null)return str;
    }
   }
   //template_
   HashMap res=null;
   if (img && !key.startsWith("te") && (res = (HashMap)res.get(key)) != null && res.containsKey(value)) {
    link = (String)getOr(key, "copyFrom", skip, use, false);
    if (link != null) {
     StringBuilder buff=new StringBuilder();
     buff.append(key, 0, i);
     buff.append(link);
     str = getDefine(buff.toString(), key, use, buff);
     if (str != null && !str.equals("IGNORE")) obj = getReal(str, value, use, img, stack);
    }
   }
  }
  return obj;
 }
 public String getBad(String value, boolean use) {
  String str=null;
  if (all != null)str = all.getBad(value, false);
  if (str != null)return str;
  for (lzloder lz:copy) {
   if ((str = lz.getBad(value, false)) != null) {
    return str;
   }
  }
  //bad global
  if (!use) {
   HashMap bad=(HashMap)ini.get("");
   if (bad != null) {
    str = (String)bad.get(value);
   }
  }
  return str;
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
 public String getDefine(String str, String where, boolean use, StringBuilder buff) {
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
      boolean isNumber=find.matcher(key).find();
      Matcher matcher=find2.matcher(key);
      int q=0,st=buff.length();
      while (matcher.find()) {
       buff.append(key, q, matcher.start());
       q = matcher.end();
       String group = matcher.group(0);
       if (!set.contains(group)) {
        String list[]=group.split("\\.", 2);
        String keyv=list[0];
        if (list.length > 1) {
         if (keyv.equals("section"))keyv = where;
         group = (String)getReal(keyv, list[1], use, false, false);
        } else {
         group = (String)getReal(where, "@define ".concat(keyv), use, false, false);
         String gl="@global ".concat(keyv);
         if (group == null)group = getBad(gl, use);
         if (group == null)group = (String)getReal("core", gl, use, false, false);
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
       if (intd == b)buff.append(intd);
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
