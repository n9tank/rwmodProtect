
import android.graphics.Paint;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main {
 public static ArrayList copy(ArrayList crr, int type, cmp cmp, int size) {
  cmp.type = type;
  Collections.sort(crr, cmp);
  ArrayList ru=new ArrayList(size);
  int i=0;
  while(i<size){
   ru.add(crr.get(i++));
  }
  return ru;
 }
 public static void main(String str[]) throws Exception {
  char to=Character.MAX_VALUE;
  int index=to - 32;
  ArrayList crr=new ArrayList();
  StringBuilder dump=new StringBuilder();
  Paint pain=new Paint();
  while (--index >= 0) {
   String s=String.valueOf(to--);
   if (pain.hasGlyph(s)) {
   crr.add(new charc(s, pain));
   }else{
   dump.append(s);
   //dump.append("\n");
   }
  }
  cmp cmp=new cmp();
  int size=crr.size();
  crr=copy(crr, 0, cmp, size/=4.3);
  crr=copy(crr, 1, cmp, size/=4.3);
  crr=copy(crr, 2, cmp, size/=4.3);
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
  for(charc c:crr){
  buff.write(c.s);
 // buff.write("\n");
  }
  buff.write(dump.toString());
  buff.flush();
  buff.close();
  System.out.println("finsh");
 }
}
