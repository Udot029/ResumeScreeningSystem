package com.screener.service;

import com.screener.dsa.CandidateRanker;
import com.screener.dsa.KeywordMatcher;
import com.screener.model.Candidate;
import com.screener.model.JobDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Orchestrator Service for the Resume Screening Pipeline.
 * 
 * Unites text processing, keyword matching, candidate profile construction,
 * and max-heap candidate ranking into a clean business logic layer.
 */
public class ScreeningService {

    private final KeywordMatcher keywordMatcher;
    private final CandidateRanker candidateRanker;

    // Regular Expression Patterns for Metadata Extraction
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("(\\+?\\d{1,3}[- .]?)?\\(?\\d{3}\\)?[- .]?\\d{3}[- .]?\\d{4}");

    /**
     * Default constructor initializing core DSA engine dependencies.
     */
    public ScreeningService() {
        this.keywordMatcher = new KeywordMatcher();
        this.candidateRanker = new CandidateRanker();
    }

    /**
     * Constructor allowing dependency injection for custom algorithm instances.
     * 
     * @param keywordMatcher  Instance of KeywordMatcher
     * @param candidateRanker Instance of CandidateRanker
     */
    public ScreeningService(KeywordMatcher keywordMatcher, CandidateRanker candidateRanker) {
        this.keywordMatcher = (keywordMatcher != null) ? keywordMatcher : new KeywordMatcher();
        this.candidateRanker = (candidateRanker != null) ? candidateRanker : new CandidateRanker();
    }

    /**
     * Processes a single raw resume text against a JobDescription target,
     * extracts candidate metadata, performs skill matching, and constructs a Candidate model.
     * 
     * @param rawResumeText Raw string content extracted from a resume file
     * @param fallbackName  Default candidate name derived from filename if not found in text
     * @param job           Target JobDescription requirements
     * @return Fully populated Candidate model with match scores
     */
    public Candidate processResume(String rawResumeText, String fallbackName, JobDescription job) {
        if (rawResumeText == null || rawResumeText.trim().isEmpty()) {
            return new Candidate(fallbackName, "N/A", "N/A", "", new HashMap<>(), 0.0);
        }

        // 1. Extract Candidate Metadata via Heuristic Rules & Regex
        String candidateName = extractCandidateName(rawResumeText, fallbackName);
        String email = extractRegex(rawResumeText, EMAIL_PATTERN, "N/A");
        String phone = extractRegex(rawResumeText, PHONE_PATTERN, "N/A");

        // 2. Perform Keyword Matching against Job Description
        List<String> requiredSkills = (job != null && job.getRequiredSkills() != null) 
                ? job.getRequiredSkills() 
                : new ArrayList<>();

        HashMap<String, Integer> matchedSkills = keywordMatcher.findMatchedSkills(rawResumeText, requiredSkills);

        // 3. Calculate Overall Match Percentage
        double matchScore = keywordMatcher.calculateMatchPercentage(matchedSkills, requiredSkills);

        // 4. Construct and return Candidate Domain Object
        return new Candidate(candidateName, email, phone, rawResumeText, matchedSkills, matchScore);
    }

    /**
     * Processes a batch of resume text strings against a JobDescription target,
     * evaluates each candidate, and returns a list sorted in descending order of match score.
     * 
     * @param resumeTexts Map of filename/identifier to raw resume text strings
     * @param job         Target JobDescription requirements
     * @return List of Candidate objects ranked from highest to lowest score
     */
    public List<Candidate> screenAndRankBatch(List<String[]> resumeTexts, JobDescription job) {
        List<Candidate> candidates = new ArrayList<>();

        if (resumeTexts == null || resumeTexts.isEmpty()) {
            return candidates;
        }

        // Process each raw text entry into a Candidate object
        for (String[] entry : resumeTexts) {
            if (entry != null && entry.length >= 2) {
                String filename = entry[0];
                String text = entry[1];
                Candidate candidate = processResume(text, cleanFilenameToName(filename), job);
                candidates.add(candidate);
            }
        }

        // Rank processed candidates using PriorityQueue Max-Heap algorithm
        return candidateRanker.rankCandidates(candidates);
    }

    // =========================================================================
    // PRIVATE HEURISTIC EXTRACTION HELPERS
    // =========================================================================

    /**
     * Extracts a candidate's name from the top lines of the resume text.
     */
    private String extractCandidateName(String text, String fallback) {
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            // Name heuristic: First non-empty line under 30 characters containing no special chars or digits
            if (!trimmed.isEmpty() && trimmed.length() <= 30 && trimmed.matches("^[a-zA-Z\\s.]+$")) {
                return trimmed;
            }
        }
        return fallback;
    }

    /**
     * Generic regex extractor utility.
     */
    private String extractRegex(String text, Pattern pattern, String fallback) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return fallback;
    }

    /**
     * Utility converting a filename (e.g. "rahul_sharma_resume.txt") into a readable fallback name ("Rahul Sharma Resume").
     */
    private String cleanFilenameToName(String filename) {
        if (filename == null || filename.isEmpty()) return "Unknown Candidate";
        
        // Remove extension
        int dotIndex = filename.lastIndexOf('.');
        String baseName = (dotIndex > 0) ? filename.substring(0, dotIndex) : filename;
        
        // Replace underscores and hyphens with spaces
        return baseName.replaceAll("[_-]", " ").trim();
    }
}