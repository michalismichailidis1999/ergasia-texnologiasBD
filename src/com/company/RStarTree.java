package com.company;

import java.util.ArrayList;

public class RStarTree {
    private final int dimensions;
    ArrayList<TreeIndex> rootIndices;

    public RStarTree(int dimensions){
        this.dimensions = dimensions;
        rootIndices = new ArrayList<>();
    }

    public void insert(String blockPointer, String nodePointer, Vertex[] bounds){
        if(rootIndices.size() == 0){
            insertRootIndex(blockPointer, nodePointer, bounds);
            return;
        }

        TreeIndex current = getTreeIndexByBounds(bounds, rootIndices);

        if(current == null){
            current = getTreeIndexByMinDistance(bounds, rootIndices);
        }

        TreeIndex root = current;
        TreeIndex nonLeafNodeToInsert = null;

        while(nonLeafNodeToInsert == null){
            if(current.getLevel() == 1){
                nonLeafNodeToInsert = current;
            }else{
                TreeIndex temp = current;

                current = getTreeIndexByBounds(bounds, current.getChildren());

                if(current == null){
                    current = getTreeIndexByMinDistance(bounds, temp.getChildren());
                }
            }
        }

        TreeIndex leafIndex = new TreeIndex(blockPointer, nodePointer, 2, dimensions);
        leafIndex.setInitialBounds(bounds);

        if(!nonLeafNodeToInsert.addChild(leafIndex)){
            Split(nonLeafNodeToInsert, blockPointer, nodePointer, bounds);
            return;
        }

        root.adjustBounds();
    }

    private void insertRootIndex(String blockPointer, String nodePointer, Vertex[] bounds){
        TreeIndex treeIndex;
        treeIndex = new TreeIndex(0, dimensions);
        treeIndex.setInitialBounds(bounds);
        rootIndices.add(treeIndex);

        TreeIndex rootIndex = rootIndices.get(rootIndices.size() - 1);

        TreeIndex nonLeafIndex = new TreeIndex(1, dimensions);
        nonLeafIndex.setInitialBounds(bounds);

        TreeIndex leafIndex = new TreeIndex(blockPointer, nodePointer, 3, dimensions);
        leafIndex.setInitialBounds(bounds);

        nonLeafIndex.addChild(leafIndex);
        rootIndex.addChild(nonLeafIndex);
    }

    private TreeIndex getTreeIndexByBounds(Vertex[] bounds, ArrayList<TreeIndex> treeIndices){
        for(TreeIndex treeIndex: treeIndices){
            if(treeIndex.isInsideBounds(bounds)){
                return treeIndex;
            }
        }

        return null;
    }

    private TreeIndex getTreeIndexByMinDistance(Vertex[] bounds, ArrayList<TreeIndex> treeIndices){
        TreeIndex bestTreeIndex = treeIndices.get(0);
        double minDistance = bestTreeIndex.getDistance(bounds);

        for(int i = 1; i < treeIndices.size(); i++){
            double currentDistance = treeIndices.get(i).getDistance(bounds);
            if(currentDistance < minDistance){
                minDistance = currentDistance;
                bestTreeIndex = treeIndices.get(i);
            }
        }

        return bestTreeIndex;
    }

    private int[] PickSeed(ArrayList<TreeIndex> treeIndices){
        int[] arrayPositions = {0, 0};
        float maxArea = 0f;

        for(int i = 0; i < treeIndices.size(); i++){
            for(int j = 0; j < treeIndices.size(); j++){
                if(i == j)
                    continue;

                float area = treeIndices.get(i).calculateArea(treeIndices.get(j));
                if(area > maxArea){
                    maxArea = area;
                    arrayPositions[0] = i;
                    arrayPositions[1] = j;
                }
            }
        }

        return arrayPositions;
    }

