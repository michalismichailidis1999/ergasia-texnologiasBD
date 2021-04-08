package com.company;

import java.util.ArrayList;

public class DataBlock {
    private final int total_bytes = 512;
    private int current_bytes;
    private ArrayList<TreeNode> nodes;

    public DataBlock(){
        current_bytes = 0;
        nodes = new ArrayList<>();
    }

    public boolean addNode(TreeNode node){
        if((current_bytes += node.getBytes()) > total_bytes){
            return false;
        }

        this.nodes.add(node);
        current_bytes += node.getBytes();

        return true;
    }

    public ArrayList<TreeNode> getNodes(){ return nodes; }
}
