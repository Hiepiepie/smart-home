package ProviderServer;

import Sensoren.Thermometer.Thermometer;
import Zentral.JavaHTTPServer;
import Zentral.MockSocket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class ProviderServerTest {

  //Server ide
  private static DataSenderHandler handler;
  private static DataSender.Processor processor;
  private static ProviderServer providerServer;

  //Client side
  private static TTransport transport;
  private static TProtocol protocol;
  private static DataSender.Client client;

  private static ByteArrayOutputStream baos;
  private static PrintStream ps;
  private static PrintStream old;


  @BeforeClass
  public static void suiteSetup() throws IOException, InterruptedException, TTransportException {
    handler = new DataSenderHandler();
    processor = new DataSender.Processor(handler);


    Runnable startThrift = new Runnable() {
      public void run() {
        startThrift(processor);
      }
    };
    new Thread(startThrift).start();
    Thread.sleep(3000);

    transport = new TSocket("localhost", 9090);
    protocol = new TBinaryProtocol(transport);
    client = new DataSender.Client(protocol);

    baos = new ByteArrayOutputStream();
    ps = new PrintStream(baos);
    old = System.out;
    System.setOut(ps);

    transport.open();
  }

  @AfterClass
  public static void suiteTeardown() throws TTransportException, InterruptedException {
    System.out.flush();
    System.setOut(old);
    transport.close();
    Thread.sleep(10000);
  }

  @Test
  public void connectTest() throws TException {
    client.ping();
    assertEquals("PING!\n", baos.toString());
  }

  @Test
  public void requestDataTest() throws TException {
    String sentData = "Test Data";
    handler.setCurrentData(sentData,"Temperatur");
    String receivedData =  client.getSensorData("Thermometer");
    assertEquals(sentData,receivedData);
    handler.setCurrentData(sentData,"Humidity");
    receivedData =  client.getSensorData("Hygrometer");
    assertEquals(sentData,receivedData);
    handler.setCurrentData(sentData,"Brightness");
    receivedData =  client.getSensorData("Light");
    assertEquals(sentData,receivedData);
  }

  @Test
  public void connectionTest() throws InterruptedException, TException, IOException {
    for (int i = 0; i < 10 ; i++){
      client.ping();
    }
    String string = baos.toString();
    int occurences = ( string.split("PING!\n", -1).length ) - 1;
    assertEquals(10, occurences);
  }

  @Test
  public void falseRequestTest() throws TException {
    String data = client.getSensorData("Sensor A");
    assertEquals("Unknown Request", data);
  }

  @Test
  public void noSensorDataRequestTest() throws TException {
    String sentData = "Test Data";
    handler.setCurrentData(sentData,"Temperatur");
    String receivedData =  client.getSensorData("Thermometer");
    receivedData =  client.getSensorData("Thermometer");
    assertEquals("no data", receivedData);
  }

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
}
