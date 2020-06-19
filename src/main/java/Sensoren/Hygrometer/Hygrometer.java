package Sensoren.Hygrometer;

import Sensoren.Sensor;

public class Hygrometer extends Sensor {

    private static final String SENSOR_NAME = "Hygrometer";

    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        Hygrometer h = new Hygrometer();
        h.connectMqtt();
        System.out.println("Hygrometer started");
        while (true){
            h.sendData(h.getDataUpdate(),h.getUnit(),SENSOR_NAME);
        }
    }

    private int hum;

    public Hygrometer() throws Exception{
        hum = rand.nextInt(101);
    }

    public String getUnit(){
        return "Humidity";
    }

    public String getDataUpdate() {
        hum += (rand.nextBoolean() ? 1 : -1);
        if(hum >= 100) hum -= 1;
        if(hum <= 0) hum += 1;
        return hum + "%";
    }
}