    private void Split(TreeIndex nonLeaf, String blockPointer, String nodePointer, Vertex[] bounds){
        int[] indeces = PickSeed(nonLeaf.getChildren());
        TreeIndex t1 = nonLeaf.getChildren().get(indeces[0]);
        TreeIndex t2 = nonLeaf.getChildren().get(indeces[1]);

        ArrayList<TreeIndex> rest = new ArrayList<>();

        for(int i = 0; i < nonLeaf.getChildren().size(); i++){
            if(i != indeces[0] && i != indeces[1]){
                rest.add(nonLeaf.getChildren().get(i));
            }
        }

        nonLeaf.clearChildren();
        nonLeaf.addChild(t1);

        TreeIndex newNonLeaf = new TreeIndex(1, dimensions);
        newNonLeaf.setBoundsToZero();
        newNonLeaf.addChild(t2);

        for(TreeIndex t: rest){
            if(nonLeaf.getDistance(t) <= newNonLeaf.getDistance(t) && nonLeaf.getChildren().size() <= TreeIndex.M / 2){
                nonLeaf.addChild(t);
            }else{
                if(newNonLeaf.getChildren().size() <= TreeIndex.M / 2){
                    newNonLeaf.addChild(t);
                }else{
                    nonLeaf.addChild(t);
                }
            }
        }

        TreeIndex leafNode = new TreeIndex(blockPointer, nodePointer, 2, dimensions);
        leafNode.setInitialBounds(bounds);

        if(nonLeaf.getDistance(leafNode) <= newNonLeaf.getDistance(leafNode) && nonLeaf.getChildren().size() <= TreeIndex.M / 2){
            nonLeaf.addChild(leafNode);
        }else{
            if(newNonLeaf.getChildren().size() <= TreeIndex.M / 2){
                newNonLeaf.addChild(leafNode);
            }else{
                nonLeaf.addChild(leafNode);
            }
        }

        TreeIndex root = null;

        for(TreeIndex t : rootIndices){
            if(t.isThisTreeIndexParent(nonLeaf)){
                root = t;
                break;
            }
        }

        // This if statement is just for empty root error (this if statement will never run)
        if(root == null){
            root = rootIndices.get(0);
        }

        if(!root.addChild(newNonLeaf)){
            int[] nonLeafIndeces = PickSeed(root.getChildren());

            ArrayList<TreeIndex> restNonLeafs = new ArrayList<>();
            for(int i = 0; i < root.getChildren().size(); i++){
                if(i != nonLeafIndeces[0] && i != nonLeafIndeces[1]){
                    restNonLeafs.add(root.getChildren().get(i));
                }
            }

            TreeIndex nonLeaf1 = root.getChildren().get(nonLeafIndeces[0]);
            TreeIndex nonLeaf2 = root.getChildren().get(nonLeafIndeces[1]);

            root.clearChildren();

            root.addChild(nonLeaf1);

            TreeIndex newRoot = new TreeIndex(0, dimensions);
            newRoot.setBoundsToZero();

            rootIndices.add(newRoot);
            newRoot.addChild(nonLeaf2);

            for(TreeIndex t: restNonLeafs){
                if(root.getDistance(t) <= newRoot.getDistance(t) && root.getChildren().size() <= TreeIndex.M / 2){
                    root.addChild(t);
                }else{
                    if(newRoot.getChildren().size() <= TreeIndex.M / 2){
                        newRoot.addChild(t);
                    }else{
                        root.addChild(t);
                    }
                }
            }

            if(root.getDistance(newNonLeaf) <= newRoot.getDistance(newNonLeaf) && root.getChildren().size() <= TreeIndex.M / 2){
                root.addChild(newNonLeaf);
            }else{
                if(newRoot.getChildren().size() <= TreeIndex.M / 2){
                    newRoot.addChild(newNonLeaf);
                }else{
                    root.addChild(newNonLeaf);
                }
            }
        }
    }

    public void printRootIndicesWithChildren(){
        int rIdx = 0;
        for(TreeIndex root: rootIndices){
            System.out.println("Root Index: " + rIdx);
            System.out.println(
                    "Root Bounds: ("
                            + root.getMinBoundValues().getCoord()[0]
                            + "," + root.getMinBoundValues().getCoord()[1]
                            + "," + root.getMaxBoundValues().getCoord()[0]
                            + "," + root.getMaxBoundValues().getCoord()[1]
                            + ")");

            System.out.println("");
            int nIdx = 0;
            for(TreeIndex nonLeaf: root.getChildren()){
                System.out.println("NonLeaf Index: " + nIdx);
                System.out.println(
                        "NonLeaf Bounds: ("
                                + nonLeaf.getMinBoundValues().getCoord()[0]
                                + "," + nonLeaf.getMinBoundValues().getCoord()[1]
                                + "," + nonLeaf.getMaxBoundValues().getCoord()[0]
                                + "," + nonLeaf.getMaxBoundValues().getCoord()[1]
                                + ")");

                System.out.println("");
                int lIdx = 0;
                for(TreeIndex leaf: nonLeaf.getChildren()){
                    System.out.println("Leaf Index: " + lIdx);
                    System.out.println(
                            "Leaf Bounds: ("
                                    + leaf.getMinBoundValues().getCoord()[0]
                                    + "," + leaf.getMinBoundValues().getCoord()[1]
                                    + "," + leaf.getMaxBoundValues().getCoord()[0]
                                    + "," + leaf.getMaxBoundValues().getCoord()[1]
                                    + ")");

                    System.out.println("");
                    lIdx++;
                }

                nIdx++;
            }

            System.out.println("==================================================================");
            System.out.println("");
            rIdx++;
        }
    }
}
