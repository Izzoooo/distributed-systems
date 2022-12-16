package de.hda.fbi.ds.mbredel;

import java.util.ArrayList;
import java.util.List;

public class ListConverter {

    public static <T> List<T> ArrayToListConversion(T array[]) {
//creating the constructor of the List class
        List<T> list = new ArrayList<>();
//using for-each loop to iterate over the array
        for (T t : array) {
//adding each element to the List
            list.add(t);
        }
//returns the list converted into Array
        return list;
    }
    public static <T> List<List<T>> ArrayToListConversion2D(T array[][]) {
//creating the constructor of the List class
        List<List<T>> list = new ArrayList<>();
//using for-each loop to iterate over the array
        for (T[] t : array) {
            List<T> innerList = ArrayToListConversion(t);
//adding each element to the List
            list.add(innerList);
        }
//returns the list converted into Array
        return list;
    }
}