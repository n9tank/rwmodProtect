package rust;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class define {
 public AtomicInteger last;
 public HashMap def;
 public define() {
  last = new AtomicInteger();
  def = new HashMap();
 }
 public static String getId(int i) {
  StringBuilder buff=new StringBuilder();
  buff.append("_");
  while (i > 0) {
   int u = i % 63;
   i /= 63;
   u += 'A' ;
   if (u > 'Z')u += 5;
   if (u > '_')u += 3;
   buff.append(u);
  }
  return buff.toString();
 }
 public String getdefine(String str) {
  String ru=(String)def.get(str);
  if (ru == null) {
   ru = getId(last.incrementAndGet() - 1);
   def.put(str, ru);
  }
  return ru;
 }
}
