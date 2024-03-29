package rust.rwp;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import carsh.log;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import rust.lib;
import rust.rwmodProtect;
public class Main extends Activity {
 boolean init;
 SharedPreferences sha;
 Intent Intent;
 Intent sw;
 String pe[];
 EditText ed;
 static ArrayAdapter arr;
 static TextView bar;
 public void finish() {
  moveTaskToBack(true);
 }
 public static void error(Throwable e, String where, Context c) {
  AlertDialog.Builder show=new AlertDialog.Builder(c);
  show.setTitle(where);
  show.setMessage(e.toString());
  show.show();
 }
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  Thread.setDefaultUncaughtExceptionHandler(new log(this));
  SharedPreferences sh=getSharedPreferences("", MODE_PRIVATE);
  sha = sh;
  setContentView(R.layout.activity_main);
  bar = findViewById(R.id.lib);
  ed = findViewById(R.id.ed);
  ListView list=findViewById(R.id.list);
  ArrayAdapter ar=new ArrayAdapter(this, android.R.layout.test_list_item, new LinkedList());
  list.setAdapter(ar);
  arr = ar;
  boolean def=sh.getBoolean("", false);
  if (def) {
   CheckBox checkbox=findViewById(R.id.ch);
   checkbox.setChecked(def);
   init = def;
  }
  def = sh.getBoolean("0", false);
  if (def) {
   CheckBox ch=findViewById(R.id.log);
   ch.setChecked(def);
   carsh.log.debug = def;
  }
  Intent i=getIntent();
  if (i != null)st(i);
  int sdk=Build.VERSION.SDK_INT;
  String s;
  if (sdk >= 23 && checkSelfPermission(s = "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
   String per[]=new String[]{s};
   requestPermissions(per, 0);
   pe = per;
  } else {
   init();
  }
  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
  intent.addCategory(Intent.CATEGORY_OPENABLE);
  intent.setType("*/*");
  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
  sw = intent.createChooser(intent, "");
 }
 public void lib() {
  bar.setVisibility(0);
  File li=new File(getExternalFilesDir(null), "lib.zip");
  cui ui=new cui("lib");
  if (!li.exists())lib.exec(getResources().openRawResource(R.raw.lib), li, ui);
  else new lib(li, null, ui);
 }
 public void init() {
  if (init)lib();
  try {
   File su=getExternalFilesDir(null);
   File ini=new File(su, ".txt");
   if (ini.exists())rwmodProtect.dictionary(new FileReader(ini));
   Reader io;
   ini = new File(su, ".ini");
   if (ini.exists())io = new FileReader(ini);
   else io = new InputStreamReader(getResources().openRawResource(R.raw.def));
   rwmodProtect.init(io);
  } catch (Throwable e) {
   log.e(this, e);
   error(e, "init", this);
  }
 }
 public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
   pe = null;
   init();
  } else if (shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
   requestPermissions(pe, 0);
  } else {
   pe = null;
   Intent intent=new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
   intent.setData(Uri.parse("package:rust.rwp"));
   Intent = intent;
   startActivityForResult(intent, 0);
  }
 }
 protected void onNewIntent(Intent intent) {
  super.onNewIntent(intent);
  st(intent);
 }
 public void st(Intent intent) {
  String type=intent.getAction();
  Uri o;
  if (type.equals(Intent.ACTION_SEND))o = intent.getParcelableExtra(Intent.EXTRA_STREAM);
  else if (type.equals(intent.ACTION_SEND_MULTIPLE)) {
   ArrayList<Uri> arr = intent.getParcelableArrayListExtra(intent.EXTRA_STREAM);
   int i=0,l=arr.size();
   while (i < l) {
    add(arr.get(i++));
   }
   return;
  } else o = intent.getData();
  if (o != null)add(o);
 }
 public void add(Uri uri) {
  String type=uri.getScheme();
  String path=uri.getPath();
  if (type.startsWith("c")) {
   String ab=uri.getAuthority();
   if (ab.startsWith("com.android.externalstorage")) {
    path = "sdcard/".concat(path.substring(18));
   } else if (ab.startsWith("com.android.providers.downloads")) {
    String ids=path.substring(14);
    //raw: msf:
    if (!path.startsWith("w", 12)) {
     ContentResolver contentResolver = getContentResolver();
     Cursor cursor = contentResolver.query(MediaStore.Downloads.getContentUri("external"), new String[]{"_data"}, "_id=?", new String[]{ids}, null);
     if (cursor != null) {
      cursor.moveToFirst();
      try {
       int idx = cursor.getColumnIndex("_data");
       path = cursor.getString(idx);
      } catch (Throwable e) {
      } finally {
       cursor.close();
      }
     }
    } else path = ids;
   }
  }
  File f=new File(path);
  if (f.exists()) {
   cui cui=new cui(path);
   cui.ui = true;
   new rwmodProtect(f, new File(f.getParent(), rwmodProtect.out(f)), cui);
   arr.add(cui);
  }
 }
 public void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);
  if (requestCode == 0) {
   if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
    Intent = null;
    init();
   } else startActivityForResult(Intent, 0);
  } else if (resultCode == RESULT_OK) {
   Uri uri=data.getData();
   if (uri != null) {
    add(uri);
   } else {
    ClipData datas=data.getClipData();
    int i=datas.getItemCount(),t=0;
    while (t < i) {
     uri = datas.getItemAt(t).getUri();
     add(uri);
     ++t;
    }
   }
  }
 }
 public void sw(View v) {
  String s=ed.getText().toString().trim();
  File f;
  if (s.length() != 0 && (f = new File(s)).exists()) {
   init = true;
   ed.setText("");
   bar.setVisibility(0);
   File lb=new File(getExternalFilesDir(null), "lib.zip");
   new lib(f, lb, new cui("lib"));
  } else startActivityForResult(sw, 1);
 }
 public void ch(View v) {
  CheckBox ch=(CheckBox)v;
  boolean is=ch.isChecked();
  if (is && !init) {
   init = true;
   lib();
  }
  SharedPreferences.Editor ed=sha.edit();
  ed.putBoolean("", is);
  ed.apply();
 }
 public void log(View v) {
  boolean debug = ((CheckBox)v).isChecked();
  SharedPreferences.Editor ed=sha.edit();
  log.debug = debug;
  ed.putBoolean("0", debug);
  ed.apply();
 }
}
