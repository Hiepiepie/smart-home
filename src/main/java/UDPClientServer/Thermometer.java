package UDPClientServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Thermometer {

    public static void main(String[] args) throws Exception {
        Thermometer h = new Thermometer();
        while (true){
            h.sendPackage();
        }
    }

    private int temp;
    private final Random rand;
    protected int port = 1234;
    protected InetAddress ia;
    protected DatagramSocket ds;
    protected DatagramPacket dp;

    public Thermometer() throws Exception{
        ia = InetAddress.getLocalHost();
        ds = new DatagramSocket();
        rand = new Random();
        temp = rand.nextInt(46) - 5;
    }

    public int getTemp(){
        return temp;
    }

    public String getType(){
        return "Temperatur ";
    }

    public String update() {
        temp += (rand.nextBoolean() ? 1 : -1);
        if(temp >= 40) temp -= 1;
        if(temp <= -5) temp += 1;
        return temp + " °Celcius";
    }
    public void sendPackage() throws Exception{
        String msg = getType() + update();
        byte[] b = (msg).getBytes();
        dp = new DatagramPacket(b, b.length, ia, port);
        ds.send(dp);
    }
}
