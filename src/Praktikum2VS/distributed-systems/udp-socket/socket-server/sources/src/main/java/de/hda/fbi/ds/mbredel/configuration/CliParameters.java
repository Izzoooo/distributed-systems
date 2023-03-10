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
     * The buffer size of the UDP buffer.
     */

    private String message = Defaults.MESSAGE;
    private int bufferSize = Defaults.BUFFER_SIZE;

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
        int zufall = rd.nextInt(99) + 2;
        return zufall;
    }

    public static Integer[][] initMatrix(int rows, int columns) {
        Integer[][] matrix = new Integer[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matrix[row][col] = 2;
            }
        }
        return matrix;
    }

    int counter = 0;

    public String getMessage() {
        int x = 5;
        int y = 5;
        //int x = setzen();
        //int y = setzen();
        Integer[][] firstParameter = initMatrix(x, y);
        Integer[][] zweitParameter = initMatrix(y, x);

        if (firstParameter[0].length != zweitParameter.length) {
            throw new RuntimeException();
        }


        int ErgebnisZellAnzahl = firstParameter.length * zweitParameter[0].length;


        if (counter <= ErgebnisZellAnzahl) {
            while (counter <= ErgebnisZellAnzahl) {
                counter++;
                if (counter == ErgebnisZellAnzahl) {
                    counter = 0;
                    message = "Matrix Matrizen zur Berechnung wird wieder neu geschickt\n";
                    return this.message;
                }
                message = x + "\n"
                        + y + "\n"
                        + y + "\n"
                        + x + "\n"
                        + x + "\n"
                        + x + "\n";
                return this.message;
            }
        }

        return null;
    }



    public int getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(String bufferSize) {
        this.bufferSize = Integer.parseInt(bufferSize);
    }

    /**
     * A private constructor to avoid
     * instantiation.
     */
    private CliParameters() {
    }
}
