package Sensoren;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.Gson;


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
//        port = 1234;
//        ds = new DatagramSocket();
        rand = new Random();
        id = 1;
//        buffer = new byte[1024];
//        dp = new DatagramPacket(buffer, buffer.length, ia, port);

        //clientId = UUID.randomUUID().toString();
        mqttClient = new MqttClient("tcp://172.20.1.1:1883", MqttClient.generateClientId());
    }

    public DatagramPacket getDp(){
        return dp;
    }
    public abstract String getUnit();
    public abstract String getDataUpdate();

    public void sendData(String data, String unit, String sensorName) throws Exception{
        SensorData sensorData = new SensorData(id, sensorName, data, unit, ia);
        Gson gson = new Gson();
        String sensorDataJson = gson.toJson(sensorData);
        id++;
        if (id >= 999) id = 1;
//        buffer = (msg).getBytes();
//        dp = new DatagramPacket(buffer, buffer.length, ia, port);
        Thread.sleep(1000);
//        ds.send(dp);

        MqttMessage mqttMessage = new MqttMessage(sensorDataJson.getBytes());
        mqttClient.publish("Sensor/" + sensorName ,mqttMessage);
    }

    public int getId() {
        return id;
    }

    public void connectMqtt() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        mqttClient.connect(options);
    }
}
