# Traceroute
## Project Description
A program that parses a network trace dump generated captured by tcpdump operations. It extracts information such as each router's address on the path to the destination and the time taken to send and receive data to each router by analyzing ICMP messages.
Involves reading data from the TCP dump file, parsing packet headers, extracting destination IP addresses, protocol types, and timestamps.

## Requirements
- Java Developement Kit (JDK)

Note: JDK 21 was used to develop this program, it is recommend to use the same version to ensure everything works properly.

# Compiling the Program
Use the provided [run script](run.sh) which will compile and run the program.
```./run.sh```

Or compile with ```javac TCPDumpParser.java```

## Running the Program
Use the [run script](run.sh) which will run the program and it will also print the output.

Or run with ```java TCPDumpParser sampletcpdump.txt```

Note: sampletcpdump.txt can be replaced with any other network trace dump.
