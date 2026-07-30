
package com.screener.model;

import java.util.ArrayList;

/**
 * Domain Model class representing a Job Description specification in the system.
 * 
 * Holds role parameters such as title, required skill keywords, and minimum
 * experience requirements used as the baseline for candidate evaluation.
 */
public class JobDescription {

    private String roleTitle;
    private ArrayList<String> requiredSkills;
    private int minimumExperience;

    /**
     * Default constructor initializing default values and an empty skills list.
     */
    public JobDescription() {
        this.roleTitle = "";
        this.requiredSkills = new ArrayList<>();
        this.minimumExperience = 0;
    }

    /**
     * Parameterized constructor for complete object initialization.
     * 
     * @param roleTitle         The job title or role name (e.g., "Java Developer")
     * @param requiredSkills    ArrayList of required skill keywords/phrases
     * @param minimumExperience Minimum required years of experience
     */
    public JobDescription(String roleTitle, ArrayList<String> requiredSkills, int minimumExperience) {
        this.roleTitle = roleTitle;
        this.requiredSkills = (requiredSkills != null) ? requiredSkills : new ArrayList<>();
        this.minimumExperience = minimumExperience;
    }

    // =========================================================================
    // GETTERS AND SETTERS
    // =========================================================================

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public ArrayList<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(ArrayList<String> requiredSkills) {
        this.requiredSkills = (requiredSkills != null) ? requiredSkills : new ArrayList<>();
    }

    public int getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(int minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    // =========================================================================
    // OVERRIDDEN OBJECT METHODS
    // =========================================================================

    @Override
    public String toString() {
        return "JobDescription{" +
                "roleTitle='" + roleTitle + '\'' +
                ", requiredSkills=" + requiredSkills +
                ", minimumExperience=" + minimumExperience + " years" +
                '}';
    }
}