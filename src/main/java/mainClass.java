import UDPClientServer.Hygrometer;
import UDPClientServer.Light;
import UDPClientServer.Thermometer;
import UDPClientServer.UDPServer;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class mainClass {

    public static void main(String[] args) throws Exception {
        String separator = File.separator;
        separator = System.getProperty("file.separator");
//        String PATH = "D:\\Studium\\VS\\vs-praktikum-ss20-fehr-mo4x-adithanakevin-letrunghieu\\build\\classes\\java\\main";
        String PATH = "build" + separator+"classes" + separator+"java"+separator+"main";
        ProcessBuilder pbHygrometer = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Hygrometer");
        ProcessBuilder pbLight = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Light");
        ProcessBuilder pbThermometer = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Thermometer");
        ProcessBuilder pbUDPServer = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.UDPServer");

        pbHygrometer.inheritIO();
        pbLight.inheritIO();
        pbThermometer.inheritIO();
        pbUDPServer.inheritIO();

        Process pHygrometer = pbHygrometer.start();
        Process pLight = pbLight.start();
        Process pThermometer = pbThermometer.start();
        Process pUDPServer = pbUDPServer.start();

        Scanner scanner = new Scanner(System.in);

        String stop = "";
        while(!(stop.equals("x"))) {
            stop = scanner.next();
        }

        pHygrometer.destroy();
        pLight.destroy();
        pThermometer.destroy();
        pUDPServer.destroy();
        System.out.println("All Devices and Server are shutted down");
    }
}



