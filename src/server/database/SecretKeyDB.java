package server.database;

public class SecretKeyDB extends SQLDB{

    public static String getSecretKey() {
        return (String) SQLDB.getFieldObject("secret_key", "token", "value");
    }
}
