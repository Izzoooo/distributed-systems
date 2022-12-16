package de.hda.fbi.ds.mbredel;

import de.hda.fbi.ds.mbredel.configuration.CliParameters;
import org.apache.thrift.TException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RpcController implements MatrixMultiplicationService.Iface {
    int cell = 0;
    int status;
    @Override
    public int multiply(List<List<Integer>> firstMatrix, List<List<Integer>> secondMatrix, int row, int col) throws TException {
        // TODO: Use already defined method in CliParameters (somehow) instead of copy pasting code


        for (int i = 0; i < secondMatrix.size(); i++) {
            cell += firstMatrix.get(row).get(i) * secondMatrix.get(i).get(col);
        }
        if(cell>0){
            status =0; //0: heißt, die Matrix wurde berechnet
        }else if(cell == 0){
            status = 1; //1: die Matrix wurde nicht berechnet
        }else if(cell<0){
            status = 2; //2: heißt, Fehler wurde bei der Berechnung aufgetreten
        }
        return status;
    }
    @Override
    public boolean saveToDB() throws TException {
 //TCP POST
        Database db = new Database();
        if(db.connection()){
            try {
                ClientHandlerTCP tcpHandler = new ClientHandlerTCP();
                tcpHandler.sendMsg2(String.valueOf(cell));
                //db.insertDataResult(cell);
                cell=0;
                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Override
    public int getCell() throws TException {
        return status;
    }
}
