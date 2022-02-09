package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Application E-commerce");

        GridPane gridpane = this.createRegistrationFormPane();
        this.addUIControls(gridpane);

        Scene scene = new Scene(gridpane, 500, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public static GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        ColumnConstraints columnOneConstraints = new ColumnConstraints();
        columnOneConstraints.setHalignment(HPos.RIGHT);


        gridPane.getColumnConstraints().addAll(columnOneConstraints);

        return gridPane;
    }

    public static void addUIControls(GridPane gridPane) {
        Label labelvalue = new Label("Max Price : ");

        TextField fieldvalue = new TextField();
        fieldvalue.setPrefHeight(30);
        fieldvalue.setPrefWidth(150);
        fieldvalue.setMaxWidth(150);


        VBox vboxvalue= new VBox(10,labelvalue,fieldvalue);
        vboxvalue.setAlignment(Pos.TOP_LEFT);



        ///////////////////////////////////////////////////
        Label labeltime = new Label("Execution Time : ");

        TextField fieldtime = new TextField();
        fieldtime.setPrefHeight(30);
        fieldtime.setPrefWidth(120);
        fieldtime.setMaxWidth(120);

        Label progress = new Label("");


        VBox vboxtime= new VBox(10,labeltime,fieldtime);
        vboxtime.setAlignment(Pos.TOP_LEFT);

        HBox hboxtime= new HBox(10, vboxtime, progress);
        hboxtime.setAlignment(Pos.BOTTOM_LEFT);


        VBox all= new VBox(10,vboxvalue,hboxtime);
        all.setAlignment(Pos.CENTER);
        all.setPadding(new Insets(10, 0, 0, 135));

        gridPane.add(all,0,1);




////////////////////////////////////////
        Stage stage= new Stage();

        FileChooser fil_chooser = new FileChooser();
        Button buttonf = new Button("Select file");
        Label labelf = new Label("no files selected");
        Label labels=new Label();
        EventHandler<ActionEvent> event =
                e -> {

                    File file = fil_chooser.showOpenDialog(stage);
                    if (file != null) {

                        labelf.setText(file.getName()
                                + "  selected");
                        labels.setText(file.getAbsolutePath());
                    }
                };

        buttonf.setOnAction(event);

        VBox vboxf = new VBox(10, buttonf, labelf);
        vboxf.setAlignment(Pos.CENTER);

        gridPane.add(vboxf,0,0);

        /////////////////////////////////////////

        Button buttonexe= new Button("Execute");
        buttonexe.setPrefHeight(35);
        buttonexe.setMaxHeight(35);

        Button buttonstop= new Button("Stop");
        buttonstop.setPrefHeight(35);
        buttonstop.setMaxHeight(35);

        HBox hboxbutton = new HBox(10, buttonexe, buttonstop);
        hboxbutton.setPadding(new Insets(10, 0, 0, 0));
        hboxbutton.setAlignment(Pos.CENTER);

        gridPane.add(hboxbutton,0,3);

        TextField sommetField = new TextField();
        sommetField.setPrefHeight(30);
        sommetField.setMaxHeight(30);
        sommetField.setPrefWidth(420);
        sommetField.setMaxWidth(420);

        gridPane.add(sommetField,0,5);


        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Integer random = ThreadLocalRandom.current().nextInt(10);

            Platform.runLater(() -> {
                /*switch (progress.getText().length()) {
                    case 0:
                        progress.setText(".");
                    case 1:
                        progress.setText(". .");
                    case 3:
                        progress.setText(". . .");
                    case 5:
                        progress.setText("");
                }*/
                if(!EcommMain.off)
                    EcommMain.p = labels.getText();

                if(EcommMain.off)
                    if(progress.getText().length()<5)
                        progress.setText(progress.getText()+".");
                    else
                        progress.setText("");
                else
                    progress.setText("");

            });
        }, 0, 700, TimeUnit.MILLISECONDS);

        System.out.println(labels.getText()+" labels");
        final EcommMain[] userThread = {new EcommMain(fieldvalue, fieldtime, labels.getText(), sommetField)};
        final Thread[] thread1 = {new Thread(userThread[0])};
        EventHandler<ActionEvent> eventExe =
                e -> {

                    fieldtime.setText("");

                    if(labels.getText()!="") {
                        userThread[0] = new EcommMain(fieldvalue,fieldtime,labels.getText(), sommetField);
                        thread1[0] = new Thread(userThread[0]);
                        thread1[0].start();
                    }
                    else
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),"FileNotFoundException", "Please select a file and try again");

                };
        buttonexe.setOnAction(eventExe);

        EventHandler<ActionEvent> eventStop =
                e -> {

                    fieldtime.setText("");

                    if(labels.getText()!="") {
                        userThread[0].stop();
                        thread1[0].stop();
                    }
                    else
                        showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(),"FileNotFoundException", "Please select a file and try again");

                };
        buttonstop.setOnAction(eventStop);



    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}