package dataaccess;

import exception.ExceptionResponse;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.createDatabase;

public class SequelAuthDAO implements AuthDAO{
    private static final String createAuthData = "INSERT INTO AuthData (authToken, username) VALUES ( ?, ?);";

    private static final String getAuthData = "SELECT authToken, username FROM AuthDATA WHERE authToken = ?;";

    private static final String deleteAuth = "DELETE FROM AuthData WHERE authToken = ?;";


    public SequelAuthDAO() {
        try{
            createDatabase();
        }catch(DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clear() throws ExceptionResponse{
        try(var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE AuthData;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch(DataAccessException | SQLException e) {
            throw new ExceptionResponse(500,e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws ExceptionResponse{
        String username = authData.getUsername();
        String authToken = authData.getAuth();

        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(createAuthData)){
                stmt.setString(1, authToken);  // Set username
                stmt.setString(2, username);  // Set password
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new ExceptionResponse(500,e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
    }

    @Override
    public AuthData getAuth(String authTkn) throws ExceptionResponse{
        AuthData authData = null;
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(getAuthData)){
                stmt.setString(1,authTkn);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String authToken = rs.getString(1);
                        String username = rs.getString(2);
                        authData = new AuthData(authToken,username);
                    }
                    else {
                        return null;
                    }
                }
            } catch (SQLException e) {
                throw new ExceptionResponse(500,e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
        return authData;
    }

    @Override
    public void deleteAuth(String authToken) throws ExceptionResponse{
        try(var conn = DatabaseManager.getConnection()) {
            try(var stmt = conn.prepareStatement(deleteAuth)){
                stmt.setString(1,authToken);
                int rowsDeleted = stmt.executeUpdate();
                if(rowsDeleted == 0) {
                    throw new ExceptionResponse(500,"Error: no rows in AuthData were deleted");
                }
            } catch(SQLException e) {
                throw new ExceptionResponse(500,e.getMessage());
            }
        }catch (DataAccessException | SQLException e) {
            throw new ExceptionResponse(500,"Error accessing database");
        }
    }
}
