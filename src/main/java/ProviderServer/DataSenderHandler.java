package ProviderServer;

import org.apache.thrift.TException;

public class DataSenderHandler implements DataSender.Iface {

  String HygrosensorData = "no data";
  String ThermosensorData = "no data";
  String LightensorData = "no data";

  @Override
  public void ping() throws TException {
    System.out.println("PING!");
  }

  @Override
  public String getSensorData(String sensorType) throws TException {
    String sentData;
    switch (sensorType){
      case "Thermometer":
        sentData = ThermosensorData;
        ThermosensorData = "no data";
        return sentData;
      case "Hygrometer":
        sentData = HygrosensorData;
        HygrosensorData = "no data";
        return sentData;
      case "Light":
        sentData = LightensorData;
        LightensorData = "no data";
        return sentData;
      default:
        return "Unknown Request";
    }
  }

  public void setCurrentData(String sensorData, String typeClient){
    switch (typeClient){
      case "Temperatur":
        this.ThermosensorData = sensorData;
        break;
      case "Humidity":
        this.HygrosensorData = sensorData;
        break;
      case "Brightness":
        this.LightensorData = sensorData;
        break;
      default:
        break;
    }
  }
}
