package journalmain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/journalmain/logon.fxml"));
        Parent root = loader.load();
        LogonContr controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root, 330, 75));
        stage.setTitle("Введите логин и пароль");
        stage.getIcons().add(new Image("/journalmain/res/images/dog.png"));
        stage.setAlwaysOnTop(true);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}