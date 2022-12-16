package de.hda.fbi.ds.mbredel.configuration;

public class Worker1 {

    int[][] firstMatrix;
    int[][] secondMatrix;

    public Worker1(int[][] firstMatrix, int[][] secondMatrix) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }

    //public void endErgebnisausgabe(int[][] endErgebnis) {}


    public int[][] getFirstMatrix() {
        return firstMatrix;
    }

    public int[][] getSecondMatrix() {
        return secondMatrix;
    }
}
