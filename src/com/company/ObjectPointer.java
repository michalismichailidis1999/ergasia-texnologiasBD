package com.company;

public class ObjectPointer {
    private String blockPointer;
    private String nodePointer; // ID of the nodeData

    public ObjectPointer(String blockPointer, String nodePointer){
        this.blockPointer = blockPointer;
        this.nodePointer = nodePointer;
    }

    public String getBlockPointer() {
        return blockPointer;
    }

    public String getNodePointer() {
        return nodePointer;
    }
}
