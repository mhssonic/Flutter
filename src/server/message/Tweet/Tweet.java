package server.message.Tweet;

import server.user.User;

import java.util.ArrayList;
import java.util.HashSet;

public class Tweet {
    HashSet<User> like = new HashSet<>();
    HashSet<String> comment;
    ArrayList<String> hashtag;
    int retweetCount;
    Boolean faveStar;



}
