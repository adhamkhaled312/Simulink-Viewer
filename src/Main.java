import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.embed.swing.*;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javafx.scene.Cursor;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import java.io.*;

import blocksPackage.*;
import linesPackage.*;
import userInterfacePackage.*;

public class Main extends Application {
    
    Stage stage = new Stage();
    VBox vbox = new VBox();
    Pane pane = new Pane();
    CustomToolBar toolBar;
    int moveX = 0;
    int moveY = 0;
    int width = 590;
    int height = 450;
    int prevMouseX = 0;
    int prevMouseY = 0;
    Block[] blocks;
    Line[] lines;
    boolean drag = false;
    int[] nums = {10,15,23,34,51,76};
    int index = 0;
    double step = nums[index];
    double pxPerStep = 10;


    public void start (Stage primaryStage)
    throws IOException, ParserConfigurationException, SAXException {
        //Image for cover of the screen
        Image coverPhoto= new Image("Images/CoverPhoto.png");
        ImageView imageView= new ImageView(coverPhoto);
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);

        //Pane for the background
        StackPane imagePane= new StackPane();
        imagePane.getChildren().add(imageView);

        //Create a button for the upload
        Button uploadButton= new Button("Upload");
        uploadButton.setPrefSize(75,75);

        //Image for the button
        ImageView ButtonCoverPhoto= new ImageView(new Image("Images/OpenSymbol.png"));
        ButtonCoverPhoto.setFitHeight(75);
        ButtonCoverPhoto.setFitWidth(75);
        uploadButton.setGraphic(ButtonCoverPhoto);
        uploadButton.setContentDisplay(ContentDisplay.TOP);


