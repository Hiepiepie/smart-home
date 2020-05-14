package Zentral_Sensor;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;

public class Zentral{
    //---------------------------------------------------------------- START UDP
        String separator = File.separator;
        private InetAddress ia;
        private final DatagramSocket ds;
        protected DatagramPacket dp;
        final int port = 1234;
        private byte[] buf = new byte[1024];
        private HashMap<Integer, Integer> checkBuffer;
        protected String msg;
        protected String[] msgArray;
        protected String iaClient;
        protected String typeClient;
        protected int portClient;
        protected int idClient;
        protected String infoClient;
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_RESET = "\u001B[0m";
        private int dataID;

        File oFile;


    public Zentral() throws IOException {
        ia = InetAddress.getLocalHost();
        this.ds = new DatagramSocket(port);

        checkBuffer = new HashMap<>();
        dp = new DatagramPacket(buf, buf.length, ia, port);

    }


        public void receivePackage () throws IOException {
        byte[] buf = new byte[1024];
        dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        msg = new String(dp.getData(), 0, dp.getLength());
    }

        public void extractPackage () throws IOException {
        msgArray = msg.split(";");
        iaClient = String.valueOf(dp.getAddress());
        portClient = dp.getPort();
        idClient = Integer.parseInt(msgArray[0]);
        typeClient = msgArray[1];
        infoClient = msgArray[2];
            Date today = new Date();
            saveData(today,typeClient,infoClient);
        }

    private void saveData(Date d,String s1, String s2) throws IOException {
        separator = System.getProperty("file.separator");
        String PATH = "src" + separator + "main" + separator + "resources" + separator + typeClient+ separator + "log.html";
        Path path = Paths.get(PATH);
        try {
            if (Files.notExists(path)) {
                oFile = new File(PATH);
                oFile.createNewFile();
                appendToFile(d, s1, s2, PATH);
            } else {
                appendToFile(d, s1, s2, PATH);
            }
        } catch ( Exception e){
            e.printStackTrace();
        }

    }

    private void appendToFile(Date d, String s1, String s2, String PATH) {
        try(FileWriter fw = new FileWriter(PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(d + "\n" + s1 + " " + s2 +"\r");

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }


    public void packetCheck () {
        if (checkBuffer.get(portClient) == null) {
            checkBuffer.put(portClient, idClient);
            return;
        }
        if ((checkBuffer.get(portClient) + 1) != idClient) {
            System.out.println(ANSI_RED + "Packet loss detected from IP : " + iaClient + " | Port : " + portClient
                    + " | Type : " + typeClient + "\n");
        }
        if (idClient == 999) checkBuffer.put(portClient, 0);
        else checkBuffer.put(portClient, idClient);
    }

        public void printInformation () {
        System.out.println(ANSI_RESET + "Server-> IP : " + iaClient + " | Port : " + port + " | " + typeClient + " Information : " + infoClient + " --- ID: " + idClient + "\n");
    }
    //--------------------------------------------------------------------- END UDP

    //-------------------------------------------------------------------- HTTP-TCP

    public void listening_HTTP_GET(Zentral zentral) throws IOException, URISyntaxException {
        String PATH = "src" + separator + "main" + separator + "resources" + separator + typeClient+ separator + "log.html";

        ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");

        while (true) {
            zentral.receivePackage();
            zentral.extractPackage();
            zentral.packetCheck();
            zentral.printInformation();

            try (Socket socket = server.accept()) {                 //open connection HTTP-GET
                Date today = new Date();
                logHTTPRequest(socket);
                File myFile = new File(PATH);
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" ; // give the data here
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            }
        }

    }

    private void logHTTPRequest(Socket socket) throws IOException {
        InputStreamReader isr =  new InputStreamReader(socket.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        String line = reader.readLine();

        while (!line.isEmpty()) {
            System.out.println(line);
            line = reader.readLine();
        }

    }

    //--------------------------------------------------------------------- main
    public static void main(String[] args) throws Exception {
        Zentral zentral = new Zentral();
        zentral.listening_HTTP_GET(zentral);
    }


}
