
package rust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.Build;

public final class off {
 public static final HashSet set;
 static{
  HashSet sset=new HashSet();
  set = sset;
  sset.add("int");
  sset.add("cos");
  sset.add("sin");
  sset.add("sqrt");
 }
 public static final Pattern find=Pattern.compile("[aA-zZ_][aA-zZ_.0-9]*");;
 public static final Pattern find2=Pattern.compile("[-+/*^%()]");
 public static final boolean off(HashMap map, HashMap setion, String str,StringBuilder buff) {
  boolean isNumber=find2.matcher(str).find();
  HashSet sset=set;
  Matcher matcher =find.matcher(str);
  int j=0,n=0,st=buff.length();
  while (matcher.find()) {
   j = matcher.start();
   buff.append(str, n, j);
   n = matcher.end();
   String group = matcher.group(0);
   int i=group.length();
   if(!group.matches("-?[0-9.]+")&& !((i == 3 || i == 4) &&sset.contains(group))){
   String list[]=str.split("\\.", 2);
   String key=list[0];
   Object o;
   tag:
   if (list.length > 1) {
    HashMap loc;
    if(key.equals("section"))loc = setion;
    else{
    loc = (HashMap)map.get(key);
    if(loc==null)return false;
    }
    group = (String)loc.get(list[1]);
   } else {
    o = setion.get("@define ".concat(key));
    if (o == null) {
     o = ((HashMap)map.get("")).get("@global ".concat(key));
    }
    group= (String)o;
   }
   }
   if(group==null)return false;
   buff.append(group);
  }
  j = str.length();
  if (j - n > 0)buff.append(str, n, j);
  str = buff.substring(st,buff.length());
  if(isNumber){
  buff.setLength(st);
  buff.append(cmp(str));
  }
  return true;
 }
 private static String cmp(String str) {
  cmp cVar = new cmp(str);
  cVar.a();
  double b = cVar.b();
  int intd=(int)b;
  if (intd == b) {
   return String.valueOf(intd);
  } else {
   return String.valueOf(b);
  }
 }
}
