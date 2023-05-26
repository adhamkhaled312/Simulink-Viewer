import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.embed.swing.*;
import java.util.Scanner;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import javafx.scene.Cursor;
import java.io.*;
import java.util.*;

import userInterfacePackage.*;
import linesPackage.*;

public class Main extends Application {
    
    static Stage stage = new Stage();
    static VBox vbox = new VBox();
    static CustomCanvas pane = new CustomCanvas();
    static ArrayList<CustomCanvas> panes = new ArrayList<CustomCanvas>();
    static HashMap<String, Integer> titles = new HashMap<String, Integer>();
    static CustomMenuBar menuBar = new CustomMenuBar();
    static CustomMenuBar tapsBar = new CustomMenuBar();
    static CustomToolBar toolBar;
    static int currentPane = 0;
    static int width = 0;
    static int height = 0;
    static boolean drag = false;

    @Override
    public void start (Stage primaryStage) {
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
            openFile(primaryStage);
            primaryStage.hide();
            drawWindow();
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
        primaryStage.getIcons().add(new Image("Images/Icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void drawWindow () {

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        width = (int) bounds.getWidth() - 10;
        height = (int) bounds.getHeight() - 150;
        
        // create menu bar, tool bar and canvas
        drawMenuBar();
        drawTaps();
        drawFileBar();
        pane.setWidthAndHeight(width, height);
        pane.draw();
        tapsBar.getMenu(0).show();
        vbox.getChildren().addAll(menuBar, toolBar, tapsBar, pane);
        
        Scene scene = new Scene(vbox, 600, 600);
        scene.getStylesheets().add("style/stylesheet.css");
        stage.setTitle("Simulink Viewer");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
        // events on key pressed
        scene.onKeyPressedProperty().setValue((e) -> {
            if (e.getCode() == KeyCode.S && e.isControlDown()) {
            } else if (e.getCode() == KeyCode.Z && e.isControlDown()) {
                pane.zoomIn();
            } else if (e.getCode() == KeyCode.X && e.isControlDown()) {
                pane.zoomOut();
            } else if (e.getCode() == KeyCode.I) {
                pane.setMoveY(-1);
                pane.draw();
            } else if (e.getCode() == KeyCode.K) {
                pane.setMoveY(1);
                pane.draw();
            } else if (e.getCode() == KeyCode.J) {
                pane.setMoveX(-1);
                pane.draw();
            } else if (e.getCode() == KeyCode.L) {
                pane.setMoveX(1);
                pane.draw();
            }
        });
        
        //events on resize of window
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            width = (int) stage.getWidth() - 20;
            pane.setWidthAndHeight(width, height);
            pane.draw();
        });
        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            height = (int) stage.getHeight() - 165;
            pane.setWidthAndHeight(width, height);
            pane.draw();
        });
        
    }

    public void drawMenuBar() {
        String[] names = {"File", "Options", "Help"};
        menuBar = new CustomMenuBar(names);
        //events on click on menu items
        menuBar.getMenu(0).idProperty().addListener((obs, oldVal, newVal) -> {
            drawFileBar();
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
        menuBar.getMenu(1).idProperty().addListener((obs, oldVal, newVal) -> {
            drawOptionBar();
            if (drag) {
                toolBar.getButtonAndLabel(2).setId("selected");
            } else {
                toolBar.getButtonAndLabel(2).setId("tool");
            }
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
        menuBar.getMenu(2).idProperty().addListener((obs, oldVal, newVal) -> {
            drawHelpBar();
            vbox.getChildren().remove(1);
            vbox.getChildren().add(1, toolBar);
        });
    }
    public void drawTaps() {
        for (int i = 0; i < tapsBar.size(); i++) {
            final int ii = i;
            tapsBar.getMenu(i).idProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    currentPane = ii;
                    pane = panes.get(currentPane);
                    Arrow.setBlocks(pane.getBlocks());
                    vbox.getChildren().remove(3);
                    vbox.getChildren().add(3, pane);
                    pane.draw();
                } catch (Exception e) {}
            });
        }
    }
    
    public void drawFileBar () {
        String[] names = {"Open", "Save", "Close"};
        toolBar = new CustomToolBar(names);
        toolBar.getButtonAndLabel(0).setOnMouseClicked((e) -> {
            openFile(stage);
            drawTaps();
            vbox.getChildren().remove(2);
            vbox.getChildren().add(2, tapsBar);
            tapsBar.getMenu(tapsBar.size()-1).show();
        });
        toolBar.getButtonAndLabel(1).setOnMouseClicked((e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage(width + 20, height + 20);
                    pane.drawWithoutBorder();
                    pane.snapshot(null, writableImage);
                    pane.draw();
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) { }
            }
        });
        toolBar.getButtonAndLabel(2).setOnMouseClicked((e) -> {
            tapsBar.removeMenu(currentPane);
            panes.remove(currentPane);
            if (tapsBar.size() == 0) {
                stage.hide();
            } else {
                drawTaps();
                tapsBar.getMenu(0).show();
            }
        });
    }
    public void drawOptionBar () {
        String[] names = {"Zoom In", "Zoom Out", "Drag"};
        toolBar = new CustomToolBar(names);
        toolBar.getButtonAndLabel(0).setOnMouseClicked((e) -> {
            pane.zoomIn();
        });
        toolBar.getButtonAndLabel(1).setOnMouseClicked((e) -> {
            pane.zoomOut();
        });
        toolBar.getButtonAndLabel(2).setOnMouseClicked((e) -> {
            if (drag) {
                for (int i = 0; i < panes.size(); i++) {
                    panes.get(i).setCursor(Cursor.DEFAULT);
                }
                drag = false;
                toolBar.getButtonAndLabel(2).setId("tool");
            } else {
                for (int i = 0; i < panes.size(); i++) {
                    panes.get(i).setCursor(Cursor.MOVE);
                }drag = true;
                toolBar.getButtonAndLabel(2).setId("selected");
            }
        });
    }
    public void drawHelpBar () {
        String[] names = {"Help"};
        toolBar = new CustomToolBar(names);
    }

    public void openFile (Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".mdl", "*.mdl"));
            File inputFile = fileChooser.showOpenDialog(stage);
            
            String fileName = inputFile.getName().substring(0, inputFile.getName().length()-4);
            FileInputStream inputStream = new FileInputStream(inputFile);
            StringBuilder s = new StringBuilder();
            int x;
            while ((x = inputStream.read()) != -1) {
                s.append((char) x);
            }
            String allFileContent = s.toString();
            Scanner scanner = new Scanner(allFileContent);
            
            FileWriter myWriter = new FileWriter(fileName + ".xml");
            
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.contains("MWOPC_PART_BEGIN") && nextLine.contains("systems/system_root.xml")) {
                    do {
                        nextLine = scanner.nextLine();
                        myWriter.write(nextLine + "\n");
                    } while (!nextLine.contains("</System>"));
                    break;
                }
            }
            myWriter.close();
            scanner.close();
            inputStream.close();
            
            pane = new CustomCanvas(new File(fileName + ".xml"));
            panes.add(pane);
            currentPane = panes.size()-1;
            if (titles.containsKey(fileName)) {
                titles.put(fileName, titles.get(fileName) +1);
                fileName += " (" + titles.get(fileName) + ")";
            } else {
                titles.put(fileName, 1);
            }
            tapsBar.addMenu(fileName);

            //pane.viewOrderProperty().set(10);
            pane.setId("canvas");
            pane.setWidthAndHeight(width, height);
            
            //events on drag
            pane.onMousePressedProperty().setValue((e) -> {
                if (e.getButton() == MouseButton.MIDDLE || drag) {
                    pane.mousePressed(e);
                }
            });
            pane.onMouseDraggedProperty().setValue((e) -> {
                if (e.getButton() == MouseButton.MIDDLE || drag) {
                    pane.mouseDragged(e);
                }
            });
            pane.onMouseReleasedProperty().setValue((e) -> {
                if (e.getButton() == MouseButton.MIDDLE || drag) {
                    pane.mouseReleased(e);
                }
            });

            pane.setOnScroll((ScrollEvent event) -> {
                if (event.getDeltaY() > 0) {
                    pane.zoomIn();
                } else {
                    pane.zoomOut();
                }
            });

        }
        catch (Exception ex) {
        }
    }
    public static void main (String[] args) {
        launch(args);
    }
}