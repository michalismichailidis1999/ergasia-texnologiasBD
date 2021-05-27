package com.company;

import java.util.Map;

public class Main {

    public static void main(String[] args){
//        DatabaseHandler databaseHandler = new DatabaseHandler();
//
//        RStarTree rStarTree = new RStarTree(2);
//
//        for(Map.Entry<String, DataBlock> dataBlock: databaseHandler.getBlocks().entrySet()){
//            for(NodeData node: dataBlock.getValue().getData()){
//                rStarTree.insert(dataBlock.getKey(), node.getId(), node.getBounds());
//            }
//        }

        DummyData dummyData = new DummyData(2);

        RStarTree rStarTree = new RStarTree(2);

        for(Map.Entry<String, DataBlock> dataBlock: dummyData.getBlocks().entrySet()){
            for(NodeData node: dataBlock.getValue().getData()){
                rStarTree.insert(dataBlock.getKey(), node.getId(), node.getBounds());
            }
        }

        //rStarTree.printRootIndicesWithChildren();

        // TODO: Range queries here
        Vertex[] bounds = new Vertex[2];

        Vertex v1 =  new Vertex(2);
        float[] point1 = {1f, 3f};
        v1.setCoord(point1);

        Vertex v2 =  new Vertex(2);
        float[] point2 = {4f, 7f};
        v2.setCoord(point2);

        bounds[0] = v1;
        bounds[1] = v2;

        rStarTree.rangeQuery(bounds);


        // TODO: k-nn queries here
    }
}
