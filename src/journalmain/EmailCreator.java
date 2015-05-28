package journalmain;


import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class EmailCreator {

    private static LocalTime currenttime = LocalTime.now();

    public static String Create(ArrayList<String> RezAnal) {
        String vah = null;
        String filename;
        System.out.println("Нулевой индекс массива соддержит: " + RezAnal.get(0));

        for (String Anal : RezAnal){
            String connectionUrl = "jdbc:sqlserver://APP104;databaseName=Journal;user=Java;password=VahVah123";
            try {
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String SQL = "USE Journal \n" +
                        "SELECT FILE_NAME \n" +
                        "FROM RezTablesFiles \n" +
                        "WHERE AT_LABEL='" + Anal + "' \n";
                ResultSet res = null;
                System.out.println("//////////Выборка имён файлов Шаблонов\\\\\\\\\\" + currenttime + "\n");
                System.out.println(SQL);
                try {
                    res = st.executeQuery(SQL);
                } catch (SQLException sqle){
                    System.out.println("ALARM! Произошла ошибка в Базе Данных SQL Journal при работе с таблицей RezTablesFiles.  Код ошибки: "+sqle+"\n");
                }
                if (res != null) {
                    while (res.next()) {
                        filename = res.getString("FILE_NAME");
                        System.out.println(Parser.parse("\\\\192.168.1.8\\data_user\\docs\\Медцентр\\Администраторы\\Программа Журнал Администраторов\\Templates\\" + filename + ".xls"));
                        if (RezAnal.contains("Кал на дисбактериоз")) {
                            System.out.println("И этоооо БАК!");
                            vah = "CHEСK ALRIGHT!";
                        } else {
                            vah = "NOTHING!!";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vah;
    }
}
