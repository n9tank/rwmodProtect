package carsh;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import android.util.Log;
public class log implements Thread.UncaughtExceptionHandler {
 public static File outPath;
 public static log mlog;
 public static void init(Context context) {
  outPath = new File(context.getExternalCacheDir(), "log");
 }
 public static void bind() {
  log log=mlog;
  if (log == null) {
   log = new log();
   mlog = log;
  }
  Thread.setDefaultUncaughtExceptionHandler(log);
 }
 public static void e(Object cla, Throwable e) {
  Log.e("rust.rwp",cla.toString(), e);
 }
 public void uncaughtException(Thread thread, Throwable ex) {
  String name=thread.getName();
  long current=System.currentTimeMillis();
  String time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
  outPath.mkdirs();
  StringBuilder str=new StringBuilder(name);
  str.append(time);
  str.append(".log");
  File out=new File(outPath, str.toString());
  try {
   PrintWriter print=new PrintWriter(new BufferedWriter(new FileWriter(out)));
   ex.printStackTrace(print);
   print.close();
  } catch (Exception e) {
  }
 }
}
