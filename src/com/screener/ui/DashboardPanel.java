package com.screener.ui;

import com.screener.model.Candidate;
import com.screener.util.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;          
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard Panel displaying statistical overview cards and a dark-themed JTable
 * of candidate evaluation results.
 */
public class DashboardPanel extends JPanel {

    // Metric Summary Labels
    private JLabel totalResumesValue;
    private JLabel selectedCandidatesValue;
    private JLabel avgScoreValue;

    // Table Components
    private JTable candidateTable;
    private DefaultTableModel tableModel;

    // Data Cache
    private List<Candidate> currentCandidates;

    /**
     * Constructs and initializes the Dashboard view panel.
     */
    public DashboardPanel() {
        this.currentCandidates = new ArrayList<>();
        this.setLayout(new BorderLayout(0, UIConstants.PADDING_LARGE));
        this.setBackground(UIConstants.COLOR_BG_PRIMARY);
        this.setBorder(BorderFactory.createEmptyBorder(
            UIConstants.PADDING_MEDIUM, 
            UIConstants.PADDING_MEDIUM, 
            UIConstants.PADDING_MEDIUM, 
            UIConstants.PADDING_MEDIUM
        ));

        initComponents();
    }

    /**
     * Instantiates summary stat cards and candidate data table layout.
     */
    private void initComponents() {
        // =========================================================================
        // TOP SECTION: STATISTICAL OVERVIEW CARDS
        // =========================================================================
        JPanel statsContainerPanel = new JPanel(new GridLayout(1, 3, UIConstants.PADDING_LARGE, 0));
        statsContainerPanel.setOpaque(false);

        // Card 1: Total Resumes
        totalResumesValue = new JLabel("0", SwingConstants.CENTER);
        JPanel card1 = createStatCard("TOTAL RESUMES", totalResumesValue, UIConstants.COLOR_ACCENT);

        // Card 2: Selected Candidates (Match >= 70%)
        selectedCandidatesValue = new JLabel("0", SwingConstants.CENTER);
        JPanel card2 = createStatCard("QUALIFIED (≥70%)", selectedCandidatesValue, UIConstants.COLOR_SUCCESS);

        // Card 3: Average Match Score
        avgScoreValue = new JLabel("0.0%", SwingConstants.CENTER);
        JPanel card3 = createStatCard("AVERAGE MATCH", avgScoreValue, UIConstants.COLOR_WARNING);

        statsContainerPanel.add(card1);
        statsContainerPanel.add(card2);
        statsContainerPanel.add(card3);

        // =========================================================================
        // CENTER SECTION: CANDIDATE RANKING TABLE
        // =========================================================================
        JPanel tableContainerPanel = new JPanel(new BorderLayout(0, UIConstants.PADDING_SMALL));
        tableContainerPanel.setBackground(UIConstants.COLOR_BG_CARD);
        tableContainerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));

        JLabel tableTitleLabel = new JLabel("Ranked Candidate Results");
        tableTitleLabel.setFont(UIConstants.FONT_SUBTITLE);
        tableTitleLabel.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        tableTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIConstants.PADDING_SMALL, 0));

        // Table Model Setup
        String[] columnNames = {"Rank", "Candidate Name", "Email", "Phone", "Matched Skills", "Match Score"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        candidateTable = new JTable(tableModel);
        configureTableAppearance();

        JScrollPane scrollPane = new JScrollPane(candidateTable);
        scrollPane.setBackground(UIConstants.COLOR_BG_CARD);
        scrollPane.getViewport().setBackground(UIConstants.COLOR_BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1));

        tableContainerPanel.add(tableTitleLabel, BorderLayout.NORTH);
        tableContainerPanel.add(scrollPane, BorderLayout.CENTER);

        // Assemble Dashboard Panel
        this.add(statsContainerPanel, BorderLayout.NORTH);
        this.add(tableContainerPanel, BorderLayout.CENTER);
    }

    /**
     * Helper method to construct a dark-themed summary card panel.
     */
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, UIConstants.PADDING_SMALL));
        card.setBackground(UIConstants.COLOR_BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.COLOR_BORDER, 1),
                BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
            )
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_CAPTION);
        titleLabel.setForeground(UIConstants.COLOR_TEXT_SECONDARY);

        valueLabel.setFont(UIConstants.FONT_TITLE);
        valueLabel.setForeground(UIConstants.COLOR_TEXT_PRIMARY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Styles the JTable with dark colors, row heights, and custom cell renderers for scores.
     */
    private void configureTableAppearance() {
        candidateTable.setBackground(UIConstants.COLOR_BG_CARD);
        candidateTable.setForeground(UIConstants.COLOR_TEXT_PRIMARY);
        candidateTable.setGridColor(UIConstants.COLOR_BORDER);
        candidateTable.setRowHeight(36);
        candidateTable.setFont(UIConstants.FONT_BODY);
        candidateTable.setSelectionBackground(UIConstants.COLOR_ACCENT);
        candidateTable.setSelectionForeground(UIConstants.COLOR_TEXT_PRIMARY);
        candidateTable.setFillsViewportHeight(true);

        // Header Styling
        JTableHeader header = candidateTable.getTableHeader();
        header.setFont(UIConstants.FONT_BODY_BOLD);
        header.setBackground(UIConstants.COLOR_BG_PRIMARY);
        header.setForeground(UIConstants.COLOR_TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));

        // Column Renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        candidateTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Rank
        candidateTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Skills count

        // Custom Score Column Renderer with Color Badges
        candidateTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(UIConstants.FONT_BODY_BOLD);

                if (!isSelected && value != null) {
                    try {
                        String scoreStr = value.toString().replace("%", "").trim();
                        double score = Double.parseDouble(scoreStr);

                        if (score >= 70.0) {
                            setForeground(UIConstants.COLOR_SUCCESS);
                        } else if (score >= 40.0) {
                            setForeground(UIConstants.COLOR_WARNING);
                        } else {
                            setForeground(UIConstants.COLOR_DANGER);
                        }
                    } catch (NumberFormatException e) {
                        setForeground(UIConstants.COLOR_TEXT_PRIMARY);
                    }
                }
                return c;
            }
        });
    }

    /**
     * Updates the dashboard cards and candidate table with new ranked data.
     * 
     * @param candidates List of ranked candidate objects
     */
    public void updateDashboardData(List<Candidate> candidates) {
        this.currentCandidates = (candidates != null) ? candidates : new ArrayList<>();
        tableModel.setRowCount(0); // Clear existing table rows

        if (this.currentCandidates.isEmpty()) {
            totalResumesValue.setText("0");
            selectedCandidatesValue.setText("0");
            avgScoreValue.setText("0.0%");
            return;
        }

        int total = this.currentCandidates.size();
        int qualifiedCount = 0;
        double sumScore = 0.0;

        for (int i = 0; i < total; i++) {
            Candidate c = this.currentCandidates.get(i);
            double score = c.getMatchScore();
            sumScore += score;

            if (score >= 70.0) {
                qualifiedCount++;
            }

            // Populate Table Row
            Object[] rowData = new Object[]{
                i + 1,
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getMatchedSkills().size() + " Skills",
                String.format("%.1f%%", score)
            };
            tableModel.addRow(rowData);
        }

        double avgScore = sumScore / total;

        // Update Stat Cards
        totalResumesValue.setText(String.valueOf(total));
        selectedCandidatesValue.setText(String.valueOf(qualifiedCount));
        avgScoreValue.setText(String.format("%.1f%%", avgScore));
    }

    // =========================================================================
    // GETTERS FOR UI CONTROLS
    // =========================================================================

    public JTable getCandidateTable() {
        return candidateTable;
    }

    public List<Candidate> getCurrentCandidates() {
        return currentCandidates;
    }
}
