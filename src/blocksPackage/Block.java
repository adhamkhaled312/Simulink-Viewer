package blocksPackage;

import java.util.*;

public class Block {
    private String blockType;
    private String name;
    private int id;
    private int[] position = new int[4];
    private int zOrder;
    HashMap<String, String> attributes;
    
    Block () {
    }

    Block (String blockType, String name, int id, int[] position, int zOrder, HashMap<String, String> attributes) {
        this.blockType = blockType;
        this.name = name;
        this.id = id;
        this.position = position;
        this.zOrder = zOrder;
        this.attributes = attributes;
    }

    public void print() {
        System.out.println("block type = " + blockType + ", name = " + name + ", ID = " + id + ", position = [" + position[0] + ", " + position[1] + ", " + position[2] + ", " + position[3] + "], zOrder = " + zOrder);
        System.out.println("attributes: " + attributes.toString() + "\n");
    }
}

