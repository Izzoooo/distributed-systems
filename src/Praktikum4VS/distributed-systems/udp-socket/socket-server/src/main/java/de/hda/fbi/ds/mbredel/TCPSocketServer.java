/*
 Copyright (c) 2017, Michael Bredel, H-DA
 ALL RIGHTS RESERVED.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Neither the name of the H-DA and Michael Bredel
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written
 permission.
 */
package de.hda.fbi.ds.mbredel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.lang.model.element.Element;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * The actual socket server that creates
 * a TCP socket and waits for incoming
 * connections.
 *
 * @author Michael Bredel
 */
public class TCPSocketServer {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TCPSocketServer.class);
    /** The TCP port the server listens to. */
    private static int PORT = 3142;

    /** The TCP server socket used to receive data. */
    private ServerSocket tcpServerSocket;

    /** States the server running. */

    private boolean running = true;
     public Socket connectionSocket;

    /**
     * Default constructor that creates, i.e., opens
     * the socket.
     *
     * @throws IOException In case the socket cannot be created.
     */
    public TCPSocketServer() throws IOException {
        tcpServerSocket = new ServerSocket( PORT );
        LOGGER.info("Started the TCP socket server at port " + PORT);
    }

    /**
     * Continuously running method that receives the data
     * from the TCP socket and logs the information.
     */
    public void run()  {
        while(running) {
            connectionSocket = null;
            try {
                // Open a connection socket, once a client connects to the server socket.
                connectionSocket = tcpServerSocket.accept();
                // Get the continuous input stream from the connection socket.
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                // Print the input stream.

                //printInputStream(inFromClient);

                post(inFromClient); //Bufferreader wird hier behandelt
            } catch (IOException e) {
                LOGGER.error("Could not receive datagram.\n" + e.getLocalizedMessage());
            } finally {
                if (connectionSocket != null) {
                    try {
                        connectionSocket.close();
                        LOGGER.debug("Connection socket closed.");
                    } catch (IOException e) {
                        // Do nothing.
                    }
                }
            }
        }
    }

    /**
     * Extracts some data of a given data stream
     * and prints the result to standard out.
     *
     * @param bufferedReader The buffered input stream from the client.
     */
    private void printInputStream(BufferedReader bufferedReader) {
        try {
            // Read a text-line form the buffer.
            String streamLine = bufferedReader.readLine();
            // Print the packet information.
            System.out.println("Received some information: " + streamLine);
        } catch (IOException e) {
            LOGGER.error("Could not read from buffered reader.");
        }
    }


    ArrayList<String> msg = new ArrayList<>();
    ArrayList<String> msgDatei = new ArrayList<>();


/*

    public void post(BufferedReader rd) throws IOException {

        BufferedWriter wr;

        String line = rd.readLine();
        String[] lines = line.split("\\r?\\n");
        String[] header = lines[0].split(" ");
        String postOrGet = header[0];

        msg.add(line);
        msgDatei.add(line);
        infoSpeichern();


        if (postOrGet.equals("\u0000P\u0000O\u0000S\u0000T\u0000")) {
            System.out.print("The data: ");
            //while (line != null) {
            System.out.println("LINE: "+ line);
            System.out.println("---------------------");
            //header[0].contains("Content-len");
            //if (line.contains("Time")) {
            wr = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            wr.write("POST HTTP/1.1 200 OK\r\n");
            wr.write(line);
            //System.out.println("Send RTT from cloud to client: "+line);
            wr.flush();
            //}else{
            // parse(line); //hier muss die Daten persisitiert werden in dem die Methode parse implementiert wird
            // also noch neue daten hinzufuegen
            System.out.println("else Seite:   " + line);
            //  }
            //}
        }
        if (postOrGet.equals("GET")) {
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
    }*/

    public void post(BufferedReader rd)throws IOException{

        BufferedWriter wr;

        String line = rd.readLine();
        String[] lines = line.split("\\r?\\n");
        String[] header = lines[0].split(" ");
        String Endergebnis = header[0];


        msg.add(Endergebnis);

        msgDatei.add(line);
        infoSpeichern();


        if (header[0].contains("POST")) {
            System.out.print("The data: ");
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Time")){
                    wr = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
                    wr.write("POST HTTP/1.1 200 OK\r\n");
                    wr.write(line);
                    //System.out.println("Send RTT from cloud to client: "+line);
                    wr.flush();
                }//else{
                // parse(line); //hier muss die Daten persisitiert werden in dem die Methode parse implementiert wird
                // also noch neue daten hinzufuegen
                //}
            }
        }

        if(header[0].contains("GET")){
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



//msg.remove(0);

    }

    private String generateHtmlData() {
        String data = "<html>"
                + "<head>" +
                "<title>Http</title> " +
                "<style> " +
                "background-color: #dddddd;"+
                "table {" +
                "font-family: arial, sans-serif;" +
                "border-collapse: collapse;" +
                "width: 100%;" +
                "}"+
                "td, th {" +
                "border: 1px solid #ddddaa;" +
                "text-align: left;" +
                "padding: 8px;" +
                "}"+
                "tr:nth-child(even) {" +
                "background-color: #ddffff;" +
                "}"+
                "h2 {" +
                "text-align: center;"+
                "}"+
                "</style>"+
                "</head>"
                + "<body>";
        data += "<h2>Matrix</h2>";
        data += "<table>";
        data += "<tr>";
        data += "<th>Matrix Ergebnis</th>";
        data += "</tr>";
       for(String s: msg) {
            data += "<td> " + s + " </td>";
        }
       // for(int f= 0; f < msg.size(); f++){
         //   data += "<td> " + msg.get(0) + " </td>";
        //}
        data += "</table>";
        data += "</body>" + "</html>";
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
            while(counter  <50) {

                for (Object s: msgDatei) {

                    writer.print("Zelle Nummer" + counter + ": ");
                    counter++;
                    writer.printf(s.toString() + "\n");

                }

            }
                writer.close();


        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }



    }

}






