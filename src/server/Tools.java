package server;

import java.util.Arrays;

public class Tools {
    public static String jenkinsHash(String s1, String s2) {
        // Sort the strings to ensure that the order of the arguments doesn't matter
        String[] sortedStrings = new String[]{s1, s2};
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
}
