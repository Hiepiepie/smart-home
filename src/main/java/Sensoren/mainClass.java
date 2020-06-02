package Sensoren;

import java.io.File;

public class mainClass {

    public static void main(String[] args) throws Exception {
        String separator = File.separator;
        separator = System.getProperty("file.separator");
        String PATH = "classes" ;

        ProcessBuilder pbHygrometer = new ProcessBuilder("java", "-cp", PATH , "Sensoren.Hygrometer.Hygrometer");
        ProcessBuilder pbLight = new ProcessBuilder("java", "-cp", PATH , "Sensoren.Light.Light");
        ProcessBuilder pbThermometer = new ProcessBuilder("java", "-cp", PATH , "Sensoren.Thermometer.Thermometer");
        ProcessBuilder central = new ProcessBuilder("java", "-cp", PATH , "Sensoren.mainClass");

        Process pHygrometer = pbHygrometer.inheritIO().start();
        Process pLight = pbLight.inheritIO().start();
        Process pThermometer = pbThermometer.inheritIO().start();
        Process pmain = central.inheritIO().start();

        pHygrometer.waitFor();
        pLight.waitFor();
        pThermometer.waitFor();
        pmain.waitFor();

    }
}
