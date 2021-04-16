package com.company;

import java.util.ArrayList;

public class TreeNode {
    // Constants
    private final int bytes;
    private final int dimensions;

    // Class Variables
    private String id, name;
    private Vertex[] coords;
    private ArrayList<TreeNode> children;

    public TreeNode(String id, String name){
        this.bytes = 8;
        this.dimensions = 2;
        this.coords = new Vertex[dimensions];
        this.children = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public TreeNode(String id, String name, int dimensions){
        this.bytes = 8;
        this.dimensions = dimensions;
        this.coords = new Vertex[dimensions];
        this.children = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Vertex[] getCoords() {
        return coords;
    }

    public  int getBytes(){ return this.bytes; }

    public void setCoords(Vertex[] coords) {
        this.coords = coords;
    }
}
