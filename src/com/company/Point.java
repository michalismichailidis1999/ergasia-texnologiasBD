package com.company;

public class Point {
    private float[] points;

    public Point(int dimensions){
        points = new float[dimensions];
    }

    public float[] getCoord(){ return points; }

    public void setAxis(float[] points){
        this.points = points;
    }
}
