package com.company;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseHandler {
    private InfoBlock infoBlock;
    private Map<String, DataBlock> blocks;
    private XMLHandler xmlHandler;
    private DatafileHandler datafileHandler;

    public DatabaseHandler() {
        blocks = new LinkedHashMap<>();
        xmlHandler = new XMLHandler();

        xmlHandler.saveNodesToDB(this);
        // For the datafileHandler to work just create a text file with name datafile datafile
        // and uncomment the line bellow if you want to append the nodes in a text file also
//        datafileHandler = new DatafileHandler("/src/com/company/datafile", infoBlock, blocks);
    }

    public void setInfoBlock(InfoBlock infoBlock){
        this.infoBlock = infoBlock;
    }

    public InfoBlock getInfoBlock(){ return infoBlock; }

    public Map<String, DataBlock> getBlocks() {
        return blocks;
    }
}
