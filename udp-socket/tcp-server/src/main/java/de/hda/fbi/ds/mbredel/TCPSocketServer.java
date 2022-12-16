package de.hda.fbi.ds.mbredel;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class TCPSocketServer {

    private static int PORT = 3142;
    private ServerSocket tcpServerSocket;
    private boolean running = true;
    private Socket connectionSocket;
    ArrayList<String> msg = new ArrayList<>();
    ArrayList<String> msgDatei = new ArrayList<>();

    private long startTime;
    private long endTime;

    public TCPSocketServer() throws IOException {
        tcpServerSocket = new ServerSocket(PORT);
        System.out.println("Started the TCP socket server at port " + PORT);
        System.out.println("TCP Server running...");
    }
    public void run() {
        while (running) {
            connectionSocket = null;
            try {
                // Open a connection socket, once a client connects to the server socket.
                connectionSocket = tcpServerSocket.accept();
                startTime = System.currentTimeMillis();
                // Get the continuous input stream from the connection socket.
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                // Case distinction HTTP-POST or HTTP-GET
                handlePostOrGetMatrix(inFromClient);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (connectionSocket != null) {
                    try {
                        connectionSocket.close();
                        endTime = System.currentTimeMillis();
                        float time = (endTime - startTime) / 1000.0f;
                        System.out.println("Connection socket closed." + time + "ms");
                    } catch (IOException e) {
                        // Do nothing.
                    }
                }
            }
        }
    }
   
    public void handlePostOrGetMatrix(BufferedReader rd) throws IOException, SQLException {
        String line = rd.readLine();
        BufferedWriter wr;

        String convertedLineToSplit = line.replaceAll("[^a-zA-Z0-9]", "");

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < convertedLineToSplit.length(); i++) {
            if (i == 4) {
                result.append(" ");
            }

            result.append(convertedLineToSplit.charAt(i));
        }
        String res = String.valueOf(result);
        System.out.println(res);

        String[] toSplit = res.split(" ");
        String postOrGet = toSplit[0];
        String value = toSplit[1];

        System.out.println(postOrGet);
        System.out.println(value);

        if(postOrGet.equals("POST")){
            msg.add(value);

            wr = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            wr.write("POST HTTP/1.1 200 OK\r\n");
            wr.write(value + "\n");
            wr.flush();

            Database db = new Database();
            db.connection();
            db.insertDataResult(Integer.parseInt(value));
        }
        if (postOrGet.equals("\u0000G\u0000E\u0000T\u0000")|| postOrGet.equals("GET")) {
            try (OutputStream clientOutput = connectionSocket.getOutputStream()) {
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write(("" + generateHtmlData() + "\r\n").getBytes());
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();

               /* Database db = new Database();
                db.connection();
                db.DataAusgabe();*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private String generateHtmlData(){
        String data = "<html>"
                + "<head>" +
                "<title>Praktikum 2</title> " +
                "<style> " +
                "table {" +
                "font-family: arial, sans-serif;" +
                "border-collapse: collapse;" +
                "width: 100%;" +
                "}" +
                "td, th {" +
                "border: 1px solid #dddddd;" +
                "text-align: left;" +
                "padding: 8px;" +
                "}" +
                "tr:nth-child(even) {" +
                "background-color: #dddddd;" +
                "}" +
                "h2 {" +
                "text-align: center;" +
                "}" +
                "</style>" +
                "</head>"
                + "<body>";
        data += "<h2>Praktikum 2</h2>";
        data += "<table>";
        data += "<tr>";
        data += "<th>Data:</th>";
        data += "</tr>";
        for (int i = 0; i < msg.size(); i++) {
            data += "<td> " + msg.get(i) + '\n' +" </td>";
        }
        data += "</table>";
        data += "</body>" + "</html>";
        //msg.remove(0);
        return data;
    }

    private void infoSpeichern() {
        //todo: RTT Ergebnisse in einer Datei speichern
        String fileName = "EndMatrix.txt";
        String encoding = "UTF-8";
        try {
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println("\t\t\t Matrix-End-Ergebnis");
            int counter = 1;
            while (counter < 50) {

                for (Object s : msgDatei) {

                    writer.print("Zelle Nummer" + counter + ": ");
                    counter++;
                    writer.printf(s.toString() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}