package ProviderServer;

import Sensoren.SensorData;
import ThriftAPI.DataSender;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import com.google.gson.Gson;

public class ProviderServer {

  static String separator = File.separator;
  static final String RESOURCE_FOLDER = "classes" + separator + "ProviderServerDatas";

  public static void main(String[] args) {
    ProviderServer providerServer = new ProviderServer();
    try {
      TTransport transport;
      transport = new TSocket("172.20.1.8", 9090);
      try{
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        DataSender.Client client = new DataSender.Client(protocol);
        while (true){
          String sensorDataJson;
          sensorDataJson = client.getSensorData("Thermometer");
          System.out.println("Provider Server : Data Received => " + sensorDataJson);
          if(!sensorDataJson.contains("no data"))
            providerServer.saveData(sensorDataJson);

          sensorDataJson = client.getSensorData("Hygrometer");
          System.out.println("Provider Server : Data Received => " + sensorDataJson);
          if(!sensorDataJson.contains("no data"))
            providerServer.saveData(sensorDataJson);

          sensorDataJson =  client.getSensorData("Light");
          System.out.println("Provider Server : Data Received => " + sensorDataJson);
          if(!sensorDataJson.contains("no data"))
            providerServer.saveData(sensorDataJson);

          Thread.sleep(1000);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        transport.close();
      }
    } catch (TException e) {
      e.printStackTrace();
    }
  }

  private void saveData(String sensorDataJson){
    Gson gson = new Gson();
    SensorData sensorData = gson.fromJson(sensorDataJson, SensorData.class);
    String separator = File.separator;
    String PATH = RESOURCE_FOLDER + separator + sensorData.getName() + ".html";
    Path path = Paths.get(PATH);
    Date today = new Date();
    try {
      if (Files.notExists(path)) {
        File oFile = new File(PATH);
        oFile.createNewFile();
        appendToFile(today,sensorData, PATH);
      } else {
        appendToFile(today, sensorData, PATH);
      }
    } catch ( Exception e){
      e.printStackTrace();
    }

  }

  private void appendToFile(Date date, SensorData sensorData, String PATH) {
    try(FileWriter fw = new FileWriter(PATH, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      out.println("<br>********************************************<br>"
          + date + " </a><br>" + "<p style=\"color: orangered\">"+ sensorData.getUnit() + " " + sensorData.getData() + " | ID : " + sensorData.getId() +"</p>" );
    } catch (IOException e) {
      e.printStackTrace();
      //exception handling left as an exercise for the reader
    }
  }
}