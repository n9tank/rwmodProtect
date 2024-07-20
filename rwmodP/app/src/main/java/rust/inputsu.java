package rust;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.IOException;
import carsh.log;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.parallel.InputStreamSupplier;

class inputsu implements InputStreamSupplier {
 ZipFile zip;
 ZipEntry en;
 inputsu(ZipFile in, ZipEntry e) {
  zip = in;
  en = e;
 }
 public InputStream get() {
  InputStream inr=null;
  try {
   inr = zip.getInputStream(en);
  } catch (Throwable e) {
   log.e(this,e);
  }
  return inr;
 }
}
