
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import rust.rwmodProtect;
import rust.ui;
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
    File path=new File(sc.nextLine());
    if (path.length() == 0) {
     out.println("文件异常");
     continue;
    } else {
     ui.exec(path, new cui(path.getPath()));
    }
   }
  } catch (Throwable e) {
   e.printStackTrace();
  }
 }
}
