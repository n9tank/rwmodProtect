package rust;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteOut extends ByteArrayOutputStream {
 public ByteOut() {
  super(256);
 }
 public void sub() {
  --count;
 }
 public byte[] get() {
  return buf;
 }
}
