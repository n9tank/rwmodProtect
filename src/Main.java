
import rust.rwmodProtect;
import rust.ui;
import java.util.zip.ZipFile;
public class Main {
 public static void main(String[] args) throws Exception {
  rwmodProtect.init("sdcard/rustedWarfare/.ini");
  ui.exec("sdcard/a.rwmod");
 }
}
