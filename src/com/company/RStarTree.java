package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

class SortByDistance implements Comparator<ArrayList<Double>>{
    @Override
    public int compare(ArrayList<Double> a , ArrayList<Double> b){
        return a.get(0).compareTo(b.get(0));
    }
}

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


    public Vertex[] knn(int n,Vertex point){
        ArrayList<TreeIndex> nearestIndices = new ArrayList<TreeIndex>(n);
        TreeIndex origin = new TreeIndex(-1,2);
        for(TreeIndex root : rootIndices){
            origin.addChild(root);
        }
        scaleAndFind(origin,nearestIndices,point,n);
        Vertex[] nearestNeighbours = new Vertex[n];
        for(int i = 0 ; i < nearestIndices.size() ; i++){
            nearestNeighbours[i] = new Vertex(2);
            float[] coords = nearestIndices.get(i).getMaxBoundValues().getCoord();
            nearestNeighbours[i].setCoord(coords);
        }
        return nearestNeighbours;
    }

    private void scaleAndFind(TreeIndex current, ArrayList<TreeIndex> closest,Vertex point, int n){

        /**
         * If children ArrayList exists,then we have a non-leaf Node.
         */
        if(current.getChildren().size() != 0){
            int childCount = current.getChildren().size();
            ArrayList<TreeIndex> children = current.getChildren();
            ArrayList<ArrayList<Double>> distances = new ArrayList<>();
            /**
             * Storing distance for every child of the current Node.
             */
            for(int i = 0 ; i < childCount ; i++){
                ArrayList<Double> pair = new ArrayList<>();
                pair.add(children.get(i).getDistanceFromEdge(point));
                pair.add((double)i);
                distances.add(pair);
            }
            /**
             * Sorting Distances,while storing the original Indices.
             */
            Collections.sort(distances,new SortByDistance());
            /**
             * Loop through every child,in the order they are sorted.
             * If our nearest-neighbours array is full,check to see if we can ignore this branch.
             */
            for(int i = 0 ; i < childCount ; i++) {
                TreeIndex child = current.getChildren().get(distances.get(i).get(1).intValue());
                if (closest.size() == n) {
                    if (child.getDistanceFromEdge(point) < closest.get(closest.size() - 1).getDistanceFromEdge(point)) {
                        scaleAndFind(child, closest, point, n);
                    }
                } else {
                    scaleAndFind(child, closest, point, n);
                }
            }
            /**
             * ArrayList Children doesn't exist,so we have a leaf Node.
              */
        } else {
            if(closest.size() == 0){
                closest.add(current);
            } else {
                int i;
                for(i = 0 ; i < closest.size() ; i++){
                    if(current.getDistanceFromEdge(point) < closest.get(i).getDistanceFromEdge(point)){
                        break;
                    }
                }
                if(i == closest.size() ) {
                    closest.add(current);
                } else {
                    for(int j = closest.size() - 2 ; j >= i ; j--){
                        closest.set(j+1,closest.get(j));
                    }
                    closest.set(i,current);
                }
            }

        }
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

    public ArrayList<TreeIndex> rangeQuery(Vertex[] bounds){
        ArrayList<TreeIndex> roots = new ArrayList<>();

        TreeIndex treeIndex = new TreeIndex(0, dimensions);
        treeIndex.setInitialBounds(bounds);

        for(TreeIndex root: rootIndices){
            if(root.isCollidingWith(treeIndex)){
                roots.add(root);
            }
        }

        ArrayList<TreeIndex> nonLeafs = new ArrayList<>();

        for(TreeIndex root: roots){
            for(TreeIndex nonLeaf: root.getChildren()){
                if(nonLeaf.isCollidingWith(treeIndex)){
                    nonLeafs.add(nonLeaf);
                }
            }
        }

        ArrayList<TreeIndex> leafs = new ArrayList<>();

        for(TreeIndex nonLeaf: nonLeafs){
            for(TreeIndex leaf: nonLeaf.getChildren()){
                if(leaf.isCollidingWith(treeIndex)){
                    leafs.add(leaf);
                }
            }
        }

        return leafs;
    }
}
