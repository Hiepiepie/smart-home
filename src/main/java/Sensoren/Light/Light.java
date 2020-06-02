package Sensoren.Light;

import Sensoren.UDPClient;

public class Light extends UDPClient {

    public static void main(String[] args) throws Exception {
        System.out.println("Light started");
        Light h = new Light();
        while (true){
            //msg will be in Form like : (SensorData ID);(Sensor Type);(SensorData Information)
            // ex : 122;Humidity;50%
            String msg = h.getId() + ";" + h.getType()+ ";" + h.getInfoUpdate();
            h.sendPackage(msg);
//            Thread.sleep(1000);
        }
    }

    private int bright;

    public Light() throws Exception{
        bright = rand.nextInt(101);
    }

    public int getBright() {
        return bright;
    }

    public String getType(){
        return "Brightness";
    }

    public String getInfoUpdate() {
        bright += (rand.nextBoolean() ? 1 : -1);
        if(bright >= 100) bright -= 1;
        if(bright <= 0) bright += 1;
        return bright + "%";
    }

}