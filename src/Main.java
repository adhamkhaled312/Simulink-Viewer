
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;

import java.util.Scanner;

public class Main  extends Application {
    public void start (Stage primaryStage){
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
        ImageView ButtonCoverPhoto= new ImageView(new Image("Images/UploadSymbol.png"));
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
                StringBuilder systemBlock = new StringBuilder();
                Scanner scanner = new Scanner(allFileContent);
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    if (nextLine.contains("<System>")) {
                        do {
                            systemBlock.append(nextLine);
                            systemBlock.append("\n");
                            nextLine = scanner.nextLine();
                        } while (!nextLine.contains("</System>"));
                    }
                }
                systemBlock.append("</System>");
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
    public static void main(String[] args) {
        launch(args);
    }
}