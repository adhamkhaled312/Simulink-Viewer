package linesPackage;

import blocksPackage.*;

public class Line {
    private int zOrder;
    private int[] src = new int[2];
    private int[] dst = new int[2];
    private int[] points;
    private Line[] branches;
    private int level;

    public Line(int zOrder, int[] src, int[] dst, int[] points, int level){
        this.zOrder = zOrder;
        this.src = src;
        this.dst = dst;
        this.points = points;
        this.level = level;
        this.branches = null;
    }
    public Line(int zOrder, int[] src, int[] dst, int[] points, int level, Line[] branches){
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
    public Line[] getBranches() {
        return branches;
    }
    public Line getBranch(int i) {
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

}
