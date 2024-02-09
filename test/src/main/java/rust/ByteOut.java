package rust;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ByteOut extends ByteArrayOutputStream {
 public ByteArrayInputStream toInput() {
  return new ByteArrayInputStream(buf, 0, count);
 }
}
