
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Color;
public class charc{
public int pix;
public float x;
public float y;
public String s;
public static Paint paint;
public static Rect recs;
public static Bitmap bit;
public static int pixe[];
public charc(String c){
s=c;
Paint pa=paint;
Rect rec=recs;
pa.getTextBounds(s,0,1,rec);
x=pa.measureText(s);
y=rec.bottom-rec.top;
int pi=0;
int[] pixes=pixe;
bit.getPixels(pixes,0,100,0,0,100,100);
int y=10000;
while (--y>=0) {
if(pixes[y]!=0) {
pi++;
}
}
pix=pi;
}
}
