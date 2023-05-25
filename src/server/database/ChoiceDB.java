package server.database;

import server.message.Attachment;
import server.message.Tweet.poll.Choice;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChoiceDB extends SQLDB{

    public static int createChoice(Choice choice) {
        try {
            Integer[] votersId = new Integer[choice.getVoters().size()];
            Array voters = connection.createArrayOf("INT" , votersId);
            preparedStatement = connection.prepareStatement("INSERT INTO choice(context , voters) VALUES (?,?) returning id" );
            preparedStatement.setString(1, choice.getText());
            preparedStatement.setArray(2,voters);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer[] creatChoices(ArrayList<Choice> choices){
        Integer[] choiceId = new Integer[choices.size()];
        int i = 0;
        for ( Choice choice : choices) {
            choiceId[i] = ChoiceDB.createChoice(choice);
            i++;
        }
        return choiceId;
    }
}
