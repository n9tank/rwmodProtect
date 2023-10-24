package rust.rwp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ProgressBar;

public class uis extends BaseAdapter {
 public static ArrayList arr=new ArrayList();
 public static LayoutInflater layout;
 public int getCount() {
  return arr.size();
 }
 public Object getItem(int p1) {
  return null;
 }
 public long getItemId(int p1) {
  return 0;
 }
 public View getView(int p1, View convertView,ViewGroup p3) {
  cui ui=(cui)arr.get(p1);
  String name=ui.ti;
  int poss=ui.to;
  TextView title;
  ProgressBar bar;
  if (convertView == null) {
   convertView = layout.inflate(R.layout.View,null,false);
   appview View=new appview();
   title = View.title = convertView.findViewById(R.id.ti);
   bar = View.bar = convertView.findViewById(R.id.ps);
   convertView.setTag(View);
  } else {
   appview View=(appview)convertView.getTag();
   title = View.title;
   bar = View.bar;
  }
  title.setText(name);
  if(poss<0)poss=100;
  bar.setProgress(poss);
  return convertView;
 }
}
