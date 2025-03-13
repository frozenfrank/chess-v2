package dataaccess;

import exception.ExceptionResponse;
import model.UserData;


import java.sql.ResultSet;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.*;


public class SequelUserDAO implements UserDAO {
    public SequelUserDAO()  {
        try {
            createDatabase();
        }catch(DataAccessException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE UserData;";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }catch(DataAccessException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String usrnm) throws ExceptionResponse{
        UserData user = null;
        String sql = "SELECT username, password, email FROM UserDATA WHERE username = ?;";
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)){
                stmt.setString(1,usrnm);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String username = rs.getString(1);
                        String password = rs.getString(2);
                        String email = rs.getString(3);
                        user = new UserData(username, password, email);
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
        return user;
    }

    @Override
    public void createUser(UserData userData) throws ExceptionResponse{
        String username = userData.getUsername();
        String password = userData.getPassword();
        String email = userData.getEmail();

        String sql = "INSERT INTO UserData (username, password, email) VALUES ( ?, ?, ?);";
        try(var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement(sql)){
                stmt.setString(1, username);  // Set username
                stmt.setString(2, password);  // Set password
                stmt.setString(3, email);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new ExceptionResponse(500,e.getMessage());
            }
        } catch (SQLException | DataAccessException e) {
            throw new ExceptionResponse(500, "There was an Error accessing the database");
        }
    }




}
