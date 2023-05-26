package linesPackage;

import javafx.scene.shape.*;

import userInterfacePackage.*;
import blocksPackage.*;

public class Arrow {
    private int zOrder;
    private int[] src = new int[2];
    private int[] dst = new int[2];
    private int[] points;
    private Arrow[] branches;
    private int level;
    private double[] srcCoordinates = new double[2];

    public Arrow(int zOrder, int[] src, int[] dst, int[] points, int level){
        this.zOrder = zOrder;
        this.src = src;
        this.dst = dst;
        this.points = points;
        this.level = level;
        this.branches = null;
    }
    public Arrow(int zOrder, int[] src, int[] dst, int[] points, int level, Arrow[] branches){
        this.zOrder = zOrder;
        this.src = src;
        this.dst = dst;
        this.points = points;
        this.level = level;
        this.branches = branches;
    }

    public int[] getDst() {
        return dst;
    }
    public int getZOrder() {
        return zOrder;
    }
    public int[] getPoints() {
        return points;
    }
    public int[] getSrc() {
        return src;
    }
    public Arrow[] getBranches() {
        return branches;
    }
    public Arrow getBranch(int i) {
        return branches[i];
    }
    public int getLevel() {
        return level;
    }

    public void print () {
        String indent = "";
        for (int i = 0; i < level; i++) {
            indent += "    ";
        }
        printData(indent);
        if (branches == null) {
            System.out.println("No Branches");
        } else {
            System.out.print(branches.length + "\n\n");
            for (int i = 0; i < branches.length; i++) {
                System.out.println(indent + "  Branch " + i);
                branches[i].print();
            }
        }
        System.out.println("");
    }

    public void printData (String indent) {
        System.out.println(indent + "ZOrder : " + zOrder);
        if (src != null) {
            System.out.println(indent + "Source : [" + src[0] + ", " + src[1] + "]");
        }
        if (points != null) {
            System.out.print(indent + "points : [");
            int i;
            for (i = 0; i < points.length-1; i++) {
                System.out.print(points[i] + ", ");
            }
            System.out.println(points[i] + "]");
        }
        if (dst != null) {
            System.out.println(indent + "Distination : [" + dst[0] + ", " + dst[1] + "]");
        }
        System.out.print(indent + "Number of Branches : ");
    }

    private static Block[] blocks;

    public static void setBlocks(Block[] myBlocks) {
        blocks = myBlocks;
    }

    public static Block getBlockById (int id) {
        for (int i = 0; i < blocks.length; i++) {
            if (id == blocks[i].getID()) {
                return blocks[i];
            }
        }
        return null;
    }

    public void draw(CustomCanvas pane, double step, double moveX, double moveY) {
        
        double[] endCoordinates = new double[2];

        if (this.src != null){
            Block srcBlock = getBlockById(this.src[0]);
            srcCoordinates = srcBlock.getPortPosition(this.src[1], "out");
            srcCoordinates[0] = (srcCoordinates[0] + moveX ) * step;
            srcCoordinates[1] = (srcCoordinates[1] + moveY ) * step;
        }

        if (this.points != null) {
            for(int i=0; i<this.points.length; i+=2){
                endCoordinates[0] = this.points[i] * step + srcCoordinates[0];
                endCoordinates[1] = this.points[i+1] * step + srcCoordinates[1];
                Line arrow = new Line(srcCoordinates[0],srcCoordinates[1],endCoordinates[0],endCoordinates[1]);
                srcCoordinates[0] = endCoordinates[0];
                srcCoordinates[1] = endCoordinates[1];
                pane.getChildren().add(arrow);
            }
        }

        if (this.dst != null){
            Block dstBlock = getBlockById(this.dst[0]);
            Polygon triangle = new Polygon();
            endCoordinates = dstBlock.getPortPosition(this.dst[1], "in");
            endCoordinates[1] = (endCoordinates[1] + moveY ) * step;
            if (dstBlock.getMirror()) {
                endCoordinates[0] = (endCoordinates[0] + 4 + moveX ) * step;
                triangle.getPoints().setAll(new Double[]{
                    endCoordinates[0], endCoordinates[1]-4*step,
                    endCoordinates[0]-4*step, endCoordinates[1],
                    endCoordinates[0], endCoordinates[1]+4*step
                });
            } else {
                endCoordinates[0] = (endCoordinates[0] - 4 + moveX ) * step;
                triangle.getPoints().setAll(new Double[]{
                    endCoordinates[0], endCoordinates[1]-4*step,
                    endCoordinates[0]+4*step, endCoordinates[1],
                    endCoordinates[0], endCoordinates[1]+4*step
                });
            }

            Line arrow = new Line(srcCoordinates[0],srcCoordinates[1],endCoordinates[0],endCoordinates[1]);            
            pane.getChildren().addAll(arrow,triangle);
        } else {
            Circle dot = new Circle(endCoordinates[0], endCoordinates[1], 2*step);
            pane.getChildren().add(dot);
            
            for (int i = 0; i < this.branches.length; i++) {
                branches[i].srcCoordinates[0] = endCoordinates[0];
                branches[i].srcCoordinates[1] = endCoordinates[1];
                branches[i].draw( pane,  step,  moveX, moveY);
            }
        }
    }
}
