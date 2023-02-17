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
    ArrayList<String> msgGet = new ArrayList<>();
    ArrayList<String> msgPost = new ArrayList<>();

//    Database db;


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
                // Get the continuous input stream from the connection socket.
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


                //Ack send füt RTT-TCP auf der andere Seite berechnen zu könenn
                OutputStream out = connectionSocket.getOutputStream();
                out.write("ACK".getBytes());
                out.flush();

                // Case distinction HTTP-POST or HTTP-GET
                handlePostOrGet(inFromClient);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            } finally {
                if (connectionSocket != null) {
                    try {
                        connectionSocket.close();
                        System.out.println("Connection socket closed");
                    } catch (IOException e) {
                        // Do nothing.s
                    }
                }
            }
        }
    }


        public void handlePostOrGet(BufferedReader rd) throws IOException, SQLException {

            BufferedWriter wr;
            String line = rd.readLine();


            String convertedLineToSplit = line.replaceAll("[^a-zA-Z0-9]", " ");
            System.out.println("Request information: " + convertedLineToSplit);

            String[] parts = convertedLineToSplit.split("   ");
            String postOrGet = parts[0];
            System.out.println("Request: " + postOrGet);


            String part22 = parts[1];
                msgGet.add(part22);



            if (postOrGet.equals(" P O S T")) {
                //String part2 = parts[1];
                //String[] againToSplit = part2.split(" ");
                //String[] againToSplit = part2.split(" ");
                String firstname = parts[1];
                System.out.println("Firstname: " + firstname);
                String lastname = parts[2];
                System.out.println("Lastname: " + lastname);
                String res = firstname + " " + lastname;

                msgPost.add(res);
                System.out.print("The data: ");
                System.out.println("LINE: " + res);
                System.out.println("---------------------");
                wr = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
                wr.write("POST HTTP/1.1 200 OK\r\n");
                wr.write(res + "\n");
                wr.flush();

                Database db = new Database();
              //  db.connection();
               // db.createTabelBeliebig(); // Im Docker mit Datnebank verbinden: nach jedem "docker compose down" (Somit wird alles gelöscht-> z.B. die Relationen, etc..)
                                         // soll die Tabelle auf IntelliJ also hier einmal erstellt, dann können wir auskomentieren, da Tebelle schon wxistieren wird.

               db.connection();
               db.insertDataBeliebig(firstname, lastname);

                db.connection();
                db.DatenAusgebenBeliebig();
            }
            else if (postOrGet.equals(" G E T") || postOrGet.equals("GET")) {
                try (OutputStream clientOutput = connectionSocket.getOutputStream()) {
                    clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                    clientOutput.write("\r\n".getBytes());
                    clientOutput.write(("" + generateHtmlData() + "\r\n").getBytes());
                    clientOutput.write("\r\n\r\n".getBytes());
                    clientOutput.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    private String generateHtmlData() {
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
        for (int i = 0; i < msgGet.size(); i++) {
            data += "<td> " + msgGet.get(i) + '\n' + " </td>";
        }
        data += "</table>";
        data += "</body>" + "</html>";
        //msg.remove(0);
        return data;
    }


}