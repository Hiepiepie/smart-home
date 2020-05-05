package UDPClientServer;

import java.io.IOException;
import java.net.*;

public class UDPServer{

    public static void main(String[] args) throws Exception {
        UDPServer udpServer = new UDPServer();
        while (true)
        udpServer.receivePackage();


    }

    private volatile boolean exit;
    private InetAddress ia;
    private final DatagramSocket ds;
    protected DatagramPacket dp;
    final int port = 1234;
    private byte[] buf = new byte[1024];

    public UDPServer() throws SocketException, UnknownHostException {
        ia = InetAddress.getLocalHost();
        this.ds = new DatagramSocket(port);
        exit = false;
    }



    public void stop(){
        exit= true;
    }

    public void receivePackage() throws IOException {
        byte[] buf = new byte[1024];
        dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        String str = new String(dp.getData(), 0, dp.getLength());
        String ipAddress = String.valueOf(dp.getAddress());
        int port = dp.getPort();

        System.out.println("Server-> IP : " + ipAddress + " | Port : " + port + " | Information : " + str + "\n");
    }



}
