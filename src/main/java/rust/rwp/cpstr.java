package rust.rwp;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;

public class cpstr implements Dialog.OnClickListener {
 static ClipboardManager manager;
 public String str;
 public void onClick(DialogInterface p1, int p2) {
  ClipData mClipData =ClipData.newPlainText("Label",str);
  manager.setPrimaryClip(mClipData);
 }
}
