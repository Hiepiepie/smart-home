package Zentral;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import Zentral_Sensor.DataSender;
import Zentral_Sensor.DataSenderHandler;
import Zentral.JavaHTTPServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;


public class Zentral implements Runnable{


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
    File oFile;

    public static DataSenderHandler handler;
    public static DataSender.Processor processor;


    //--------------------------------------------------------------------- main
    public static void main(String[] args) throws Exception {

        int PORT = 8080;
        try {
            handler = new DataSenderHandler();
            processor = new DataSender.Processor(handler);

            Runnable startThrift = new Runnable() {
                public void run() {
                    startThrift(processor);
                }
            };
            new Thread(startThrift).start();

            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
            Zentral zentral = new Zentral();
            new Thread(zentral).start();
            // we listen until user halts server execution
            while (true) {

                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
                Thread myHTTP = new Thread(myServer);
                myHTTP.start();

            }
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }


    //---------------------------------------------------------------------- START UDP

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
        saveData(today,typeClient,infoClient,idClient);
    }

    private void saveData(Date date,String type, String info, int idClient) throws IOException {
        separator = System.getProperty("file.separator");
        String PATH = "src" + separator + "main" + separator + "resources" + separator + typeClient+ separator + "log.html";
        Path path = Paths.get(PATH);
        try {
            if (Files.notExists(path)) {
                oFile = new File(PATH);
                oFile.createNewFile();
                appendToFile(date, type, info, idClient, PATH);
            } else {
                appendToFile(date, type, info, idClient, PATH);
            }
        } catch ( Exception e){
            e.printStackTrace();
        }

    }

    private void appendToFile(Date date, String type, String info, int idClient, String PATH) {
        String device;
        switch (type){
            case("Temperatur"):
                device = "thermometer";
                break;

            case("Brightness"):
                device = "light";
                break;

            case("Humidity"):
                device = "hygrometer";
                break;

            default: device = " ";
        }
        try(FileWriter fw = new FileWriter(PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println("<br>********************************************<br>"
                + "<a href=\"http://localhost:8080/" + device + "/id=" + idClient + "\"> " + date + " </a><br>" + "<p style=\"color: orangered\">"+type + " " + info + " | ID : " + idClient +"</p>" );

        } catch (IOException e) {
            e.printStackTrace();
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
        System.out.println(ANSI_RESET + "Server-> IP : " + iaClient + " | Port : " + portClient + " | " + typeClient + " Information : " + infoClient + " --- ID: " + idClient + "\n");
    }

    public void clearLogFile() throws IOException {
        separator = System.getProperty("file.separator");
        String str = "<body style=\"background: antiquewhite; font-size: 15pt; text-align: center\">";

        String PATH = "src" + separator + "main" + separator + "resources" + separator + "Brightness" + separator + "log.html";
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH));
        writer.write(str);
        writer.close();

        PATH = "src" + separator + "main" + separator + "resources" + separator + "Temperatur" + separator + "log.html";
        writer = new BufferedWriter(new FileWriter(PATH));
        writer.write(str);
        writer.close();

        PATH = "src" + separator + "main" + separator + "resources" + separator + "Humidity" + separator + "log.html";
        writer = new BufferedWriter(new FileWriter(PATH));
        writer.write(str);
        writer.close();
    }

    //Thrift Methods
    public static void startThrift(DataSender.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the Thrift Service on port 9090...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            this.clearLogFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                this.receivePackage();
                this.extractPackage();
                handler.setCurrentData(msg, typeClient);
                this.packetCheck();
                this.printInformation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}




