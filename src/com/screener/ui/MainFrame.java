package com.screener.ui;

import com.screener.dsa.CandidateRanker;
import com.screener.dsa.KeywordMatcher;
import com.screener.model.Candidate;
import com.screener.model.JobDescription;
import com.screener.service.ScreeningService;
import com.screener.util.TextFileReader;
import com.screener.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Window Frame for the Application.
 * 
 * Responsibilities:
 * 1. Constructs a top-level JFrame container styled in Dark Mode.
 * 2. Displays a clean header bar with the application title and a primary action button ("Scan Resumes").
 * 3. Provides a main content panel layout container ready for embedding dynamic dashboard panels.
 * 4. Orchestrates the resume screening pipeline: file selection, text reading, keyword matching, ranking, and display.
 */
public class MainFrame extends JFrame {

    private JPanel headerPanel;
    private JLabel titleLabel;
    private JButton scanButton;
    private JPanel contentPanel;

    // Core Application Components
    private DashboardPanel dashboardPanel;
    private ScreeningService screeningService;
    private TextFileReader textFileReader;
    private JobDescription jobDescription;

    /**
     * Constructs and initializes the main application window frame.
     */
    public MainFrame() {
        super("AI-Based Resume Screening and Candidate Ranking System");
        
        // Initialize service layer and data components
        this.screeningService = new ScreeningService(new KeywordMatcher(), new CandidateRanker());
        this.textFileReader = new TextFileReader();
        this.jobDescription = createDefaultJobDescription();
        
        initComponents();
        configureFrame();
        attachEventListeners();
    }

    /**
     * Creates a default sample JobDescription to use when none is provided.
     */
    private JobDescription createDefaultJobDescription() {
        ArrayList<String> requiredSkills = new ArrayList<>();
        requiredSkills.add("java");
        requiredSkills.add("python");
        requiredSkills.add("sql");
        requiredSkills.add("spring");
        requiredSkills.add("machine learning");
        requiredSkills.add("data structures");
        requiredSkills.add("algorithms");
        requiredSkills.add("javascript");
        requiredSkills.add("react");
        requiredSkills.add("docker");
        requiredSkills.add("git");
        requiredSkills.add("rest api");
        requiredSkills.add("agile");
        requiredSkills.add("linux");
        requiredSkills.add("cloud computing");

        return new JobDescription("Software Engineer", requiredSkills, 2);
    }

    /**
     * Instantiates and arranges all swing UI components within the main window layout.
     */
    private void initComponents() {
        // Base content pane setup with dark background
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(UIConstants.COLOR_BG_PRIMARY);

        // =========================================================================
        // HEADER PANEL SETUP
        // =========================================================================
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.COLOR_BG_CARD);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.COLOR_BORDER),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_LARGE, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_LARGE)
        ));

        // Application Title Label
        titleLabel = new JLabel("★ AI Resume Screener & Candidate Ranker");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        // Scan Action Button
        scanButton = new JButton("Scan Resumes");
        scanButton.setFont(UIConstants.FONT_BODY_BOLD);
        scanButton.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        scanButton.setBackground(UIConstants.COLOR_ACCENT);
        scanButton.setFocusPainted(false);
        scanButton.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM));
        scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action controls wrapper panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(scanButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        // =========================================================================
        // MAIN CONTENT AREA SETUP
        // =========================================================================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.COLOR_BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));

        // =========================================================================
        // DASHBOARD PANEL INTEGRATION
        // =========================================================================
        dashboardPanel = new DashboardPanel();
        contentPanel.add(dashboardPanel, BorderLayout.CENTER);

        // Assemble layout
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        this.setContentPane(mainContainer);
    }

    /**
     * Attaches all event listeners for user interaction.
     */
    private void attachEventListeners() {
        // =========================================================================
        // SCAN RESUMES BUTTON ACTION
        // =========================================================================
        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openResumeFileChooser();
            }
        });

        // =========================================================================
        // CANDIDATE TABLE DOUBLE-CLICK → OPEN DETAIL DIALOG
        // =========================================================================
        dashboardPanel.getCandidateTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = dashboardPanel.getCandidateTable().getSelectedRow();
                    if (selectedRow >= 0 && selectedRow < dashboardPanel.getCurrentCandidates().size()) {
                        Candidate selectedCandidate = dashboardPanel.getCurrentCandidates().get(selectedRow);
                        CandidateDetailDialog dialog = new CandidateDetailDialog(MainFrame.this, selectedCandidate);
                        dialog.setVisible(true);
                    }
                }
            }
        });
    }

    /**
     * Opens a JFileChooser allowing the user to select multiple .txt resume files.
     * Reads each file, processes them through the screening pipeline, and updates the dashboard.
     */
    private void openResumeFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Resume Files (.txt)");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Filter to show only .txt files
        FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text Files (*.txt)", "txt");
        fileChooser.setFileFilter(txtFilter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            if (selectedFiles.length == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No files selected. Please select at least one .txt resume file.", 
                    "No Files Selected", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Read selected files in a background thread to keep UI responsive
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    scanButton.setEnabled(false);
                    scanButton.setText("Scanning...");
                }
            });

            // Use a separate thread for the I/O and processing to not block EDT
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<String[]> resumeTexts = new ArrayList<>();
                        
                        for (File file : selectedFiles) {
                            try {
                                String content = textFileReader.readTextFile(file);
                                resumeTexts.add(new String[]{file.getName(), content});
                            } catch (IOException ex) {
                                System.err.println("Error reading file: " + file.getName() + " - " + ex.getMessage());
                            }
                        }

                        if (resumeTexts.isEmpty()) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JOptionPane.showMessageDialog(MainFrame.this,
                                        "Could not read any of the selected files. Please ensure they are valid .txt files.",
                                        "Read Error",
                                        JOptionPane.ERROR_MESSAGE);
                                    scanButton.setEnabled(true);
                                    scanButton.setText("Scan Resumes");
                                }
                            });
                            return;
                        }

                        // Process and rank candidates
                        final List<Candidate> rankedCandidates = screeningService.screenAndRankBatch(resumeTexts, jobDescription);

                        // Update UI on EDT
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                dashboardPanel.updateDashboardData(rankedCandidates);
                                scanButton.setEnabled(true);
                                scanButton.setText("Scan Resumes");
                                
                                JOptionPane.showMessageDialog(MainFrame.this,
                                    "Successfully screened " + rankedCandidates.size() + " resume(s)!\n"
                                    + "Job Description: " + jobDescription.getRoleTitle() + "\n"
                                    + "Required Skills: " + jobDescription.getRequiredSkills().size(),
                                    "Screening Complete",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        });
                    } catch (Exception ex) {
                        System.err.println("Error during screening pipeline: " + ex.getMessage());
                        ex.printStackTrace();
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                JOptionPane.showMessageDialog(MainFrame.this,
                                    "An error occurred during resume screening:\n" + ex.getMessage(),
                                    "Processing Error",
                                    JOptionPane.ERROR_MESSAGE);
                                scanButton.setEnabled(true);
                                scanButton.setText("Scan Resumes");
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * Configures primary window properties such as dimensions, close behavior, and screen centering.
     */
    private void configureFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        this.pack();
        this.setMinimumSize(new Dimension(800, 500));
        this.setLocationRelativeTo(null); // Center window on screen
    }

    // =========================================================================
    // GETTERS FOR UI CONTROLS & CONTAINERS
    // =========================================================================

    public JButton getScanButton() {
        return scanButton;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }

    public DashboardPanel getDashboardPanel() {
        return dashboardPanel;
    }

    public ScreeningService getScreeningService() {
        return screeningService;
    }
}
