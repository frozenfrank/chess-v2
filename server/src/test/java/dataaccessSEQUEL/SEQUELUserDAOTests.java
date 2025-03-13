package dataaccessSEQUEL;

import dataaccess.SequelUserDAO;
import dataaccess.UserDAO;
import exception.ExceptionResponse;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

public class SEQUELUserDAOTests extends dataaccessSQLAbstractClass{
    @Test
    @DisplayName("Successful Clear")
    public void clearSuccess() {
        UserDAO users = new SequelUserDAO();

        users.clear();
        try {
            assertEquals(0,checkNumRows("UserData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Successful Create")
    public void createSuccess() throws ExceptionResponse{
        UserDAO users = new SequelUserDAO();
        UserData newUser = new UserData("pwynn","password","pwynn@me.com");
        users.createUser(newUser);
        UserData userToCheck = users.getUser("pwynn");
        assertEquals(newUser.getUsername(),userToCheck.getUsername());
        users.clear();
    }
    @Test
    @DisplayName("Failed Create")
    public void createFailure() {
        UserDAO users = new SequelUserDAO();
        UserData newUser = new UserData("pwynn",null,"pwynn@me.com");

        ExceptionResponse e = assertThrows(ExceptionResponse.class, () ->users.createUser(newUser));
        System.out.println("Thrown exception: " + e.getMessage());
        users.clear();
    }
    @Test
    @DisplayName("get user success")
    public void getUserSuccess() {

        UserDAO users = new SequelUserDAO();
        users.clear();
        UserData newUser = new UserData("pwynn","jimm","pwynn@me.com");
        UserData userToCheck = null;
        try {
            users.createUser(newUser);
            userToCheck = users.getUser("pwynn");
            assertEquals(newUser.getUsername(),userToCheck.getUsername());
        } catch(ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
            assertEquals(newUser.getUsername(),userToCheck.getUsername());
        }
        users.clear();
    }
    @Test
    @DisplayName("get user failure")
    public void getUserFailure() {
        UserDAO users = new SequelUserDAO();
        users.clear();
        UserData newUser1 = new UserData("pwynn","poder","pwynn@me.com");
        UserData newUser2 = new UserData("max","jim","max@me.com");
        try {
            users.createUser(newUser1);
            users.createUser(newUser2);
            UserData userToCheck = users.getUser("pwynn");
            assertEquals(newUser1.getUsername(),userToCheck.getUsername());

            ExceptionResponse e = assertThrows(ExceptionResponse.class,()->users.getUser("Makayla"));
            System.out.println("Thrown exception: " + e.getMessage());
        } catch(ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }


    }

}
