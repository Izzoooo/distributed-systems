package de.hda.fbi.ds.mbredel.configuration;

public class matrix {
    int[][] firstMatrix;
    int[][] secondMatrix;

    matrix(int[][] firstMatrix, int[][] secondMatrix) {

        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
    }


    public int[][] getFirstMatrix() {
        return firstMatrix;
    }

    public void setFirstMatrix(int[][] firstMatrix) {
        this.firstMatrix = firstMatrix;
    }


    public int[][] getSecondMatrix() {
        return secondMatrix;
    }

    public void setSecondMatrix(int[][] secondMatrix) {
        this.secondMatrix = secondMatrix;
    }


}
