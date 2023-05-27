package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChoiceDB extends SQLDB {

    public static int createChoice(String choice) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO choice(context ) VALUES (?) returning id");
            preparedStatement.setString(1, choice);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer[] creatChoices(ArrayList<String> choices) {
        Integer[] choiceId = new Integer[choices.size()];
        int i = 0;
        for (String choice : choices) {
            choiceId[i] = ChoiceDB.createChoice(choice);
            i++;
        }
        return choiceId;
    }
}
