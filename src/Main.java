
import rust.rwmodProtect;
import rust.ui;
public class Main {
 public static void main(String[] args) throws Exception {
  rwmodProtect.init("sdcard/rustedWarfare/.ini");
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
