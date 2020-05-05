package UDPClientServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public abstract class UDPClient {
    protected int port;
    protected InetAddress ia;
    protected DatagramPacket dp;
    protected DatagramSocket ds;
    protected Random rand;
    protected byte[] buffer;
    protected int id;

    public UDPClient() throws Exception{
     ia = InetAddress.getLocalHost();
     port = 1234;
     ds = new DatagramSocket();
     rand = new Random();
     id = 1;
     buffer = new byte[1024];
     dp = new DatagramPacket(buffer, buffer.length, ia, port);
    }

    public DatagramPacket getDp(){
        return dp;
    }
    public abstract String getType();
    public abstract String getInfoUpdate();

    public void sendPackage(String msg) throws Exception{
        //msg will be in Form like : (SensorData ID);(Sensor Type);(SensorData Information)
        // ex : 122;Humidity;50%
        id++;
        if (id >= 999) id = 1;
        buffer = (msg).getBytes();
        dp = new DatagramPacket(buffer, buffer.length, ia, port);
        ds.send(dp);
    }

    public int getId() {
        return id;
    }
}
