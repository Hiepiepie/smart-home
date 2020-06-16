package Sensoren;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public abstract class UDPClient {
    protected int port;
    protected InetAddress ia;
    protected DatagramPacket dp;
    protected DatagramSocket ds;
    protected Random rand;
    protected byte[] buffer;
    protected int id;
    protected MqttClient mqttClient;


    public UDPClient() throws Exception{
        ia = InetAddress.getLocalHost();
        port = 1234;
        ds = new DatagramSocket();
        rand = new Random();
        id = 1;
        buffer = new byte[1024];
        dp = new DatagramPacket(buffer, buffer.length, ia, port);

        mqttClient = new MqttClient();
    }

    public DatagramPacket getDp(){
        return dp;
    }
    public abstract String getType();
    public abstract String getInfoUpdate();

    public void sendPackage(String msg, String sensor) throws Exception{
        //msg will be in Form like : (SensorData ID);(Sensor Type);(SensorData Information)
        // ex : 122;Humidity;50%
        id++;
        if (id >= 999) id = 1;
        buffer = (msg).getBytes();
        dp = new DatagramPacket(buffer, buffer.length, ia, port);
        Thread.sleep(1000);
        ds.send(dp);

        MqttMessage mqttMessage = new MqttMessage(msg.getBytes());
        mqttClient.publish(sensor,mqttMessage);
    }

    public int getId() {
        return id;
    }
}
