
import java.io.File;
import rust.rwmodProtect;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import rust.loder;
import rust.ui;
import rust.off;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.io.FileReader;public class Main {
 public static void main(String[] args) throws Exception {
  rwmodProtect.init("sdcard/.ini");
  ui.exec("sdcard/a.rwmod");
 /* ZipFile zip=new ZipFile("sdcard/a.rwmod");
  Enumeration<? extends ZipEntry> en=zip.entries();
  while(en.hasMoreElements()){
   System.out.println(en.nextElement());
  }*/
  /*loder lod=new loder(new FileReader("sdcard/a.ini"));
  System.out.println(lod.eqz());*/
 }
}
