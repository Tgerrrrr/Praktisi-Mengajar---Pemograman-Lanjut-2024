import java.io.FileInputStream;
import java.io.FileOutputStream;

public class main {

    public static void main(String[] args) {
        int kar;
        try {
            FileInputStream filel = new FileInputStream(teks.txt);
            FileOutputStream fileout = new FileOutputStream(teks.txt);
            while ((kar = filel.read()) != -1) {
                fileout.write(kar);
                System.out.print((char) kar);
            }
            fileout.close();
            filel.close();
        } catch (Exception e) {
            System.out.println("File Tidak Ada");
        }
        System.out.println(" ");
    }
}
