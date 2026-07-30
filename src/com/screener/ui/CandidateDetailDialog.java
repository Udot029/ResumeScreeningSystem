package com.screener.ui;

import com.screener.model.Candidate;
import com.screener.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;

/**
 * Modal Dialog displaying granular evaluation metrics, matched skills breakdown,
 * contact info, and raw resume text for a selected candidate.
 */
public class CandidateDetailDialog extends JDialog {

    private final Candidate candidate;

    /**
     * Constructs and initializes the candidate inspection dialog window.
     * 
     * @param parent    Parent frame window context
     * @param candidate Target candidate model to inspect
     */
    public CandidateDetailDialog(JFrame parent, Candidate candidate) {
        super(parent, "Candidate Details - " + (candidate != null ? candidate.getName() : "Details"), true);
        this.candidate = candidate;

        initComponents();
        configureDialog();
    }

    /**
     * Instantiates and arranges the dark-themed detailed view controls.
     */
    private void initComponents() {
        JPanel mainContainer = new JPanel(new BorderLayout(0, UIConstants.PADDING_MEDIUM));
        mainContainer.setBackground(UIConstants.COLOR_BG_PRIMARY);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.PADDING_LARGE, 
            UIConstants.PADDING_LARGE, 
            UIConstants.PADDING_LARGE, 
            UIConstants.PADDING_LARGE
        ));

        // =========================================================================
        // TOP SECTION: HEADER & CONTACT INFO CARD
        // =========================================================================
        JPanel headerCard = new JPanel(new BorderLayout(UIConstants.PADDING_MEDIUM, 0));
        headerCard.setBackground(UIConstants.COLOR_BG_CARD);
        headerCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        // Info Grid (Name, Email, Phone)
        JPanel infoGridPanel = new JPanel(new GridLayout(3, 1, 0, 4));
        infoGridPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(candidate != null ? candidate.getName() : "Unknown");
        nameLabel.setFont(UIConstants.FONT_TITLE);
        nameLabel.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JLabel emailLabel = new JLabel("Email: " + (candidate != null ? candidate.getEmail() : "N/A"));
        emailLabel.setFont(UIConstants.FONT_BODY);
        emailLabel.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        JLabel phoneLabel = new JLabel("Phone: " + (candidate != null ? candidate.getPhone() : "N/A"));
        phoneLabel.setFont(UIConstants.FONT_BODY);
        phoneLabel.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        infoGridPanel.add(nameLabel);
        infoGridPanel.add(emailLabel);
        infoGridPanel.add(phoneLabel);

        // Match Score Badge Box
        JPanel scoreBadgePanel = new JPanel(new BorderLayout());
        scoreBadgePanel.setOpaque(false);

        double score = (candidate != null) ? candidate.getMatchScore() : 0.0;
        JLabel scoreLabel = new JLabel(String.format("%.1f%%", score), SwingConstants.CENTER);
        scoreLabel.setFont(UIConstants.FONT_TITLE);
        
        if (score >= 70.0) {
            scoreLabel.setForeground(UIConstants.COLOR_SUCCESS);
        } else if (score >= 40.0) {
            scoreLabel.setForeground(UIConstants.COLOR_WARNING);
        } else {
            scoreLabel.setForeground(UIConstants.COLOR_DANGER);
        }

        JLabel scoreSubLabel = new JLabel("MATCH SCORE", SwingConstants.CENTER);
        scoreSubLabel.setFont(UIConstants.FONT_CAPTION);
        scoreSubLabel.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        scoreBadgePanel.add(scoreLabel, BorderLayout.CENTER);
        scoreBadgePanel.add(scoreSubLabel, BorderLayout.SOUTH);

        headerCard.add(infoGridPanel, BorderLayout.CENTER);
        headerCard.add(scoreBadgePanel, BorderLayout.EAST);

        // =========================================================================
        // CENTER SECTION: MATCHED SKILLS & RESUME TEXT
        // =========================================================================
        JPanel centerContentPanel = new JPanel(new GridLayout(2, 1, 0, UIConstants.PADDING_MEDIUM));
        centerContentPanel.setOpaque(false);

        // Panel 1: Matched Skills Listing
        JPanel skillsPanel = new JPanel(new BorderLayout(0, UIConstants.PADDING_SMALL));
        skillsPanel.setBackground(UIConstants.COLOR_BG_CARD);
        skillsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JLabel skillsTitle = new JLabel("Matched Skill Keywords & Frequency");
        skillsTitle.setFont(UIConstants.FONT_SUBTITLE);
        skillsTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JTextArea skillsArea = new JTextArea();
        skillsArea.setEditable(false);
        skillsArea.setLineWrap(true);
        skillsArea.setWrapStyleWord(true);
        skillsArea.setBackground(UIConstants.COLOR_BG_INPUT);
        skillsArea.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        skillsArea.setFont(UIConstants.FONT_BODY);
        skillsArea.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));

        StringBuilder skillsBuilder = new StringBuilder();
        if (candidate != null && candidate.getMatchedSkills() != null && !candidate.getMatchedSkills().isEmpty()) {
            for (Map.Entry<String, Integer> entry : candidate.getMatchedSkills().entrySet()) {
                skillsBuilder.append("• ").append(entry.getKey().toUpperCase())
                             .append(" (").append(entry.getValue()).append(" occurrences)\n");
            }
        } else {
            skillsBuilder.append("No target skill keywords were matched in this resume.");
        }
        skillsArea.setText(skillsBuilder.toString());

        JScrollPane skillsScrollPane = new JScrollPane(skillsArea);
        skillsScrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1));

        skillsPanel.add(skillsTitle, BorderLayout.NORTH);
        skillsPanel.add(skillsScrollPane, BorderLayout.CENTER);

        // Panel 2: Raw Resume Text Viewer
        JPanel rawTextPanel = new JPanel(new BorderLayout(0, UIConstants.PADDING_SMALL));
        rawTextPanel.setBackground(UIConstants.COLOR_BG_CARD);
        rawTextPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JLabel rawTextTitle = new JLabel("Raw Resume Content (.txt)");
        rawTextTitle.setFont(UIConstants.FONT_SUBTITLE);
        rawTextTitle.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        JTextArea rawTextArea = new JTextArea(candidate != null ? candidate.getRawResumeText() : "");
        rawTextArea.setEditable(false);
        rawTextArea.setLineWrap(true);
        rawTextArea.setWrapStyleWord(true);
        rawTextArea.setBackground(UIConstants.COLOR_BG_INPUT);
        rawTextArea.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        rawTextArea.setFont(UIConstants.FONT_BODY);
        rawTextArea.setCaretPosition(0); // Scroll to top
        rawTextArea.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));

        JScrollPane rawTextScrollPane = new JScrollPane(rawTextArea);
        rawTextScrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1));

        rawTextPanel.add(rawTextTitle, BorderLayout.NORTH);
        rawTextPanel.add(rawTextScrollPane, BorderLayout.CENTER);

        centerContentPanel.add(skillsPanel);
        centerContentPanel.add(rawTextPanel);

        // =========================================================================
        // BOTTOM SECTION: CLOSE ACTION CONTROL
        // =========================================================================
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerPanel.setOpaque(false);

        JButton closeButton = new JButton("Close Window");
        closeButton.setFont(UIConstants.FONT_BODY_BOLD);
        closeButton.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        closeButton.setBackground(UIConstants.COLOR_ACCENT);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.PADDING_SMALL, 
            UIConstants.PADDING_LARGE, 
            UIConstants.PADDING_SMALL, 
            UIConstants.PADDING_LARGE
        ));
        closeButton.addActionListener(e -> dispose());

        footerPanel.add(closeButton);

        // Assemble Layout
        mainContainer.add(headerCard, BorderLayout.NORTH);
        mainContainer.add(centerContentPanel, BorderLayout.CENTER);
        mainContainer.add(footerPanel, BorderLayout.SOUTH);

        this.setContentPane(mainContainer);
    }

    /**
     * Sets dialog dimensions and centers it relative to parent frame.
     */
    private void configureDialog() {
        this.setSize(650, 680);
        this.setResizable(false);
        this.setLocationRelativeTo(getParent());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
