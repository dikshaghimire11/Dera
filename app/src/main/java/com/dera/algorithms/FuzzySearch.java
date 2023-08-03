package com.dera.algorithms;

public class FuzzySearch {
    public static boolean fuzzySearch(String userInput, String databaseEntry) {
        // Tokenization: Split the user input and database entry into tokens
        String[] userTokens = tokenize(userInput);
        String[] dbTokens = tokenize(databaseEntry);

        // Fuzzy Matching and Scoring
        int numMatchingTokens = 0;
        for (String userToken : userTokens) {
            for (String dbToken : dbTokens) {
                if (isFuzzyMatch(userToken, dbToken)) {
                    numMatchingTokens++;
                    break; // Match found, no need to check the rest of the dbTokens
                }
            }
        }
        // Calculate a score based on the number of matching tokens
        double similarityThreshold = 0.7; // Adjust this threshold as needed
        double score = (double) numMatchingTokens / userTokens.length;

        // Return true if the score meets the similarity threshold
        return score >= similarityThreshold;
    }

    public static String[] tokenize(String input) {
        // Preprocessing: Normalize tokens by converting to lowercase and removing punctuation
        return input.toLowerCase().split("\\W+");
    }

    public static boolean isFuzzyMatch(String str1, String str2) {
        // Fuzzy Matching using Levenshtein Distance
        int distance = levenshteinDistance(str1, str2);
        int maxLength = Math.max(str1.length(), str2.length());
        double similarity = 1.0 - (double) distance / maxLength;

        double similarityThreshold = 0.7; // Adjust this threshold as needed
        return similarity >= similarityThreshold;
    }

    public static int levenshteinDistance(String word1, String word2) {
        // Levenshtein Distance algorithm
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + cost, Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }
}
