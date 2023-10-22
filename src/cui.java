import java.io.PrintStream;
import rust.ui;

public class cui implements ui {
 String show;
 long g;
 int len;
 public void poss(int i) {
  PrintStream out=System.out;
  int last=len;
  while(--last>=0){
  out.print('\b');
  }
  String str=String.valueOf(i);
  len=str.length();
  out.print(str);
 }
 public void end(Throwable e) {
  if (e != null) {
   e.printStackTrace();
  } else {
   PrintStream out=System.out;
   if(len>0)out.print('\n');
   out.print(show);
   out.print(':');
   out.print(System.currentTimeMillis() - g);
   out.println("ms");
  }
 }
}
