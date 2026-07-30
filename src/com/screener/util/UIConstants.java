package com.screener.util;

import java.awt.Color;
import java.awt.Font;

/**
 * Global UI Constants holding design tokens for the Dark Theme.
 * 
 * Defines colors, fonts, margins, and dimension metrics used across all Swing components
 * to maintain visual consistency throughout the application.
 */
public final class UIConstants {

    // Private constructor to prevent instantiation
    private UIConstants() {
        throw new UnsupportedOperationException("UIConstants is a utility class and cannot be instantiated.");
    }

    // =========================================================================
    // COLOR PALETTE (Dark Theme Design System)
    // =========================================================================

    /** Primary background color for windows and main container panels (Deep Slate Charcoal) */
    public static final Color COLOR_BG_PRIMARY = new Color(30, 30, 46);

    /** Surface background color for cards, tables, and dialogs (Dark Slate) */
    public static final Color COLOR_BG_CARD = new Color(37, 37, 56);

    /** Slightly elevated background color for hover states and input fields */
    public static final Color COLOR_BG_INPUT = new Color(48, 48, 70);

    /** Border color for subtle structural separation */
    public static final Color COLOR_BORDER = new Color(60, 60, 85);

    /** Primary interactive accent color (Electric Purple) */
    public static final Color COLOR_ACCENT = new Color(98, 0, 238);

    /** Hover state color for primary accent controls */
    public static final Color COLOR_ACCENT_HOVER = new Color(120, 30, 255);

    /** High contrast text color for titles, body text, and prominent elements */
    public static final Color COLOR_TEXT_PRIMARY = new Color(240, 240, 240);

    /** Muted text color for labels, subheaders, and secondary details */
    public static final Color COLOR_TEXT_SECONDARY = new Color(170, 170, 170);

    /** Success badge and high match percentage color (Green) */
    public static final Color COLOR_SUCCESS = new Color(46, 125, 50);

    /** Warning badge and medium match percentage color (Amber) */
    public static final Color COLOR_WARNING = new Color(245, 124, 0);

    /** Danger badge and low match percentage color (Red) */
    public static final Color COLOR_DANGER = new Color(198, 40, 40);


    // =========================================================================
    // TYPOGRAPHY (Fonts)
    // =========================================================================

    /** Header font for primary view titles and prominent stat counts */
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);

    /** Subtitle font for card headers and section titles */
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);

    /** Regular body text font for standard components and input fields */
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 12);

    /** Bold variant of body text for table headers and highlighted metrics */
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 12);

    /** Small caption font for metadata, status indicators, and field help text */
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 10);


    // =========================================================================
    // DIMENSIONS & PADDING METRICS
    // =========================================================================

    /** Standard window width on initial launch */
    public static final int WINDOW_WIDTH = 1024;

    /** Standard window height on initial launch */
    public static final int WINDOW_HEIGHT = 700;

    /** Standard padding spacing (8px) */
    public static final int PADDING_SMALL = 8;

    /** Medium padding spacing (16px) */
    public static final int PADDING_MEDIUM = 16;

    /** Large padding spacing (24px) */
    public static final int PADDING_LARGE = 24;
}
