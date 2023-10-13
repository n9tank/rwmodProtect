
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import rust.rwmodProtect;
import rust.ui;
public class Main {
 public static void main(String[] args){
  long g=System.currentTimeMillis();
  String path=System.getProperty("user.dir");
  if (path.length() == 1) {
   path = "sdcard/rustedWarfare/rwmod";
  }
  try{
  rwmodProtect.init(new File(path, ".ini"));
  try{
  rwmodProtect.lib(new File(path, "lib.zip"));
  }catch(Exception e){
  e.printStackTrace();
  }
  PrintStream out=System.out;
  out.print(System.currentTimeMillis()-g);
  out.println("ms");
  Scanner sc=new Scanner(System.in);
  while (true) {
   File f=new File(sc.nextLine());
   if (f.length() == 0) {
    out.println("文件异常");
    continue;
   } else {
    ui.exec(f);
   }
  }
  }catch(Exception e){
   e.printStackTrace();
  }
 }
}
