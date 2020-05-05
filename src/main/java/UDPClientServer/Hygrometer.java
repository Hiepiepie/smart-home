package UDPClientServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Hygrometer{

    public static void main(String[] args) throws Exception {
        Hygrometer h = new Hygrometer();
        while (true){
            h.sendPackage();
        }
    }

    private int hum;
    private final Random rand;
    protected int port = 1234;
    protected InetAddress ia;
    protected DatagramSocket ds;
    protected DatagramPacket dp;

    public Hygrometer() throws Exception{
        ia = InetAddress.getLocalHost();
        ds = new DatagramSocket();
        rand = new Random();
        hum = rand.nextInt(101);
    }

    public int getHum() {
        return hum;
    }

    public String getType(){
        return "Humidity ";
    }

    public String update() {
        hum += (rand.nextBoolean() ? 1 : -1);
        if(hum >= 100) hum -= 1;
        if(hum <= 0) hum += 1;
        return hum + "%";
    }
    public void sendPackage() throws Exception{
        String msg = getType() + update();
        byte[] b = (msg).getBytes();
        dp = new DatagramPacket(b, b.length, ia, port);
        ds.send(dp);
    }
}
