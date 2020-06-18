package Sensoren.Thermometer;

import Sensoren.UDPClient;

public class Thermometer extends UDPClient {

    private static final String SENSOR_NAME = "Thermometer";

    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        Thermometer h = new Thermometer();
        h.connectMqtt();
        System.out.println("Thermometer started");
        while (true){
            h.sendData(h.getDataUpdate(),h.getUnit(),SENSOR_NAME);
        }
    }

    private int temp;

    public Thermometer() throws Exception{
        temp = rand.nextInt(46) - 5;
    }

    public int getTemp(){
        return temp;
    }

    public String getUnit(){
        return "Temperatur";
    }

    public String getDataUpdate() {
        temp += (rand.nextBoolean() ? 1 : -1);
        if(temp >= 40) temp -= 1;
        if(temp <= -5) temp += 1;
        return temp + " Grad Celcius";
    }

}
