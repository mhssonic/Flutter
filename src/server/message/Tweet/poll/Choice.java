package server.message.Tweet.poll;

import java.util.ArrayList;

public class Choice {
    private int choiceId;
    private String text;
    private ArrayList<String> voters;

    public Choice(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public ArrayList<String> getVoters() {
        return voters;
    }

}
