package Sensoren;

import java.io.File;

public class mainClass {

    public static void main(String[] args) throws Exception {
        String separator = File.separator;
        separator = System.getProperty("file.separator");
        String PATH = "classes" ;
        ProcessBuilder central = new ProcessBuilder("java", "-jar", "CentralServer-jar-with-dependencies.jar");
        ProcessBuilder pbHygrometer = new ProcessBuilder("java", "-jar", "Hygrometer-jar-with-dependencies.jar");
        ProcessBuilder pbLight = new ProcessBuilder("java", "-jar",  "Light-jar-with-dependencies.jar");
        ProcessBuilder pbThermometer = new ProcessBuilder("java", "-jar", "Thermometer-jar-with-dependencies.jar");
        ProcessBuilder provider = new ProcessBuilder("java", "-jar", "ProviderServer-jar-with-dependencies.jar");

        Process pmain = central.inheritIO().start();
        Process pHygrometer = pbHygrometer.inheritIO().start();
        Process pLight = pbLight.inheritIO().start();
        Process pThermometer = pbThermometer.inheritIO().start();
        Process pProvider = provider.inheritIO().start();

        pHygrometer.waitFor();
        pLight.waitFor();
        pThermometer.waitFor();
        pmain.waitFor();
        pProvider.waitFor();

    }
}
