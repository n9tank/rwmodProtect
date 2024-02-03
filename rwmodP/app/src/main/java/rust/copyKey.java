package rust;
import java.util.Arrays;

public class copyKey {
 public loder copy[];
 public loder all;
 public int hashCode;
 public copyKey(loder copy[], loder all) {
  int hash=Arrays.hashCode(copy);
  this.copy = copy;
  if (all != null) {
   if (hash == 0)hash = 1;
   hash = 31 * hash + all.hashCode();
   this.all = all;
  }
  hashCode = hash;
 }
 public int hashCode() {
  return hashCode;
 }
 public boolean equals(Object obj) {
  copyKey key=(copyKey)obj;
  return all == key.all && Arrays.equals(copy, key.copy);
 }
}
