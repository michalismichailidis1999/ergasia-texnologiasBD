package com.company;

import java.util.LinkedHashMap;
import java.util.Map;

public class DummyData {
    private final int dimensions;
    private final Map<String, DataBlock> blocks;

    public Map<String, DataBlock> getBlocks() {
        return blocks;
    }

    public DummyData(int dimensions){
        this.dimensions = dimensions;
        this.blocks = new LinkedHashMap<>();

        blocks.put("block_1", new DataBlock());



        float[][][] points = {
                {
                        {1, 2}, {2, 4}
                },
                {
                        {3, 4}, {4, 5}
                },
                {
                        {5, 1}, {6, 2}
                },
                {
                        {6, 2}, {7, 3}
                },
                {
                        {8, 2}, {9, 4}
                },
                {
                        {10, 4}, {11, 5}
                },
                {
                        {12, 1}, {13, 2}
                },
                {
                        {13, 2}, {14, 3}
                }
        };

        for(int i = 0; i < points.length; i++){
            NodeData node = new NodeData("1", "node" + (i + 1), dimensions);

            Vertex[] bounds = new Vertex[dimensions];
            bounds[0] = new Vertex(dimensions);
            bounds[0].setCoord(points[i][0]);
            bounds[1] = new Vertex(dimensions);
            bounds[1].setCoord(points[i][1]);

            node.setBounds(bounds);

            blocks.get("block_1").addData(node);
        }
    }
}
