package rust.rwp;

import rust.ui;
import android.widget.ProgressBar;
import android.widget.ListAdapter;
import java.util.LinkedList;
import android.widget.TextView;

public class cui implements ui,Runnable {
 String ti;
 boolean ui;
 int to;
 Throwable err;
 public cui(String s) {
  ti = s;
 }
 public void poss(int i) {
  to = i;
  Main.bar.post(this);
 }
 public void end(Throwable e) {
  to=-1;
  err = e;
  Main.bar.post(this);
 }
 public void run() {
  Throwable e;
  ProgressBar br=Main.bar;
  if ((e = err) != null) {
   Main.error(e, ti, br.getContext());
  }
  if (!ui) {
   int i=to;
   if (i >= 0) {
    br.setVisibility(0);
    br.setProgress(i);
    return;
   }
   br.setVisibility(8);
  } else {
   if(to<0)uis.arr.remove(this);
   Main.ui.notifyDataSetChanged();
  }
 }
}
