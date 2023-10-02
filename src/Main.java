
import rust.rwmodProtect;
import rust.ui;
public class Main {
 public static void main(String[] args) throws Exception {
  rwmodProtect.init("sdcard/rustedWarfare/.ini");
  ui.exec("sdcard/a.rwmod");
 }
}
