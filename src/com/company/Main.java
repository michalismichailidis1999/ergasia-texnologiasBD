package com.company;

public class Main {

    public static void main(String[] args){
        long startTime = System.nanoTime();
        new DatabaseHandler();
        long stopTime = System.nanoTime();
        System.out.println((float)(stopTime - startTime) / 1000000000);
    }
}
