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
package de.hda.fbi.ds.mbredel.configuration;

import org.apache.commons.io.EndianUtils;

import java.util.List;
import java.util.Random;

/**
 * A container class that contains all the
 * CLI parameters. It is implemented using
 * the Singleton pattern (GoF) to make sure
 * we only have one object of this class.
 *
 * @author Michael Bredel
 */
public class CliParameters {

    /**
     * The one and only instance of CLI parameters.
     */
    private static CliParameters instance;

    /**
     * The port of the UDP socket server. Initially set to the default port.
     */
    private int port = Defaults.PORT;
    /**
     * The message that is published.
     */
    private String message = Defaults.MESSAGE;
    /**
     * The destination host to connect to.
     */
    private String destination = Defaults.DST_HOST;

    /**
     * The static getter for the CLI parameters instance.
     *
     * @return The CLI parameters instance.
     */
    public static CliParameters getInstance() {
        if (instance == null)
            instance = new CliParameters();
        return instance;
    }

    //
    // Getter and Setter
    //

    public static int setzen() {
        Random rd = new Random();
        int zufall = rd.nextInt(100) + 1;
        return zufall;
    }

    public static int[][] initMatrix(int rows, int columns) {
        int[][] matrix = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matrix[row][col] = 2;
            }
        }
        return matrix;
    }


    public int getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }


    public int multiplyMatricesCell(int[][] firstMatrix, int[][] secondMatrix, int row, int col) {
        int cell = 0;

        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }

        return cell;
    }

    public static void swap(Integer i, Integer j)  //this will not change i j values in main
    {
        Integer temp = new Integer(i);
        i = j;
        j = temp;
    }

    public void endErgebnisausgabe(int[][] endErgebnis) {
        System.out.println("...");
    }

    public String getMessageZwei() {

        message = "Worker 1";
        return this.message;
    }


    public void setMessage(List<String> args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
            sb.append(" ");
        }
        this.message = sb.toString().trim();
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * A private constructor to avoid
     * instantiation.
     */
    private CliParameters() {
    }
}
