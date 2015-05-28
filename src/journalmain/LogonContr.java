package journalmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LogonContr implements Initializable {
    public PasswordField NewPass;
    public TextField CheckNewPass;
    public Label NewPas;
    private Stage stage;
    public Button Enter;
    public Button ChangePass;
    public PasswordField Pass;
    public TextField Login;
    public Label JourLowLabel;
    public javafx.scene.layout.Pane Pane;
    public String LogPas;
    public int UserID;
    private int VAH=0;
    private int StatVAH =0;
    private String Name;
    private int UnNorm =0;

    public void Enter(ActionEvent actionEvent) {
        EnterAll();
    }

    public void EnterAll (){
        VAH=StatVAH=UnNorm=0;
        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
        try{                        //Вход по проверке логина и пароля
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = st.executeQuery("Select * from Users");
            while (rs.next()){
                if (Login.getText().equals(rs.getString("Login"))){
                    if(Pass.getText().equals(rs.getString("Pass"))){
                        LogPas=rs.getString("LogPas");                  /*Определение будущей значимости пользователя*/
                        UserID=rs.getInt("User_ID");
                        Name=rs.getString("Name");
                        MainContr.Name = Name;
                        MainContr.LogPas = LogPas;
                        MainContr.UserID = UserID;
                        MainContr.Login = rs.getString("Login");
                        Statement statRES = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet RES = statRES.executeQuery("USE Journal SELECT Login FROM PochRefreshStatus");
                        while (RES.next()){
                            StatVAH++;
                            System.out.println(MainContr.Login);
                            System.out.println(RES.getString("Login"));
                            if (MainContr.Login.equals(RES.getString("Login"))){
                                JourLowLabel.setText("Под этим логином работают. Обратитесь к ГлавАдмину!");
                                JourLowLabel.setTextFill(Color.RED);
                                UnNorm =1;
                                RES.last();
                            } else  VAH++;
                        }
                        if (VAH==StatVAH) {
                            Statement statLog = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            String SQLRefreshStatus = "USE Journal \n" +
                                    "INSERT INTO PochRefreshStatus \n" +
                                    "(Login, Status, Restart) \n" +
                                    "VALUES('" + MainContr.Login + "',0,0)";
                            System.out.println(SQLRefreshStatus);
                            statLog.executeUpdate(SQLRefreshStatus);
                            statLog.close();
                            Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize ();
                            if (sSize.height<900){
                                MainContr.vertik = 600;
                            } else {
                                MainContr.vertik = 900;
                            }
                            JourLowLabel.setText("Логин и пароль найдены!");
                            JourLowLabel.setTextFill(Color.GREEN);
                            LogonContr.this.stage.close();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/journalmain/main.fxml"));
                            Parent root = loader.load();
                            MainContr controller = loader.getController();
                            controller.setStage(stage);
                            System.out.println("Высота будущего окна в связи с текущим разрешением экрана = " + MainContr.vertik);
                            Scene scene = new Scene(root, 1200, MainContr.vertik );
                            stage.setScene(scene);
                            stage.setTitle("Журнал Администраторов");
                            stage.getIcons().add(new Image("/journalmain/res/images/nicewhale.png"));
                            stage.setAlwaysOnTop(true);
                            stage.setResizable(false);
                            stage.setWidth(1200);
                            stage.setHeight(MainContr.vertik);
                            stage.centerOnScreen();
                            // На закрытии удалять присутствие текущего пользователя из таблицы проверки Глобального Обновления в базе сиквела
                            stage.setOnCloseRequest(event -> {
                                MainContr.timer.cancel();
                                MainContr.RefreshTimer.cancel();
                                String connectionUrl1 = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
                                try {
                                    Connection con1 = DriverManager.getConnection(connectionUrl1);
                                    Statement StatDel = con1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    String SQLDeleteStatus = ("USE Journal\n" +
                                            "DELETE FROM PochRefreshStatus\n" +
                                            "WHERE Login='" + MainContr.Login + "'");
                                    System.out.println("///////////////////////////////////////////Запрос на удаление собственного присутствия в сети. Остановка таймеров \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
                                    System.out.println(SQLDeleteStatus);
                                    StatDel.executeUpdate(SQLDeleteStatus);
                                    StatDel.close();
                                    con1.close();
                                } catch (SQLException ignored) {
                                }
                            });
                            stage.show();
//                            File sour = new File ("\\\\192.168.1.8\\data_user\\docs\\Медцентр\\Администраторы\\Программа Журнал Администраторов\\Журнал.lnk");
//                            File dest0 = new File ("C:\\Users\\du-reg1\\desktop\\Журнал.lnk");
//                            File dest1 = new File ("C:\\Users\\du-reg1\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\Журнал.lnk");
//                            dest0.delete();dest1.delete();
//                            Files.copy(sour.toPath(), dest0.toPath());
//                            Files.copy(sour.toPath(), dest1.toPath());
                        }

                    }else {
                        if(UnNorm !=0){} else{
                            JourLowLabel.setText("Пароль не правильный!");
                            JourLowLabel.setTextFill(Color.RED);
                            rs.close();
                        }
                    }
                }else{
                    if(UnNorm !=0){} else{
                        JourLowLabel.setText("Такого логина не существует!");
                        JourLowLabel.setTextFill(Color.RED);
                    }
                }
            }
            rs.close();
            st.close();
            con.close();
        }catch (SQLException ignored) {} catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ChangePass(ActionEvent actionEvent) {
        if (Enter.isDisabled()) {                                    /*Если форма смена пароля открыта*/
            if (NewPass.getText().equals(CheckNewPass.getText())){   /*Если пароли совпадают*/
                if(NewPass.getText().equals("")) {                   /*Если новый пароль пуст*/
                    JourLowLabel.setText("Пароли не могут быть пустыми");
                    JourLowLabel.setTextFill(Color.RED);
                }else{                                               /*Если новый пароль не пуст - основной код обработки*/
                    int i = 180;
                    while (i > 114) {
                        i=i-1;
                        stage.setHeight(i);                          /*Уменьшение окна и обработка полей на изначальное состояние*/
                    }
                    NewPass.setFocusTraversable(false);
                    CheckNewPass.setFocusTraversable(false);        /*Удаление лишнего фокуса*/
                    String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
                    try{                                             /*Попытка записи в сиквел нового пароля, через проверку текущего логина-пароля*/
                        Connection con = DriverManager.getConnection(connectionUrl);
                        Statement st = con.createStatement();
                        ResultSet res = st.executeQuery("Select * from Users");
                        while (res.next()){
                            if (Login.getText().equals(res.getString("Login"))){
                                System.out.println(res.getString("Login"));
                                if(Pass.getText().equals(res.getString("Pass"))){
                                    System.out.println(res.getString("Pass"));
                                    ResultSet rs = st.executeQuery("use Journal \n" +   /*Апдейт пароля в сиквеле*/
                                            "UPDATE Users \n" +
                                            "SET Pass='"+CheckNewPass.getText()+"'\n"+
                                            "WHERE Login='"+res.getString("Login")+"'");
                                    System.out.println(String.valueOf(rs));
                                    rs.close();
                                    res.last();
                                }else {
                                    JourLowLabel.setText("Пароль не правильный!");
                                    JourLowLabel.setTextFill(Color.RED);
                                    res.last();
                                }
                            }else{
                                JourLowLabel.setText("Такого логина не существует!");
                                JourLowLabel.setTextFill(Color.RED);
                            }
                        }
                        res.close();
                        con.close();
                    }catch (SQLException ignored) {}
                    NewPass.setText("");CheckNewPass.setText("");
                    JourLowLabel.setLayoutY(65);
                    JourLowLabel.setText("Пароль изменен. Для входа введите новый пароль");
                    JourLowLabel.setTextFill(Color.GREEN);
                    Pass.setText("");
                    NewPas.setLayoutY(98.0);
                    NewPass.setLayoutY(85.0);
                    ChangePass.setText("Смена пароля");
                    ChangePass.setLayoutX(230);
                    Enter.setDisable(false);
                }
            }
            else{
                JourLowLabel.setText("Новые пароли НЕ совпадают");
                JourLowLabel.setTextFill(Color.RED);
            }
        }else{                                                          /*Если форма смена пароля закрыта*/
            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            try{
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("Select * from Users");
                while (rs.next()){
                    if (Login.getText().equals(rs.getString("Login"))){
                        if(Pass.getText().equals(rs.getString("Pass"))){    /*Если логин и пароль совпадают*/
                            int i = 75;
                            while (i < 180) {
                                i++;
                                stage.setHeight(i);                         /*Уменьшение окна*/
                            }
                            NewPass.setFocusTraversable(true);          /*Выставление дополнительного фокуса*/
                            CheckNewPass.setFocusTraversable(true);
                            JourLowLabel.setLayoutY(133);
                            JourLowLabel.setText("");
                            NewPas.setLayoutY(78.0);
                            NewPass.setLayoutY(75.0);
                            ChangePass.setText("Сменить");
                            ChangePass.setLayoutX(247.5);
                            Enter.setDisable(true);
                            JourLowLabel.setText("Введите новый пароль и подтверждение");
                            JourLowLabel.setTextFill(Color.GREEN);
                            rs.last();
                        }else {
                            JourLowLabel.setText("Пароль не правильный!");
                            JourLowLabel.setTextFill(Color.RED);
                            rs.last();
                        }
                    }else{
                        JourLowLabel.setText("Такого логина не существует!");
                        JourLowLabel.setTextFill(Color.RED);
                    }
                }
                rs.close();
                con.close();
            }catch (SQLException ignored) {}
        }
    }

    public void setStage(Stage st ){
        stage = st;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void EnterKey(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) EnterAll();
    }
}