package dataaccessSEQUEL;

import dataaccess.AuthDAO;
import dataaccess.SequelAuthDAO;
import exception.ExceptionResponse;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static service.UserService.generateToken;

public class SEQUELAuthDAOTests extends dataaccessSQLAbstractClass {
    @Test
    @DisplayName("Successful Clear")
    public void clearSuccess() {
        AuthDAO auths = new SequelAuthDAO();
        String newAuthToken = generateToken();
        AuthData newAuth = new AuthData(newAuthToken,"pwynn");
        try {
            auths.createAuth(newAuth);
            auths.clear();
            assertEquals(0,checkNumRows("AuthData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Create")
    public void createSuccess(){
        AuthDAO auths = new SequelAuthDAO();
        String newAuthToken = generateToken();
        AuthData newAuth = new AuthData(newAuthToken,"pwynn");
        try {
            auths.createAuth(newAuth);
            AuthData authToCheck = auths.getAuth(newAuthToken);
            assertEquals(newAuthToken, authToCheck.getAuth());
            auths.clear();
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }
    // same usernames different auth tokens
    @Test
    @DisplayName("Same usernames, failed auth tokens")
    public void createFailure() throws ExceptionResponse{
        AuthDAO auths = new SequelAuthDAO();
        String authToken1 = generateToken();
        String authToken2 = generateToken();
        AuthData authData1  = new AuthData(authToken1,"pwynn");
        AuthData authData2 = new AuthData(null,"pwynn");

        auths.createAuth(authData1);
        assertThrows(ExceptionResponse.class,() ->auths.createAuth(authData2));
        auths.clear();

    }

    @Test
    @DisplayName ("create and get correct auth")
    public void getSuccess() {
        AuthDAO auths = new SequelAuthDAO();

        String authToken1 = generateToken();
        String authToken2 = generateToken();
        AuthData authData1  = new AuthData(authToken1,"pwynn");
        AuthData authData2 = new AuthData(authToken2,"pwynn");

        try {
            auths.createAuth(authData1);
            auths.createAuth(authData2);
            auths.getAuth(authToken1);
            auths.clear();
        }
        catch(ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }

    }
    @Test
    @DisplayName ("getAuth with incorrect auth token")
    public void getFailure() {
        AuthDAO auths = new SequelAuthDAO();

        String authToken1 = generateToken();
        String authToken2 = generateToken();
        AuthData authData1  = new AuthData(authToken1,"jim");
        AuthData authData2 = new AuthData(authToken2,"brown");

        try {
            auths.createAuth(authData1);
            auths.createAuth(authData2);
            assertThrows(ExceptionResponse.class,()->auths.getAuth("wrongToken"));
            auths.clear();
        }
        catch(ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }
    @Test
    @DisplayName ("delete an auth that does exist")
   public void deleteSuccess() {
       AuthDAO auths = new SequelAuthDAO();
       String authToken1 = generateToken();

       AuthData authData1  = new AuthData(authToken1,"jim");

       try {
           auths.clear();
           auths.createAuth(authData1);
           auths.deleteAuth(authToken1);
           assertEquals(0,checkNumRows("AuthData"));
       } catch (ExceptionResponse e) {
           System.out.println("Thrown exception: " + e.getMessage());
       }

   }

    @Test
    @DisplayName ("delete an auth that doesn't exist")
    public void deleteFailure() {
        AuthDAO auths = new SequelAuthDAO();
        String authToken = generateToken();
        AuthData authData  = new AuthData(authToken,"jim");
        try {
            auths.createAuth(authData);
            assertThrows(ExceptionResponse.class,()->auths.deleteAuth("randomToken"));
        }catch(ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }
}
