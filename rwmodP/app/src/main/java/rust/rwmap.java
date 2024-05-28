package rust;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import carsh.log;

public class rwmap implements Runnable {
 ui ui;
 File in;
 File ou;
 public rwmap(File i, File u, ui uo) {
  in = i;
  ou = u;
  ui = uo;
 }
 public void run() {
  Throwable ex=null;
  try {
   RwMapCompressor.output(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(ou)), true);
  } catch (Throwable e) {
   log.e(this, e);
   ex = e;
  }
  if (ex != null)ou.delete();
  ui.end(ex);
 }
}
