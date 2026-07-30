package com.screener.model;

import java.util.HashMap;

/**
 * Domain Model class representing a Candidate in the screening system.
 * 
 * Holds parsed candidate metadata, contact details, extracted raw resume text,
 * matched skill keyword frequencies, and the final calculated match score.
 */
public class Candidate {

    private String name;
    private String email;
    private String phone;
    private String rawResumeText;
    private HashMap<String, Integer> matchedSkills;
    private double matchScore;

    /**
     * Default constructor initializing default empty structures.
     */
    public Candidate() {
        this.name = "";
        this.email = "";
        this.phone = "";
        this.rawResumeText = "";
        this.matchedSkills = new HashMap<>();
        this.matchScore = 0.0;
    }

    /**
     * Parameterized constructor for full object construction.
     * 
     * @param name           Candidate's full name
     * @param email          Candidate's contact email address
     * @param phone          Candidate's contact phone number
     * @param rawResumeText  Complete raw text contents read from resume file
     * @param matchedSkills  HashMap mapping found skill keywords to their occurrence frequencies
     * @param matchScore     Final calculated match percentage score (0.0 to 100.0)
     */
    public Candidate(String name, String email, String phone, String rawResumeText, 
                     HashMap<String, Integer> matchedSkills, double matchScore) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rawResumeText = rawResumeText;
        this.matchedSkills = (matchedSkills != null) ? matchedSkills : new HashMap<>();
        this.matchScore = matchScore;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRawResumeText() {
        return rawResumeText;
    }

    public void setRawResumeText(String rawResumeText) {
        this.rawResumeText = rawResumeText;
    }

    public HashMap<String, Integer> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(HashMap<String, Integer> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    // =========================================================================
    // OVERRIDDEN OBJECT METHODS
    // =========================================================================

    @Override
    public String toString() {
        return "Candidate{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", matchScore=" + String.format("%.2f", matchScore) + "%" +
                ", matchedSkillsCount=" + matchedSkills.size() +
                '}';
    }
}
