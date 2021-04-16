package com.company;

public class Vertex {
    private float[] points;

    public Vertex(int dimensions){
        points = new float[dimensions];
    }

    public float[] getCoord(){ return points; }

    public void setAxis(float[] points){
        this.points = points;
    }
}
