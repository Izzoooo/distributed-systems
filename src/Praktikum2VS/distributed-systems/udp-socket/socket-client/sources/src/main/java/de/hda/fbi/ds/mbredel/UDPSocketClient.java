/*
 Copyright (c) 2018, Michael Bredel, H-DA
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

import ch.qos.logback.core.BasicStatusManager;
import de.hda.fbi.ds.mbredel.configuration.CliParameters;
import de.hda.fbi.ds.mbredel.configuration.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import static org.apache.commons.lang.CharSetUtils.count;
import static sun.net.www.http.HttpClient.New;


/**
 * The actual socket server that creates
 * a UDP socket and waits for incoming
 * datagram.
 *
 * @author Michael Bredel
 */
public class UDPSocketClient {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPSocketClient.class);

    /**
     * The IP address the client connects to.
     */
    private InetAddress address;
    public ArrayList<Double> rtt_buff = new ArrayList<>();
    public ArrayList<String> datenbuffer = new ArrayList<>();
    public ArrayList<String> developer_Daten1 = new ArrayList<>();
    public ArrayList<String> developer_Daten2 = new ArrayList<>();


    int bestaetigungCount = 0;
    int gesendetCount = 0;
    int anzahlpaketverluste = 0;
    long t1 = 0;
    long t2 = 0;
    int counter = 0;


    /**
     * Default constructor that initializes Internet
     * address the client connects to.
     */
    public UDPSocketClient() {
        // Try to set the destination host address.
        try {
            address = InetAddress.getByName(CliParameters.getInstance().getDestination()); //"127.19.0.2"
            //address = InetAddress.getByName("172.19.0.1");
            // clientSocket = new Socket(address,8080);

        } catch (UnknownHostException e) {
            LOGGER.error("Can not parse the destination host address.\n{}", e.getMessage());
            System.exit(Defaults.EXIT_CODE_ERROR);
        }
    }

    /**
     * Method that transmits a String message via the UDP socket.
     * <p>
     * This method is used to demonstrate the usage of datagram sockets
     * in Java. To this end, uses a try-with-resources statement that
     * closes the socket in any case of error.
     *
     * @param msg The String message to transmit.
     * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">The try-with-resources Statement</a>
     */
    public void sendMsg(String msg) throws IOException {


        // Create the UDP datagram socket.
        try (DatagramSocket udpSocket = new DatagramSocket()) {

            LOGGER.info("Started the UDP socket that connects to {}.", address.getHostAddress());

            // Convert the message into a byte-array.
            byte[] buf = msg.getBytes();
            // Create a new UDP packet with the byte-array as payload.
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, CliParameters.getInstance().getPort());

            // Send the data.
            t1 = System.currentTimeMillis();
            udpSocket.send(packet);

            gesendetCount++;


            byte[] bufhealth = new byte[200];
            DatagramPacket packet2 = new DatagramPacket(bufhealth, bufhealth.length);

            udpSocket.setSoTimeout(600); //auf Antwort von Server warten,
            // falls er Stromausfall hat oder irgendwie abgesturzt wurde

            udpSocket.receive(packet2);
            t2 = System.currentTimeMillis();

            bestaetigungCount++;


            //todo::healthcheck bool-wert abchecken
            String ss = new String(packet2.getData(), 0, packet2.getLength());
            System.out.println(ss);
            String workerEins = CliParameters.getInstance().getMessageZwei();
            System.out.println(workerEins);

            boolean checkk = healthCheckk(ss);
            System.out.println(checkk);
            if (checkk) {
                System.out.println("Ich bin healthy");
            }


            anzahlpaketverluste = gesendetCount - bestaetigungCount;


            double umlaufzeit = t2 - t1;
            rtt_buff.add(umlaufzeit);


            System.out.println("Datenverluste: " + anzahlpaketverluste);
            System.out.println("RTT: " + umlaufzeit + " ms");

            infoSpeichern();
            System.out.println("----------------------");


            //todo:Matrixdaten von Controller empfangen, entpacken
            byte[] bufMatrizen = new byte[250];
            DatagramPacket packet3 = new DatagramPacket(bufMatrizen, bufMatrizen.length);
            udpSocket.receive(packet3);

            String mm = new String(packet3.getData(), 0, packet3.getLength());


            if (mm.length() > 20) {
                Scanner scan1 = new Scanner(mm);
                String exceptionScan = scan1.nextLine();
                System.out.println("----------");
                System.out.println(exceptionScan);
                System.out.println("----------");
            } else {

                Scanner scan = new Scanner(mm);
                int s1 = Integer.parseInt(scan.nextLine());
                int s2 = Integer.parseInt(scan.nextLine());
                int s3 = Integer.parseInt(scan.nextLine());
                int s4 = Integer.parseInt(scan.nextLine());
                int s5 = Integer.parseInt(scan.nextLine());
                int s6 = Integer.parseInt(scan.nextLine());


                int[][] firstM = CliParameters.initMatrix(s1, s2);
                int[][] secondM = CliParameters.initMatrix(s3, s4);

                System.out.println("First-Mtrix: " + s1 + " " + s2);
                System.out.println("Second-Matrix: " + s3 + " " + s4);
                System.out.println(s5 + " " + s6);


                //Worker1 w = new Worker1(firstM,secondM);
                int ergebnis = CliParameters.getInstance().multiplyMatricesCell(firstM, secondM, s5 - 1, s6 - 1);
                System.out.println(ergebnis);

                String zellErgebnis = String.valueOf(ergebnis);
                datenbuffer.add(zellErgebnis); //für TCP

            }


            //todo: Daten an ClientHandlerTCP umlenken, um TCP-Transaktion starten zu können

            ClientHandlerTCP tcpClient = new ClientHandlerTCP();
            counter++;
            if (counter == 1) {
                developer_Daten1.add("Izzdin");
                developer_Daten1.add("Ahmad");
                tcpClient.sendMsgPOST(developer_Daten1);
            }
            if (counter == 2) {
                developer_Daten2.add("Shawket");
                developer_Daten2.add("AbuAlnasir");
                tcpClient.sendMsgPOST(developer_Daten2);
            }
            if (counter != 1 && counter != 2) {
                tcpClient.sendMsgGET(datenbuffer);
            }


            // LOGGER.info("Message sent with payload: {}", msg);
        } catch (SocketException e) {
            LOGGER.error("Could not start the UDP socket server.\n{}", e.getLocalizedMessage());

        } catch (IOException e) {
            LOGGER.error("Could not send data.\n{}", e);
        }


    }


    private void infoSpeichern() {
        //todo: RTT Ergebnisse in einer Datei speichern
        String fileName = "RTT.txt";
        String encoding = "UTF-8";
        try {

            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println("RTT:      ");

            for (int i = 0; i < rtt_buff.size(); i++) {

                writer.printf(rtt_buff.get(i) + " ms   \n");
                //writer.println(ping_buff.get(i) + " ms    ");

            }

            writer.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    private boolean healthCheckk(String packet2) {
        String workerEins = CliParameters.getInstance().getMessageZwei();
        if (packet2.equals(workerEins)) {
            return true;
        } else {
            return false;
        }

    }

}