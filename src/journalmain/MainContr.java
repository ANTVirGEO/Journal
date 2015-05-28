package journalmain;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.Date;

public class MainContr implements Initializable {

    public static String LogPas;
    public static int UserID;
    public static String Login;
    public static Timer timer;
    public static Timer RefreshTimer;
    public static String Name;
    public static int vertik;
    public static String RezPacDR ="";
    public static String FIO="";
    public Button JourButtonCheck;
    public Label JourLabelLow;
    public HTMLEditor JourHTMLInput;
    public ComboBox JourComboDate;
    public DatePicker JourDateData;
    public DatePicker JourDatePeriod;
    public DatePicker PochDateData;
    public DatePicker PochDatePeriod;
    public ComboBox PochComboDate;
    public Label PochLabelLow;
    public ComboBox<Object> PochComboStatus;
    public Button PochButtonSend;
    public Button PochButtonCheck;
    public Tab JourTab;
    public Tab InfoTab;
    public Tab PochTab;
    public Circle PochPointer;
    public Button PochButStat;
    public Label PochLabelSender;
    public Label PochLabelReceiveDate;
    public Label PochLabelSentDate;
    public Label PochLabelTema;
    public Label PochLabelTaskDate;
    public Label PochLabelInWorkDate;
    public Label PochLabelDoneDate;
    public ToggleButton PochButTakeWork;
    public ToggleButton PochButDoneWork;
    public Label PochLabelStatus;
    public Label PochLabelCancelDate;
    public ToggleButton PochButCancelWork;
    public GridPane Grid;
    public Label PochLabelInWorkByLabel;
    public Label PochLabelDoneByLabel;
    public Label PochLabelInWorkBy;
    public Label PochLabelDoneBy;
    public Label PochLabelCancelByLabel;
    public Label PochLabelCancelBy;
    public Label PochLabelTaskID;
    public Label PochLabelUserID;
    public Label PochLabelInWorkDateLabel;
    public Label PochLabelDoneDateLabel;
    public Label PochLabelCancelDateLabel;
    public TextArea PochLabelCommentLabel;
    public Button PochLabelComment;
    public Button PochUserAdd;
    public TextField PochNewUserLogin;
    public CheckBox PochMOOORE;
    public Label PochLabelNewUser;
    public ListView PochStatUsers;
    public HTMLEditor PochHTML;
    public Button ResButShowPacients;
    public TextField RezOtchestvo;
    public TextField RezImya;
    public TextField RezFamiliya;
    public TextField RezEMK;
    public Label RezLabelLow;
    public TableView<RezPacData> RezTablePac;
    public TableView<PochData> PochTable;
    public TableView<RezPriemData> RezTablePriem;
    public TableView<RezAnalData> RezTableAnal;
    public Button RezSend;
    public TextArea RezPochta;
    public Tab ResTab;
    public TabPane MainTabPane;
    public int TabCount =1;
    public Tab PreCalculationTab;
    public Tab DolgTab;
    public Label RezPochtaLabel;
    private Stage stage;
    private String PochStatus = "Все письма";
    private LocalTime currenttime = LocalTime.now();
    private LocalDate BeginDate = LocalDate.now();
    private LocalDate EndDate = LocalDate.now().plusDays(1);
    private SimpleDateFormat fin = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private SimpleDateFormat fout = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private String BeginTaskDate = String.valueOf(BeginDate);
    private String EndTaskDate = String.valueOf(EndDate);
    private String PerTaskDate = null;
    private String PerInWorkDate = null;
    private String PerDoneDate = null;
    private String PerReceiveDate = null;
    private String PerSentDate = null;
    private String PerCancelDate = null;
    private int Stroka = 0;
    private String PochStatusUpdate = "null=)";
    private String PochStatusUpdateENG = "null=)";
    private Timer NewMailtimer;
    private int StatusHere = 0;
    private int StatusRestart = 0;
    private int StatusNoAnswer = 0;
    private int LocalRestart = 0;
    private int RezPriemPatientsID;
    private String EMAIL;
    private ArrayList<String> RezAnaliz= new ArrayList<>();



    public void setStage(Stage stage) {
        this.stage = stage;
    }
    //Инициализация программы, запуск нужных таймеров, проверок и т.д.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (vertik==600){
            PochTable.setPrefHeight(280);
        }else{
            PochTable.setPrefHeight(580);}
//Листенеры на выборы строк в таблицах для каких-либо действий последующих
        PochTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPochData(newValue));
        RezTablePac.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRezPacData(newValue));
        RezTablePriem.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showRezPriemData(newValue));
//Этот таймер при инициализации старует проверку почты раз в N минут. ЗЫ: останавливается при закрытии приложения
        timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    PochLabelLow.setText("Автоматическая проверка почты включена");
                    PochLabelLow.setTextFill(Color.ALICEBLUE);
                    try {
                        PochCheck();
                        System.out.println("PochCheck was started");
                    } catch (MessagingException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 600000, 600000);
