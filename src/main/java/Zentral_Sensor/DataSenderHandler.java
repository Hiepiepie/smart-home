package Zentral_Sensor;

import Zentral_Sensor.DataSender.Iface;
import org.apache.thrift.TException;

public class DataSenderHandler implements Iface {


  @Override
  public void ping() throws TException {
      System.out.println("PING!");
  }

  @Override
  public String getSensorData(String sensorType) throws TException {
    return null;
  }
}
