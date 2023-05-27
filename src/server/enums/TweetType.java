package server.enums;

public enum TweetType {
    TWEET(0),
    RETWEET(1),
    POLL(2),
    QUOTE_TWEET(3),
    MESSAGE(4),
    COMMENT(5);
    private final int mod;
    public final static int count = 6;
    TweetType(int mod){
        this.mod = mod;
    }

    public int getMod() {
        return mod;
    }


}
