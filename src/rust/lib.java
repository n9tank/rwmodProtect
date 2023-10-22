package rust;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class lib implements Runnable {
 File In;
 File Ou;
 ui Ui;
 public static Future exec(File in, File ou, ui ui) {
  lib lib=new lib();
  lib.In = in;
  lib.Ou = ou;
  lib.Ui = ui;
  return ui.pool.submit(lib);
 }
 public void run() {
  ui ui=Ui;
  try {
   ZipFile zip=new ZipFile(In);
   int size=zip.size();
   int now=0;
   int index=0;
   ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(Ou)));
   out.setLevel(9);
   OutputStreamWriter wt=new OutputStreamWriter(out);
   try {
    Enumeration<? extends ZipEntry> en=zip.entries();
    StringBuilder def= new StringBuilder();
    while (en.hasMoreElements()) {
     ZipEntry zipe=en.nextElement();
     String name;
     if (!zipe.isDirectory() && (name = zipe.getName()).endsWith("i") && name.charAt(7) == 'u') {
      loder loder=new loder(new InputStreamReader(zip.getInputStream(zipe)), def);
      loder.str = name.substring(13);
      loder.write(loder, out, wt);
     }
     int to=(index += 100) / size;
     if (to != now)ui.poss(now = to);
    }
   } finally {
    if (wt != null)wt.close();
    zip.close();
   }
   ui.end(null);
  } catch (InterruptedIOException e) {
  } catch (Throwable e) {
   ui.end(e);
  }
 }
}
