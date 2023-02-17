package de.hda.fbi.ds.mbredel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.util.Collections.max;

public class LamportFIFO {

      LamportFIFO(){}


    static int zeile_ii;

    public static void zeile(int z) {
        zeile_ii = z;
    }
    static int spalte_jj;

    public static void spalte(int s) {
        spalte_jj = s;
    }
    static int zell;

    public static void zelle(int ze) {
        zell = ze;
    }





    static int timeStemp;

    public static void incrementtimeStemp() {
        timeStemp++;
    }

    int latestTime;

    public void updateTo(int timestamp) {
        latestTime = timestamp;
    }





}

