package Sensoren.Hygrometer;

import Sensoren.UDPClient;

public class Hygrometer extends UDPClient {

    public static void main(String[] args) throws Exception {
        System.out.println("Hygrometer started");
        Hygrometer h = new Hygrometer();
        while (true){
            //msg will be in Form like : (SensorData ID);(Sensor Type);(SensorData Information)
            // ex : 122;Humidity;50%
            String msg = h.getId() + ";" + h.getType()+ ";" + h.getInfoUpdate();
            h.sendPackage(msg);
//            Thread.sleep(1000);
        }
    }

    private int hum;

    public Hygrometer() throws Exception{
        hum = rand.nextInt(101);
    }

    public int getHum() {
        return hum;
    }

    public String getType(){
        return "Humidity";
    }

    public String getInfoUpdate() {
        hum += (rand.nextBoolean() ? 1 : -1);
        if(hum >= 100) hum -= 1;
        if(hum <= 0) hum += 1;
        return hum + "%";
    }
}
