package rust;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageUtil {
 public static File tmxPngOpt(byte[] imgarr, boolean ARGB_8888, int tileWidth, int tileHeight, int size, int first, int tilec, int tilew, HashMap<Integer, Integer> tiles) throws IOException {
  int imageType = ARGB_8888 ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_USHORT_565_RGB;
  BufferedImage img=null;
  ImageInputStream inputStream =ImageIO.createImageInputStream(new ByteArrayInputStream(imgarr));
  ImageReader reader=ImageIO.getImageReadersByFormatName("png").next();
  try {
   reader.setInput(inputStream, false, true);
   ImageReadParam pr=reader.getDefaultReadParam();
   img = new BufferedImage(reader.getWidth(0), reader.getHeight(0), imageType);
   pr.setDestination(img);
   reader.read(0, pr);
  } finally {
   reader.dispose();
   inputStream.close();
  }
  BufferedImage bm2 = new BufferedImage(tileWidth, tileHeight * size, imageType);
  Graphics g2d= bm2.getGraphics();
  int v = 0, j = 0;
  for (int c = tilec; --c >= 0;) {
   Integer key = c + first;
   if (tiles.containsKey(key)) {
    int left = c % tilew * tileWidth;
    int top = c / tilew * tileHeight;
    int n = v + tileHeight;
    tiles.put(key, j++ + first);
    g2d.drawImage(img, 0, v, tileWidth, n, left, top, left + tileWidth, top + tileHeight, null);
    v = n;
   }
  }
  g2d.dispose();
  File tmp = rwmapOpt.openTmp();
  ImageIO.write(bm2, "png", tmp);
  return tmp;
 }
}
