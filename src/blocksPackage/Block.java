package blocksPackage;

import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import java.util.*;
import javafx.geometry.Pos;


public class Block {
    private String blockType;
    private String name;
    private int id;
    private int[] position = new int[4];
    private int zOrder;
    private HashMap<String, String> attributes;
    private Rectangle rectangle;

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

    public void draw(Pane pane, double step, double moveX, double moveY) {

        //Create a rectangle
        rectangle = new Rectangle((position[2]-position[0])*step, (position[3]-position[1])*step);

        // Load the image for the block background
        Image blockImage;
        try {
            blockImage = new Image("Images/Blocks/" + this.blockType + ".png");
        } catch (Exception e) {
            blockImage = new Image("Images/Blocks/Error.png");
        }

        ImagePattern imagePattern = new ImagePattern(blockImage);
        rectangle.setFill(imagePattern);
        
        // Create a label for the block name
        Label nameLabel = new Label(this.name);
        nameLabel.setId("block-name");
        nameLabel.setFont(new Font("Ariel", 10*step));
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX((position[0]+moveX)*step);
        vbox.setLayoutY((position[1]+moveY)*step);

        // Create a pane to hold the block image and label
        vbox.getChildren().addAll(rectangle, nameLabel);
        pane.getChildren().add(vbox);
        
    }
}
