import java.io.File;
import java.net.InetAddress;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {
        int port = 1234;
        InetAddress ia = InetAddress.getLocalHost();
        ProcessBuilder pb = new ProcessBuilder();
        String fullClassName = Thermometer.class.getName();
        pb.command("java","-cp","./build/classes", fullClassName, "myArg");
        pb.start();
        System.out.println("All Devices and Server are shutted down");
    }
}
