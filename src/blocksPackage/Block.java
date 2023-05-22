package blocksPackage;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.*;

import javafx.scene.canvas.GraphicsContext;

public class Block {
    private String blockType;
    private String name;
    private int id;
    private int[] position = new int[4];
    private int zOrder;
    private HashMap<String, String> attributes;

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

    public String getBlockType() {
        return blockType;
    }
    public String getName() {
        return name;
    }
    public int getID() {
        return id;
    }
    public int[] getPosition() {
        return position;
    }
    public int getZOrder() {
        return zOrder;
    }
    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setPosition(int[] position) {
        this.position = position;
    }
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }
    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public void print() {
        System.out.println("block type = " + blockType + ", name = " + name + ", ID = " + id + ", position = [" + position[0] + ", " + position[1] + ", " + position[2] + ", " + position[3] + "], zOrder = " + zOrder);
        System.out.println("attributes: " + attributes.toString() + "\n");
    }

    public void draw(GraphicsContext gc, int step, int moveX, int moveY) {
        Image photo= new Image("Images/Blocks/" + blockType + ".png");
        gc.setStroke(Color.BLACK);
        gc.strokeRect((position[0]+moveX)*step,
                      (position[1]+moveY)*step,
                      (position[2]-position[0])*step,
                      (position[3]-position[1])*step);
        gc.drawImage(photo, (position[0]+moveX)*step,
                            (position[1]+moveY)*step,
                            (position[2]-position[0])*step,
                            (position[3]-position[1])*step);
    }
}
