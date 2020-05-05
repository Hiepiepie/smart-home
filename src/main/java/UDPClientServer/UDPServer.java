package UDPClientServer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UDPServer{

    public static void main(String[] args) throws Exception {
        UDPServer udpServer = new UDPServer();
        while (true){
            udpServer.receivePackage();
            udpServer.extractPackage();
            udpServer.packetCheck();
            udpServer.printInformation();
        }

    }

    private volatile boolean exit;
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


    public UDPServer() throws SocketException, UnknownHostException {
        ia = InetAddress.getLocalHost();
        this.ds = new DatagramSocket(port);
        exit = false;
        checkBuffer = new HashMap<>();
        dp = new DatagramPacket(buf, buf.length, ia, port);

    }

    public void stop(){
        exit= true;
    }

    public void receivePackage() throws IOException {
        byte[] buf = new byte[1024];
        dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        msg = new String(dp.getData(), 0, dp.getLength());
    }

    public void extractPackage() {
        msgArray = msg.split(";");
        iaClient = String.valueOf(dp.getAddress());
        portClient = dp.getPort();
        idClient = Integer.parseInt(msgArray[0]);
        typeClient = msgArray[1];
        infoClient = msgArray[2];
    }

    public void packetCheck(){
        if(checkBuffer.get(portClient) == null){
            checkBuffer.put(portClient, idClient);
            return;
        }
        if((checkBuffer.get(portClient) + 1) != idClient){
            System.out.println(ANSI_RED + "Packet loss detected from IP : " + iaClient + " | Port : " + portClient
                    + " | Type : " + typeClient + "\n");
        }
        if(idClient == 999) checkBuffer.put(portClient, 0);
        else checkBuffer.put(portClient, idClient);
    }

    public void printInformation(){
        System.out.println(ANSI_RESET + "Server-> IP : " + iaClient + " | Port : " + port + " | Information : " + infoClient + "\n");
    }
}
