package ProviderServer;

import Zentral_Sensor.DataSender;
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
import org.apache.thrift.transport.TTransportException;

public class ProviderServer {

  public static void main(String[] args) {
    ProviderServer providerServer = new ProviderServer();
    try {
      TTransport transport;
      transport = new TSocket("localhost", 9090);
      try{
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        DataSender.Client client = new DataSender.Client(protocol);
        while (true){
          client.ping();
          String msg;
          msg = client.getSensorData("Thermometer");
          providerServer.saveData(msg);
          msg = client.getSensorData("Hygrometer");
          providerServer.saveData(msg);
          msg =  client.getSensorData("Light");
          providerServer.saveData(msg);
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

  private void saveData(String data){
    String[] msgArray = data.split(";");
    int idClient = Integer.parseInt(msgArray[0]);
    String typeClient = msgArray[1];
    String infoClient = msgArray[2];
    String separator = File.separator;
    String PATH = "src" + separator + "main" + separator + "resources" + separator + "ProviderServerDatas" + separator + typeClient + ".html";
    Path path = Paths.get(PATH);
    Date today = new Date();
    try {
      if (Files.notExists(path)) {
        File oFile = new File(PATH);
        oFile.createNewFile();
        appendToFile(today, typeClient, infoClient, idClient, PATH);
      } else {
        appendToFile(today, typeClient, infoClient, idClient, PATH);
      }
    } catch ( Exception e){
      e.printStackTrace();
    }

  }

  private void appendToFile(Date date, String type, String info, int idClient, String PATH) {
    String device;
    switch (type){
      case("Temperatur"):
        device = "thermometer";
        break;

      case("Brightness"):
        device = "light";
        break;

      case("Humidity"):
        device = "hygrometer";
        break;

      default: device = " ";
    }
    try(FileWriter fw = new FileWriter(PATH, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw)) {
      out.println("<br>********************************************<br>"
          + device + "/id=" + idClient + "\"> " + date + " </a><br>" + "<p style=\"color: orangered\">"+type + " " + info + " | ID : " + idClient +"</p>" );
    } catch (IOException e) {
      e.printStackTrace();
      //exception handling left as an exercise for the reader
    }
  }
}