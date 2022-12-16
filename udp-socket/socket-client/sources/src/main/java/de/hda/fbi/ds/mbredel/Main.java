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
import de.hda.fbi.ds.mbredel.configuration.CliProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * The main class that contains the
 * main method that starts the client.
 *
 * @author Michael Bredel
 */
public class Main {

    /**
     * The logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "https://www.google.com/search?q=javaguides";
    //private static final String POST_URL = "http://localhost:8080/login-jsp-jdbc-mysql-example/login.jsp";
    private static final String POST_URL = "https://postman-echo.com/post";
    private static final String POST_PARAMS = "userName=Ramesh&password=Pass@123";

    public static MatrixMultiplicationService.Processor processor;
    public static RpcController handler;

    /**
     * Sets the command-line options with values in environment variables.
     * This can be used to ease the configuration of the server running
     * in Docker compose.
     */
    private static void parseOptionsFromEnv() {
        try {
            CliParameters.getInstance().setDestination(System.getenv("DESTINATION"));
        } catch (NullPointerException e) {
            LOGGER.debug("Environment variable \"DESTINATION\" does not exist");
        }
    }

    /**
     * The main method that starts the
     * whole client. Thus, it creates
     * a UDP socket client and transmits
     * a string.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) throws IOException {

        // Parse environemnt variables.
        parseOptionsFromEnv();

        // Parse the command line arguments.
        CliProcessor.getInstance().parseCliOptions(args);
        startRpcServer();

        // Create the UDP socket client.
        UDPSocketClient udpSocketClient = new UDPSocketClient();

        // sendHttpGETRequest();
        System.out.println("--------------------------------------------------------");
        //sendPOST();
        System.out.println("----------------------------------------------------------");
        //sendPost2();

// Matrix Prinzip: Um zwei Matrizen miteinander multiplizieren zu können, muss die Spaltenzahl der ersten Matrix mit der Zeilenzahl der zweiten Matrix übereinstimmen

        for (int counter = 0; counter < 50; ++counter) {
            udpSocketClient.sendMsg(CliParameters.getInstance().getMessageZwei());
            wait(1000);
        }



        //    HttpClient httpClient =  HttpClient.newBuilder().connectTimeot(Duration.ofSeconds((5)).build());


    }

    private static void startRpcServer() {
        try {
            handler = new RpcController();
            processor = new MatrixMultiplicationService.Processor(handler);

            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            // Use this for a multithreaded server
            // TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    public static void wait(int ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        }

    }

    //GET
    private static void sendHttpGETRequest() throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println("Print REsult: " + response.toString());
        } else {
            System.out.println("GET request not worked");
        }

        for (int i = 1; i <= 20; i++) {
            System.out.println(httpURLConnection.getHeaderFieldKey(i) + " = " + httpURLConnection.getHeaderField(i));
        }

    }

    //POST
    private static void sendPOST() throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);

        // For POST only - START
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }


    private static void sendPost2() throws MalformedURLException {
        URL dummyUrl = new URL("https://postman-echo.com/post");
        //URL dummyUrl = new URL("https://localhost:8080/post");
        //String dummyData = "firstname=john&lastname=doe";
        String dummyData = "<h1>Hallo</h>\r\n";

        try {
            HttpURLConnection httpUrlConnection = (HttpURLConnection) dummyUrl.openConnection();

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);


            httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("charset", "utf-8");
            httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(dummyData.length()));

            DataOutputStream dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
            dataOutputStream.writeBytes(dummyData);

            InputStream inputStream = httpUrlConnection.getInputStream();


            //Ausdgabe
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String stringLine;
            while ((stringLine = bufferedReader.readLine()) != null) {
                System.out.println(stringLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}










