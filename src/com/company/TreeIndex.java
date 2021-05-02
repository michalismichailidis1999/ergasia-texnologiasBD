package com.company;

import java.util.ArrayList;

public class TreeIndex {
    private ObjectPointer objectPointer;
    private int level;
    private final int dimensions;
    private ArrayList<TreeIndex> children;
    private Vertex minBoundValues;
    private Vertex maxBoundValues;

    public static final int m = 1; // minimum number of children
    public static final int M = 4; // maximum number of children

    // Non-Leaf Node Constructor
    public TreeIndex(int level, int dimensions){
        this.level = level;
        this.dimensions = dimensions;
        this.children = new ArrayList<>();
        minBoundValues = new Vertex(dimensions);
        maxBoundValues = new Vertex(dimensions);
    }

    // Leaf Node Constructor
    public TreeIndex(String blockPointer, String nodePointer, int level, int dimensions){
        this.objectPointer = new ObjectPointer(blockPointer, nodePointer);
        this.level = level;
        this.dimensions = dimensions;
        this.children = new ArrayList<>();
        minBoundValues = new Vertex(dimensions);
        maxBoundValues = new Vertex(dimensions);
    }

    public void setInitialBounds(Vertex[] bounds) {
        if(bounds.length != dimensions){
            System.out.println("Dimensions do not much");
            return;
        }

        float[] minVals = new float[dimensions];
        float[] maxVals = new float[dimensions];

        for(int i = 0; i < dimensions; i++){
            float[] currentPoint = bounds[i].getCoord();
            for(int j = 0; j < dimensions; j++){
                if(i == 0){
                    minVals[j] = currentPoint[j];
                    maxVals[j] = currentPoint[j];
                }else{
                    if(currentPoint[j] < minVals[j]){
                        minVals[j] = currentPoint[j];
                    }

                    if(currentPoint[j] > maxVals[j]){
                        maxVals[j] = currentPoint[j];
                    }
                }
            }
        }

        minBoundValues.setCoord(minVals);
        maxBoundValues.setCoord(maxVals);
    }

    public Vertex getMinBoundValues() {
        return minBoundValues;
    }

