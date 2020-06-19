package Sensoren.Light;

import Sensoren.Sensor;

public class Light extends Sensor {

    private static final String SENSOR_NAME = "Light";

    public static void main(String[] args) throws Exception {
        Thread.sleep(5000);
        Light h = new Light();
        h.connectMqtt();
        System.out.println("Light started");
        while (true){
            h.sendData(h.getDataUpdate(),h.getUnit(),SENSOR_NAME);
        }
    }

    private int bright;

    public Light() throws Exception{
        //bright = 0;
        bright = rand.nextInt(101);
    }

    public String getUnit(){
        return "Brightness";
    }

    public String getDataUpdate() {
        bright += (rand.nextBoolean() ? 1 : -1);
        if(bright >= 100) bright -= 1;
        if(bright <= 0) bright += 1;
        return bright + "%";
    }

}