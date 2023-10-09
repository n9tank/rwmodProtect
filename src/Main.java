
import java.io.File;
import java.util.Scanner;
import rust.rwmodProtect;
import rust.ui;
public class Main {
 public static void main(String[] args) throws Exception {
  rwmodProtect.init();
  Scanner sc=new Scanner(System.in);
  while(true){
   File f=new File(sc.nextLine());
   if(f.length()==0){
    System.out.println("文件异常");
    continue;
   }else{
    ui.exec(f);
   }
  }
 }
}
