package de.hda.fbi.ds.mbredel;

import de.hda.fbi.ds.mbredel.configuration.CliParameters;
import org.apache.thrift.TException;

import java.io.IOException;
import java.nio.channels.Channel;
import java.sql.SQLException;
import java.util.List;


public class RpcController implements MatrixMultiplicationService.Iface {
    int cell = 0;
    int status;
    int zeile_i = 0;
    int spalte_j = 0;


    @Override
    public int multiply(List<List<Integer>> firstMatrix, List<List<Integer>> secondMatrix, int row, int col) throws TException {
        // TODO: Use already defined method in CliParameters (somehow) instead of copy pasting code


        for (int i = 0; i < secondMatrix.size(); i++) {
            cell += firstMatrix.get(row).get(i) * secondMatrix.get(i).get(col);

            zeile_i = row;
            spalte_j = col;
        }
        if (cell > 0) {
            status = 0; //0: heißt, die Matrix wurde berechnet
        } else if (cell == 0) {
            status = 1; //1: die Matrix wurde nicht berechnet
        } else if (cell < 0) {
            status = 2; //2: heißt, Fehler wurde bei der Berechnung aufgetreten
        }
        return status;
    }

    @Override
    public boolean saveToDB() {
        //TCP POST
            try {
                ClientHandlerTCP tcpHandler = new ClientHandlerTCP();
                tcpHandler.sendMatrixResult(String.valueOf(zeile_i), String.valueOf(spalte_j), String.valueOf(cell));
                cell = 0;
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    public int getCell() {
        return status;
    }
}


