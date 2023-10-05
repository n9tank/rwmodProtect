
import java.io.File;
import rust.rwmodLib;
import rust.rwmodProtect;
import rust.ui;
import rust.off;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.HashMap;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
public class Main {
 public static void main(String[] args) throws Exception {
  BitSet bit=new BitSet(0);
  bit.get(99);
  System.out.println(bit.size());
  ui.exec(new File("sdcard/a.rwmod"));
 }
}
