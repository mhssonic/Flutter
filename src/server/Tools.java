package server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import server.database.SecretKeyDB;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

public class Tools {
    public static int jenkinsHash(int a, int b, boolean sort) {
        if (sort && a < b){
            int c = a;
            a = b;
            b = c;
        }
        int hash = 0;

        hash += a;
        hash += hash << 10;
        hash ^= hash >>> 6;

        hash += b;
        hash += hash << 10;
        hash ^= hash >>> 6;

        hash += hash << 3;
        hash ^= hash >>> 11;
        hash += hash << 15;

        return hash;
    }

    public static String jenkinsHash(String s1, String s2, boolean sort) {
        // Sort the strings to ensure that the order of the arguments doesn't matter
        String[] sortedStrings = new String[]{s1, s2};
        if(sort)
            Arrays.sort(sortedStrings);

        // Compute the hash value using the Jenkins hash function
        int hash = jenkinsHashCode(sortedStrings[0], sortedStrings[1]);

        // Format the hash value as a fixed-length string of 16 characters
        String hashString = Integer.toHexString(hash);
        if(hashString.length() > 16)
            return hashString.substring(0, 16);
        return hashString;
    }

    private static int jenkinsHashCode(String s1, String s2) {
        byte[] bytes1 = s1.getBytes();
        byte[] bytes2 = s2.getBytes();
        int hash = 0;
        int len1 = bytes1.length;
        int len2 = bytes2.length;
        int offset = 0;
        for (int i = 0; i < len1; i++) {
            hash += (bytes1[i] & 0xFF) << offset;
            offset += 8;
            if (offset == 32) {
                offset = 0;
            }
        }
        for (int i = 0; i < len2; i++) {
            hash += (bytes2[i] & 0xFF) << offset;
            offset += 8;
            if (offset == 32) {
                offset = 0;
            }
        }
        hash = mix(hash);
        return hash;
    }

    private static int mix(int hash) {
        hash += (hash << 12);
        hash ^= (hash >>> 22);
        hash += (hash << 4);
        hash ^= (hash >>> 9);
        hash += (hash << 10);
        hash ^= (hash >>> 2);
        hash += (hash << 7);
        hash ^= (hash >>> 12);
        return hash;
    }

    public static String creatJWT(String id, LocalDate createdAt, LocalDate expiration){
        String key = SecretKeyDB.getSecretKey();

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(Date.from(createdAt.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .setExpiration(Date.from(expiration.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {
        String key = SecretKeyDB.getSecretKey();
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(jwt).getBody();
            return claims;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
