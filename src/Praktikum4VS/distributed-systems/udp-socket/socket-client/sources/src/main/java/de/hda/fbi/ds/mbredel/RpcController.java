package de.hda.fbi.ds.mbredel;

import org.apache.thrift.TException;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.exit;


public class RpcController implements MatrixMultiplicationService.Iface {
    int cell;
    int status;

    public static List<Integer> inkrement_Liste= Collections.synchronizedList (new ArrayList<Integer>());
    public static List<Integer> Daten_Liste= Collections.synchronizedList (new ArrayList<Integer>());
    static LamportFIFO fifo = new LamportFIFO();

    @Override
    public int multiply(List<List<Integer>> firstMatrix, List<List<Integer>> secondMatrix, int row, int col) throws TException {
        // TODO: Use already defined method in CliParameters (somehow) instead of copy pasting code


        cell = 0;
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


        //todo::Fuelle die Liste aus
        LamportFIFO.incrementtimeStemp();
        inkrement_Liste.add(LamportFIFO.timeStemp);


        //todo::Daten aktualisieren
        LamportFIFO.zeile(row);
        Daten_Liste.add(LamportFIFO.zeile_ii);

        LamportFIFO.spalte(col);
        Daten_Liste.add(LamportFIFO.spalte_jj);

        LamportFIFO.zelle(cell);
        Daten_Liste.add(LamportFIFO.zell);

        return status;
    }


    @Override
    public boolean saveToDB(){
             //TCP POST
      if(cell > 0){
            try {
                ClientHandlerTCP tcpHandler = new ClientHandlerTCP();




                //todo:Publichen
                System.out.println("Got here!!!!!!");
                MqttPublisher mqttPublisher = new MqttPublisher("tcp://mosquitto:1883", "Request", LamportFIFO.timeStemp);
                String message = "Request";
                try {
                    mqttPublisher.init();
                    mqttPublisher.sendMessage(message);

                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }




                //todo: Subsriben
                MqttSubscriber mqttSubscriber = new MqttSubscriber("tcp://mosquitto:1883", "Request", LamportFIFO.timeStemp);

                fifo.latestTime = Collections.max(inkrement_Liste);
                fifo.updateTo(fifo.latestTime);


                int niderigsteTimeStemp = Collections.min(inkrement_Liste);
                System.out.println("Client mit TimeStep:" + LamportFIFO.timeStemp);


                if (niderigsteTimeStemp < fifo.latestTime ){

                    System.out.println("niedrigiste ZeitStempel: " + niderigsteTimeStemp);
                    System.out.println("Ich bin dran ---> Der Client mit ZeitStempel = " + niderigsteTimeStemp);
                    mqttPublisher.sendMessage("Release");

                    System.out.println(message);

                    mqttSubscriber.run();

                   tcpHandler.sendMatrixResult(String.valueOf(Daten_Liste.get(0)), String.valueOf(Daten_Liste.get(1))
                           ,String.valueOf(Daten_Liste.get(2)));

                   for (int m = 0; m < 3; m++) {
                       Daten_Liste.remove(0);
                    }


                    wait(1000);
                   // mqttPublisher.sendMessage("Ack");

                    inkrement_Liste.remove(Integer.valueOf(niderigsteTimeStemp)); //Stelle mit Inhakt loeschen
                    if(inkrement_Liste.size() !=  0 ) {
                        saveToDB();
                    }/*else {
                        exit(0);
                    }*/

                } else {
                    System.out.println("Geduld haben bitte, Just wait");
                   checkAndLock();

                }

                return true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
      }
        return false;
    }


    @Override
    public int getCell() throws TException {
        return status;
    }



    public synchronized void checkAndLock(){
        while ( inkrement_Liste.get(fifo.latestTime) > fifo.latestTime ) {
            System.out.println("Client mit TimeStep:" + LamportFIFO.timeStemp);
            fifo.updateTo(fifo.latestTime);
            saveToDB();

        }
    }
    public static void wait(int ms) {
        try {
            Thread.sleep((long) ms);
        } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
        }

    }
}


