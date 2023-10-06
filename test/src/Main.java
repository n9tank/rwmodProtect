
import android.graphics.Paint;
import android.graphics.Rect;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import rust.loderLib;


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
 // StringBuilder dump=new StringBuilder();
 /* Bitmap map=Bitmap.createBitmap(20,20,Bitmap.Config.ALPHA_8);
  Canvas vs=new Canvas(map);*/
  Paint pain=new Paint();
  charc.paint=pain;
  charc.recs=new Rect();
  /*pain.setAntiAlias(true);
  pain.setTextSize(10);
  pain.setColor(Color.BLACK);
  charc.bit=map;
  charc.pixe=new int[400];*/
  while (--index >= 0){
   String s=String.valueOf(to--);
   if (pain.hasGlyph(s)){
    //vs.drawText(s,0,10,pain);
    charc c=new charc(s);
    if(c.x==0){
    crr.add(c);
    }
    //map.eraseColor(0);
   } else {
  // dump.append(s);
   //dump.append("\n");
   }
  }
  cm=new cmpU();
  srot(crr,3);
  BufferedWriter buff=new BufferedWriter(new FileWriter("sdcard/a.txt"));
  for(charc c:crr){
  buff.write(c.s);
  }
  //buff.write(dump.toString());
  buff.flush();
  buff.close();
 }
 public static void main(String arg[]) throws Exception{
  //toLib("sdcard/rustedWarfare/lib.apk");
  //charList();
  System.out.println("finsh");
 }
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
 }
}
