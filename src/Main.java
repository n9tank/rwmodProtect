
import java.io.File;
import rust.rwmodLib;
import rust.rwmodProtect;
import rust.ui;
import rust.off;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
public class Main {
 public static void main(String[] args) throws Exception {
  ui.exec(new File("sdcard/a.rwmod"));
  /*ZipFile zip=new ZipFile("sdcard/b.rwmod");
  Enumeration<? extends ZipEntry> en=zip.entries();
  while(en.hasMoreElements()){
   System.out.println(en.nextElement());
  }*/
 }
}
