package journalmain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PochData {
    private final SimpleIntegerProperty TaskID = new SimpleIntegerProperty();
    private final SimpleIntegerProperty UserID = new SimpleIntegerProperty();
    private final SimpleStringProperty UserName = new SimpleStringProperty();
    private final SimpleStringProperty InMailID = new SimpleStringProperty();
    private final SimpleStringProperty Status = new SimpleStringProperty();
    private final SimpleStringProperty TaskDate = new SimpleStringProperty();
    private final SimpleStringProperty InWorkDate = new SimpleStringProperty();
    private final SimpleStringProperty DoneDate = new SimpleStringProperty();
    private final SimpleStringProperty Comment = new SimpleStringProperty();
    private final SimpleStringProperty ReceiveDate = new SimpleStringProperty();
    private final SimpleStringProperty SentDate = new SimpleStringProperty();
    private final SimpleStringProperty Tema = new SimpleStringProperty();
    private final SimpleStringProperty CancelDate = new SimpleStringProperty();
    private final SimpleStringProperty MailContent = new SimpleStringProperty();
    private final SimpleStringProperty InWorkBy = new SimpleStringProperty();
    private final SimpleStringProperty DoneBy = new SimpleStringProperty();
    private final SimpleStringProperty CancelBy = new SimpleStringProperty();

    public PochData() {
        this(0,0, "", "", "", "", "", "", "", "", "", "", "","","","","");
    }

    public PochData(Integer TaskID, Integer UserID, String UserName, String InMailID, String Status, String TaskDate, String InWorkDate, String DoneDate,
                    String Comment, String ReceiveDate, String SentDate, String Tema, String CancelDate, String MailContent, String InWorkBy, String DoneBy, String CancelBy) {
        setTaskID(TaskID);
        setUserID(UserID);
        setUserName(UserName);
        setInMailID(InMailID);
        setStatus(Status);
        setTaskDate(TaskDate);
        setInWorkDate(InWorkDate);
        setDoneDate(DoneDate);
        setComment(Comment);
        setReceiveDate(ReceiveDate);
        setSentDate(SentDate);
        setTema(Tema);
        setCancelDate(CancelDate);
        setMailContent(MailContent);
        setInWorkBy(InWorkBy);
        setDoneBy(DoneBy);
        setCancelBy(CancelBy);
    }

    public Integer getTaskID() {
        return TaskID.get();
    }

    public void setTaskID(Integer fName) {
        TaskID.set(fName);
    }

    public SimpleIntegerProperty TaskIDProperty(){
        return TaskID;
    }

    public Integer getUserID() {
        return UserID.get();
    }

    public void setUserID(Integer fName) {
        UserID.set(fName);
    }

    public SimpleIntegerProperty UserIDProperty(){
        return UserID;
    }

    public String getUserName() {
        return UserName.get();
    }

    public void setUserName(String fName) {
        UserName.set(fName);
    }

    public SimpleStringProperty UserNameProperty(){
        return UserName;
    }

    public String getInMailID() {
        return InMailID.get();
    }

    public void setInMailID(String fName) {
        InMailID.set(fName);
    }

    public SimpleStringProperty InMailIDProperty(){
        return InMailID;
    }

    public String getStatus() {
        return Status.get();
    }

    public void setStatus(String fName) {
        Status.set(fName);
    }

    public SimpleStringProperty StatusProperty(){
        return Status;
    }

    public String getTaskDate() {
        return TaskDate.get();
    }

    public void setTaskDate(String fName) {
        TaskDate.set(fName);
    }

    public SimpleStringProperty TaskDateProperty(){
        return TaskDate;
    }

    public String getInWorkDate() {
        return InWorkDate.get();
    }

    public void setInWorkDate(String fName) {
        InWorkDate.set(fName);
    }

    public SimpleStringProperty InWorkDateProperty(){
        return InWorkDate;
    }

    public String getDoneDate() {
        return DoneDate.get();
    }

    public void setDoneDate(String fName) {
        DoneDate.set(fName);
    }

    public SimpleStringProperty DoneDateProperty(){
        return DoneDate;
    }

    public String getComment() {
        return Comment.get();
    }

    public void setComment(String fName) {
        Comment.set(fName);
    }

    public SimpleStringProperty CommentProperty(){
        return Comment;
    }

    public String getReceiveDate() {
        return ReceiveDate.get();
    }

    public void setReceiveDate(String fName) {
        ReceiveDate.set(fName);
    }

    public SimpleStringProperty ReceiveDateProperty(){
        return ReceiveDate;
    }

    public String getSentDate() {
        return SentDate.get();
    }

    public void setSentDate(String fName) {
        SentDate.set(fName);
    }

    public SimpleStringProperty SentDateProperty(){
        return SentDate;
    }

    public String getTema() {
        return Tema.get();
    }

    public void setTema(String fName) {
        Tema.set(fName);
    }

    public SimpleStringProperty TemaProperty(){
        return Tema;
    }

    public String getCancelDate() {
        return CancelDate.get();
    }

    public void setCancelDate(String fName) {
        CancelDate.set(fName);
    }

    public SimpleStringProperty CancelDateProperty(){
        return CancelDate;
    }

    public String getMailContent() {
        return MailContent.get();
    }

    public void setMailContent(String fName) {
        MailContent.set(fName);
    }

    public SimpleStringProperty MailContentProperty(){
        return MailContent;
    }

    public String getInWorkBy() {
        return InWorkBy.get();
    }

    public void setInWorkBy(String fName) {
        InWorkBy.set(fName);
    }

    public SimpleStringProperty InWorkByProperty(){
        return InWorkBy;
    }

    public String getDoneBy() {
        return DoneBy.get();
    }

    public void setDoneBy(String fName) {
        DoneBy.set(fName);
    }

    public SimpleStringProperty DoneByProperty(){
        return DoneBy;
    }

    public String getCancelBy() {
        return CancelBy.get();
    }

    public void setCancelBy(String fName) {
        CancelBy.set(fName);
    }

    public SimpleStringProperty CancelByProperty(){
        return CancelBy;
    }
}