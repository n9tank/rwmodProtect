
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import rust.lib;
import rust.rwmodProtect;
public class Main {
 public static void main(String[] args) {
  long g=System.currentTimeMillis();
  String dir=System.getProperty("user.dir");
  if (dir.length() == 1) {
   dir = "sdcard/rustedWarfare/rwmod";
  }
  try {
   rwmodProtect.init(new File(dir, ".ini"));
   try {
    rwmodProtect.lib(new File(dir, "lib.zip"));
   } catch (Throwable e) {
    e.printStackTrace();
   }
   PrintStream out=System.out;
   out.print(System.currentTimeMillis() - g);
   out.println("ms");
   Scanner sc=new Scanner(System.in);
   while (true) {
    String str=sc.next();
    boolean islib;
    if (islib = str.equals("lib"))str = sc.next();
    File path=new File(str);
    if (path.length() == 0) {
     out.println("文件异常");
     continue;
    } else {
     cui ui=new cui(str);
     if (islib) {
      File ou=new File(dir, ".zip");
      lib.exec(path, ou, ui);
     } else rwmodProtect.exec(path, ui);
    }
   }
  } catch (Throwable e) {
   e.printStackTrace();
  }
 }
}
