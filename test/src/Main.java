
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
  cmp.type=type;
  Collections.sort(crr,cmp);
 }
 public static void charList() throws Exception {
  char to=Character.MAX_VALUE;
  int index=to - 32;
  ArrayList crr=new ArrayList();
  StringBuilder dump=new StringBuilder();
  Bitmap map=Bitmap.createBitmap(100,100,Bitmap.Config.ALPHA_8);
  Canvas vs=new Canvas(map);
  Paint pain=new Paint();
  charc.paint=pain;
  charc.recs=new Rect();
  pain.setAntiAlias(true);
  pain.setTextSize(40);
  pain.setColor(Color.BLACK);
  charc.bit=map;
  charc.pixe=new int[10000];
  while (--index >= 0){
   if(to>=0xd800&&to<=0xdfff){
    to=0xd7ff;
    index=to-32;
   }
   String s=String.valueOf(to--);
   if (pain.hasGlyph(s)){
    vs.drawText(s,50,50,pain);
    charc c=new charc(s);
    if(c.x<=0.f){
    crr.add(c);
    }
    map.eraseColor(0);
   } else{
   dump.append(s);
   //dump.append("\n");
   }
  }
  cm=new cmpU();
  int size=crr.size();
 // srot(crr,2);
  srot(crr,0);
  size*=0.2;
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
  int i=0;
  do{
  charc c=(charc)crr.get(i);
  buff.write(c.s);
  }while(++i<size);
  buff.write(dump.toString());
  buff.flush();
  buff.close();
 }
 public static void main(String arg[]) throws Exception{
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
