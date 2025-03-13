package dataaccessSEQUEL;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import exception.ExceptionResponse;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class dataaccessSQLAbstractClass {
    public final String checkNumRowsCommandAuths = "SELECT COUNT(*) FROM AuthData;";
    public final String checkNumRowsCommandUsers = "SELECT COUNT(*) FROM UserData;";
    public final String checkNumRowsCommandGames = "SELECT COUNT(*) FROM GameData;";

    public int numRows;

    public int checkNumRows(String table_name) throws ExceptionResponse {
        try (var conn = DatabaseManager.getConnection()) {
            String checkNumRowsCommand;
            if (table_name == "AuthData") {
                checkNumRowsCommand = checkNumRowsCommandAuths;
            } else if (table_name == "UserData") {
                checkNumRowsCommand = checkNumRowsCommandUsers;
            }else {
                checkNumRowsCommand = checkNumRowsCommandGames;
            }

            try (var stmt = conn.prepareStatement(checkNumRowsCommand)) {
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()) {
                        numRows = rs.getInt(1);
                    }
                }
            }catch (SQLException e) {
                throw new ExceptionResponse(500, e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, e.getMessage());
        }
        return numRows;
    }
}