//Этот таймер-мой выход из ситуации когда нет толстого серверного приложения, а есть сразу ТолстыйКлиент-База. Т.к. дедлайн) Когда-нибудь переделаю в адекватное приложение, но чую проще будет с нуля написать с этим же ГУИ х)
//Таймер проверяет таблицу пользователь-статус. Если хоть у одного пользователя сменился статус(он обновил данные), то все клиенты начинаются обновлять данные методом TableRefresh(и статусы). Когда статус обновится,
//У всех произойдет возврат на 0-значения всех статусов, в ожидании следующего обновления данных кем-нибудь из пользователей. Мне в голову пришло только это) Cyber-forumвские деды сказали,
//Что такое кол-во запросов база вытерпит вполне:) в расчете 20 пользователей(при текущих 6-8-10 актуальных на ближайшие лет 20-30)*60секунд=1200 запросов в минуту. Ок, верим на слово ^^.
        RefreshTimer = new java.util.Timer();
        RefreshTimer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    int StatCount=0;
                    int Stat1=0;
                    int StatRes1=0;
                    String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
                    try {
                        Connection con = DriverManager.getConnection(connectionUrl);
                        //запрос на глобальную проверку статусов. Нет, совместить с нижней проверкой статусов нельзя! ><
                        Statement StatRefALL = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet ResRefALL = StatRefALL.executeQuery("USE Journal \n" +
                                "SELECT * \n" +
                                "FROM PochRefreshStatus");
                        while(ResRefALL.next()){
                            StatCount++;
                            if (ResRefALL.getInt("Status")==1) {
                                Stat1++;
                            }
                            if (ResRefALL.getInt("Restart")==1) {
                                StatRes1++;
                            }
                        }
                        StatCount--;    //Поправка на первую обязательую строку в таблице Статусов пользователей, чтобы можно было делать проверку на повторяющихся пользователей. См.ЛогонКонтр.Костыли)
                        if (Stat1!=0){  //Если кто-то обновил данные:
                            StatusNoAnswer++;
                            System.out.println(StatusNoAnswer);
                            if (Stat1==StatCount){  //если все клиенты послали единицу==обновились, присвоить всем ноль
                                Statement StatUpdALL = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                String SQLUpdALL=("USE Journal \n" +
                                        "UPDATE PochRefreshStatus \n" +
                                        "SET Status=0 \n" +
                                        "WHERE Login<>'VAH'");
                                System.out.println("///////////////////////////////////////////Обновление статусов всех пользователей в связи с тем, что все обновили основную таблицу, ввиду изменения информации\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+ currenttime);
                                System.out.println(SQLUpdALL);
                                StatUpdALL.executeUpdate(SQLUpdALL);
                                StatUpdALL.close();
                                StatusNoAnswer=0;
                            } else { //если еще не все клиенты обновились:
                                if (StatusHere==0){ //Если еще не обновлялся - обновляйся!
                                    Statement StatSetStatus = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    String SQLSetStatus = "USE Journal \n" +
                                            "UPDATE PochRefreshStatus \n" +
                                            "SET Status=1 \n" +
                                            "WHERE Login='" + Login + "'";
                                    System.out.println("//////////Запрос на обновление статуса текущего пользователя в связи с обновлением базы. Обновление основной таблицы относительно базы\\\\\\\\\\" + currenttime + "\n");
                                    System.out.println(SQLSetStatus);
                                    StatSetStatus.executeUpdate(SQLSetStatus);
                                    StatSetStatus.close();
                                    TableRefresh();
                                }
                                if (StatusNoAnswer==60){ //если в течение 60 секунд клиент не меняет свой статус-удаляем его присутствие из сети
                                    Statement StatNoAnswer = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                    String SQLNoAnswer = "USE Journal \n" +
                                            "DELETE FROM PochRefreshStatus \n" +
                                            "WHERE Status=0";
                                    System.out.println("//////////Запрос на обновление статуса текущего пользователя в связи с обновлением базы. Обновление основной таблицы относительно базы\\\\\\\\\\" + currenttime + "\n");
                                    System.out.println(SQLNoAnswer);
                                    StatNoAnswer.executeUpdate(SQLNoAnswer);
                                    StatNoAnswer.close();
                                    StatusNoAnswer=0;
                                }
                            }
                        }
                        if (StatRes1!=0){ //Если был выставлен статус рестарта
                            if (StatusRestart==0) { //если не рестартился - рестартись!
                                Statement StatSetStatusRes = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                                String SQLSetStatusRes = "USE Journal \n" +
                                        "UPDATE PochRefreshStatus \n" +
                                        "SET Restart=1 \n" +
                                        "WHERE Login='" + Login + "'";
                                System.out.println("//////////Запрос на обновление Рестарта текущего пользователя в связи с обновлением базы. Обновление основной таблицы относительно базы\\\\\\\\\\" + currenttime + "\n");
                                System.out.println(SQLSetStatusRes);
                                StatSetStatusRes.executeUpdate(SQLSetStatusRes);
                                StatSetStatusRes.close();
                                PochLabelNewUser.setVisible(true);
                                PochLabelNewUser.setText("Вышла новая, улучшенная версия программы. Перезапустите программу");
                                PochLabelNewUser.setTextFill(Color.DARKRED);
                                TableRefresh();
                            }
                        }
                        ResRefALL.close();
                        StatRefALL.close();
                        //запрос на проверку статуса текущего пользователя и его наличия (повсеместная система удаления присутствия ввиду зависаний, перезагрузок и т.д.)
                        Statement StatStatRef = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet ResStatRef = StatStatRef.executeQuery("USE Journal \n" +
                                "SELECT * \n" +
                                "FROM PochRefreshStatus \n" +
                                "WHERE Login='" + Login + "'");
                        while(ResStatRef.next()){
                            LocalRestart = 1;
                            if (ResStatRef.getInt("Status")==1) { //если статус уже обновился, ставим переменной '1', и она не инициализирует больше обновление
                                StatusHere = 1;
                            } else {
                                StatusHere = 0;
                            }
                            if (ResStatRef.getInt("Restart")==1) { //если статус рестарта уже обновился, ставим переменной '1', и она не инициализирует больше обновление
                                StatusRestart = 1;
                            } else {
                                StatusRestart = 0;
                            }
                        }
                        if (LocalRestart == 0) { //если все-таки приложение отвисло после 60сек., и его присутствие удалили, он создает его заново
                            Statement statLog = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                            String SQLRefreshStatus = "USE Journal \n" +
                                    "INSERT INTO PochRefreshStatus \n" +
                                    "(Login, Status, Restart) \n" +
                                    "VALUES('" + Login + "',0,0)";
                            System.out.println(SQLRefreshStatus);
                            statLog.executeUpdate(SQLRefreshStatus);
                            statLog.close();
                        }
                        LocalRestart = 0;
                        ResStatRef.close();
                        StatStatRef.close();
                        con.close();
                    } catch (SQLException ignored) {
                    }
                });
            }
        }, 1000, 1000);
    }
    //Смена вкладки приводит к обнулению данных на вкладках, скрытию элементов и т.п.
    public void TabChanged(Event event) {
        TabCount++;
        System.out.println("TAB CHANGED, TabCount= " + TabCount);
        if (TabCount==2){
            if (MainTabPane.getSelectionModel().getSelectedIndex()==0){

                System.out.println("JOURNAL ACTIVATED, TabCount= " + TabCount+"\n");
                JourLabelLow.setText("");
                JourLabelLow.setTextFill(Color.BLACK);
            }
            if (MainTabPane.getSelectionModel().getSelectedIndex()==1){
                System.out.println("INFO ACTIVATED, TabCount= " + TabCount+"\n");
            }
            if (MainTabPane.getSelectionModel().getSelectedIndex()==2){
                System.out.println("POCHTA ACTIVATED, TabCount= " + TabCount+"\n");
                PochLabelLow.setText("");
                PochLabelLow.setTextFill(Color.BLACK);
                HideBottom();
                hideHTMLEditorToolbars(PochHTML);
                if (LogPas.equals("Moderator")){
                    PochMOOORE.setVisible(true);
                }
                PochDateData.setValue(BeginDate);
            }
            if (MainTabPane.getSelectionModel().getSelectedIndex()==3){
                System.out.println("RESULTS ACTIVATED, TabCount= " + TabCount+"\n");
                RezLabelLow.setText("");
                RezLabelLow.setTextFill(Color.BLACK);
                RezEMK.setText("");
                RezFamiliya.setText("");
                RezImya.setText("");
                RezOtchestvo.setText("");
                RezPochta.setText("");
                RezPochta.setVisible(false);
                ObservableList<RezPriemData> DataPriemClear = RezTablePriem.getItems();
                DataPriemClear.setAll();
                RezTablePriem.setVisible(false);
                ObservableList<RezPacData> DataPacClear = RezTablePac.getItems();
                DataPacClear.setAll();
                RezTablePac.setVisible(false);
                ObservableList<RezAnalData> DataAnalClear = RezTableAnal.getItems();
                DataAnalClear.setAll();
                RezTableAnal.setVisible(false);
            }
            if (MainTabPane.getSelectionModel().getSelectedIndex()==4){
                System.out.println("CALCULATION ACTIVATED, TabCount= " + TabCount+"\n");
            }
            if (MainTabPane.getSelectionModel().getSelectedIndex()==5){
                System.out.println("DOLGI ACTIVATED, TabCount= " + TabCount+"\n");
            }
            TabCount = 0;
        }

    }


    /////////////////////////////////////////////////////////Отделение Журнала//////////////////////////////////////////////////////////////////
    public void JourButtonCheck(ActionEvent actionEvent) {
        JourLabelLow.setText(JourHTMLInput.getHtmlText());
        System.out.println(LogPas);
        this.stage.close();
    }

    public void JourComboDate(ActionEvent actionEvent) {
        String s = String.valueOf(JourComboDate.getSelectionModel().getSelectedIndex());
        if (s.equals("0")) {
            JourDatePeriod.setVisible(false);
            JourDatePeriod.setDisable(true);
        }
        else if (s.equals("1")) {
            JourDatePeriod.setVisible(true);
            JourDatePeriod.setDisable(false);
        }
    }


    /////////////////////////////////////////////////////////Отделение почты. Хыхы xD Не обращать внимания, если будет тормозить =D//////////////////////////////////////////////////////////////////
    //Метод запуска таймер фокуса, при получении новой почты
    public void NewEmailPseudoTimer(){
        NewMailtimer = new java.util.Timer();
        NewMailtimer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (stage.isFocused()){
                            System.out.println("ФОКУС ЕСТЬ");
                            NewMailtimer.cancel();
                            System.out.println("Таймер отменен!");
                        } else {
                            System.out.println("ФОКУСА НЕТ");
                            stage.toFront();
                            System.out.println("Таймер в работе: " + Date.from(Instant.now()));
                        }
                    }
                });
            }
        }, 500, 3000);
    }
    //Кнопка выбора Даты или Периода
    public void PochComboDate(ActionEvent actionEvent) {
        String s = String.valueOf(PochComboDate.getSelectionModel().getSelectedIndex());
        if (s.equals("0")) {
            PochDatePeriod.setVisible(false);
            PochDatePeriod.setDisable(true);
            PochDatePeriod.setValue(PochDateData.getValue());
            TableRefresh();
        }
        else if (s.equals("1")) {
            PochDatePeriod.setVisible(true);
            PochDatePeriod.setDisable(false);
            PochDatePeriod.setValue(PochDateData.getValue());
        }
    }
    //Кнопка выбора статуса
    public void PochComboStatus(ActionEvent actionEvent) {
        PochStatus = String.valueOf(PochComboStatus.getValue());
        System.out.println(String.valueOf(PochComboStatus.getValue()));
        HideBottom();
        TableRefresh();
    }
    //Кнопко тестового письма
    public void PochButtonSend(ActionEvent actionEvent) throws MessagingException {
        final String ENCODING = "koi8-r";
        String SMTP_AUTH_USER = "results@alfamed-nsk.ru";
        String SMTP_AUTH_PWD = "VahVah123";
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_AUTH_USER);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.sendpartial", "true");
        props.put("mail.mime.charset", "UTF-8");
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        Transport transport = session.getTransport();
        transport.connect("smtp.gmail.com", 465, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        MimeMessage message = new MimeMessage(session);
        message.setSubject("Тестовое письмо для проверок связей!!!");
        message.setText("Этот текст служит проверкой всем полям! А заодно и связи клиентов всех пользователей с сервером и между собой. " +
                "В случае если этот текст виден, письмо берется в работу и у всех все отображается - следует сообщить об этом Главному администратору");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("Results@alfamed-nsk.ru"));
        message.setSentDate(new Date());
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        PochLabelLow.setText("Тестовое письмо успешно отправлено");
        PochLabelLow.setTextFill(Color.GREEN);
    }
    //Кнопко получения письма
    public void PochButtonCheck(ActionEvent actionEvent) throws MessagingException, IOException {
        PochCheck();
    }
    //Проверка почты. Если есть новые письма, создание строк в сиквеле в таблицах 'InMail' и 'Tasks'. Передача к обновлению основной таблицы програмы в методе TableRefresh
    private void PochCheck() throws MessagingException, IOException {
       /* String POP_AUTH_USER = "results@alfamed-nsk.ru";
        String POP_AUTH_PWD = "VahVah123";
        String FOLDER_INDOX = "INBOX"; // При папке "Входящие"
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties imapProps = new Properties();
        imapProps.setProperty("proxySet", "true");
        imapProps.setProperty("socksProxyHost", "192.168.1.11");
        imapProps.setProperty("socksProxyPort", "3128");
        imapProps.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
        imapProps.setProperty("mail.imap.socketFactory.fallback", "false");
        imapProps.setProperty("mail.imap.port", "993");
        imapProps.setProperty("mail.imap.socketFactory.port", "993");
        imapProps.setProperty("mail.mime.charset", "UTF-8");
        URLName url = new URLName("imap", "imap.googlemail.com", 993, "", POP_AUTH_USER, POP_AUTH_PWD);
        Session session = Session.getInstance(imapProps, null);
        Store store = session.getStore(url);
        store.connect();
        Folder folder = store.getFolder(FOLDER_INDOX);
        try {
            folder.open(Folder.READ_WRITE);
        } catch (MessagingException ex) {
            folder.open(Folder.READ_ONLY);
        }
        FlagTerm StatusFlag = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message[] messages = folder.search(StatusFlag);
// Проверка наличия новых писем
        for (int i = 0, n = messages.length; i < n; i++) {
            Sender = Arrays.toString(messages[i].getFrom());
            Tema = messages[i].getSubject().toLowerCase().trim();
            SentDate = String.valueOf(fin.format(messages[i].getSentDate()));
            System.out.println(SentDate);
            ReceiveDate = String.valueOf(fin.format(messages[i].getReceivedDate()));
            System.out.println(ReceiveDate);
            MailContent =messages[i].getContent().toString();
            System.out.println(MailContent);

            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            try {
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement StMail = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String TaskDate = fin.format(Date.from(Instant.now()));
                ResultSet ResInMail = StMail.executeQuery("USE Journal SELECT * FROM InMail");
                while (ResInMail.next()) {
                    MailID++;
                    TaskID++;
                }
//Добавление строки нового письма в таблицу 'InMail'
                String SQLInMail = "USE Journal \n" +
                        "INSERT INTO InMail \n" +
                        "(InMail_ID,Sender,ReceiveDate,SentDate,Tema,MailContent) \n" +
                        "VALUES(" + MailID + ",'" + Sender + "', '" + ReceiveDate + "', '" + SentDate +
                        "', '" + Tema + "', '" + MailContent + "')";
                System.out.println("//////////Добавление строки нового письма в сиквел в таблицу 'InMail'\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLInMail);
                StMail.executeUpdate(SQLInMail);
//Добавление новой строки новой задачи в сиквел  в таблицу 'Tasks'
                Statement StTask = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQLTasks = "USE Journal \n" +
                        "INSERT INTO Tasks \n" +
                        "(Task_ID, InMail_ID,Status,TaskDate) \n" +
                        "VALUES(" + TaskID + ", " + MailID + ", 'Не взято', '" + TaskDate + "')";
                System.out.println("//////////Добавление новой строки новой задачи в сиквел  в таблицу 'Tasks'\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLTasks);
                StTask.executeUpdate(SQLTasks);
                MailID = 0;
                TaskID = 0;
                ResInMail.close();
                StMail.close();
                StTask.close();
                con.close();
//Проверка времени, чтобы относительно него и статуса запустить обновление основной таблицы программы
                PochDateCheck();
            } catch (SQLException ignored) { }
            NewEmailPseudoTimer();
        }
        folder.close(false);
        store.close();
*/
    }
    //Обновление основной таблицы
    public void TableRefresh() {
        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
        try {
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement StatTask = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet ResTask;
            if (PochStatus.equals("Все письма")) {
//                BeginTaskDate= "2015.04.24 00:00:00";
//                EndTaskDate="2015.04.25 00:00:00";
                String SQLAll = ("USE Journal SELECT Tasks.Task_ID, Tasks.User_ID, Users.Name, InMail.Sender, " +
                        "Tasks.Status, Tasks.TaskDate, Tasks.InWorkDate, Tasks.DoneDate, Tasks.Comment, " +
                        "InMail.ReceiveDate, InMail.SentDate, InMail.Tema, Tasks.CancelDate, InMail.MailContent, " +
                        "Tasks.InWorkBy, Tasks.DoneBy, Tasks.CancelBy \n" +
                        "FROM Tasks Tasks LEFT OUTER JOIN Users Users ON Users.User_ID=Tasks.User_ID \n" +
                        "LEFT OUTER JOIN InMail InMail ON InMail.InMail_ID=Tasks.InMail_ID \n" +
                        "WHERE (Tasks.TaskDate>'" + BeginTaskDate + "') AND (Tasks.TaskDate<'" + EndTaskDate + "')");
                System.out.println("///////////////////////////////////////////Выборка для обновление основной таблицы программы БЕЗ статуса\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+ currenttime);
                System.out.println(SQLAll);
                ResTask = StatTask.executeQuery(SQLAll);
            } else {
                String SQLStatus = ("USE Journal SELECT Tasks.Task_ID, Tasks.User_ID, Users.Name, InMail.Sender, " +
                        "Tasks.Status, Tasks.TaskDate, Tasks.InWorkDate, Tasks.DoneDate, Tasks.Comment, " +
                        "InMail.ReceiveDate, InMail.SentDate, InMail.Tema, Tasks.CancelDate, InMail.MailContent, " +
                        "Tasks.InWorkBy, Tasks.DoneBy, Tasks.CancelBy \n" +
                        "FROM Tasks Tasks LEFT OUTER JOIN Users Users ON Users.User_ID=Tasks.User_ID \n" +
                        "LEFT OUTER JOIN InMail InMail ON InMail.InMail_ID=Tasks.InMail_ID \n" +
                        "WHERE (Tasks.Status='" + PochStatus + "') AND (Tasks.TaskDate>'" + BeginTaskDate + "') AND (Tasks.TaskDate<'" + EndTaskDate + "')");
                System.out.println("///////////////////////////////////////////Выборка для обновление основной таблицы программы С УЧЕТОМ статуса =" + PochStatus + "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+ currenttime);
                System.out.println(SQLStatus);
                ResTask = StatTask.executeQuery(SQLStatus);
            }
            ObservableList<PochData> dataclear = PochTable.getItems();
            dataclear.setAll();
            int vah = 0;
            int vah1 ;
            while (ResTask.next()) {
                vah++;
//            ResTask.first();
                vah1 = ResTask.getInt("Task_ID");
                System.out.println(vah1);
                System.out.println(ResTask);
//Ну. Здесь происходит Магия Тупизма. Чтобы в адекватном для меня виде добавлять к классу Персон Даты для вывода в основную таблицу, пришлось творить это.
//Это - если дата не обНУЛЛена, то происходит её трансформация после взятия из сиквела в нужный мне формат(который кстати преобразует время в ноль),
//Ввиду того, что Я додумался сменить кодировку всей ИДЕ с УТФ-8 на 1251, у меня слетела вся киррилица в комментах >< Замечу, что до этого тут была просто гора текста :)
// после обрезается нулевое время и добавляется время с базы, и это все добро преобразуется в текст для ввода в класс Персон ^_^
                if (ResTask.getDate("TaskDate") != null) {
                    PerTaskDate = String.valueOf(fout.format(ResTask.getDate("TaskDate")).substring(0, 11) + ResTask.getTime("TaskDate"));
                }
                if (ResTask.getDate("InWorkDate") != null)
                    PerInWorkDate = String.valueOf(fout.format(ResTask.getDate("InWorkDate")).substring(0, 11) + ResTask.getTime("InWorkDate"));
                if (ResTask.getDate("DoneDate") != null)
                    PerDoneDate = String.valueOf(fout.format(ResTask.getDate("DoneDate")).substring(0, 11) + ResTask.getTime("DoneDate"));
                if (ResTask.getDate("ReceiveDate") != null)
                    PerReceiveDate = String.valueOf(fout.format(ResTask.getDate("ReceiveDate")).substring(0, 11) + ResTask.getTime("ReceiveDate"));
                if (ResTask.getDate("SentDate") != null)
                    PerSentDate = String.valueOf(fout.format(ResTask.getDate("SentDate")).substring(0, 11) + ResTask.getTime("SentDate"));
                if (ResTask.getDate("CancelDate") != null)
                    PerCancelDate = String.valueOf(fout.format(ResTask.getDate("CancelDate")).substring(0, 11) + ResTask.getTime("CancelDate"));
                ObservableList<PochData> data = PochTable.getItems();
//PochData(Integer TaskID, Integer UserID, String UserName, String InMailID, String Status, String TaskDate, String InWorkDate, String DoneDate,
                //       String Comment, String ReceiveDate, String SentDate, String Tema, String CancelDate, String MailContent, String InWorkBy, String DoneBy, String CancelBy)
                data.add(new PochData(ResTask.getInt("Task_ID"), ResTask.getInt("User_ID"), ResTask.getString("Name"), ResTask.getString("Sender"), ResTask.getString("Status"),
                        PerTaskDate, PerInWorkDate, PerDoneDate, ResTask.getString("Comment"), PerReceiveDate, PerSentDate, ResTask.getString("Tema"),
                        PerCancelDate, ResTask.getString("MailContent"), ResTask.getString("InWorkBy"), ResTask.getString("DoneBy"), ResTask.getString("CancelBy")));
                PerTaskDate = null;
                PerInWorkDate = null;
                PerDoneDate = null;
                PerReceiveDate = null;
                PerSentDate = null;
                PerCancelDate = null;
//И после присвоения в класс Персон идет обнуление, дабы следующая строка тоже нормально добавилась и данные переменных не перенеслись. Криворукое рукожопство, ибо сила во мне ещё не пробудилась в полной мере\\*_*//
//И еще: Ввиду того, что тут в отдельный класс Персон Я натолкал все в текстовом формате, все даты в таблице - текстовые и упорядочиваются тоже по правилам текста, а не дат :)
//Поэтому упорядочивание дат в периоде большем, чем между начальной и конечной датой месяца работать адекватно НЕ БУДЕТ. По крайне мере без доп.костылей. На них времени нет х)
//            }
            }
            System.out.println("Количество строк= "+vah);
            ResTask.close();
            StatTask.close();
            con.close();
        } catch (SQLException ignored) {
        }
        PochTable.getSelectionModel().select(Stroka);
    }
    //Удаление элементов управления HTML
    public static void hideHTMLEditorToolbars(final HTMLEditor editor){
        editor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for (Node node : nodes) {
                node.setVisible(false);
                node.setManaged(false);
            }
            editor.setVisible(true);
        });
    }

    //Кнопка статистики
    public void PochButStat(ActionEvent actionEvent) {
//        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
//        try {
//            Connection con = DriverManager.getConnection(connectionUrl);
//            Statement StCheckUserID = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            ResultSet ResNewUser = StCheckUserID.executeQuery("USE Journal SELECT Users.Name, " +
//                    "Tasks.Status, Tasks.TaskDate, Tasks.InWorkDate, Tasks.DoneDate, Tasks.Comment, " +
//                    "InMail.ReceiveDate, InMail.SentDate, InMail.Tema, Tasks.CancelDate, InMail.MailContent, " +
//                    "Tasks.InWorkBy, Tasks.DoneBy, Tasks.CancelBy \n" +
//                    "FROM Tasks Tasks LEFT OUTER JOIN Users Users ON Users.User_ID=Tasks.User_ID \n" +
//                    "LEFT OUTER JOIN InMail InMail ON InMail.InMail_ID=Tasks.InMail_ID \n" +
//                    "WHERE (Tasks.Status='" + PochStatus + "') AND (Tasks.TaskDate>CONVERT(datetime,'" + BeginTaskDate + "',102)) AND (Tasks.TaskDate<CONVERT(datetime,'" + EndTaskDate + "',102))");
//            int NewUserID=0;
//            while (ResNewUser.next()){
//                NewUserID=ResNewUser.getInt("User_ID");
//            }
//            ResNewUser.close();
//            StCheckUserID.close();
//            con.close();
//        } catch (SQLException ignored) {}
        PochLabelLow.setTextFill(Color.RED);
        PochLabelLow.setText("Статистика будет работать чуть позже.");
    }
    //Блок проверки даты и периода дат. Вроде все отлаженно работает. Изменение дат стартует обновление таблицы, а уже статусом обновляются записи в текущих датах. Но работает и наоборот) Спасибо моей жене :* Светлая голова, чтобы Я без её логики делал вообще) ума не приложу =)
    public void PochDate(Event event) {
        PochDateCheck();
    }
    public void PochDateCheck(){
        BeginDate=PochDateData.getValue();
        PochStatus=String.valueOf(PochComboStatus.getValue());
        if (PochDatePeriod.isDisabled()) {
            BeginTaskDate = String.valueOf(BeginDate);
            EndTaskDate = String.valueOf(PochDateData.getValue().plusDays(1));
            HideBottom();
            TableRefresh();
        } else {
            PochDatePeriodCheck();
        }
    }
    public void PochDatePeriod(ActionEvent actionEvent) {
        PochDatePeriodCheck();
    }
    public void PochDatePeriodCheck (){
        BeginDate = PochDateData.getValue();
        EndDate = PochDatePeriod.getValue();
        if (PochDatePeriod.getValue() == null) {
            PochLabelLow.setTextFill(Color.RED);
            PochLabelLow.setText("Даты в полях НЕ МОГУТ БЫТЬ ПУСТЫМИ!!! И также начальная дата НЕ МОЖЕТ БЫТЬ ПОЗЖЕ конечной!!! Нужно перевыбрать дату!");
            BeginTaskDate = "";
            EndTaskDate = "";
            HideBottom();
            TableRefresh();
        }else{
            if (BeginDate.isBefore(EndDate.plusDays(1))) {
                BeginTaskDate = String.valueOf(BeginDate);
                EndTaskDate = String.valueOf(EndDate.plusDays(1));
                HideBottom();
                TableRefresh();
            } else {
                PochLabelLow.setTextFill(Color.RED);
                PochLabelLow.setText("Даты в полях НЕ МОГУТ БЫТЬ ПУСТЫМИ!!! И также начальная дата НЕ МОЖЕТ БЫТЬ ПОЗЖЕ конечной!!! Нужно перевыбрать дату!");
                PochDatePeriod.setValue(null);
            }
        }
    }
    //Показ дополнительных данных, если сработал слушатель на выборе строки в сновной таблице. А также тут происходит проверка статуса и изменение кнопок, относительно него.
    private void showPochData(PochData pochData) {
        if (pochData != null) {
            PochLabelCommentLabel.setVisible(true);
            PochLabelCommentLabel.setDisable(false);
            PochLabelCommentLabel.setEditable(false);
            Stroka = PochTable.getSelectionModel().getSelectedIndex();
            PochLabelSender.setText(pochData.getInMailID());
            PochLabelReceiveDate.setText(pochData.getReceiveDate());
            PochLabelSentDate.setText(pochData.getSentDate());
            PochLabelTema.setText(pochData.getTema());
            PochLabelStatus.setText(pochData.getStatus());
            if (UserID!= pochData.getUserID()){
                if (PochLabelStatus.getText().equals("В работе")) {
                    PochLabelStatus.setTextFill(Color.BLUE);
                    PochButTakeWork.setDisable(true);
                    PochButDoneWork.setDisable(true);
                    PochButCancelWork.setDisable(true);
                    PochLabelComment.setVisible(false);
                    PochLabelCommentLabel.setVisible(true);
                    PochLabelCommentLabel.setDisable(false);
                    PochLabelCommentLabel.setEditable(false);
                    PochLabelLow.setTextFill(Color.DARKRED);
                    PochLabelLow.setText("Это письмо находится в работе у другого пользователя. Никто кроме этого пользователя не может как-то повлиять на это письмо!");
                }
                if (PochLabelStatus.getText().equals("Выполнено")) {
                    PochLabelStatus.setTextFill(Color.GREEN);
                    PochButTakeWork.setDisable(true);
                    PochButDoneWork.setDisable(true);
                    PochButCancelWork.setDisable(true);
                    PochLabelComment.setVisible(false);
                    PochLabelCommentLabel.setVisible(true);
                    PochLabelCommentLabel.setDisable(false);
                    PochLabelCommentLabel.setEditable(false);
                    PochLabelLow.setTextFill(Color.DARKRED);
                    PochLabelLow.setText("Это письмо было Выполнено другим пользователем. Можно поздравить коллегу с успехом!");
                }
            } else {
                if (PochLabelStatus.getText().equals("В работе")) {
                    PochLabelStatus.setTextFill(Color.BLUE);
                    PochButTakeWork.setDisable(true);
                    PochButDoneWork.setDisable(false);
                    PochButCancelWork.setDisable(false);
                    PochLabelComment.setVisible(true);
                    PochLabelCommentLabel.setVisible(true);
                    PochLabelCommentLabel.setDisable(false);
                    PochLabelCommentLabel.setEditable(true);
                    PochLabelLow.setTextFill(Color.BLUE);
                    PochLabelLow.setText("Это письмо находится в работе, никто кроме текущего пользователя не сможет его завершить или отменить.");
                }
                if (PochLabelStatus.getText().equals("Выполнено")) {
                    PochLabelStatus.setTextFill(Color.GREEN);
                    PochButTakeWork.setDisable(true);
                    PochButDoneWork.setDisable(true);
                    PochButCancelWork.setDisable(true);
                    PochLabelComment.setVisible(false);
                    PochLabelCommentLabel.setVisible(true);
                    PochLabelCommentLabel.setDisable(false);
                    PochLabelCommentLabel.setEditable(false);
                    PochLabelLow.setTextFill(Color.GREEN);
                    PochLabelLow.setText("Это письмо уже было выполнено текущим пользователем. Примите поздравления!");
                }
            }
            if (PochLabelStatus.getText().equals("Не взято")) {
                PochLabelStatus.setTextFill(Color.RED);
                PochButTakeWork.setDisable(false);
                PochButDoneWork.setDisable(true);
                PochButCancelWork.setDisable(true);
                PochLabelComment.setVisible(false);
                PochLabelCommentLabel.setVisible(true);
                PochLabelCommentLabel.setDisable(false);
                PochLabelCommentLabel.setEditable(false);
                PochLabelLow.setTextFill(Color.RED);
                PochLabelLow.setText("Это НОВОЕ письмо и его еще никто не заметил - оно не обработано! Его можно взять в работу.");
            }
            if (PochLabelStatus.getText().equals("Отменено")) {
                PochLabelStatus.setTextFill(Color.DARKVIOLET);
                PochButTakeWork.setDisable(false);
                PochButDoneWork.setDisable(true);
                PochButCancelWork.setDisable(true);
                PochLabelComment.setVisible(false);
                PochLabelCommentLabel.setVisible(true);
                PochLabelCommentLabel.setDisable(false);
                PochLabelCommentLabel.setEditable(false);
                PochLabelLow.setTextFill(Color.DARKVIOLET);
                PochLabelLow.setText("ВНИМАНИЕ!!! Это письмо было отменено из работы предыдущим пользователем. Его можно снова взять в работу!");
            }
            PochLabelTaskDate.setText(pochData.getTaskDate());
            PochLabelInWorkBy.setText(pochData.getInWorkBy());
            PochLabelDoneBy.setText(pochData.getDoneBy());
            PochLabelCancelBy.setText(pochData.getCancelBy());
            PochLabelInWorkDate.setText(pochData.getInWorkDate());
            PochLabelDoneDate.setText(pochData.getDoneDate());
            PochLabelCancelDate.setText(pochData.getCancelDate());
            PochLabelTaskID.setText(pochData.getTaskID().toString());
            PochLabelUserID.setText(pochData.getUserID().toString());
            PochLabelCommentLabel.setText(pochData.getComment());
            PochHTML.setHtmlText(pochData.getMailContent());
            ShowBottom();
        }
    }
    //Показать нижнюю часть информационного блока
    public void ShowBottom (){
        Grid.setVisible(true);Grid.setDisable(false);
        PochHTML.setVisible(true);PochHTML.setDisable(false);
        CheckBotStat();
    }
    //Спрятать нижнюю часть информационного блока
    public void HideBottom (){
        Grid.setDisable(true);Grid.setVisible(false);
        PochHTML.setDisable(true);PochHTML.setVisible(false);
        PochLabelComment.setVisible(false);
        PochLabelCommentLabel.setVisible(false);
        CheckBotStat();
    }
    //Скрытие/показ от отсутствия/наличия информации в нижних полях дат и пользователей задачи
    public void CheckBotStat(){
        if (PochLabelInWorkBy.getText()==null) {PochLabelInWorkByLabel.setVisible(false);} else {PochLabelInWorkByLabel.setVisible(true);}
        if (PochLabelDoneBy.getText()==null) {PochLabelDoneByLabel.setVisible(false);} else {PochLabelDoneByLabel.setVisible(true);}
        if (PochLabelCancelBy.getText()==null) {PochLabelCancelByLabel.setVisible(false);} else {PochLabelCancelByLabel.setVisible(true);}
        if (PochLabelInWorkDate.getText()==null) {PochLabelInWorkDateLabel.setVisible(false);} else {PochLabelInWorkDateLabel.setVisible(true);}
        if (PochLabelDoneDate.getText()==null) {PochLabelDoneDateLabel.setVisible(false);} else {PochLabelDoneDateLabel.setVisible(true);}
        if (PochLabelCancelDate.getText()==null) {PochLabelCancelDateLabel.setVisible(false);} else {PochLabelCancelDateLabel.setVisible(true);}
    }
    //Кнопко взятие работы
    public void PochTakeWork(Event event) {
        PochLabelInWorkByLabel.setVisible(true);
        PochStatusUpdate = "В работе";
        PochStatusUpdateENG = "InWork";
        StatusUpdateButtons();
    }
    //Кнопко выполнения работы
    public void PochDoneWork(Event event) {
        PochLabelDoneByLabel.setVisible(true);
        PochStatusUpdate = "Выполнено";
        PochStatusUpdateENG = "Done";
        StatusUpdateButtons();
    }
    //Кнопко отмены работы
    public void PochCancelWork(Event event) {
        PochLabelCancelByLabel.setVisible(true);
        PochStatusUpdate = "Отменено";
        PochStatusUpdateENG = "Cancel";
        StatusUpdateButtons();
    }
    //Обновление таблицы относительно нажатой кнопки в нижней части информационного блока
    public void StatusUpdateButtons (){
        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
        try {
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement StTask = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQLTasks = "USE Journal \n" +
                    "UPDATE Tasks \n" +
                    "SET User_ID=" + UserID + ",Status='" + PochStatusUpdate + "'," + PochStatusUpdateENG + "Date='" + String.valueOf(fin.format(Date.from(Instant.now()))) +
                    "', " + PochStatusUpdateENG + "By='" + Name + "' \n" +
                    "WHERE Task_ID='" + String.valueOf(PochLabelTaskID.getText()) + "'";
            System.out.println("//////////Обновление Статуса задачи в сиквеле, таблице 'Tasks' относительно Таск_ИД\\\\\\\\\\" + currenttime + "\n");
            System.out.println(SQLTasks);
            StTask.executeUpdate(SQLTasks);
            StTask.close();
            Statement StUpdStat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQLUpdStat = "USE Journal \n" +
                    "UPDATE PochRefreshStatus \n" +
                    "SET Status=1 \n" +
                    "WHERE Login='" + Login + "'";
            System.out.println("//////////Долгожданное обновление в сиквеле, в таблице 'PochRefreshStatus' Статуса Глобального Обновления, для общего обновления информации во всех клиентах\\\\\\\\\\" + currenttime + "\n");
            System.out.println(SQLUpdStat);
            StUpdStat.executeUpdate(SQLUpdStat);
            StUpdStat.close();
            con.close();
        } catch (SQLException ignored) {}
        TableRefresh();
    }
    //Добавление комментария
    public void AddComment(Event event) {
        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
        try {
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement StTaskCom = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQLTaskCom = "USE Journal \n" +
                    "UPDATE Tasks \n" +
                    "SET Comment='" + PochLabelCommentLabel.getText() + "' \n" +
                    "WHERE Task_ID='" + String.valueOf(PochLabelTaskID.getText()) + "'";
            System.out.println("//////////Обновление Комментаария задачи в сиквеле, таблице 'Tasks' относительно Таск_ИД\\\\\\\\\\" + currenttime + "\n");
            System.out.println(SQLTaskCom);
            StTaskCom.executeUpdate(SQLTaskCom);
            StTaskCom.close();
            con.close();
        } catch (SQLException ignored) {}
        TableRefresh();
    }
    //Расширение возможностей пользователей со статусом Moderator
    public void PochMOOORE(ActionEvent actionEvent) {
        if (PochMOOORE.isSelected()){
            PochLabelNewUser.setVisible(true);
            PochNewUserLogin.setVisible(true);
            PochUserAdd.setVisible(true);
            PochButtonCheck.setVisible(false);
            PochButStat.setVisible(false);
            PochButtonSend.setVisible(false);
        } else {
            PochLabelNewUser.setVisible(false);
            PochNewUserLogin.setVisible(false);
            PochUserAdd.setVisible(false);
            PochButtonCheck.setVisible(false);
            PochButStat.setVisible(false);
            PochButtonSend.setVisible(false);
        }
    }
    //Добавление нового пользователя
    public void AddNewUser(ActionEvent actionEvent) {
        String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
        try {
            Connection con = DriverManager.getConnection(connectionUrl);
            Statement StCheckUserID = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet ResNewUser = StCheckUserID.executeQuery("USE Journal SELECT User_ID FROM Users");
            int NewUserID=0;
            while (ResNewUser.next()){
                NewUserID=ResNewUser.getInt("User_ID");
            }
            ResNewUser.close();
            StCheckUserID.close();
            NewUserID++;
            Statement StNewUser = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String SQLNewUser = "USE Journal \n" +
                    "INSERT INTO Users \n" +
                    "(User_ID,Login,Pass,Name,LogPas) \n" +
                    "VALUES(" + NewUserID + ",'" + PochNewUserLogin.getText() + "', '123456', '" + PochNewUserLogin.getText() +
                    "', 'User')";
            System.out.println("//////////Добавление нового пользователя в сиквеле, таблице 'Users' относительно Таск_ИД\\\\\\\\\\" + currenttime + "\n");
            System.out.println(SQLNewUser);
            StNewUser.executeUpdate(SQLNewUser);
            PochLabelLow.setText("Был успешно добавлен пользователь = " + PochNewUserLogin.getText());
            PochLabelLow.setTextFill(Color.GREEN);
            PochNewUserLogin.setText("Пользователь добавлен");
            NewUserID=0;
            StNewUser.close();
            con.close();
        } catch (SQLException ignored) {}
        if (PochNewUserLogin.getText().equals("Пользователь добавлен")){ } else {
            PochLabelLow.setText("Произошла ошибка при добавлении пользователя! Попробуйте ещё раз.");
            PochLabelLow.setTextFill(Color.RED);
        }
    }
    //Очистка поля нового пользователя при нажатии
    public void PochClear(Event event) {
        if (PochNewUserLogin.getText().equals("Пользователь добавлен")){
            PochNewUserLogin.setText("");
        }
    }

    /////////////////////////////////////////////////////////Отделение отправки результатов анализов на почту пациенту//////////////////////////////////////////////////////////////////
    //Показать найденных пациентов
    public void RezButShowPacients(ActionEvent actionEvent) {
        RezHideSendBut();
        RezTablePriem.setVisible(false);
        RezTableAnal.setVisible(false);
        String PATIENTS = "";
        String EMK = "";
        String familiya = "";
        String imya = "";
        String otchestvo = "";
        if (!RezEMK.getText().equals("")) EMK = "(PATIENTS_ID=" + RezEMK.getText() + ") AND";
        if (!RezFamiliya.getText().equals("")) familiya = "(NOM LIKE '%" + RezFamiliya.getText() + "%') AND";
        if (!RezImya.getText().equals("")) imya ="(PRENOM LIKE '%" + RezImya.getText() + "%') AND";
        if (!RezOtchestvo.getText().equals("")) otchestvo ="(PATRONYME LIKE '%" + RezOtchestvo.getText() + "%')";
        PATIENTS = EMK + familiya + imya + otchestvo;
        if (PATIENTS.equals("")){
            RezLabelLow.setTextFill(Color.RED);
            RezLabelLow.setText("Нужно хотя бы заполнить одно из полей!!!");
        } else {
            RezLabelLow.setTextFill(Color.BLACK);
            RezLabelLow.setText("");
            PATIENTS = EMK + familiya + imya + otchestvo;
            int x=1;
            while (x==1){   //удаление лишних ЭНДов
                x=0;
                if (PATIENTS.substring((PATIENTS.length() - 3), (PATIENTS.length())).equals("AND")){
                    PATIENTS = PATIENTS.substring(0, PATIENTS.length() - 3);
                     x=1;
                }
            }
            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            try {
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement StRezPat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQLRes = "USE Medialog710 \n" +
                        "SELECT PATIENTS_ID, N_OMON, NOM, PRENOM, PATRONYME, POL, NE_LE, TEL, MOBIL_TELEFON, RAB_TEL, E_MAIL\n" +
                        "FROM PATIENTS \n" +
                        "WHERE " + PATIENTS;
                System.out.println("//////////Выборка пациентов из PATIENTS в базе сиквела Medialog710\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLRes);
                ResultSet ResRezPat = StRezPat.executeQuery(SQLRes);
                ObservableList<RezPacData> dataclear = RezTablePac.getItems();
                dataclear.setAll();
                RezTablePac.setVisible(true);
                String SEX;
                String Telefon;
                String DR;
                String FinalOtchestvo;

                while (ResRezPat.next()) {
                    if (ResRezPat.getString("POL") == null){
                        SEX="Нет";
                    } else if (ResRezPat.getString("POL").equals("1")) {
                        SEX="Жен";
                    } else {
                        SEX="Муж";
                    }
                    if (ResRezPat.getString("MOBIL_TELEFON") == null){
                        if (ResRezPat.getString("TEL") == null){
                            if (ResRezPat.getString("RAB_TEL") == null){
                                Telefon = "не указан";
                            } else {
                                Telefon = ResRezPat.getString("RAB_TEL");
                            }
                        } else {
                            Telefon = ResRezPat.getString("TEL");
                        }
                    } else {
                        Telefon = ResRezPat.getString("MOBIL_TELEFON");
                    }
                    if (ResRezPat.getString("NE_LE") == null){
                        DR = "не указана";
                    } else {
                        DR = String.valueOf(fout.format(ResRezPat.getDate("NE_LE")).substring(0, 11));
                    }
                    if (ResRezPat.getString("PATRONYME") == null){
                        FinalOtchestvo = "не указано";
                    } else {
                        FinalOtchestvo = ResRezPat.getString("PATRONYME");
                    }
                    if (ResRezPat.getString("E_MAIL") == null){
                        EMAIL = "не указан";
                    } else {
                        EMAIL = ResRezPat.getString("E_MAIL");
                    }
                    RezPriemPatientsID = ResRezPat.getInt("Patients_ID");
                    ObservableList<RezPacData> data = RezTablePac.getItems();
                    data.add(new RezPacData(String.valueOf(RezPriemPatientsID), ResRezPat.getString("N_OMON"), ResRezPat.getString("NOM"), ResRezPat.getString("PRENOM"),
                            FinalOtchestvo, SEX, DR, Telefon, EMAIL ));
                }
                ResRezPat.close();
                StRezPat.close();
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

    //Показ типов приема Лаборатория выбранного пациента
    private void showRezPacData(RezPacData rezPacData) {
        if (rezPacData != null) {
            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            RezPacDR = rezPacData.getDataR();
            FIO=rezPacData.getFamiliya()+" "+rezPacData.getImya()+" "+rezPacData.getOtchestvo();
            try {

                Connection con = DriverManager.getConnection(connectionUrl);
                Statement StRezPriem = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQLRezPriem = "USE Medialog710 \n" +
                        "SELECT PATIENTS_ID, MOTCONSU_ID, REC_STATUS, DATE_CONSULTATION, MEROPRIQTIE, MODELENAME, mo.MODELS_ID \n" +
                        "FROM MOTCONSU m LEFT JOIN MODELS mo ON m.models_id=mo.models_id \n" +
                        "WHERE PATIENTS_ID=" + RezPriemPatientsID + " AND MEROPRIQTIE=6 \n" +
                        "ORDER BY DATE_CONSULTATION DESC \n";
                System.out.println("//////////Выборка типов записи 'Лаборатория' выбранного пациента из MOTCONSU в базе сиквела Medialog710\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLRezPriem);
                ResultSet ResRezPriem = StRezPriem.executeQuery(SQLRezPriem);
                ObservableList<RezPriemData> dataclear = RezTablePriem.getItems();
                dataclear.setAll();
                RezTablePriem.setVisible(true);
                String REC_STATUS;
                while (ResRezPriem.next()) {
                    if (ResRezPriem.getString("REC_STATUS").equals("A")){
                        REC_STATUS="Подтвержденные";
                    } else {
                        REC_STATUS="Предварительные";
                    }
                    RezPriemPatientsID = Integer.parseInt(rezPacData.getPat_ID());
                    ObservableList<RezPriemData> data = RezTablePriem.getItems();
                    data.add(new RezPriemData(String.valueOf(RezPriemPatientsID),(String.valueOf(ResRezPriem.getString("MOTCONSU_ID"))),(String.valueOf(ResRezPriem.getString("MODELS_ID"))),REC_STATUS,
                            (String.valueOf(fout.format(ResRezPriem.getDate("DATE_CONSULTATION")).substring(0, 11)) + ResRezPriem.getTime("DATE_CONSULTATION")),
                            ResRezPriem.getString("MODELENAME")));
                }
                ResRezPriem.close();
                StRezPriem.close();
                con.close();
            } catch (SQLException ignored) {
            }
        }
        RezHideSendBut();
    }

    //Скрыть кнопку и окно почты, чтобы не светились нигде кроме выбора результата анализов
    @FXML
    private void RezHideSendBut() {
        RezSend.setVisible(false);
        RezPochta.setVisible(false);
        RezPochtaLabel.setVisible(false);
        RezTableAnal.setVisible(false);
    }
    //Показать кнопку и окно почты, присвоить почту выбранного пациента
    public void RezShowSendBut() {
        RezSend.setVisible(true);
        RezPochta.setVisible(true);
        RezPochtaLabel.setVisible(true);
        RezPochta.setText(EMAIL);
    }
    //Очистка почти всех полей и скрытие всех элементов до позиции Старта
    public void RezClear(Event event) {
        RezHideSendBut();
        RezTablePac.setVisible(false);
        RezTablePriem.setVisible(false);
    }

    //Нажатие определенной записи лаборатории приводит к выбору всех анализов данной записи и формированию письма
    private void showRezPriemData(RezPriemData rezPriemData) {
        System.out.println("////////////////////Проверка анализов и занесение их в память!\\\\\\\\\\\\\\\\\\\\\\\\" + currenttime + "\n");
        RezLabelLow.setText("");
        RezLabelLow.setTextFill(Color.BLACK);
        if (rezPriemData != null) {
            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            try {
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement StRezAnal = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQLRezAnal = "USE Medialog710 \n" +
                        "EXECUTE USP_ANALYSIS_LIST \n" +
                        "" + rezPriemData.getMODELS_ID() + " \n" +
                        "," + rezPriemData.getMOTCONSU_ID() + " \n" +
                        ",'" + Login + "' \n";
                System.out.println("//////////Исполнение хранимой процедуры по составлению анализов в базе сиквела Medialog710\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLRezAnal);
                if (LogPas.equals("Moderator")){
                    RezTableAnal.setVisible(true);
                }
                try {
                    StRezAnal.executeQuery(SQLRezAnal);
                }catch (SQLException sqle){
                    System.out.println("Внимание! Произошла ошибка в Базе Данных SQL! Код ошибки: "+sqle+"\n");
                }
                String SQLRezAnalTab = "USE Medialog710 \n" +
                        "SELECT * FROM ANALYSIS_REPORTS \n" +
                        "WHERE USERID='" + Login + "' \n";
                ResultSet ResRezAnal = null;
                System.out.println("//////////Выбор из универсальной таблицы анализов в базе сиквела Medialog710, сформированной движком\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQLRezAnalTab);
                try {
                    ResRezAnal = StRezAnal.executeQuery(SQLRezAnalTab);
                } catch (SQLException sqle){
                    System.out.println("ALARM! Произошла ошибка в Базе Данных SQL! Код ошибки: "+sqle+"\n");
                }
                ObservableList<RezAnalData> dataclear = RezTableAnal.getItems();
                dataclear.setAll();
                if (ResRezAnal != null) {
                    String ANALYSIS_TABLE=""; String checkANALYSIS_TABLE="";
                    while (ResRezAnal.next()) {
                        ObservableList<RezAnalData> data = RezTableAnal.getItems();
                        data.add(new RezAnalData(ResRezAnal.getString("RVAL"), ResRezAnal.getString("LABEL"), ResRezAnal.getString("ANALYSIS_LABEL")));
                        ANALYSIS_TABLE=ResRezAnal.getString("ANALYSIS_LABEL");
                        if (!checkANALYSIS_TABLE.equals(ANALYSIS_TABLE)){
                            RezAnaliz.add(ANALYSIS_TABLE);
                            System.out.println("Найден новый тип анализа: "+ANALYSIS_TABLE);
                            checkANALYSIS_TABLE=ANALYSIS_TABLE;
                            RezLabelLow.setText("Анализы найдены! Проверьте адрес электронной почты пациента и нажмиет кнопку \"Отправить результаты\"");
                            RezLabelLow.setTextFill(Color.GREEN);
                            RezShowSendBut();
                        }
                    }
                    if (checkANALYSIS_TABLE.equals("")){
                        RezLabelLow.setText("Не найдено ни одного анализа!");
                        RezLabelLow.setTextFill(Color.RED);
                        RezHideSendBut();
                    }
                    ResRezAnal.close();
                } else {
                    System.out.println("Программа не смогла найти данные по этому виду анализов!\n");
                }
                StRezAnal.close();
                con.close();
            } catch (SQLException ignored) {
            }
            //левый поток на надпись о заканчивании формирования письма
        }
    }

    //Отослать письмо. Да, все так просто. У пользователя будет просто выбор результата и кнопка. Он не будет знать, что за ад будет творится под покровом этого ужаса
    public void RezSend(ActionEvent actionEvent) throws MessagingException {
        EmailCreator EC = new EmailCreator();
        System.out.println("Массив типов анализов: "+RezAnaliz);
        String RezEmailText=EmailCreator.Create(RezAnaliz);
        RezAnaliz.clear();
        System.out.println("Возврат класса E-Mail Creator: "+RezEmailText);
//        if (EMAIL.matches("^[a-zA-Zа-яА-ЯёЁ0-9-_].*@{1}.*[a-zA-Zа-яА-ЯёЁ0-9-_]$")){
//            String SMTP_AUTH_USER = "results@alfamed-nsk.ru";
//            String SMTP_AUTH_PWD = "VahVah123";
//            Properties props = new Properties();
//            props.put("mail.transport.protocol", "smtps");
//            props.put("mail.smtps.host", SMTP_AUTH_USER);
//            props.put("mail.smtps.auth", "true");
//            props.put("mail.smtp.sendpartial", "true");
//            props.put("mail.mime.charset", "UTF-8");
//            Session session = Session.getDefaultInstance(props);
//            session.setDebug(true);
//            Transport transport = session.getTransport();
//            transport.connect("smtp.gmail.com", 465, SMTP_AUTH_USER, SMTP_AUTH_PWD);
//            MimeMessage message = new MimeMessage(session);
//            message.setSubject("Результаты анализов ООО ЛДЦ \"АльфаМед\"");
//            message.setContent(RezEmailText,"text/html; charset=utf-8");
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(EMAIL));
//            message.setSentDate(new Date());
//            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
//            RezLabelLow.setText("Письмо успешно отправлено!");
//            RezLabelLow.setTextFill(Color.GREEN);
//        } else {
//            RezLabelLow.setText("НЕВЕРНЫЙ E-MAIL!!! Проверьте правильность введенного в МИС Медиалог почтового ящика пациента.");
//            RezLabelLow.setTextFill(Color.RED);
//        }
    }
}