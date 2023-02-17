package de.hda.fbi.ds.mbredel;

import java.sql.*;

public class Database{

    String jdbcURL = "jdbc:postgresql://db:5432/postgres"; //statt localhost db  für docker compose
    String username = "postgres";
    String password = "1234IzzoReng";
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
    private static final String CRATE_SQL = "create table matrixergebnisse("+
            "zeile_i int NOT NULL," +
            "spalte_j int NOT NULL," +
            "ergebniss int NOT NULL)";
    private static final String INSERT_SQL = "INSERT INTO matrixergebnisse" +
            "  (zeile_i, spalte_j, ergebniss) VALUES " +
            " (?,?,?);";
    private static final String ausgabe_SQL = "SELECT * FROM matrixergebnisse;";
    private static final String loeschen_SQL = "Delete FROM matrixergebnisse *;";


    public void creteTable() throws SQLException {
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(CRATE_SQL);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connection.close();

        }
    }

    public void insertDataResultMitAngaben(int zeile , int spalte, int result) throws SQLException {
        if(connection != null){
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);
                preparedStatement.setInt(1, zeile);
                preparedStatement.setInt(2, spalte);
                preparedStatement.setInt(3, result);
                System.out.println(preparedStatement);
                System.out.println("Wurde in die Datenbank geschrieben");
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection.close();
    }
    public void DatenAusgeben() throws SQLException {
        if(connection != null){
            try {
                Statement stm = connection.createStatement();
                ResultSet rs =stm.executeQuery(ausgabe_SQL);

                while(rs.next()){
                    System.out.print("Zeile i: " + rs.getString(1));
                    System.out.print("  |Spalte j: "+ rs.getString(2));
                    System.out.println("  |zelle-Ergebnis: "+ rs.getString(3));
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        connection.close();
    }

    public void loeschen() throws SQLException {
        if (connection != null) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(loeschen_SQL);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            connection.close();

        }
    }
}