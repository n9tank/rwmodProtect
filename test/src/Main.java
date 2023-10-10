
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
//import rust.loderLib;


public class Main {
 static cmpU cm;
 public static void srot(ArrayList crr, int type) {
  cmpU cmp=cm;
  cmp.type = type;
  Collections.sort(crr, cmp);
 }
 public static void charList() throws Exception {
  char to=Character.MAX_VALUE;
  int index=to - 32;
  ArrayList crr=new ArrayList();
  //StringBuilder dump=new StringBuilder();
  Bitmap map=Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
  Canvas vs=new Canvas(map);
  Paint pain=new Paint();
  Rect rec=new Rect();
  pain.setAntiAlias(true);
  pain.setTextSize(40);
  pain.setColor(Color.BLACK);
  int pixes[]=new int[10000];
  while (--index >= 0) {
   if (to >= 0xd800 && to <= 0xdfff) {
    to = 0xd7ff;
    index = to - 32;
   }
   String s=String.valueOf(to--);
   if (pain.hasGlyph(s)) {
    float x=pain.measureText(s);
    if (x <= 0.f) {
     pain.getTextBounds(s, 0, 1, rec);
     vs.drawText(s, -rec.left, -rec.top, pain);
     int w=rec.width();
     int h=rec.height();
     map.getPixels(pixes, 0, w, 0, 0,w,h);
     int ym=w*h;
     int pi=0;
     while (--ym >= 0) {
      if (pixes[ym] != 0) {
       ++pi;
      }
     }
     charc c=new charc(s,w,h,pi);
     crr.add(c);
     map.eraseColor(0);
    }
   } else {
   // dump.append(s);
   }
  }
  cm = new cmpU();
  int size=crr.size();
  srot(crr,0);
 // size*=0.2;
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
  int i=0;
  do{
   charc c=(charc)crr.get(i);
   buff.write(c.s);
  }while(++i < size);
 // buff.write(dump.toString());
  buff.flush();
  buff.close();
 }
 public static void main(String arg[]) throws Exception {
  //toLib("sdcard/rustedWarfare/lib.apk");
  charList();
  System.out.print("finsh");
 }
 /*
  public static void toLib(String str) throws Exception{
  ZipFile zip=new ZipFile(str);
  ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("sdcard/lib.zip")));
  OutputStreamWriter wt=new OutputStreamWriter(out);
  Enumeration<? extends ZipEntry> en=zip.entries();
  while(en.hasMoreElements()){
  ZipEntry zipe=en.nextElement();
  String name;
  if(!zipe.isDirectory()&&(name=zipe.getName()).endsWith(".ini")){
  loderLib loder=new rust.loderLib(zip.getInputStream(zipe));
  out.putNextEntry(new ZipEntry(name.substring(13)));
  loder.write(wt);
  out.closeEntry();
  }
  }
  zip.close();
  out.close();
  }*/
}
