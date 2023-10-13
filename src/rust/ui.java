package rust;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ui {
 //控制台的ui显示，请重写该类，让它用于GUI
String show;
long g;
final static ExecutorService pool=Executors.newCachedThreadPool();
 public ui(String show) {
  this.show = show;
  g = System.currentTimeMillis();
 }
 public static void exec(File path) {
  String name=path.getName();
  int l=name.length();
  if (name.startsWith(".", l -= 6)) {
   name = name.substring(0, l);
  }
  File ou = new File(path.getParent(), name.concat("_r.rwmod"));
  rwmodProtect rw=new rwmodProtect(path, ou, new ui(ou.getPath()));
  pool.execute(rw);
 }
 public void finsh() {
  PrintStream out=System.out;
  out.print(show);
  out.print(':');
  out.print(System.currentTimeMillis() - g);
  out.println("ms");
 }
 void fali(Exception e) {
  e.printStackTrace();
 }
}
