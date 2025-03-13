package dataaccessSEQUEL;


import dataaccess.GameDAO;

import dataaccess.SequelGameDAO;
import exception.ExceptionResponse;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static service.UserService.generateToken;

public class SEQUELGameDAOTests extends dataaccessSQLAbstractClass{
    @BeforeEach
    public void clear() {
        GameDAO games = new SequelGameDAO();
        try {
            games.clear();
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Successful Clear")
    public void clearSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "first game";
        try {
            games.createGame(gameName);
            games.clear();
            assertEquals(0,checkNumRows("GameData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void createGameSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "first game";
        try {
            games.createGame(gameName);
            assertNotEquals(0,checkNumRows("GameData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void createGameFailure() {
        GameDAO games = new SequelGameDAO();
        String gameName = "";
        try {
            games.createGame(gameName);
            assertNotEquals(0,checkNumRows("GameData"));
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void listgamesSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            Collection<GameData> gameList =  games.listGames();

            for (GameData game : gameList) {
                assertEquals(gameName,game.getGameName());
            }
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void listGamesFailure() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            Collection<GameData> gameList =  games.listGames();

            for (GameData game : gameList) {
                assertEquals(gameName,game.getGameName());
            }
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void updateWhiteSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        String username = "benny";
        try {
            games.createGame(gameName);
            games.updateGameWhiteUsername(username,1);
            Collection<GameData> gameList =  games.listGames();
            for (GameData game : gameList) {
                assertEquals(username,game.getWhiteUsername());
            }

        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void updateWhiteFailure() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            assertThrows(ExceptionResponse.class,()->games.updateGameWhiteUsername("username",4));

        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void upBlackSuccess() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        String username = "benny";
        try {
            games.createGame(gameName);
            games.updateGameBlackUsername(username,1);
            Collection<GameData> gameList =  games.listGames();
            for (GameData game : gameList) {
                assertEquals(username,game.getBlackUsername());
            }
        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void upBlackFail() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            assertThrows(ExceptionResponse.class,()->games.updateGameBlackUsername("username",4));

        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void getGameS() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            assertNotNull(games.getGame(1));

        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }
    }

    @Test
    void getGameF() {
        GameDAO games = new SequelGameDAO();
        String gameName = "game1";
        try {
            games.createGame(gameName);
            assertThrows(ExceptionResponse.class,()->games.getGame(4));

        } catch (ExceptionResponse e) {
            System.out.println("Thrown exception: " + e.getMessage());
        }

    }
}
