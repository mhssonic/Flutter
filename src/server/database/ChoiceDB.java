package server.database;

import server.enums.error.ErrorType;
import server.message.tweet.poll.Choice;

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

    public static ArrayList<Choice> getChoice(int[] choiceIds) {
        ResultSet resultSet;
        ArrayList<Choice> choices = new ArrayList<>();
        for (int choiceId : choiceIds) {
            resultSet = getResultSet("choice", choiceId);
            try {
                if (resultSet.next()) {
                    String context = resultSet.getString("context");
                    Object[] voters = (Object[]) (resultSet.getArray("voters").getArray());

                    Choice choice = new Choice(context, voters);
                    choices.add(choice);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return choices;
    }

    public static ErrorType addVoters(int userId , int choiceId){
        if(containInArrayFieldObject("choice" , choiceId , "voters" , userId)){
            return ErrorType.ALREADY_VOTED;
        }
        appendToArrayField("choice" , choiceId , "voters" , userId );
        return ErrorType.SUCCESS;
    }
}