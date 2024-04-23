import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPDumpParser {
    private static BufferedReader input;
    private static BufferedWriter output;
    private static Pattern tcpPattern = Pattern.compile("^(\\d+\\.\\d+) IP \\(tos .*? ttl (\\d+), id (\\d+), .*");
    private static Pattern icmpPattern = Pattern.compile(
            "^(\\d+\\.\\d+) IP \\(tos .*? ttl (\\d+), id (\\d+), .*\\)\n\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+).*?\n\\s+IP \\(tos .*? ttl (\\d+), id (\\d+), .*\\)");

    private static List<TCPPacket> tcpPackets = new ArrayList<>();
    private static List<ICMPMessage> icmpMessages = new ArrayList<>();

    public static void main(String[] args) throws IOException {
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
        Comparator<TCPPacket> tcpComparator = Comparator.comparing(TCPPacket::getId);
        Comparator<ICMPMessage> icmpComparator = Comparator.comparing(ICMPMessage::getId);

        Collections.sort(tcpPackets, tcpComparator);
        Collections.sort(icmpMessages, icmpComparator);

        // for (TCPPacket packet : tcpPackets) {
        // System.out.println(String.format("id: %d, ttl: %d, timestamp : %f",
        // packet.getId(), packet.getTtl(),
        // packet.getTimestamp()));
        // }

        // for (ICMPMessage message : icmpMessages) {
        // System.out.println(
        // String.format("id: %d, ttl: %d, timestamp : %f, ip : %s", message.getId(),
        // message.getTtl(),
        // message.getTimestamp(), message.getIp()));
        // }

        int ttl = 0;
        int currTTL = ttl;
        double rtt = 0;

        while (!(tcpPackets.isEmpty() || icmpMessages.isEmpty())) {
            if (tcpPackets.get(0).getId() == 0) {
                tcpPackets.remove(0);
                continue;
            }

            ttl = tcpPackets.get(0).getTtl();
            if (ttl != currTTL) {
                currTTL = ttl;
                output.write(String.format("TTL %d \n", tcpPackets.get(0).getTtl()));
                output.write(String.format("%s \n", icmpMessages.get(0).getIp()));
            } else {
                if (tcpPackets.get(0).getId() == icmpMessages.get(0).getId()) {
                    rtt = (icmpMessages.get(0).getTimestamp() - tcpPackets.get(0).getTimestamp()) * 1000;
                    output.write(String.format("%3.3fms \n", rtt));
                    tcpPackets.remove(0);
                    icmpMessages.remove(0);
                }
            }
        }
        output.close();
        input.close();
    }

    static void parse() {
        try {
            String line;
            Matcher matcher;
            while ((line = input.readLine()) != null) {
                if (line.contains("TCP")) {
                    matcher = tcpPattern.matcher(line);
                    if (matcher.find()) {
                        double timestamp = Double.parseDouble(matcher.group(1));
                        int ttl = Integer.parseInt(matcher.group(2));
                        int id = Integer.parseInt(matcher.group(3));
                        tcpPackets.add(new TCPPacket(id, timestamp, ttl));
                    }
                } else if (line.contains("ICMP")) {
                    line += "\n" + input.readLine() + "\n" + input.readLine();
                    matcher = icmpPattern.matcher(line);
                    if (matcher.find()) {
                        double timestamp = Double.parseDouble(matcher.group(1));
                        int ttl = Integer.parseInt(matcher.group(2));
                        int id = Integer.parseInt(matcher.group(6));
                        String ip = matcher.group(4);
                        icmpMessages.add(new ICMPMessage(id, ip, timestamp, ttl));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class TCPPacket {
        private int id;
        private double timestamp;
        private int ttl;

        public TCPPacket(int id, double timestamp, int ttl) {
            this.id = id;
            this.timestamp = timestamp;
            this.ttl = ttl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(double timestamp) {
            this.timestamp = timestamp;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            long temp;
            temp = Double.doubleToLongBits(timestamp);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + ttl;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TCPPacket other = (TCPPacket) obj;
            if (id != other.id)
                return false;
            if (Double.doubleToLongBits(timestamp) != Double.doubleToLongBits(other.timestamp))
                return false;
            if (ttl != other.ttl)
                return false;
            return true;
        }

    }

    static class ICMPMessage {
        private int id;
        private String ip;
        private double timestamp;
        private int ttl;

        public ICMPMessage(int id, String ip, double timestamp, int ttl) {
            this.id = id;
            this.ip = ip;
            this.timestamp = timestamp;
            this.ttl = ttl;
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

        public double getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(double timestamp) {
            this.timestamp = timestamp;
        }

        public int getTtl() {
            return ttl;
        }

        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            result = prime * result + ((ip == null) ? 0 : ip.hashCode());
            long temp;
            temp = Double.doubleToLongBits(timestamp);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + ttl;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            ICMPMessage other = (ICMPMessage) obj;
            if (id != other.id)
                return false;
            if (ip == null) {
                if (other.ip != null)
                    return false;
            } else if (!ip.equals(other.ip))
                return false;
            if (Double.doubleToLongBits(timestamp) != Double.doubleToLongBits(other.timestamp))
                return false;
            if (ttl != other.ttl)
                return false;
            return true;
        }

    }
}
