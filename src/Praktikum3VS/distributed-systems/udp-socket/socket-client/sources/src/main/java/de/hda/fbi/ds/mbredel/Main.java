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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
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
    public static void main(String[] args) throws IOException, InterruptedException {

        // Parse environemnt variables.
        parseOptionsFromEnv();

        // Parse the command line arguments.
        CliProcessor.getInstance().parseCliOptions(args);

        // Create the RPC socket client.
        startRpcServer();

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
        }
    }

}










