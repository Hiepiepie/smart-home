package Sensoren.Thermometer;

import Sensoren.UDPClient;

public class Thermometer extends UDPClient {

    public static void main(String[] args) throws Exception {
        System.out.println("Thermometer started");
        Thermometer h = new Thermometer();
        while (true){
            //msg will be in Form like : (SensorData ID);(Sensor Type);(SensorData Information)
            // ex : 122;Humidity;50%
            String msg = h.getId() + ";" + h.getType()+ ";" + h.getInfoUpdate();
            h.sendPackage(msg);

        }
    }

    private int temp;

    public Thermometer() throws Exception{
        temp = rand.nextInt(46) - 5;
    }

    public int getTemp(){
        return temp;
    }

    public String getType(){
        return "Temperatur";
    }

    public String getInfoUpdate() {
        temp += (rand.nextBoolean() ? 1 : -1);
        if(temp >= 40) temp -= 1;
        if(temp <= -5) temp += 1;
        return temp + " Grad Celcius";
    }

}