    public Vertex getMaxBoundValues() {
        return maxBoundValues;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<TreeIndex> getChildren() {
        return children;
    }

    public void adjustBounds(){
        float[] minVals = new float[dimensions];
        float[] maxVals = new float[dimensions];

        for(int i = 0; i < children.size(); i++){
            for(int j = 0; j < dimensions; j++){
                float[] currentPoint = children.get(i).getMinBoundValues().getCoord();

                if(i == 0 && j == 0){
                    for(int k = 0; k < dimensions; k++){
                        minVals[k] = currentPoint[k];
                        maxVals[k] = currentPoint[k];
                    }

                    currentPoint = children.get(i).getMaxBoundValues().getCoord();
                    for(int k = 0; k < dimensions; k++){
                        if(currentPoint[k] > maxVals[k]){
                            maxVals[k] = currentPoint[k];
                        }

                        if(currentPoint[k] < minVals[k]){
                            minVals[k] = currentPoint[k];
                        }
                    }
                }else{
                    for(int k = 0; k < dimensions; k++){
                        if(currentPoint[k] > maxVals[k]){
                            maxVals[k] = currentPoint[k];
                        }

                        if(currentPoint[k] < minVals[k]){
                            minVals[k] = currentPoint[k];
                        }
                    }

                    currentPoint = children.get(i).getMaxBoundValues().getCoord();
                    for(int k = 0; k < dimensions; k++){
                        if(currentPoint[k] > maxVals[k]){
                            maxVals[k] = currentPoint[k];
                        }

                        if(currentPoint[k] < minVals[k]){
                            minVals[k] = currentPoint[k];
                        }
                    }
                }
            }
        }

        minBoundValues.setCoord(minVals);
        maxBoundValues.setCoord(maxVals);
    }

    public boolean addChild(TreeIndex treeIndex){
        if(hasReachedMaximumChildrenNumber()){
            return false;
        }

        children.add(treeIndex);
        adjustBounds();

        return true;
    }

    public boolean isInsideBounds(Vertex[] other){
        int count = 0;

        for(int i = 0; i < dimensions; i++){
            for(int j = 0; j < dimensions; j++){
                if(other[i].getCoord()[j] >= minBoundValues.getCoord()[j] && other[i].getCoord()[j] <= maxBoundValues.getCoord()[j]){
                    count++;
                }
            }
        }

        return count == dimensions * dimensions;
    }

    public boolean isInsideBounds(Vertex minBoundValues, Vertex maxBoundValues){
        int count = 0;

        for(int i = 0; i < dimensions; i++){
            if(
                    minBoundValues.getCoord()[i] >= this.minBoundValues.getCoord()[i] &&
                            maxBoundValues.getCoord()[i] <= this.maxBoundValues.getCoord()[i]
            ){
                count++;
            }
        }

        return count == dimensions;
    }

    private float[] getCenter(){
        float[] center = new float[dimensions];

        for(int i = 0; i < dimensions; i++){
            center[i] = (maxBoundValues.getCoord()[i] + minBoundValues.getCoord()[i]) / 2f;
        }

        return center;
    }

    public double getDistance(Vertex[] bounds){
        float[] center = getCenter();

        TreeIndex treeIndex = new TreeIndex(level, dimensions);
        treeIndex.setInitialBounds(bounds);

        float[] otherCenter = treeIndex.getCenter();

        double distance = 0.0;

        for(int i = 0; i < dimensions; i++){
            distance += (center[i] - otherCenter[i]) * (center[i] - otherCenter[i]);
        }

        distance = Math.sqrt(distance);

        return distance;
    }

    public double getDistance(TreeIndex treeIndex){
        float[] center = getCenter();

        float[] otherCenter = treeIndex.getCenter();

        double distance = 0.0;

        for(int i = 0; i < dimensions; i++){
            distance += (center[i] - otherCenter[i]) * (center[i] - otherCenter[i]);
        }

        distance = Math.sqrt(distance);

        return distance;
    }

    public float calculateArea(TreeIndex other){
        float area = 1f;

        float[] maxVals = new float[dimensions];
        float[] minVals = new float[dimensions];

        for(int i = 0; i < dimensions; i++){
            if(maxBoundValues.getCoord()[i] > other.getMaxBoundValues().getCoord()[i]){
                maxVals[i] = maxBoundValues.getCoord()[i];
            }else{
                maxVals[i] = other.getMaxBoundValues().getCoord()[i];
            }

            if(minBoundValues.getCoord()[i] < other.getMinBoundValues().getCoord()[i]){
                minVals[i] = minBoundValues.getCoord()[i];
            }else{
                minVals[i] = other.getMinBoundValues().getCoord()[i];
            }
        }

        for(int i = 0; i < dimensions; i++){
            area *= (maxVals[i] - minVals[i]);
        }

        if(area < 0){
            area *= -1;
        }

        return area;
    }

    public void clearChildren(){
        children = new ArrayList<>();
        setBoundsToZero();
    }

    public boolean hasReachedMaximumChildrenNumber(){
        return TreeIndex.M == children.size();
    }

    public boolean isThisTreeIndexParent(TreeIndex treeIndex){
        if(treeIndex.getLevel() != level + 1){
            return false;
        }

        return isInsideBounds(treeIndex.getMinBoundValues(), treeIndex.getMaxBoundValues());
    }

    public void setBoundsToZero(){
        float[] point = new float[dimensions];
        for(int i = 0; i < dimensions; i++){
            point[i] = 0f;
        }

        for(int i = 0; i < dimensions; i++){
            minBoundValues.setCoord(point);
            maxBoundValues.setCoord(point);
        }
    }
}
