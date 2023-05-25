package server.message.Tweet.poll;

import java.util.ArrayList;

public class Choice {
    private int choiceId;
    private String text;
    private ArrayList<String> voters;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getVoters() {
        return voters;
    }

    public void setVoters(ArrayList<String> voters) {
        this.voters = voters;
    }
}
