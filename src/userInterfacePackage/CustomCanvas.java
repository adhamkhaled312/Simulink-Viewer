package userInterfacePackage;

import javafx.scene.input.*;
import javafx.scene.layout.*;
import java.io.File;

import javafx.scene.paint.*;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.Rectangle;


import blocksPackage.*;
import linesPackage.*;

public class CustomCanvas extends Pane {
    private Block[] blocks;
    private Arrow[] lines;
    private int moveX = 0;
    private int moveY = 0;
    private int width = 0;
    private int height = 0;
    private double step = 10;
    private double pxPerStep = 10;
    private int index = 0;
    private static int[] nums = {10,15,23,34,51,76};
    private static int prevMouseX = 0;
    private static int prevMouseY = 0;

    
    public CustomCanvas () {
    }
    public CustomCanvas (File file) {
        blocks = BuildBlocks.parse(file);
        lines = BuildLines.parse(file);
        Arrow.setBlocks(blocks);
    }


    public Block[] getBlocks() {
        return blocks;
    }
    public void setMoveX (int move) {
        moveX += move;
    }
    public void setMoveY (int move) {
        moveY += move;
    }
    public void setWidthAndHeight (int width, int height) {
        this.width = width;
        this.height = height;
        this.setPrefSize(width, height);
    }

    public void draw() {
        drawWithoutBorder();
        Rectangle rectangle = new Rectangle(width-10, height-45);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.rgb(245, 246, 247));
        rectangle.setStrokeWidth(20);
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setLayoutX(9);
        rectangle.setLayoutY(9);
        getChildren().add(rectangle);
    }
    public void drawWithoutBorder() {
        this.getChildren().clear();
        for (int i = 0; i < this.blocks.length; i++) {
            this.blocks[i].draw(this, step/pxPerStep, moveX*pxPerStep, moveY*pxPerStep);
        }
        for (int i = 0; i < this.lines.length; i++) {
            this.lines[i].draw(this, step/pxPerStep, moveX*pxPerStep, moveY*pxPerStep);
        }
    }
    public void zoomIn() {
        index++;
        double prevStep = nums[(index+5)%6];
        if (index > 5) {
            index = 0;
            pxPerStep /= 10;
            moveX *= 10;
            moveY *= 10;
            prevStep /= 10;
        }
        step = nums[index];
        moveX += Math.round(((0.5*width*(prevStep-step))/(prevStep))/step);                
        moveY += Math.round(((0.5*height*(prevStep-step))/(prevStep))/step);
        draw();
    }
    public void zoomOut() {
        index--;
        double prevStep = nums[(index+1)%6];
        if (index < 0) {
            index = 5;
            pxPerStep *= 10;
            moveX = Math.round(moveX/10);
            moveY = Math.round(moveY/10);
            prevStep *= 10;
        }
        step = nums[index];
        moveX += Math.round(((0.5*width*(prevStep-step))/(prevStep))/step);
        moveY += Math.round(((0.5*height*(prevStep-step))/(prevStep))/step);
        draw();
    }

    public void mousePressed (MouseEvent e) {
        prevMouseX = (int)e.getX();
        prevMouseY = (int)e.getY();
    }
    public void mouseDragged (MouseEvent e) {
        int disX = (int)Math.round(((e.getX()-prevMouseX))/step);
        int disY = (int)Math.round(((e.getY()-prevMouseY))/step);
        moveX += disX;
        moveY += disY;
        draw();
        moveX -= disX;
        moveY -= disY;
    }
    public void mouseReleased (MouseEvent e) {
        moveX += Math.round(((e.getX()-prevMouseX))/step);
        moveY += Math.round(((e.getY()-prevMouseY))/step);
        draw();
    }


}
