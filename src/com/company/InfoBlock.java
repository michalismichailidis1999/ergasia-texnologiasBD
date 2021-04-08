package com.company;

public class InfoBlock {
    private final String id = "block_0";

    private float minlat, minlon, maxlat, maxlon;
    private int totalNodes, totalBlocks;

    InfoBlock(float minlat, float minlon, float maxlat, float maxlon, int totalNodes, int totalBlocks) {
        this.minlat = minlat;
        this.minlon = minlon;
        this.maxlat = maxlat;
        this.maxlon = maxlon;
        this.totalNodes = totalNodes;
        this.totalBlocks = totalBlocks;
    }

    public String getId() {
        return id;
    }

    public float getMinlat() {
        return minlat;
    }

    public float getMinlon() {
        return minlon;
    }

    public float getMaxlat() {
        return maxlat;
    }

    public float getMaxlon() {
        return maxlon;
    }

    public int getTotalNodes() {
        return totalNodes;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }
}
