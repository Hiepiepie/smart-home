package Zentral;


import Sensoren.SensorData;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import ThriftAPI.DataSender;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.Gson;


public class Zentral implements Runnable, MqttCallback {


    String separator = File.separator;
    private InetAddress ia;
    private final DatagramSocket ds;
    protected DatagramPacket dp;
    final int port = 1234;
    private byte[] buf = new byte[1024];
    private HashMap<String, Integer> checkBuffer;
    protected String msg;
    protected String[] msgArray;
    protected String iaClient;
    protected String typeClient;
    protected int portClient;
    protected int idClient;
    protected String infoClient;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    protected File oFile;
    protected SensorData sensorData;


    protected MqttClient mqttClient;
    private final int qos = 1;
    //protected String clientId;


    //private final String RESOURCES_FOLDER = "src" + separator + "main" + separator + "resources" + separator + "CentralDatas";
    private final String RESOURCES_FOLDER = "classes" + separator + "CentralDatas";

    public static DataSenderHandler handler;
    public static DataSender.Processor processor;


    //--------------------------------------------------------------------- main
    public static void main(String[] args) throws Exception {

        int PORT = 8080;
        try {
            Thread.sleep(5000);
            handler = new DataSenderHandler();
            processor = new DataSender.Processor(handler);

            Runnable startThrift = new Runnable() {
                public void run() {
                    startThrift(processor);
                }
            };
            new Thread(startThrift).start();

            Zentral zentral = new Zentral();
            new Thread(zentral).start();

             //we listen until user halts server execution
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
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

    public Zentral() throws IOException, MqttException {
        ia = InetAddress.getLocalHost();
        this.ds = new DatagramSocket(port);
        checkBuffer = new HashMap<>();
        mqttClient = new MqttClient("tcp://172.20.1.1:1883", MqttClient.generateClientId());
    }

    public void receivePackage () throws IOException {
        byte[] buf = new byte[1024];
        dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        msg = new String(dp.getData(), 0, dp.getLength());
    }

    protected void parseData(String sensorDataJson) {
        Gson gson = new Gson();
        sensorData = gson.fromJson(sensorDataJson, SensorData.class);
    }

    private void saveData() throws IOException {
        separator = System.getProperty("file.separator");
        String pathString = RESOURCES_FOLDER + separator + sensorData.getName() + separator + "log.html";
        Path path = Paths.get(pathString);
        try {
            if (Files.notExists(path)) {
                oFile = new File(pathString);
                oFile.createNewFile();
                appendToFile(pathString);
            } else {
                appendToFile(pathString);
            }
        } catch ( Exception e){
            e.printStackTrace();
        }
    }

    private void appendToFile(String pathString) {
        Date date = new Date();
        try(FileWriter fw = new FileWriter(pathString, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println("<br>********************************************<br>"
                + "<a href=\"http://localhost:8080/" + sensorData.getName().toLowerCase() + "/id=" + sensorData.getId() + "\"> "
                + date + " </a><br>" + "<p style=\"color: orangered\">"+ sensorData.getUnit() + " " + sensorData.getData() + " | ID : " + sensorData.getId() +"</p>" );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void packetCheck () {
        if (checkBuffer.get(sensorData.getName()) == null) {
            checkBuffer.put(sensorData.getName(), sensorData.getId());
            return;
        }
        if ((checkBuffer.get(sensorData.getName()) + 1) != sensorData.getId()) {
            System.out.println(ANSI_RED + "Packet loss detected from IP : " + sensorData.getIa() + " | Sensor name : " + sensorData.getName());
        }
        if (sensorData.getId() == 999) checkBuffer.put(sensorData.getName(), 0);
        else checkBuffer.put(sensorData.getName(), sensorData.getId());
    }

    public void clearLogFile() throws IOException {
        separator = System.getProperty("file.separator");
        String str = "<body style=\"background: antiquewhite; font-size: 15pt; text-align: center\">";

        String PATH = RESOURCES_FOLDER + separator + "Thermometer" + separator + "log.html";
        BufferedWriter writer = new BufferedWriter(new FileWriter(PATH));
        writer.write(str);
        writer.close();

        PATH = RESOURCES_FOLDER + separator + "Hygrometer" + separator + "log.html";
        writer = new BufferedWriter(new FileWriter(PATH));
        writer.write(str);
        writer.close();

        PATH = RESOURCES_FOLDER + separator + "Light" + separator + "log.html";
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

    //Mosquitto
    public void connectMqtt() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        mqttClient.setCallback(this);
        mqttClient.connect(options);
        mqttClient.subscribe("Sensor/#", qos);
        System.out.println("\nSubscribing to Broker on tcp://172.20.1.1:1883 ...\n");
    }

    //MQTT Callback methods
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Central Server -> Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws IOException {
        String sensorDataJson = new String(mqttMessage.getPayload());
        parseData(sensorDataJson);
        System.out.println(ANSI_RESET + sensorData.toString());
        packetCheck();
        saveData();
        handler.addNewData(sensorData);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Override
    public void run() {
        try {
            this.clearLogFile();
            connectMqtt();
        } catch (MqttException | IOException e) {
            e.printStackTrace();
        }
//        while (true) {
//            try {
//                this.receivePackage();
//                this.extractPackage();
//                this.saveData();
//                handler.setCurrentData(msg, typeClient);
//                this.packetCheck();
//                this.printInformation();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


}




