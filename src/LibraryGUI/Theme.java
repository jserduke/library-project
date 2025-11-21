
package LibraryGUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


public final class Theme {
    private Theme(){}

    // ---- Palette ------------------------------------------------------------
    public static final Color PRIMARY_DARK  = new Color(38, 70, 120);   // header bar
    public static final Color PRIMARY       = new Color(22, 78, 110);   // nav background
    public static final Color SURFACE       = new Color(245, 247, 250); // page background
    public static final Color STATUS        = new Color(90, 90, 90);    // status bar
    public static final Color TEXT_LIGHT    = Color.WHITE;
    public static final Color TEXT_DARK     = new Color(33, 37, 41);

    // Accents for buttons
    public static final Color ACCENT_BLUE    = new Color(29, 78, 216);
    public static final Color ACCENT_GREEN   = new Color(16, 121, 69);
    public static final Color ACCENT_ORANGE  = new Color(198, 93, 16);
    public static final Color ACCENT_PURPLE  = new Color(124, 58, 237);
    public static final Color DARK_BUTTON    = new Color(55, 65, 81);

    // ---- Typography ---------------------------------------------------------
    public static final Font  HEADER_FONT   = new Font("SansSerif", Font.BOLD, 20);
    public static final Font  SECTION_FONT  = new Font("SansSerif", Font.BOLD, 16);
    public static final Font  ITEM_FONT     = new Font("SansSerif", Font.PLAIN, 13);

    // ---- Header -------------------------------------------------------------
    public static void styleHeaderBar(JPanel bar, JComponent leftTitleOrLogo) {
        bar.setLayout(new BorderLayout());
        bar.setBackground(PRIMARY_DARK);
        bar.setBorder(new EmptyBorder(10, 12, 10, 12));
        if (leftTitleOrLogo != null) {
            if (leftTitleOrLogo instanceof JLabel lbl) {
                lbl.setForeground(TEXT_LIGHT);
                lbl.setFont(HEADER_FONT);
            }
            bar.add(leftTitleOrLogo, BorderLayout.WEST);
        }
    }

    public static JPanel statusBar(String message) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(STATUS);
        JLabel l = new JLabel("  " + message);
        l.setForeground(TEXT_LIGHT);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    // ---- Buttons ------------------------------------------------------------
    public static void styleButton(AbstractButton b, Color bg) {
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setFont(ITEM_FONT);
        b.setBorder(new EmptyBorder(8, 14, 8, 14));
        if (b instanceof JButton) {
            ((JButton)b).setMargin(new Insets(6,12,6,12));
        }
    }

    public static JButton darkButton(String text) {
        JButton btn = new JButton(text);
        styleButton(btn, DARK_BUTTON);
        return btn;
    }

    // ---- Tables -------------------------------------------------------------
    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(ITEM_FONT);
        table.setSelectionBackground(new Color(219, 234, 254));

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(SECTION_FONT);
        header.setBackground(new Color(243, 244, 246));
        header.setForeground(TEXT_DARK);

        // Zebra striping
        TableCellRenderer defaultRenderer = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
            Component c = defaultRenderer.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
            if (!isSelected) {
                c.setBackground((row % 2 == 0) ? Color.WHITE : new Color(248, 250, 252));
            }
            if (c instanceof JComponent jc) {
                jc.setBorder(new EmptyBorder(4, 8, 4, 8));
            }
            return c;
        });
    }

    // ---- Utility panels -----------------------------------------------------
    public static void styleControlStrip(JComponent strip) {
        strip.setBorder(new EmptyBorder(10, 12, 10, 12));
        strip.setBackground(SURFACE);
        if (strip instanceof JComponent jc) jc.setOpaque(true);
    }

    public static void applySurface(Container c) {
        c.setBackground(SURFACE);
        if (c instanceof JComponent jc) jc.setOpaque(true);
    }
}
