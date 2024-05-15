import java.io.*;

class DemoEksepsi1 {
    public static void main(String arg[]) {
        try {
            File test = new File("c:\test.txt");
            test.createNewFile();
        } catch (IOException e) {
            System.out.println("IO Exception sudah ditangani");
        }
    }
}
