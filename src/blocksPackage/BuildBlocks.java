package blocksPackage;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;

public class BuildBlocks {

    public static Block[] parse (File xmlFile)
    throws IOException, ParserConfigurationException, SAXException {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);

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
            HashMap<String, String> attributes = new HashMap<String, String>();

            for (int j = 0; j < attributeList.getLength(); j++) {
                
                Element attribute = (Element) attributeList.item(j);
                String attributeName = attribute.getAttribute("Name");
                
                if (attributeName.equals("Position")) {
                    String[] positionString = (attribute.getTextContent().replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "")).split(",");
                    for (int k = 0; k < 4; k++) {
                        blockPosition[k] = (int) Math.round(0.1*Integer.parseInt(positionString[k]));
                    }
                } else if (attributeName.equals("ZOrder")) {
                    blockZOrder = Integer.parseInt(attribute.getTextContent());
                } else {
                    attributes.put(attributeName, attribute.getTextContent());
                }
            }
            blocks[i] = new Block(blockType, blockName, blockID, blockPosition, blockZOrder, attributes);
        }
        return blocks;
    }
}
