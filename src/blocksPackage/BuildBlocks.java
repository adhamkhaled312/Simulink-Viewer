package blocksPackage;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class BuildBlocks {

    public static Block[] parse (File xmlFile) {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
        } catch (Exception e) {
            return null;
        }

        NodeList blockList = document.getElementsByTagName("Block");
        Block[] blocks = new Block[blockList.getLength()];

        for (int i = 0; i < blockList.getLength(); i++) {

            Element block = (Element) blockList.item(i);
            NodeList attributeList = block.getElementsByTagName("P");
            
            String blockType = block.getAttribute("BlockType");
            String blockName = block.getAttribute("Name");
            int blockID = Integer.parseInt(block.getAttribute("SID"));
            int[] blockPosition = new int[4];
            int blockZOrder = 0;
            int[] blockPorts = new int[2];
            Boolean blockMirror = false;
            blockPorts[0] = 1;
            blockPorts[1] = 1;
            HashMap<String, String> attributes = new HashMap<String, String>();

            for (int j = 0; j < attributeList.getLength(); j++) {
                
                Element attribute = (Element) attributeList.item(j);
                String attributeName = attribute.getAttribute("Name");
                
                if (attributeName.equals("Position")) {
                    String[] positionString = (attribute.getTextContent().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "")).split(",");
                    for (int k = 0; k < 4; k++) {
                        blockPosition[k] = (int) Math.round(Integer.parseInt(positionString[k]));
                    }
                } else if (attributeName.equals("ZOrder")) {
                    blockZOrder = Integer.parseInt(attribute.getTextContent());
                } else if (attributeName.equals("Ports")) {
                    String[] portsString = (attribute.getTextContent().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "")).split(",");                    
                    for (int k = 0; k < portsString.length; k++) {
                        blockPorts[k] = Integer.parseInt(portsString[k]);
                    }
                } else if (attributeName.equals("BlockMirror") && attribute.getTextContent().equals("on")) {
                    blockMirror = true;
                } else {
                    attributes.put(attributeName, attribute.getTextContent());
                }
            }

            NodeList portsCount = block.getElementsByTagName("PortCounts");
            if (portsCount.getLength() > 0) {
                Element portsElement = (Element) portsCount.item(0);
                String port = portsElement.getAttribute("in");
                if (!port.equals("")) {
                    blockPorts[0] = Integer.parseInt(port);
                }
                port = portsElement.getAttribute("out");
                if (!port.equals("")) {
                    blockPorts[1] = Integer.parseInt(port);
                }
            }
            blocks[i] = new Block(blockType, blockName, blockID, blockPosition, blockZOrder, blockPorts, blockMirror, attributes);
        }
        return blocks;
    }
}
