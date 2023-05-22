import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public void draw(Pane mainPane,Stage primaryStage){
        final int WINDOW_WIDTH = 1200;
        final int WINDOW_HEIGHT = 600;

        // Parse the input to get block details
        /*String input = "block type = Sum, name = Add, ID = 3, position = [1040, 209, 1070, 241], zOrder = 3\n" +
                "attributes: {Ports=[3, 1], IconShape=rectangular, Inputs=+++}";*/
        /*String input = "block type = Sum, name = Add, ID = 3, position = [1040, 209, 1070, 241], zOrder = 3\n" +
                "attributes: {Ports=[3, 1], IconShape=rectangular, Inputs=+++}";
        int i=0;
        String type = input.substring(input.indexOf('=',i)+2,input.indexOf(',',i));
        i=input.indexOf(',',i)+1;
        String name = input.substring(input.indexOf('=',i)+2,input.indexOf(',',i));
        i=input.indexOf(',',i)+1;
        int ID = Integer.parseInt(input.substring(input.indexOf('=',i)+2,input.indexOf(',',i)));
        i=input.indexOf(',',i)+1;
        String position = input.substring(input.indexOf('[',i)+1,input.indexOf(']',i));
        i=input.indexOf(']',i)+1;
        int zOrder = Integer.parseInt(input.substring(input.indexOf('=',i)+2,input.indexOf("attributes")-1));

        i=0;
        int x1 = Integer.parseInt(position.substring(0,position.indexOf(',',i)));
        i=position.indexOf(',',i)+1;
        int y1 = Integer.parseInt(position.substring(i+1,position.indexOf(',',i)));
        i=position.indexOf(',',i)+1;
        int x2 = Integer.parseInt(position.substring(i+1,position.indexOf(',',i)));
        i=position.indexOf(',',i)+1;
        int y2 = Integer.parseInt(position.substring(i+1,position.length()));*/

        //Create a rectangle
        double rectangleWidth = position[2]-position[0];
        double rectangleHeight = position[3]-position[1];
        double borderWidth = 1;

        Rectangle rectangle = new Rectangle(rectangleWidth, rectangleHeight);
        rectangle.setFill(Color.BLACK);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(borderWidth);
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setLayoutX(position[0]-700);
        rectangle.setLayoutY(position[1]);
        rectangle.setWidth(rectangleWidth);
        rectangle.setHeight(rectangleHeight);

        // Load the image for the block background
        Image blockImage;
        try {
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\" + this.blockType + ".png");
        }catch(Exception e){
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Error.png");
        }
        /*if(this.blockType.equals("Sum"))
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Sum.png");
        else if(this.blockType.equals("Constant"))
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Constant.png");
        else if(this.blockType.equals("Saturate"))
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Saturate.png");
        else if(this.blockType.equals("Scope"))
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Scope.png");
        else if(this.blockType.equals("UnitDelay"))
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\UnitDelay.png");
        else
            blockImage = new Image("C:\\Users\\aliom\\OneDrive\\Pictures\\Saved Pictures\\Error.png");*/

        // Calculate the image size to be slightly smaller than the rectangle

        // Calculate the image size to be slightly smaller than the rectangle
        /*double imageWidth = rectangleWidth - (5 * borderWidth);
        double imageHeight = rectangleHeight - (5 * borderWidth);*/

        ImagePattern imagePattern = new ImagePattern(blockImage);
        rectangle.setFill(imagePattern);

        // Create an ImageView for the block background
        /*ImageView blockImageView = new ImageView(blockImage);
        blockImageView.setFitWidth(x2 - x1);
        blockImageView.setFitHeight(y2 - y1);
        blockImageView.setLayoutX(x1/2);
        blockImageView.setLayoutY(y1);*/

        // Create a label for the block name
        Label nameLabel = new Label(this.name);
        nameLabel.setLayoutX(position[0]-700);
        nameLabel.setLayoutY(position[3] + 5);
        nameLabel.setTextFill(Color.BLUE);

        // Create a pane to hold the block image and label
        mainPane.getChildren().addAll(rectangle, nameLabel);
        mainPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainPane.setPadding(new Insets(10));

        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double widthRatio = newWidth.doubleValue() / WINDOW_WIDTH;
            double newX = (position[0]-700) * widthRatio;
            rectangle.setLayoutX(newX);
            nameLabel.setLayoutX(newX);
        });
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double heightRatio = newHeight.doubleValue() / WINDOW_HEIGHT;
            double newY = position[1] * heightRatio;
            rectangle.setLayoutY(newY);
            nameLabel.setLayoutY(newY + (position[3]-position[1]) + 5);
        });

        /*Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Block Viewer");
        primaryStage.show();*/
    }
}
