package server.message.tweet.poll;

import java.util.ArrayList;

public class Choice {
    private int choiceId;
    private String text;
    private Object[] voters;

    public Choice(String text, Object[] voters) {
        this.text = text;
        this.voters = voters;
    }

    public String getText() {
        return text;
    }


    public Object[] getVoters() {
        return voters;
    }

}
