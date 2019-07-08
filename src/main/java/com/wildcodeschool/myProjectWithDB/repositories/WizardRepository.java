package com.wildcodeschool.myProjectWithDB.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.wildcodeschool.myProjectWithDB.entities.Wizard;

public class WizardRepository {

    private final static String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "password";

    public static List<Wizard> selectByLastname(String lastname) {
        try(
            Connection connection = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM wizard WHERE lastname LIKE ?"
            );
        ) {
            statement.setString(1, lastname);

            try(
                ResultSet resulSet = statement.executeQuery();
            ) {
                List<Wizard> wizards = new ArrayList<Wizard>();

                while(resulSet.next()){
                    int id = resulSet.getInt("id");
                    String firstname = resulSet.getString("firstname");
                    lastname = resulSet.getString("lastname");
                    Date birthday = resulSet.getDate("birthday");
                    String birthPlace = resulSet.getString("birth_place");
                    String biography = resulSet.getString("biography");
                    boolean isMuggle = resulSet.getBoolean("is_muggle");

                    wizards.add(new Wizard(id, firstname, lastname, birthday, birthPlace, biography, isMuggle));
                }

                return wizards;
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }

    public static Wizard selectById(int id) {
        try(
            Connection connection = DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD
            );
            PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM wizard WHERE id=?"
            );
        ) {
            statement.setInt(1, id);

            try(
                ResultSet resulSet = statement.executeQuery();
            ) {
                if(resulSet.next()){
                    String firstname = resulSet.getString("firstname");
                    String lastname = resulSet.getString("lastname");
                    Date birthday = resulSet.getDate("birthday");
                    String birthPlace = resulSet.getString("birth_place");
                    String biography = resulSet.getString("biography");
                    boolean isMuggle = resulSet.getBoolean("is_muggle");

                    return new Wizard(id, firstname, lastname, birthday, birthPlace, biography, isMuggle);
                }
                else {
                    throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "unknown id in table wizards"
                    );
                }
            }
        }
        catch (SQLException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "", e
            );
        }
    }
    public static int insert(
    String firstname,
    String lastname,
    Date birthday,
    String birthPlace,
    String biography,
    Boolean isMuggle
) {
    try(
        Connection connection = DriverManager.getConnection(
            DB_URL, DB_USER, DB_PASSWORD
        );
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO wizard (firstname, lastname, birthday, birth_place, biography, is_muggle) VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
    ) {
        statement.setString(1, firstname);
        statement.setString(2, lastname);
        statement.setDate(3, birthday);
        statement.setString(4, birthPlace);
        statement.setString(5, biography);
        statement.setBoolean(6, isMuggle);

        if(statement.executeUpdate() != 1) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "failed to insert data"
            );
        }

        try(
            ResultSet generatedKeys = statement.getGeneratedKeys();
        ) {
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
            else {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "failed to get inserted id"
                );
            }
        }
    }
    catch (SQLException e) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, "", e
        );
    }
}
}