        //Label for the upload
        Label label=new Label("Upload .mdl File");
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 20));

        uploadButton.setOnAction((ActionEvent e) ->{
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".mdl", "*.mdl"));
                File inputFile = fileChooser.showOpenDialog(primaryStage);
                if (inputFile != null) {
                    System.out.println(inputFile.getAbsolutePath());
                }
                FileInputStream inputStream = new FileInputStream(inputFile);
                StringBuilder s = new StringBuilder();
                int x;
                while ((x = inputStream.read()) != -1) {
                    s.append((char) x);
                }
                String allFileContent = s.toString();
                Scanner scanner = new Scanner(allFileContent);
                
                StringBuilder systemBlock = new StringBuilder();
                FileWriter myWriter = new FileWriter("system_root.xml");
                
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.contains("<System>")) {
                        do {
                            systemBlock.append(nextLine + "\n");
                            myWriter.write(nextLine + "\n");
                            nextLine = scanner.nextLine();
                        } while (!nextLine.contains("</System>"));
						break;
                    }
                }
				systemBlock.append("</System>");
				myWriter.write("</System>");
                myWriter.close();
                scanner.close();
                inputStream.close();
                
                blocks = BuildBlocks.parse(new File("system_root.xml"));
                lines = BuildLines.parse(new File("system_root.xml"));
                for (int i = 0; i < lines.length; i++) {
                    lines[i].print();
                }
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i].print();
                }
                primaryStage.hide();
                draw();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        //Pane for the button
        VBox buttonPane=new VBox(5);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(uploadButton,label);

        //The main pane for the program
        StackPane mainPane=new StackPane();
        mainPane.getChildren().addAll(imagePane,buttonPane);

        Scene scene = new Scene (mainPane,600,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulink Viewer");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void draw () {

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        width = (int) bounds.getWidth() - 10;
        height = (int) bounds.getHeight() - 150;
        
        // create menu bar, tool bar and canvas
        CustomMenuBar menuBar = drawMenuBar();
        toolBar = drawFileBar();
        pane.setId("canvas");
        drawCanvas();
        vbox.getChildren().addAll(menuBar, toolBar, pane);
        pane.viewOrderProperty().set(10);;
        
        pane.setPrefSize(width, height);
        Scene scene = new Scene(vbox, bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add("style/stylesheet.css");
        stage.setTitle("Simulink Viewer");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
        // events on key pressed
        scene.onKeyPressedProperty().setValue((e) -> {
            if (e.getCode() == KeyCode.S && e.isControlDown()) {
            } else if (e.getCode() == KeyCode.Z && e.isControlDown()) {
                zoomIn();
            } else if (e.getCode() == KeyCode.X && e.isControlDown() && step > 2) {
                zoomOut();
            } else if (e.getCode() == KeyCode.I) {
                moveY -= 1;
                drawCanvas();
            } else if (e.getCode() == KeyCode.K) {
                moveY += 1;
                drawCanvas();
            } else if (e.getCode() == KeyCode.J) {
                moveX -= 1;
                drawCanvas();
            } else if (e.getCode() == KeyCode.L) {
                moveX += 1;
                drawCanvas();
            }
        });
        
        //events on resize of window
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            width = (int) stage.getWidth() - 20;
            drawCanvas();
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            height = (int) stage.getHeight() - 200;
            drawCanvas();
        });
        
        //events on drag
        pane.onMousePressedProperty().setValue((e) -> {
            if (e.getButton() == MouseButton.MIDDLE || drag) {
                prevMouseX = (int)e.getX();
                prevMouseY = (int)e.getY();
            }
        });
        pane.onMouseDraggedProperty().setValue((e) -> {
            if (e.getButton() == MouseButton.MIDDLE || drag) {
                int disX = (int)Math.round(((e.getX()-prevMouseX))/step);
                int disY = (int)Math.round(((e.getY()-prevMouseY))/step);
                moveX += disX;
                moveY += disY;
                drawCanvas();
                moveX -= disX;
                moveY -= disY;
            }
        });
        pane.onMouseReleasedProperty().setValue((e) -> {
            if (e.getButton() == MouseButton.MIDDLE || drag) {
                moveX += Math.round(((e.getX()-prevMouseX))/step);
                moveY += Math.round(((e.getY()-prevMouseY))/step);
                drawCanvas();
            }
        });

        pane.setOnScroll((ScrollEvent event) -> {
            if (event.getDeltaY() > 0) {
                zoomIn();
            } else if (step > 2) {
                zoomOut();
                
            }
        });
    }
    public void drawCanvas() {
        //draw all blocks
        pane.getChildren().clear();
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].draw(pane, step/pxPerStep, moveX*pxPerStep, moveY*pxPerStep);
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
        drawCanvas();
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
        drawCanvas();
    }

    public CustomMenuBar drawMenuBar() {
        String[] names = {"File", "Options", "Help"};
        CustomMenuBar menuBar = new CustomMenuBar(names);
        //events on click on menu items
        menuBar.getMenu(0).idProperty().addListener((obs, oldVal, newVal) -> {
            toolBar = drawFileBar();
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
        menuBar.getMenu(1).idProperty().addListener((obs, oldVal, newVal) -> {
            toolBar = drawOptionBar();
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
        menuBar.getMenu(2).idProperty().addListener((obs, oldVal, newVal) -> {
            toolBar = drawHelpBar();
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
        return menuBar;
    }
    public CustomToolBar drawFileBar () {
        String[] names = {"Open", "Save", "Close"};
        CustomToolBar fileBar = new CustomToolBar(names);
        fileBar.getButtonAndLabel(1).setOnMouseClicked((e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage(width + 20, height + 20);
                    pane.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) { }
            }
        });
        fileBar.getButtonAndLabel(2).setOnMouseClicked((e) -> {
            stage.hide();
        });
        return fileBar;
    }
    public CustomToolBar drawOptionBar () {
        String[] names = {"Zoom In", "Zoom Out", "Drag"};
        CustomToolBar optionBar = new CustomToolBar(names);
        optionBar.getButtonAndLabel(0).setOnMouseClicked((e) -> {
            zoomIn();
        });
        optionBar.getButtonAndLabel(1).setOnMouseClicked((e) -> {
            zoomOut();
        });
        optionBar.getButtonAndLabel(2).setOnMouseClicked((e) -> {
            if (drag) {
                pane.setCursor(Cursor.DEFAULT);
                drag = false;
                optionBar.getButtonAndLabel(2).setId("tool");
            } else {
                pane.setCursor(Cursor.MOVE);
                drag = true;
                optionBar.getButtonAndLabel(2).setId("selected");
            }
        });
        // scene.setCursor(Cursor.HAND);
        return optionBar;
    }
    public CustomToolBar drawHelpBar () {
        String[] names = {"Help"};
        CustomToolBar helpBar = new CustomToolBar(names);
        return helpBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
