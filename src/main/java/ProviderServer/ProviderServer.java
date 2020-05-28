package ProviderServer;

import Zentral_Sensor.DataSender;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class ProviderServer {

  public static void main(String[] args) {

    try {
      TTransport transport;

      transport = new TSocket("localhost", 9090);
      transport.open();

      TProtocol protocol = new TBinaryProtocol(transport);
      DataSender.Client client = new DataSender.Client(protocol);

      client.ping();
      System.out.println("ping()");

      transport.close();
    } catch (TTransportException e) {
      e.printStackTrace();
    } catch (TException x) {
      x.printStackTrace();
    }
  }

}