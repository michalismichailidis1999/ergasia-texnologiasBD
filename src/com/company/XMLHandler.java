package com.company;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;

public class XMLHandler {
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    private final String filePath = "src/com/company/map.xml";
    private File file;

    public XMLHandler(){
        try{
            file = new File(filePath);

            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();

            builder.parse(file);

            document = builder.parse(file);

        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
    }

    public void saveNodesToDB(DatabaseHandler databaseHandler){
        NodeList boundsList = document.getElementsByTagName("bounds");
        Element bounds = (Element) boundsList.item(0);

        float minlat = Float.parseFloat(bounds.getAttribute("minlat"));
        float minlon = Float.parseFloat(bounds.getAttribute("minlon"));
        float maxlat = Float.parseFloat(bounds.getAttribute("maxlat"));
        float maxlon = Float.parseFloat(bounds.getAttribute("maxlon"));

        NodeList nodeList = document.getElementsByTagName("node");

        int totalNodes = nodeList.getLength();

        int currentBlock = 1;
        String blockId = "block_" + currentBlock;

        databaseHandler.getBlocks().put(blockId, new DataBlock());

        for(int i = 0; i < totalNodes; i++){
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element nodeElement = (Element) node;

                String id = nodeElement.getAttribute("id");
                String name = nodeElement.getAttribute("user");
                float lat = Float.parseFloat(nodeElement.getAttribute("lat"));
                float lon = Float.parseFloat(nodeElement.getAttribute("lon"));

                NodeData nodeData = new NodeData(id, name, 2);

                Vertex[] rectBounds = new Vertex[2];

                float[] point1 = new float[2];
                point1[0] = lat;
                point1[1] = lat;

                Vertex bound1 = new Vertex(2);
                bound1.setCoord(point1);

                float[] point2 = new float[2];
                point2[0] = lon;
                point2[1] = lon;

                Vertex bound2 = new Vertex(2);
                bound1.setCoord(point2);

                rectBounds[0] = bound1;
                rectBounds[1] = bound2;

                nodeData.setBounds(rectBounds);

                boolean isAdded = databaseHandler.getBlocks().get(blockId).addData(nodeData);

                if(!isAdded){
                    currentBlock++;
                    blockId = "block_" + currentBlock;
                    databaseHandler.getBlocks().put(blockId, new DataBlock());
                }
            }
        }

        int totalBlocks = databaseHandler.getBlocks().size();

        databaseHandler.setInfoBlock(new InfoBlock(minlat, minlon, maxlat, maxlon, totalNodes, totalBlocks));
    }
}
