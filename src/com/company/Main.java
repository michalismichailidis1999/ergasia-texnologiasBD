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

        rStarTree.printRootIndicesWithChildren();

        // TODO: Range queries here

        // TODO: k-nn queries here
    }
}
