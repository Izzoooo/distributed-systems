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
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.List;

/**
 * The main class that contains the
 * main method that starts the server.
 *
 * @author Michael Bredel
 */
public class Main {

    /**
     * The logger.
     */
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static volatile TTransport transport;
    private static volatile MatrixMultiplicationService.Client client;

    /**
     * The main method that starts the
     * whole server. Thus, it creates
     * a UDP socket server and starts
     * the run-method that listens to
     * the socket.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {

        // Parse the command line arguments.
        CliProcessor.getInstance().parseCliOptions(args);

        /* Create the RPC socket server. */
        try {
            connectToRpcServer();
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

        System.out.println("" + CliProcessor.getInstance());


        int row = 5;
        int col = 5;
        Integer[][] firstM = CliParameters.initMatrix(row, col);
        Integer[][] secondM = CliParameters.initMatrix(col, row);
        List<List<Integer>> matrix1 = ListConverter.ArrayToListConversion2D(firstM);
        List<List<Integer>> matrix2 = ListConverter.ArrayToListConversion2D(secondM);

        int ErgZellAnzahl = firstM.length * secondM[0].length;
        System.out.println(ErgZellAnzahl + " Zellen sollen in Ergebnis-Matrix ausgegeben werden.");

        if (firstM[0].length != secondM.length) {// Matrix Prinzip: Um zwei Matrizen miteinander multiplizieren zu können,
            // muss die Spaltenzahl der ersten Matrix
            // mit der Zeilenzahl der zweiten Matrix übereinstimmen
            throw new RuntimeException();
        }


        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                try {
                    int result = client.multiply(matrix1, matrix2, i, j);
                    System.out.println("Status: " + result);
                    if (client.getCell() == 0) {
                        System.out.println("Info: OK.. Matrix berechnet!");
                        client.saveToDB();
                    } else if (client.getCell() == 1) {
                        System.out.println("Info: ERROR: Matrix wurde nicht berechnet!");
                    } else if (client.getCell() == 2) {
                        System.out.println("Info: Error: Fehler bei der Berechnung aufgetreten!");
                    }
                } catch (TException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    private static void connectToRpcServer() throws TTransportException {
        transport = new TSocket("client", 9090); // statt localhost client für Docker Compose.yml
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new MatrixMultiplicationService.Client(protocol);
    }


}
