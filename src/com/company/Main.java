package com.company;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        DatabaseHandler databaseHandler = new DatabaseHandler();

        RStarTree rStarTree = new RStarTree(2);

        for(Map.Entry<String, DataBlock> dataBlock: databaseHandler.getBlocks().entrySet()){
            for(NodeData node: dataBlock.getValue().getData()){
                rStarTree.insert(dataBlock.getKey(), node.getId(), node.getBounds());
            }
        }

        float minLat = databaseHandler.getInfoBlock().getMinlat();
        float maxLat = databaseHandler.getInfoBlock().getMaxlat();
        float minLon = databaseHandler.getInfoBlock().getMinlon();
        float maxLon = databaseHandler.getInfoBlock().getMaxlon();

        float latStep = (maxLat - minLat - 0.003f) / 1000f;
        float lonStep = (maxLon - minLon - 0.003f) / 1000f;

        float lat1 = (maxLat + minLat) / 2f;
        float lon1 = (maxLon + minLon) / 2f;

        float lat2 = (maxLat + minLat) / 2f;
        float lon2 = (maxLon + minLon) / 2f;

        // Range queries here
        System.out.println("Range Queries");

        for(int i = 0; i < 10000; i++){
            Vertex[] bounds = new Vertex[2];

            Vertex v1 =  new Vertex(2);
            float[] point1 = {lat1 + latStep, lon1 + lonStep};
            v1.setCoord(point1);

            Vertex v2 =  new Vertex(2);
            float[] point2 = {lat2 - latStep, lon2 - lonStep};
            v1.setCoord(point2);

            bounds[0] = v1;
            bounds[1] = v2;

            ArrayList<TreeIndex> leafsInRange = rStarTree.rangeQuery(bounds);

            System.out.println("Range Query found " + leafsInRange.size() + " nodes with range of: ");
            System.out.print("(x1, y1), (x2, y2) => (" + lat1 + ", " + lon1 + "), (" + lat2 + ", " + lon2 + ")");
            System.out.println("");

            lat1 += latStep;
            lon1 += lonStep;

            lat2 -= latStep;
            lon2 -= lonStep;
        }

        // K-NN queries here
        System.out.println("KNN Queries");

        float lat = (maxLat + minLat) / 2f;
        float lon = (maxLon + minLon) / 2f;

        for(int k = 0; k < 10000; k+=100){
            Vertex v =  new Vertex(2);
            float[] point3 = {lat, lon};
            v.setCoord(point3);

            long startTime = System.currentTimeMillis();
            Vertex[] verts = rStarTree.knn(k + 5, v);
            long stopTime = System.currentTimeMillis();

            long timeDiff = stopTime - startTime;

            System.out.println("For k=" + k + " algorithm finished in " + timeDiff + " milliseconds");
            System.out.println("");
        }

        System.out.println("=============================================================");
    }
}
