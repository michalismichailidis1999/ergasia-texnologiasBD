package com.company;

import java.io.*;
import java.util.Map;

public class DatafileHandler {
    private final String filePath = "src/com/company/datafile";
    private File file;
    private BufferedWriter writer;
    private boolean fileExists;

    public DatafileHandler(InfoBlock infoBlock, Map<String, DataBlock> blocks){
        this.file = new File(filePath);

        if(!file.exists()){
            fileExists = false;
        }else{
            fileExists = true;
        }

        writeToFile(infoBlock, blocks);
    }

    private void writeToFile(InfoBlock infoBlock, Map<String, DataBlock> blocks){
        if(!fileExists){
            return;
        }

        try {
            writer = new BufferedWriter(new FileWriter(file));

            writer.write(infoBlock.getId() + "\n");
            writer.write("minlat=" + infoBlock.getMinlat() + ",");
            writer.write("minlon=" + infoBlock.getMinlon() + ",");
            writer.write("maxlat=" + infoBlock.getMaxlat() + ",");
            writer.write("maxlon=" + infoBlock.getMaxlon() + ",");
            writer.write("total_nodes=" + infoBlock.getTotalNodes() + ",");
            writer.write("total_blocks=" + infoBlock.getTotalBlocks() + "\n");

            for(Map.Entry<String, DataBlock> blockEntry: blocks.entrySet()){
                writer.write("\n" + blockEntry.getKey() + "\n");

                for(TreeNode node: blockEntry.getValue().getNodes()){
                    writer.write(node.getId() + ",");
                    writer.write(node.getName());

                    for(Point point: node.getCoords()){
                        writer.write(",");
                        float[] coord = point.getCoord();

                        String tuple = "(";

                        for(int i = 0; i < coord.length; i++){
                            tuple += coord[i];

                            if(i == coord.length - 1){
                                tuple += ")";
                            }else{
                                tuple += ",";
                            }
                        }

                        writer.write(tuple);
                    }

                    writer.write("\n");
                }
            }

            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
