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

import de.hda.fbi.ds.mbredel.configuration.CliParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * The actual socket server that creates
 * a UDP socket and waits for incoming
 * datagram.
 *
 * @author Michael Bredel
 */
public class UDPSocketServer {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UDPSocketServer.class);

    /**
     * A buffer array to store the datagram information.
     */
    private byte[] buf; //recive
    private byte[] buffHealth;

    private byte[] buf_send = new byte[500];// send

    /**
     * States the server running.
     */
    private boolean running = true;
    private InetAddress addressss;

    private ArrayList<DatagramPacket> datenbuffer = new ArrayList<>();

    /**
     * Default constructor that initializes the UDP
     * buffer byte array with the given buffer size.
     */
    public UDPSocketServer() {
        // Initialize the UDP buffer.
        buf = new byte[CliParameters.getInstance().getBufferSize()];
        buffHealth = new byte[CliParameters.getInstance().getBufferSize()];
    }

    /**
     * Method that creates the UDP socket and continuously receives
     * the data from the UDP socket and logs the datagram information.
     * <p>
     * You may simplify the try-catch-finally block by using a
     * try-with-resources statement for creating the socket, i.e.
     * try (DatagramSocket udpSocket = new DatagramSocket(..)) {..}.
     * <p>
     * You may also want to separate socket creation and
     * package reception into multiple methods, like createSocket()
     * and receiveMsg();
     */
    public void run() {
        String workerNachricht = "";
        // Create the UDP datagram socket.
        try (DatagramSocket udpSocket = new DatagramSocket(CliParameters.getInstance().getPort())) {
            LOGGER.info("Started the UDP socket server at port {} with buffer size {}.",
                    CliParameters.getInstance().getPort(),
                    buf.length);
            // Receive packets continuously.
            while (running) {


                // Create the datagram packet structure that contains the received datagram information.

                //todo:hier Healchcheck
                //buf = (CliParameters.getInstance().getMessageZwei()).getBytes();
                //DatagramPacket udpPaket2 = new DatagramPacket(buf ,buf.length, Integer.parseInt(CliParameters.getInstance().getMessage()));
                DatagramPacket udpPaketHealch = new DatagramPacket(buffHealth, buffHealth.length);

                // Receive message
                udpSocket.setSoTimeout(4000);
                udpSocket.receive(udpPaketHealch);


                //Print some packet data.
                printPacketData(udpPaketHealch);

                InetAddress adresse = udpPaketHealch.getAddress();
                int port = udpPaketHealch.getPort();


                udpPaketHealch.setPort(port);
                udpPaketHealch.setAddress(adresse);

                System.out.println("Answer to " + adresse + ": " + port);


                //Send message
                udpSocket.send(udpPaketHealch);


                workerNachricht = new String(udpPaketHealch.getData(), 0, udpPaketHealch.getLength());
                System.out.println("Nachricht: " + workerNachricht);


                if (!workerNachricht.isEmpty()) {
                    LOGGER.info(workerNachricht + " Betreibsbereit => Sucsessfuly",
                            CliParameters.getInstance().getPort(),
                            buffHealth.length);
                }


            }
        } catch (SocketTimeoutException e) {
            System.out.println(e.getMessage());
            System.out.println(workerNachricht + " nicht Betriebstbereit");
            run();
        } catch (SocketException e) {
            LOGGER.error("Could not start the UDP socket server.\n{}", e);
        } catch (IOException e) {
            LOGGER.error("Could not receive packet.\n{}", e);
        }


    }


    /**
     * Stop the UDP socket server.
     */
    @SuppressWarnings("unused")
    public void stop() {
        this.running = false;
    }

    /**
     * Extracts some data of a given datagram packet
     * and prints the result to standard out.
     *
     * @param udpPacket The datagram packet to extract and print.
     */
    private void printPacketData(DatagramPacket udpPacket) {
        // Get IP address and port.
        InetAddress address = udpPacket.getAddress();
        int port = udpPacket.getPort();
        // Get packet length.
        int length = udpPacket.getLength();
        // Get the payload from the buffer. Mind the buffer size and the packet length!
        byte[] playload = Arrays.copyOfRange(udpPacket.getData(), 0, length);
        // Print the packet information.
        System.out.println("Received a packet: IP:Port: " + address + ":" + port + ", length: " + length + ", payload: " + new String(playload));
    }
}