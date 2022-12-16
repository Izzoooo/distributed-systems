


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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.parser.JSONParser;

/**
 * The actual socket client that creates
 * a UDP socket and sends the data.
 *
 * @author Michael Bredel
 */
public class ClientHandlerTCP {

    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandlerTCP.class);
    /**
     * The UDP port the client connects to.
     */
    private static int PORTT = 3142;

    /**
     * The TCP client socket used to send data.
     */
    private Socket clientSocket;
    /**
     * The IP address the client connects to.
     */
    private InetAddress address;

    /**
     * Default constructor that creates, i.e., opens
     * the socket.
     *
     * @throws IOException In case the socket cannot be created.
     */
    public ClientHandlerTCP() throws IOException {
        address = InetAddress.getByName("localhost");
        clientSocket = new Socket(address, PORTT);
        LOGGER.info("Started the TCP socket that connects to " + address.getHostAddress());
    }

    /**
     * Method that transmits a String message
     * via the UDP socket.
     */
    public void sendMsg(ArrayList<String> zellErgebnis) {
        // Send the data.
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeChars(zellErgebnis.get(0) + "\n");
            LOGGER.debug("Message sent with payload: " + zellErgebnis.get(0));
            zellErgebnis.remove(0);
        } catch (IOException e) {
            LOGGER.error("Could not send data.\n" + e.getLocalizedMessage());
        }
    }
    public void sendMsg2(String zellErgebnis1) {
        // Send the data.
        try {
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeChars("POST " + zellErgebnis1 + "\r"+"\n" );
            LOGGER.debug("Message sent with payload: " + zellErgebnis1);
        } catch (IOException e) {
            LOGGER.error("Could not send data.\n" + e.getLocalizedMessage());
        }
    }

/*
    public void sendMessageJSON(){
        JSONParser jsonParser = new JSONParser();

        try (FileReader rd = new FileReader("C:\\Users\\izzdi\\vsPraktikumsRueden\\distributed-systems\\tcp-socket\\socket-client\\src\\main\\java\\de\\hda\\fbi\\ds\\mbredel\\data.json")){

            Object obj = jsonParser.parse(rd);
            JSONArray studentenList = (JSONArray) obj;
            System.out.println(studentenList);

            studentenList.forEach(std ->parseStudentenObjeckt((JSONObject) std) );

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            throw new RuntimeException(e);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }

    }*/
    /*
    public void parseStudentenObjeckt(JSONObject student){
        JSONObject studentObject = (JSONObject) student.get("student");

        String firstName= (String) studentObject.get("firstName");
        System.out.println(firstName);

        String lastName = (String) studentObject.get("lastName");
        System.out.println(lastName);


        try{
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeChars(student + "\n");

        }catch(IOException e){
            LOGGER.error("Could not send data.\n" + e.getLocalizedMessage());
        }

    }*/

}