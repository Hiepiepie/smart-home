package Zentral;

import java.io.*;
import java.lang.invoke.SwitchPoint;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


// Each Client Connection will be managed in a dedicated Thread
public class JavaHTTPServer implements Runnable{

    static String separator = File.separator;
    static final File WEB_ROOT = new File(".");
    static final String DEFAULT_FILE = "index.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String METHOD_NOT_SUPPORTED = "not_supported.html";

    //static final String RESOURCE_FOLDER = "src" + separator + "main" + separator + "resources" + separator + "CentralDatas";
    //static final String HTML_FOLDER = "src" + separator + "main" + separator + "resources" + separator + "HTMLPage" + separator;

    static final String RESOURCE_FOLDER = "classes" + separator + "CentralDatas";
    static final String HTML_FOLDER = "classes" + separator + "HTMLPage";

    // port to listen connection
    static final int PORT = 8080;

    // verbose mode
    static final boolean verbose = true;

    // Client Connection via Socket Class
    private Socket clientSocket;


    public JavaHTTPServer(Socket c) {
        clientSocket = c;
    }

//    public static void main(String[] args) {
//        try {
//            ServerSocket serverConnect = new ServerSocket(PORT);
//            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
//
//            // we listen until user halts server execution
//            while (true) {
//                JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
//
//                if (verbose) {
//                    System.out.println("Connection opened. (" + new Date() + ")");
//                }
//
//                // create dedicated thread to manage the client connection
//                Thread thread = new Thread(myServer);
//                thread.start();
//            }
//
//        } catch (IOException e) {
//            System.err.println("Server Connection error : " + e.getMessage());
//        }
//    }

    @Override
    public void run() {
        // we manage our particular client connection
        BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
        String fileRequested = null;

        try {
            // we read characters from the client via input stream on the socket
            in = getRequestFrom(clientSocket);
            // we get character output stream to client (for headers)
            out = getPrinterWriterOut(clientSocket);
            // get binary output stream to client (for requested data)
            dataOut = new BufferedOutputStream(clientSocket.getOutputStream());

            // get first line of the request from the client
            String input = in.readLine();

            //check valid HTTP Request
            if(!checkValidRequest(in)){
                throw new FileNotFoundException();
            }

            // we parse the request with a string tokenizer
            StringTokenizer parse = new StringTokenizer(input);
            String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
            // we get file requested
            fileRequested = parse.nextToken().toLowerCase();

            // we support only GET and HEAD methods, we check
            if (!method.equals("GET")  &&  !method.equals("HEAD")) {
                handleNotSupported(out, dataOut, method);

            } else {
                // GET or HEAD method
                if (fileRequested.endsWith("/")) {
                    fileRequested += DEFAULT_FILE;
                }

                String content = handleGetHead(out, dataOut, fileRequested, method);

                if (verbose) {
                    System.out.println("File " + fileRequested + " of type " + content + " returned");
                }

            }

        } catch (FileNotFoundException fnfe) {
            try {
                fileNotFound(out, dataOut, fileRequested);
            } catch (IOException ioe) {
                System.err.println("Error with file not found exception : " + ioe.getMessage());
            }

        } catch (IOException ioe) {
            System.err.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                clientSocket.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }


    }

    private String handleGetHead(PrintWriter out, BufferedOutputStream dataOut, String fileRequested, String method) throws IOException {
        String content;
        String dataRequested = "0";
        System.out.println(fileRequested);
        if(fileRequested.contains("/id")){
            dataRequested = fileRequested.substring(fileRequested.indexOf("=")+1);
            fileRequested = fileRequested.substring(0,fileRequested.indexOf("/id"));
        }
        System.out.println(dataRequested);
        switch(fileRequested) {
            case "/thermometer":
            {
                File parent;
                File file;
                if(searchSensorData(RESOURCE_FOLDER + separator + "Temperatur/log.html", dataRequested)){
                    System.out.println("hey");
                    parent = new File(HTML_FOLDER);
                    file = new File(parent, "data.html");
                } else {
                    parent = new File(RESOURCE_FOLDER + separator + "Temperatur");
                    file = new File(parent, "log.html");
                }
                int fileLength = (int) file.length();
                content = getContentType(fileRequested+".html");

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);
                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    outHeader(out, dataOut, fileLength, content, fileData);
                }
                break;
            }
            case "/hygrometer":
            {
                File parent;
                File file;
                if(searchSensorData(RESOURCE_FOLDER + separator + "Humidity/log.html", dataRequested)){
                    parent = new File(HTML_FOLDER);
                    file = new File(parent, "data.html");

                } else{
                    parent = new File(RESOURCE_FOLDER + separator + "Humidity");
                    file = new File(parent, "log.html");
                }
                int fileLength = (int) file.length();
                content = getContentType(fileRequested+".html");

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);
                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    outHeader(out, dataOut, fileLength, content, fileData);
                }
                break;
            }
            case "/light":
            {
                File parent;
                File file;
                if(searchSensorData(RESOURCE_FOLDER + separator + "Brightness/log.html", dataRequested)){
                    parent = new File(HTML_FOLDER);
                    file = new File(parent, "data.html");
                } else {
                    parent = new File(RESOURCE_FOLDER + separator + "Brightness");
                    file = new File(parent, "log.html");
                }
                int fileLength = (int) file.length();
                content = getContentType(fileRequested+".html");

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);
                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    outHeader(out, dataOut, fileLength, content, fileData);
                }
                break;
            }
            default:{
                File file = new File(WEB_ROOT, HTML_FOLDER + fileRequested);
                int fileLength = (int) file.length();
                content = getContentType(fileRequested);

                if (method.equals("GET")) { // GET method so we return content
                    byte[] fileData = readFileData(file, fileLength);
                    // send HTTP Headers
                    out.println("HTTP/1.1 200 OK");
                    outHeader(out, dataOut, fileLength, content, fileData);
                }
                break;
            }
        }
