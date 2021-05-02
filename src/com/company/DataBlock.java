package com.company;

import java.util.ArrayList;

public class DataBlock {
    private final int total_bytes = 512;
    private int current_bytes;
    private ArrayList<NodeData> data;

    public DataBlock(){
        current_bytes = 0;
        data = new ArrayList<>();
    }

    public boolean addData(NodeData node){
        if((current_bytes += node.getBytes()) > total_bytes){
            return false;
        }

        this.data.add(node);
        current_bytes += node.getBytes();

        return true;
    }

    public ArrayList<NodeData> getData(){ return data; }
}
