package rust.rwp;

import rust.ui;
import android.widget.ProgressBar;
import android.widget.ListAdapter;
import java.util.LinkedList;
import android.widget.TextView;

public class cui implements ui,Runnable {
 String ti;
 boolean ui;
 Throwable err;
 public cui(String s) {
  ti = s;
 }
 public void end(Throwable e) {
  err = e;
  Main.bar.post(this);
 }
 public String toString() {
  return ti;
 }
 public void run() {
  Throwable e;
  TextView br=Main.bar;
  if ((e = err) != null) {
   Main.error(e, ti, br.getContext());
  }
  if (!ui) {
   br.setVisibility(8);
  } else {
   Main.arr.remove(this);
  }
 }
}
