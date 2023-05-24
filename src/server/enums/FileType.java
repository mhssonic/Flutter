package server.enums;

public enum FileType {
    IMAGE("image"),
    VIDEO("video");
    private final String sqlVersion;
    FileType(String sqlVersion){
        this.sqlVersion = sqlVersion;
    }

    public String getSqlVersion() {
        return sqlVersion;
    }
}
