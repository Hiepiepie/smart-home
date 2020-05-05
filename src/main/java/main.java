import UDPClientServer.Hygrometer;
import UDPClientServer.Light;
import UDPClientServer.Thermometer;
import UDPClientServer.UDPServer;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {
        String separator = File.separator;
        separator = System.getProperty("file.separator");
//        String PATH = "D:\\Studium\\VS\\vs-praktikum-ss20-fehr-mo4x-adithanakevin-letrunghieu\\build\\classes\\java\\main";
        String PATH = "build" + separator+"classes" + separator+"java"+separator+"main";
        ProcessBuilder pb1 = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Hygrometer");
        ProcessBuilder pb2 = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Light");
        ProcessBuilder pb3 = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.Thermometer");
        ProcessBuilder pb4 = new ProcessBuilder("java", "-cp", PATH , "UDPClientServer.UDPServer");

        pb1.inheritIO();
        pb2.inheritIO();
        pb3.inheritIO();
        pb4.inheritIO();

        Process p4 = pb4.start();
        Process p1 = pb1.start();
        Process p2 = pb2.start();
        Process p3 = pb3.start();





    }


    }



