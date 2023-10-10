
import java.util.Comparator;
public class cmpU implements Comparator<charc> {
 int type;
 public int compare(charc o1, charc o2) {
  int m,n;
  switch(type) {
   case 0:
    m = o1.pix;
    n = o2.pix;
    break;
   case 1:
    m = o1.x;
    n = o2.x;
    break;
   case 2:
    m = o1.y;
    n = o2.y;
    break;
   default:
    m = (Math.max(o1.x, 1) * Math.max(o1.y,1));
    n = (Math.max(o2.x, 1) * Math.max(o2.y,1));
    break;
  }
  if (m > n)return 1;
  else if (m < n)return -1;
  return 0;
 }
}
