package rust;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.parallel.InputStreamSupplier;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import java.io.IOException;
import carsh.log;

class inputsu implements InputStreamSupplier {
 ZipFile zip;
 ZipArchiveEntry en;
 inputsu(ZipFile in, ZipArchiveEntry e) {
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
