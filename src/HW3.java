/*
  JAVAFX - HOMEWORK3

  @author ordekalo
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HW3 extends Application {

    int lastCount = 0;// Last tick count before clicked undo button
    int count = 0; // Tick counter
    int interval = 1000; // Tick interval
    double lastLocation = 0; // Last location before clicked undo button
    int lastInterval = 1000; // Last tick interval before clicked undo button
    boolean flag = false; // Flag to validate if redo button clicked
    private BorderPane borderPane;
    private HBox hboxText;
    private Text txtCount, txtInterval;
    private String strCount = "tickCount: ", strInterval = "tickInterval: ";

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        borderPane = new BorderPane();

        // create buttons and order it on Hbox
        Button btnUndo = new Button("Undo");
        btnUndo.requestFocus(); // set focus on undo button
        Button btnRedo = new Button("Redo");
        HBox hboxButtons = new HBox(15);
        hboxButtons.setPadding(new Insets(15, 15, 15, 15));
        hboxButtons.getChildren().add(btnUndo);
        hboxButtons.getChildren().add(btnRedo);

        // Creates texts and order it on Hbox
        txtCount = new Text(strCount + count);
        txtInterval = new Text(strInterval + interval);
        hboxText = new HBox(15);
        hboxText.setPadding(new Insets(15, 15, 15, 15));
        txtCount.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        txtInterval.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        hboxText.getChildren().add(txtCount);
        hboxText.getChildren().add(txtInterval);

        // Add the hboxs to the main pane
        borderPane.setTop(hboxButtons);
        borderPane.setLeft(hboxText);

        startAnimation();

        hboxText.translateXProperty().addListener(e -> clickListener());

        // setting preferred button actions
        btnUndo.setOnAction(e -> undoAction());
        btnRedo.setOnAction(e -> redoAction());

        Scene scene = new Scene(borderPane, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(false);
        primaryStage.setTitle("DisplayMovingMessage");
        primaryStage.show();

    }

    public void clickListener() {
        // check if redo button clicked
        if (flag) {
            // set the last value before clicking on undo button
            count = lastCount;
            interval = lastInterval;
            flag = false;
        } else {
            // redo button not clicked -> normal inc  the count and interval
            count++;
            interval = 1000 + count;
        }
        // update texts value
        txtCount.setText(strCount + count);
        txtInterval.setText(strInterval + interval);
        //check if hbox of texts is not in the window bound
        if (!inBound(hboxText))
            hboxText.setTranslateX(0);
    }

    public void redoAction() {
        // set all value and location before click on undo button
        hboxText.setTranslateX(lastLocation - 1);
        count = lastCount;
        interval = lastInterval;
        txtCount.setText(strCount + lastCount);
        txtInterval.setText(strInterval + lastInterval);
        flag = true;
    }

    public void undoAction() {
        // save last location and values
        lastLocation = hboxText.getTranslateX();
        lastCount = count;
        lastInterval = interval;
        //reset all values and location
        count = -1; // actual value displayed is 0
        interval = 999; // actual value displayed is 1000
        txtCount.setText(strCount + count);
        txtInterval.setText(strInterval + interval);

        hboxText.setTranslateX(0);
    }

    /**
     * function take hbox and check if it in the window bounds
     *
     * @param hbox H-box
     * @return true if the hbox still in bounds else false
     */
    public boolean inBound(HBox hbox) {
        return hbox.getBoundsInParent().intersects(borderPane.getLayoutBounds()) || borderPane.getLayoutBounds().contains(hbox.getBoundsInParent());
    }

    /**
     * function calls the TranslateX animation every 1 sec
     * when click double click on mouse or press s/S key
     */
    public void startAnimation() {
        KeyFrame kf = new KeyFrame(Duration.seconds(1), e -> hboxText.setTranslateX(hboxText.getTranslateX() + 10));
        Timeline timeline = new Timeline(kf);
        timeline.setCycleCount(Timeline.INDEFINITE);
        //double clicked
        borderPane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                timeline.play(); // start animation
        });

        //S/s pressed
        borderPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S)
                timeline.play(); // start animation
        });
    }

}
