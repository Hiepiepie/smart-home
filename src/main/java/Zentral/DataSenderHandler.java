package Zentral;

import Sensoren.Light.Light;
import Sensoren.SensorData;
import ThriftAPI.DataSender.Iface;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.thrift.TException;
import com.google.gson.Gson;

public class DataSenderHandler implements Iface {

  Queue<SensorData> hygrometerDatas;
  Queue<SensorData> thermometerDatas;
  Queue<SensorData> lightDatas;

  public DataSenderHandler(){
    hygrometerDatas = new LinkedList<>();
    thermometerDatas = new LinkedList<>();
    lightDatas = new LinkedList<>();
  }


  @Override
  public void ping() throws TException {
    System.out.println("PING!");
  }

  @Override
  public String getSensorData(String sensorType) throws TException {
    String sentData;
    Gson gson = new Gson();
    String sensorDataJson;
    switch (sensorType){
      case "Thermometer":
       if(thermometerDatas.peek() == null) {
         return "no data";
       } else {
         sensorDataJson = gson.toJson(thermometerDatas.remove());
         return sensorDataJson;
       }
      case "Hygrometer":
        if(hygrometerDatas.peek() == null) {
          return "no data";
        } else {
          sensorDataJson = gson.toJson(hygrometerDatas.remove());
          return sensorDataJson;
        }
      case "Light":
        if(lightDatas.peek() == null) {
          return "no data";
        } else {
          sensorDataJson = gson.toJson(lightDatas.remove());
          return sensorDataJson;
        }
      default:
        return "Unknown Request";
    }
  }

  public void addNewData(SensorData sensorData){
    switch (sensorData.getName()){
      case "Thermometer":
        this.thermometerDatas.add(sensorData);
        break;
      case "Hygrometer":
        this.hygrometerDatas.add(sensorData);
        break;
      case "Light":
        this.lightDatas.add(sensorData);
        break;
      default:
        break;
    }
  }
}
