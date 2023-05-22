import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import java.io.*;

import blocksPackage.*;
import userInterfacePackage.*;

public class Main extends Application {
    
    Stage stage = new Stage();
    VBox vbox = new VBox();
    StackPane pane = new StackPane();
    CustomToolBar toolBar;
    int step = 5;
    int moveX = 0;
    int moveY = 0;
    int width = 590;
    int height = 450;
    int prevMouseX = 0;
    int prevMouseY = 0;
    Block[] blocks;

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

    public void draw (){

        // create menu bar, tool bar and canvas
        CustomMenuBar menuBar = drawMenuBar();
        toolBar = drawFileBar();
        drawCanvas();
        vbox.getChildren().addAll(menuBar, toolBar, pane);
        
        Scene scene = new Scene(vbox, 600, 600);
        scene.getStylesheets().add("style/stylesheet.css");
        stage.setTitle("Simulink");
        stage.setScene(scene);
        stage.show();
        
        // events on key pressed
        scene.onKeyPressedProperty().setValue((e) -> {
            if (e.getCode() == KeyCode.S && e.isControlDown()) {
            } else if (e.getCode() == KeyCode.Z && e.isControlDown()) {
                step += 1;
                moveX -= Math.round(((0.5*width)/(step-1))/step);                
                moveY -= Math.round(((0.5*height)/(step-1))/step);
                drawCanvas();
            } else if (e.getCode() == KeyCode.X && e.isControlDown() && step > 2) {
                step -= 1;
                moveX += Math.round(((0.5*width)/(step+1))/step);
                moveY += Math.round(((0.5*height)/(step+1))/step);
                drawCanvas();
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
            prevMouseX = (int)e.getX();
            prevMouseY = (int)e.getY();
        });
        pane.onMouseDraggedProperty().setValue((e) -> {
            int disX = (int)Math.round(((e.getX()-prevMouseX))/step);
            int disY = (int)Math.round(((e.getY()-prevMouseY))/step);
            moveX += disX;
            moveY += disY;
            drawCanvas();
            moveX -= disX;
            moveY -= disY;
        });
        pane.onMouseReleasedProperty().setValue((e) -> {
            moveX += Math.round(((e.getX()-prevMouseX))/step);
            moveY += Math.round(((e.getY()-prevMouseY))/step);
            drawCanvas();
        });
        
        //events on click of tool bar buttons
        toolBar.getButtonAndLabel(2).getButton().onMouseClickedProperty().setValue((e) -> {
            stage.hide();
        });;
    }

    public void drawCanvas() {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        pane.getChildren().clear();
        pane.getChildren().add(canvas);
        pane.setId("canvas");

        //draw all blocks
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].draw(gc, step, moveX, moveY);
        }

        //draw dots in background
        gc.setFill(Color.BLACK);
        for (int j = 0; j < height; j += 2*step) {
            for (int i = 0; i < width; i += 2*step) {
                gc.fillRect(i,j,1,1);
            }
        }
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
        return fileBar;
    }
    public CustomToolBar drawOptionBar () {
        String[] names = {"Zoom In", "Zoom Out"};
        CustomToolBar optionBar = new CustomToolBar(names);
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
