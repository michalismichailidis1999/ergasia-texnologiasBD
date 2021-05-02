package com.company;

public class NodeData {
    private String id, name;
    private Vertex[] bounds;

    private final int dimensions;
    private final int BYTES = 8;

    NodeData(String id, String name, int dimensions){
        this.id = id;
        this.name = name;
        this.dimensions = dimensions;
    }

    public void setBounds(Vertex[] bounds){
        if(bounds.length != dimensions){
            System.out.println("Dimensions do not match!");
            return;
        }

        this.bounds = bounds;
    }

    public Vertex[] getBounds() {
        return bounds;
    }

    public int getBytes() {
        return BYTES;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
