package rust;
import java.util.regex.Pattern;
import java.util.HashSet;
class res {
 String str;
 boolean close;
 static final Pattern pt=Pattern.compile("^(?:SHADOW:)?(?:CORE|SHARED):");
 static boolean isV(String str, String s, byte i) {
  if (str.equalsIgnoreCase("none") || str.equals("IGNORE") || (str.equalsIgnoreCase("auto") && s.equals("image_shadow")))
   return true;
  String list[]=null;
  Pattern upt=pt;
  if (i >= 2) {
   list = str.split("\\,");
  }
  if (i == 2) {
   list = str.split("\\,");
   int l=list.length;
   while (--l >= 0) {
    str=list[l].trim().split("\\*",2)[0];
    if (!upt.matcher(str).find())return false;
   }
  } else if (i == 1) {
   return upt.matcher(str).find();
  } else {
   HashSet music=rwmodProtect.music;
   int l=list.length;
   while (--l >= 0){
    str=list[l].trim().split("(?<!^ROOT):",2)[0];
    if (!music.contains(str))return false;
   }
  }
  return true;
 }
}
