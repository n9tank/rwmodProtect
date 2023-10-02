
package rust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 public static final Pattern find=Pattern.compile("[aA-zZ_][aA-zZ_.]*");;
 public static final Pattern find2=Pattern.compile("[-+/*^%()]");
 public static strictfp boolean isNumber(String string) {
  int n = 0;
  int l=string.length();
  while (n < l) {
   char c=string.charAt(n);
   if (!Character.isDigit(c) && c != '.' && (c != '-' || n != 0))return false;
   ++n;
  }
  return true;
 }
 public static final String off(HashMap map,HashMap setion,String str) {
  boolean isNumber=find2.matcher(str).find();
  StringBuilder buff = new StringBuilder();
  HashSet sset=set;
  Matcher matcher =find.matcher(str);
  int j=0,n=0;
  while(matcher.find()){
   j=matcher.start();
   buff.append(str,n,j);
   n=matcher.end();
   String group = matcher.group(0);
   int i=group.length();
   if (isNumber(group)||((i == 3 || i == 4) && sset.contains(group)))continue;
   String list[]=str.split("\\.",2);
   String key=list[0];
   Object o;
   tag:
   if(list.length>1){
    if(key.equals("section.key")){
     map=setion;
     }else{
     o=map.get(key);
     if(o!=null)map=(HashMap)o;
     else{
     key=null;
     break tag;
     }
     key=(String)map.get(list[1]);
     }
   }else{
    o=setion.get("@define ".concat(key));
    if(o==null){
     o=((HashMap)map.get("")).get("@global ".concat(key));
    }
    key=(String)o;
   }
   if(key==null)return key;
   buff.append(key);
  }
  j=str.length();
  if(j-n>0)buff.append(str,n,j);
  str=buff.toString();
  return isNumber?cmp(str):str;
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
