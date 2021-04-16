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

    // Change this if you want different dimensions
    private final int dimensions = 2;

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

                TreeNode treeNode = new TreeNode(id, name);

                Vertex[] coords = new Vertex[dimensions];

                float[] axis = new float[dimensions];
                axis[0] = lat;
                axis[1] = lon;

                Vertex coord1 = new Vertex(dimensions);
                coord1.setAxis(axis);

                coords[0] = coord1;
                coords[1] = coord1;

                treeNode.setCoords(coords);

                boolean isAdded = databaseHandler.getBlocks().get(blockId).addNode(treeNode);

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
