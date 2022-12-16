package de.hda.fbi.ds.mbredel;

import java.sql.*;

public class Database{

    String jdbcURL = "jdbc:postgresql://localhost:5432/postgres"; //hier soll auch angepasst werden
    String username = "postgres";
    String password = "1234IzzoReng";//hier musst du dein eigenes Password einlegen
                 //nachdem du lokales Datenbank PostgresSQl erstellst.
    Connection connection;
    public boolean connection(){
        try {
            connection = DriverManager.getConnection(jdbcURL,username,password);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            System.out.println("Error in Connection");
            throw new RuntimeException(e);
        }
        return true;
    }
    private static final String INSERT_USERS_SQL = "INSERT INTO information" +
            "  (firstname, lastname) VALUES " +
            " (?, ?);";

    //HTTP-POST uses insertData to save the information in the Database
    public void insertData(String firstname, String lastname) throws SQLException {
        if(connection() == true){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
                preparedStatement.setString(1, firstname);
                preparedStatement.setString(2, lastname);
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection.close();
    }
    private static final String INSERT_USERS_SQL1 = "INSERT INTO resultmatrix" +
            "  (result_matrix) VALUES " +
            " (?);";

    private static final String ausgabe_SQL2 = "SELECT * FROM resultmatrix;";
    //HTTP-POST uses insertData to save the information in the Database
    public void insertDataResult(int result) throws SQLException {
        if(connection() == true){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL1);
                preparedStatement.setInt(1, result);
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection.close();
    }

    public void DataAusgabe() throws SQLException {
        if(connection() == true){
            try {
                //PreparedStatement preparedStatement = connection.prepareStatement(ausgabe_SQL2);
                //System.out.println(preparedStatement);
                //preparedStatement.executeUpdate();

                Statement stm = connection.createStatement();
                ResultSet rs =stm.executeQuery(ausgabe_SQL2);

                while(rs.next()){
                    System.out.println(rs.getString(1));
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection.close();
    }
}