//        File file = new File(WEB_ROOT, fileRequested);
//        int fileLength = (int) file.length();
//        String content = getContentType(fileRequested);
//
//        if (method.equals("GET")) { // GET method so we return content
//            byte[] fileData = readFileData(file, fileLength);
//            // send HTTP Headers
//            out.println("HTTP/1.1 200 OK");
//            outHeader(out, dataOut, fileLength, content, fileData);
//        }

        return content;
    }

    private void handleNotSupported(PrintWriter out, BufferedOutputStream dataOut, String method) throws IOException {
        if (verbose) {
            System.out.println("501 Not Implemented : " + method + " method.");
        }

        // we return the not supported file to the client
        File file = new File(WEB_ROOT, HTML_FOLDER + separator+ METHOD_NOT_SUPPORTED);
        int fileLength = (int) file.length();
        String contentMimeType = "text/html";
        //read content to return to client
        byte[] fileData = readFileData(file, fileLength);

        // we send HTTP Headers with data to client
        out.println("HTTP/1.1 501 Not Implemented");
        outHeader(out, dataOut, fileLength, contentMimeType, fileData);
    }

    private byte[] readFileData(File file, int fileLength) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null)
                fileIn.close();
        }

        return fileData;
    }

    // return supported MIME Types
    private String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
            return "text/html";
        else
            return "text/plain";
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        File file = new File(WEB_ROOT, HTML_FOLDER + separator + FILE_NOT_FOUND);
        int fileLength = (int) file.length();
        String content = "text/html";
        byte[] fileData = readFileData(file, fileLength);

        out.println("HTTP/1.1 404 File Not Found");
        outHeader(out, dataOut, fileLength, content, fileData);

        if (verbose) {
            System.out.println("File " + fileRequested + " not found");
        }
    }

    protected void outHeader(PrintWriter out, OutputStream dataOut, int fileLength, String content, byte[] fileData) throws IOException {
        out.println("Server: Java HTTP Server : 1.1");
        out.println("Date: " + new Date());
        out.println("Content-type: " + content);
        out.println("Content-length: " + fileLength);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
    }

    private boolean searchSensorData(String path, String dataRequested) throws IOException{
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<>();
        String line;

        while ((line = bufferedReader.readLine()) != null)
        {
            lines.add(line);
        }

        bufferedReader.close();

        for (String data: lines) {

            if (data.contains("| ID : " + dataRequested)){
                String str = "<body style=\"background: antiquewhite; font-size: 15pt; text-align: center\">"+data;
                BufferedWriter writer = new BufferedWriter(new FileWriter(HTML_FOLDER + separator + "data.html"));
                writer.write(str);
                writer.close();
                System.out.println("Sensordata found");
                return true;
            }
        }
        System.out.println("Sensordata not found");
        return false;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public BufferedReader getRequestFrom(Socket socket) throws IOException{
        return new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
    }

    protected PrintWriter getPrinterWriterOut(Socket socket) throws IOException{
        return new PrintWriter(socket.getOutputStream());
    }

    public void sendResponse(Socket socket, String responseBody) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.print(responseBody);
        out.flush();
    }

    private boolean checkValidRequest(BufferedReader in) throws IOException {
        StringBuilder lines = new StringBuilder();
        int line;
        while ((line = in.read()) != -1)
        {
            lines.append((char)line);
            //System.out.print((char)line);
            if (lines.toString().contains("\r\n\r\n")) return true;
        }
        return false;
    }
}