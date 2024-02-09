package rust;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class zipout extends ZipArchiveOutputStream {
 public zipout(File path) throws IOException {
  super(path);
 }
 public void addRawArchiveEntry(ZipArchiveEntry entry, InputStream rawStream) throws IOException {
  entry.setSize(0);
  super.addRawArchiveEntry(entry, rawStream);
 } 
}
