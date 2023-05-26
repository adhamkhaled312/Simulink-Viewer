package linesPackage;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

public class BuildLines {

    public static Arrow[] parse(File xmlFile) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFile);
        } catch (Exception e) {
            return null;
        }
        NodeList lines = document.getElementsByTagName("Line");
        Arrow[] lineArray = new Arrow[lines.getLength()];

        for (int i = 0; i < lines.getLength(); i++) {
            Element line = (Element) lines.item(i);  
            lineArray[i] = createLine(line, 0);
        }
        return lineArray;
    }

    public static Arrow createLine(Element line, int level) {
        int numOfBranches = 0;
        NodeList childNodes = line.getChildNodes();
        
        ArrayList<Element> pTagElements = new ArrayList<Element>();
        ArrayList<Element> branchElements = new ArrayList<Element>();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element child = (Element) childNodes.item(i);
            String tagName = child.getTagName();
            if (tagName.equals("P")) {
                pTagElements.add(child);
            } else if (tagName.equals("Branch")) {
                branchElements.add(child);
                numOfBranches++;
            }
        }

        int lineZOrder = 0;
        int[] lineSrc = null;
        int[] lineDst = null;
        int[] linePoints = null;
        for (int i = 0; i < pTagElements.size(); i++) {
            String attributeName = pTagElements.get(i).getAttribute("Name");
            String value = pTagElements.get(i).getTextContent();
            if (attributeName.equals("ZOrder")){
                lineZOrder = Integer.parseInt(value);
            } else if (attributeName.equals("Src")) {
                lineSrc = new int[2];
                String[] srcString = (value.replaceAll("out:", "")).split("#");
                lineSrc[0] = Integer.parseInt(srcString[0]);
                lineSrc[1] = Integer.parseInt(srcString[1]);
            } else if (attributeName.equals("Dst")) {
                lineDst = new int[2];
                String[] dstString = (value.replaceAll("in:", "")).split("#");
                lineDst[0] = Integer.parseInt(dstString[0]);
                lineDst[1] = Integer.parseInt(dstString[1]);
            } else if(attributeName.equals("Points")) {
                String[] pointsString = (value.replaceAll("\\[", "").replaceAll("]", "").replaceAll(" ", "")).split(";");
                linePoints = new int[pointsString.length*2];
                for (int j = 0; j < pointsString.length; j++) {
                    String[] point = pointsString[j].split(",");
                    linePoints[2*j] = Integer.parseInt(point[0]);
                    linePoints[(2*j)+1] = Integer.parseInt(point[1]);
                }
            }
        }

        if (numOfBranches == 0) {
            return new Arrow(lineZOrder, lineSrc, lineDst, linePoints, level);
        } else {
            Arrow[] branches = new Arrow[numOfBranches];
            for (int i = 0; i < numOfBranches; i++) {
                branches[i] = createLine(branchElements.get(i), level+1);
            }
            return new Arrow(lineZOrder, lineSrc, lineDst, linePoints, level, branches);
        }
    }

}