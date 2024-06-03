package rust;
import java.util.Arrays;

public class copyKey {
 public loder copy[];
 public loder all;
 public int hashCode;
 public copyKey(loder copy[], loder al) {
  int hash =17;
  hash=hash*31+Arrays.hashCode(copy);
  hashCode = hash * 31 +((all=al)==null?0: al.hashCode());
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
