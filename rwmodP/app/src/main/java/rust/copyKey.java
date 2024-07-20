package rust;
import java.util.Arrays;
import rust.copyKey;

public class copyKey {
 public loder copy[];
 public int hashCode;
 public copyKey(loder copy[]) {
  int hash =17;
  hashCode=hash*31+Arrays.hashCode(copy);
  this.copy = copy;
 }
 public int hashCode() {
  return hashCode;
 }
 public boolean equals(Object obj) {
  return hashCode == obj.hashCode() && Arrays.equals(copy, ((copyKey)obj).copy);
 }
}
