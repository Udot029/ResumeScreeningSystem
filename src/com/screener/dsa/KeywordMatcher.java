package com.screener.dsa;


import java.util.HashMap;
import java.util.List;


/**
 * Keyword Matching Algorithm Component utilizing HashMap.
 * 
 * Provides linear $O(n)$ text processing, token frequency extraction, 
 * skill dictionary matching, and overall match percentage calculation.
 */
public class KeywordMatcher {

    /**
     * Extracts word frequencies from raw text into a HashMap.
     * Sanitizes input by converting to lowercase and stripping non-alphanumeric characters.
     * 
     * Time Complexity: $O(n)$ where $n$ is the length of the text.
     * 
     * @param rawText Raw resume text string
     * @return HashMap mapping unique cleaned words to their occurrence count
     */
    public HashMap<String, Integer> buildWordFrequencyMap(String rawText) {
        HashMap<String, Integer> wordMap = new HashMap<>();
        
        if (rawText == null || rawText.trim().isEmpty()) {
            return wordMap;
        }

        // Convert to lower case and replace punctuation/symbols with whitespace
        String cleanedText = rawText.toLowerCase().replaceAll("[^a-z0-9\\s]", " ");
        
        // Split on one or more whitespace characters
        String[] words = cleanedText.split("\\s+");

        for (String word : words) {
            String trimmedWord = word.trim();
            if (!trimmedWord.isEmpty()) {
                wordMap.put(trimmedWord, wordMap.getOrDefault(trimmedWord, 0) + 1);
            }
        }

        return wordMap;
    }

    /**
     * Cross-references required skills against candidate text and returns matched skills with frequencies.
     * Supports multi-word skills (e.g., "data structures", "spring boot") via full text search,
     * and single-word skills via $O(1)$ HashMap lookups.
     * 
     * Time Complexity: $O(K)$ where $K$ is the number of required skills.
     * 
     * @param rawText        Cleaned or raw resume text
     * @param requiredSkills List of target required skill keywords/phrases
     * @return HashMap containing matched skill strings and their frequency count
     */
    public HashMap<String, Integer> findMatchedSkills(String rawText, List<String> requiredSkills) {
        HashMap<String, Integer> matchedSkills = new HashMap<>();

        if (rawText == null || requiredSkills == null || requiredSkills.isEmpty()) {
            return matchedSkills;
        }

        HashMap<String, Integer> wordFrequencyMap = buildWordFrequencyMap(rawText);
        String lowerRawText = rawText.toLowerCase();

        for (String skill : requiredSkills) {
            if (skill == null || skill.trim().isEmpty()) {
                continue;
            }

            String cleanSkill = skill.trim().toLowerCase();

            // Case 1: Multi-word skill phrase (e.g., "machine learning", "data structures")
            if (cleanSkill.contains(" ")) {
                int count = countOccurrences(lowerRawText, cleanSkill);
                if (count > 0) {
                    matchedSkills.put(cleanSkill, count);
                }
            } 
            // Case 2: Single-word skill (e.g., "java", "python", "sql") - O(1) HashMap lookup
            else {
                if (wordFrequencyMap.containsKey(cleanSkill)) {
                    matchedSkills.put(cleanSkill, wordFrequencyMap.get(cleanSkill));
                }
            }
        }

        return matchedSkills;
    }

    /**
     * Calculates the match percentage score based on the proportion of required skills matched.
     * 
     * Formula: $\text{Score} = \left( \frac{\text{Number of Matched Skills}}{\text{Total Required Skills}} \right) \times 100$
     * 
     * @param matchedSkills  HashMap of matched skills
     * @param requiredSkills Total list of required skills defined in Job Description
     * @return Calculated percentage score between 0.0 and 100.0
     */
    public double calculateMatchPercentage(HashMap<String, Integer> matchedSkills, List<String> requiredSkills) {
        if (requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }

        int totalRequired = requiredSkills.size();
        int matchedCount = 0;

        for (String skill : requiredSkills) {
            if (skill != null && matchedSkills.containsKey(skill.trim().toLowerCase())) {
                matchedCount++;
            }
        }

        double score = ((double) matchedCount / totalRequired) * 100.0;
        return Math.min(100.0, Math.max(0.0, score)); // Clamp between 0.0 and 100.0
    }

    /**
     * Helper method to count non-overlapping occurrences of a substring within a larger text block.
     * 
     * @param text      Full text string in lowercase
     * @param substring Target phrase in lowercase
     * @return Frequency count of the substring
     */
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}