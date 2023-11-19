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

class inputsu implements InputStreamSupplier {
 loder ini;
 ZipFile zip;
 ZipArchiveEntry en;
 inputsu(loder lod) {
  ini = lod;
 }
 inputsu(ZipFile in, ZipArchiveEntry e) {
  zip = in;
  en = e;
 }
 public InputStream get() {
  InputStream inr=null;
  ZipArchiveEntry ens=en;
  if (ens == null) {
   try {
    ByteArrayOutputStream rus = new ByteArrayOutputStream();
    BufferedWriter out=new BufferedWriter(new OutputStreamWriter(rus));
    loder lod=ini;
    if (lod.ini == null)lod.call();
    try {
     HashMap map=lod.ini;
     Iterator<Map.Entry<String,HashMap>> ite=map.entrySet().iterator();
     if (ite.hasNext()) {
      Map.Entry en = ite.next();
      map = (HashMap)en.getValue();
      boolean size=map.size() > 0;
      while (true) {
       if (size) {
        String key=(String)en.getKey();
        out.write('[');
        out.write(key);
        out.write("]\n");
        Iterator<Map.Entry> ite2=map.entrySet().iterator();
        boolean nx=ite2.hasNext();
        while (nx) {
         en = ite2.next();
         out.write((String)en.getKey());
         out.write(':');
         out.write((String)en.getValue());
         nx = ite2.hasNext();
         if (nx)out.write('\n');
        }
       }
       if (!ite.hasNext())break;
       en = ite.next();
       map = (HashMap)en.getValue();
       size = map.size() > 0;
       if (size)out.write('\n');
      }
     }
     out.flush();
     return new ByteArrayInputStream(rus.toByteArray());
    } finally {
     out.close();
    }
   } catch (Throwable e) {
   }
  } else {
   try {
    inr = zip.getInputStream(en);
   } catch (Throwable e) {
   }
  }
  return inr;
 }
}
