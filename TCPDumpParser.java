import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class TCPDumpParser {
    private static BufferedReader input;
    private static BufferedWriter output;

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                input = new BufferedReader(new FileReader(args[0]));
                output = new BufferedWriter(new FileWriter("output.txt"));
                parse();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("No file provided");
        }
    }

    static void parse() {
        try {
            String line;
            while ((line = input.readLine()) != null) {
                output.write(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Packet {
        private int id;
        private String ip;
        private Long timestamp;

        public Packet(int id, String ip, Long timestamp) {
            this.id = id;
            this.ip = ip;
            this.timestamp = timestamp;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

    }
}
