package server.enums;

public enum TweetType {
    TWEET(0),
    RETWEET(1),
    POLL(2),
    QUOTE_TWEET(3),
    MESSAGE(4);
    private final int mod;
    TweetType(int mod){
        this.mod = mod;
    }
}
