package com.company;

public class Vertex {
    private float[] axisValues;

    public Vertex(int dimensions){
        axisValues = new float[dimensions];
    }

    public float[] getCoord(){ return axisValues; }

    public void setCoord(float[] axisValues){
        this.axisValues = axisValues;
    }
}
