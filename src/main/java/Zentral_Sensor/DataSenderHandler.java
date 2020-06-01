package Zentral_Sensor;

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
    switch (sensorType){
      case "Thermometer":
        return ThermosensorData;
      case "Hygrometer":
        return HygrosensorData;
      case "Light":
        return LightensorData;
      default:
        return null;
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
