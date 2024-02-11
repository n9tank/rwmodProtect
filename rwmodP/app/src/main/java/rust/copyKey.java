package rust;
import java.util.Arrays;

public class copyKey {
 public loder copy[];
 public loder all;
 public int hashCode;
 public copyKey(loder copy[], loder al) {
  int hash = Arrays.hashCode(copy);
  if (al != null) {
   all = al;
   if (hash == 0)hash = 1;
   hash = hash * 31 + al.hashCode();
  }
  hashCode = hash;
  this.copy = copy;
 }
 public int hashCode() {
  return hashCode;
 }
 public boolean equals(Object obj) {
  copyKey co;
  return hashCode == obj.hashCode() && (all == (co = (copyKey)obj).all) && Arrays.equals(copy, co.copy);
 }
}
