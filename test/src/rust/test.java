package rust;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class test {
 static cmpU cm;
 
 public static void srot(ArrayList crr, int type) {
  cmpU cmp=cm;
  cmp.type = type;
  Collections.sort(crr, cmp);
 }
 public static void charList() throws Exception {
  char to=0xFFFF;
  int index=to - 32;
  ArrayList crr=new ArrayList();
  StringBuilder dump=new StringBuilder();
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
    if (x <= 0) {
     pain.getTextBounds(s, 0, 1, rec);
     int h=rec.height();
    if (h > 0)continue;
     int w=rec.width();
      vs.drawText(s, -rec.left, -rec.top, pain);
      map.getPixels(pixes, 0, w, 0, 0, w, h);
      int ym=w * h;
      int pi=0;
      while (--ym >= 0) {
      if (pixes[ym] != 0) {
      ++pi;
      }
      }
     charc c=new charc(s, w, h, pi);
     crr.add(c);
     map.eraseColor(0);
    }
   } else {
    dump.append(s);
   }
  }
  cm = new cmpU();
  int size=crr.size();
  srot(crr, 0);
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
 int i=0;
  do{
   charc c=(charc)crr.get(i);
   buff.write(c.s);
  }while(++i < size);
  buff.write(dump.toString());
  buff.flush();
  buff.close();
 }
 public static void main(String arg[])throws Exception{
  charList();
  System.out.println();
 }
}
