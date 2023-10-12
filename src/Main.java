
import java.io.File;
import java.util.Scanner;
import rust.rwmodLib;
import rust.rwmodProtect;
import rust.ui;
public class Main {
public static void main(String[] args) throws Exception {
  ui def=new ui("def");
  String path=System.getProperty("user.dir");
  if (path.length() == 1) {
   path = "sdcard/rustedWarfare/rwmod";
  }
  rwmodProtect.init(new File(path,".ini"), def);
  rwmodLib.init(new File(path,"lib.zip"), def);
  def.finsh();
  Scanner sc=new Scanner(System.in);
  while (true) {
   File f=new File(sc.nextLine());
   if (f.length() == 0) {
    System.out.println("文件异常");
    continue;
   } else {
    ui.exec(f);
   }
  }
 }
}
