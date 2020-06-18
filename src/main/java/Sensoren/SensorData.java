package Sensoren;

import java.net.InetAddress;

public class SensorData {
  private String unit;
  private InetAddress ia;
  private String data;
  private int id;
  private String name;

  public SensorData(){
  }

  public SensorData(int id, String name, String data, String unit, InetAddress ia){
    this.id = id;
    this.unit = unit;
    this.data = data;
    this.ia = ia;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getData() {
    return data;
  }

  public String getUnit() {
    return unit;
  }

  public String getName() {
    return name;
  }

  public InetAddress getIa() {
    return ia;
  }


  @Override
  public String toString() {
    return  "Client IP : " + ia + " | Sensor Name : "
        + name + " | Data : " + data + " " + unit + " | Data ID: " + id;
  }
}
