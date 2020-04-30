import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Random;
import org.junit.runner.JUnitCore;



abstract public class UDPClient extends Thread{

    public static void main(String[] args){
        JUnitCore.runClasses(TestPrimer.class);
        Thermometer thermometer = new Thermometer();
        Light light = new Light();
        Hygrometer hygrometer = new Hygrometer();
        thermometer.start();
        light.start();
        hygrometer.start();
    }

    UDPClient(){
    }

    public void run() {
        try {
            DatagramSocket ds = new DatagramSocket();
            InetAddress ia = InetAddress.getLocalHost();
            while (true){
                String msg = new String(getType() + update());
                byte[] b = (msg).getBytes();
                //byte[] inData = new byte[1024];
                DatagramPacket dp = new DatagramPacket(b, b.length, ia, 1234);
                ds.send(dp);
                Thread.sleep(1000);

//                ds.setSoTimeout(1000); //timeout
//                //wait for data from Server
//
//                DatagramPacket recievePkt = new DatagramPacket(inData, inData.length);
//
////                System.out.println("ready receive message from server)");                         //Optional
//
//                ds.receive(recievePkt);
//
////                System.out.println("receive messag");                                             //Optional
////                System.out.println("Replay from Server: " + new String(recievePkt.getData()));    //Optional
            }
        } catch( Exception e){
            e.printStackTrace();
        }
    }

    public abstract String getType();
    public abstract String update();
}

class Thermometer extends UDPClient{
    private Random rand = new Random();
    private int temp = rand.nextInt(46) - 5;
    public String getType(){
        return "Temperatur ";
    }

    public String update() {
        temp += (rand.nextBoolean() ? 1 : -1);
        if(temp >= 40) temp -= 1;
        if(temp <= -5) temp += 1;
        return temp + " °Celcius";
    }
}

class Light extends UDPClient{
    private Random rand = new Random();
    private int bright = rand.nextInt(101);
    public String getType(){
        return "Brightness ";
    }

    public String update() {
        bright += (rand.nextBoolean() ? 1 : -1);
        if(bright >= 100) bright -= 1;
        if(bright <= 0) bright += 1;
        return bright + "%";
    }
}

class Hygrometer extends UDPClient{
    private Random rand = new Random();
    private int hum = rand.nextInt(101);
    public String getType(){
        return "Humidity ";
    }

    public String update() {
        hum += (rand.nextBoolean() ? 1 : -1);
        if(hum >= 100) hum -= 1;
        if(hum <= 0) hum += 1;
        return hum + "%";
    }
}
