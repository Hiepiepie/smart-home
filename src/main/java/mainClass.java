import java.io.File;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws Exception {
        String separator = File.separator;
        separator = System.getProperty("file.separator");
        String PATH = "target" + separator+"classes" ;

        ProcessBuilder pbHygrometer = new ProcessBuilder("java", "-cp", PATH , "Zentral_Sensor.Hygrometer");
        ProcessBuilder pbLight = new ProcessBuilder("java", "-cp", PATH , "Zentral_Sensor.Light");
        ProcessBuilder pbThermometer = new ProcessBuilder("java", "-cp", PATH , "Zentral_Sensor.Thermometer");
        ProcessBuilder pbCentralServer = new ProcessBuilder("java", "-cp", PATH , "Zentral_Sensor.Zentral");
        ProcessBuilder pbProviderServer = new ProcessBuilder("java", "-cp", PATH, "ProviderServer.ProviderServer");

        //Process pHygrometer = pbHygrometer.inheritIO().start();
        //Process pLight = pbLight.inheritIO().start();
        //Process pThermometer = pbThermometer.inheritIO().start();
        Process pCentralServer = pbCentralServer.inheritIO().start();
        Process pProviderServer = pbProviderServer.inheritIO().start();

        pCentralServer.waitFor();
        //pHygrometer.waitFor();
        //pLight.waitFor();
        //pThermometer.waitFor();
        pProviderServer.waitFor();
    }
}



