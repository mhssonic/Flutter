package server.enums;

public enum TweetType {
    TWEET(0),
    COMMENT(1),
    RETWEET(2),
    POLL(3),
    QUOTE_TWEET(4),
    MESSAGE(5);
    private final int mod;
    public final static int count = 6;
    TweetType(int mod){
        this.mod = mod;
    }

    public int getMod() {
        return mod;
    }


}
