import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        FileReader fileReader;
        char[] buffer = new char[100];
        if (args.length > 0) {
            try {
                fileReader = new FileReader(args[0]);
                if (fileReader.ready()) {
                    while (fileReader.read() != -1) {
                        // System.out.println(fileReader.read(buffer, 0, 10));
                        fileReader.read(buffer, 0, 100);

                        System.out.println(buffer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
