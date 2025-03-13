package dataaccess;


import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
//        try (var connOne = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
//            var statementOne = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
//            // Create the database
//            try (var preparedStatement = connOne.prepareStatement(statementOne)) {
//                preparedStatement.executeUpdate();
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
        createTable(createAuthData);
        createTable(createGameData);
        createTable(createUserData);
    }

    static void createTable(String table) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(table)) {
                    preparedStatement.executeUpdate();
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException(e.getMessage());
        }
    }

    private static final String createUserData =
            """
           CREATE TABLE IF NOT EXISTS UserData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
           """;

    private static final String createGameData =
            """
            CREATE TABLE IF NOT EXISTS GameData (
              `GameID` int NOT NULL,
              `WhiteUsername` varchar(256),
              `BlackUsername` varchar(256),
              `GameName` varchar(256) NOT NULL,
              `ChessGame` varchar(256) NOT NULL,
              PRIMARY KEY (`GameID`)
            )
            """;
    private static final String createAuthData =
            """
            CREATE TABLE IF NOT EXISTS AuthData (
              `AuthToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """;
    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
