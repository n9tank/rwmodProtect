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
import java.util.Random;

public class test {
 public static void charList(int start, int max, BufferedWriter buff) throws Exception {
  ArrayList crr=new ArrayList();
  //Bitmap map=Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
  //Canvas vs=new Canvas(map);
  Paint pain=new Paint();
  Rect rec=new Rect();
  pain.setAntiAlias(true);
  pain.setTextSize(40);
  pain.setColor(Color.BLACK);
  //int pixes[]=new int[10000];
  while (start < max) {
   if (start >= 0xd800 && start <= 0xdfff)start = 0xe000;
   String s=String.valueOf((char)start++);
   if (pain.hasGlyph(s)) {
    float x=pain.measureText(s);
    if (x <= 0) {
     pain.getTextBounds(s, 0, 1, rec);
     int h=rec.height();
     if (h > 0)continue;
     /* int w=rec.width();
      vs.drawText(s, -rec.left, -rec.top, pain);
      map.getPixels(pixes, 0, w, 0, 0, w, h);
      int ym=w * h;
      int pi=0;
      while (--ym >= 0) {
      if (pixes[ym] != 0) {
      ++pi;
      }
      }*/
     //map.eraseColor(0);
    }
   } else {
    crr.add(s);
   }
  }
  boolean notry=false;
  charc charc=new charc();
  Collections.sort(crr, charc);
  for (int i=0,l=crr.size();i < l;++i) {
   String c=(String)crr.get(i);
   if (!notry && (notry = charc.cons(c) == 1)) {
    System.out.println(i);
   }
   buff.write(c);
  }
  buff.write('\n');
  System.out.println("len:"+crr.size());
 }
 public static void main(String arg[])throws Exception {
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
   charList(33, 0x7f, buff);
  charList(0x80, 0x07ff, buff);
  charList(0x0800, 0xffff, buff);
  buff.close();
  System.out.println();
 }
}
