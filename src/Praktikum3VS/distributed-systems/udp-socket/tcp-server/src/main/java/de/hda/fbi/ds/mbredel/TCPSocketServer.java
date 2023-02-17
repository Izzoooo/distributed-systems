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

    int dbCounter = 0;


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

                // Case distinction HTTP-POST or HTTP-GET
                handlePostOrGetMatrix(inFromClient);


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

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (i == 2) {
                sb.append(" ");
            }
            sb.append(value.charAt(i));
        }

        System.out.println(sb);

        String zeileSpalte = String.valueOf(sb);
        String[] zs = zeileSpalte.split(" ");

        String ergebnisMatrix = zs[0];
        String zs2 = zs[1];

        StringBuilder sb1 = new StringBuilder();

        for (int i = 0; i < zs2.length(); i++) {
            if (i == 1) {
                sb1.append(" ");
            }
            sb1.append(zs2.charAt(i));
        }
        String zeileSpalte1 = String.valueOf(sb1);
        String[] zs1 = zeileSpalte1.split(" ");

        String value_i = zs1[0];
        String value_j = zs1[1];

        System.out.println(value_i);
        System.out.println(value_j);

        if (postOrGet.equals("POST")) {
            msgGet.add(value);

            wr = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            wr.write("POST HTTP/1.1 200 OK\r\n");
            wr.write(value + "\n");
            wr.flush();

            Database db = new Database();

            //db.connection();
            //db.creteTable();// Im Docker mit Datnebank verbinden: nach jedem "docker compose down" (Somit wird alles gelöscht-> z.B. die Relationen, etc..)
                              // soll die Tabelle auf IntelliJ also hier einmal erstellt, dann können wir auskomentieren, da Tebelle schon wxistieren wird.


            dbCounter++;
            if(dbCounter == 1) {
                db.connection();
                db.loeschen();
            }

            db.connection();
            db.insertDataResultMitAngaben(Integer.parseInt(value_i), Integer.parseInt(value_j), Integer.parseInt(ergebnisMatrix));

            dbCounter++;
            if(dbCounter == 24) {
                db.connection();
                db.DatenAusgeben();
            }
        }
        //if (postOrGet.equals("\u0000G\u0000E\u0000T\u0000") || postOrGet.equals("GET")) {
            try (OutputStream clientOutput = connectionSocket.getOutputStream()) {
                clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write(("" + generateHtmlData() + "\r\n").getBytes());
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
       // }

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