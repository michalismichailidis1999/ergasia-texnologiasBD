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
//        datafileHandler = new DatafileHandler(infoBlock, blocks);
    }

    public void setInfoBlock(InfoBlock infoBlock){
        this.infoBlock = infoBlock;
    }

    public InfoBlock getInfoBlock(){ return infoBlock; }

    public Map<String, DataBlock> getBlocks() {
        return blocks;
    }
}
