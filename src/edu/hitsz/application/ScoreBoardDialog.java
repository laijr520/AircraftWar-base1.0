package edu.hitsz.application;

import edu.hitsz.dao.PlayerScoreDao;
import edu.hitsz.gameConfig.DifficultyLevel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ScoreBoardDialog extends JDialog {

    public enum Mode { PRE_GAME, POST_GAME }

    private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Color HIGHLIGHT_BG = new Color(255, 240, 130);

    private final Mode mode;
    private final DifficultyLevel difficulty;
    private final PlayerScoreDao dao;

    private final ScoreTableModel tableModel;
    private final JTable table;
    private int highlightRow = -1;

    private JTextField nameField;
    private JLabel scoreLabel;
    private int finalScore = 0;

    private Consumer<String> onStart = n -> {};
    private Runnable onCancel = () -> {};
    private Runnable onRestart = () -> {};
    private Runnable onExit = () -> {};

    public ScoreBoardDialog(Frame owner, Mode mode, DifficultyLevel difficulty, PlayerScoreDao dao) {
        super(owner, "排行榜 - " + labelOf(difficulty), true);
        this.mode = mode;
        this.difficulty = difficulty;
        this.dao = dao;

        this.tableModel = new ScoreTableModel();
        this.table = new JTable(tableModel);
        this.table.setRowHeight(24);
        this.table.getTableHeader().setReorderingAllowed(false);
        this.table.setDefaultRenderer(Object.class, new HighlightRenderer());
        tuneColumnWidths();
        reload();

        setLayout(new BorderLayout(8, 8));
        add(buildTop(),    BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        setSize(500, 560);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (mode == Mode.PRE_GAME) {
                    dispose();
                    onCancel.run();
                } else {
                    dispose();
                    onExit.run();
                }
            }
        });
    }

    // ---- callbacks / state setters
    public void setOnStart(Consumer<String> cb)   { if (cb != null) onStart = cb; }
    public void setOnCancel(Runnable cb)          { if (cb != null) onCancel = cb; }
    public void setOnRestart(Runnable cb)         { if (cb != null) onRestart = cb; }
    public void setOnExit(Runnable cb)            { if (cb != null) onExit = cb; }

    public void setFinalScore(int score) {
        this.finalScore = score;
        if (scoreLabel != null) {
            scoreLabel.setText("得分: " + score);
        }
    }

    public void setHighlightRow(int row) {
        this.highlightRow = row;
        if (row >= 0 && row < tableModel.getRowCount()) {
            table.scrollRectToVisible(table.getCellRect(row, 0, true));
        }
        table.repaint();
    }

    // ---- layout

    private JComponent buildTop() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel title = new JLabel("难度: " + labelOf(difficulty));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        p.add(title);
        return p;
    }

    private JComponent buildBottom() {
        JPanel bottom = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton del = new JButton("删除所选");
        del.addActionListener(e -> onDelete());
        left.add(del);
        bottom.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (mode == Mode.PRE_GAME) {
            right.add(new JLabel("姓名:"));
            nameField = new JTextField(10);
            right.add(nameField);
            JButton start = new JButton("开始");
            start.addActionListener(e -> {
                String name = nameField.getText() == null ? "" : nameField.getText().trim();
                dispose();
                onStart.accept(name);
            });
            JButton cancel = new JButton("取消");
            cancel.addActionListener(e -> {
                dispose();
                onCancel.run();
            });
            right.add(start);
            right.add(cancel);
        } else {
            scoreLabel = new JLabel("得分: " + finalScore);
            scoreLabel.setFont(scoreLabel.getFont().deriveFont(Font.BOLD, 16f));
            right.add(scoreLabel);
            JButton restart = new JButton("重新游戏");
            restart.addActionListener(e -> {
                dispose();
                onRestart.run();
            });
            JButton exit = new JButton("退出游戏");
            exit.addActionListener(e -> {
                dispose();
                onExit.run();
            });
            right.add(restart);
            right.add(exit);
        }
        bottom.add(right, BorderLayout.EAST);
        return bottom;
    }

    private void tuneColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
    }

    // ---- actions

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选中一行", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "确认删除第 " + (row + 1) + " 条记录？", "确认",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        dao.deleteByIndex(difficulty, row);
        if (row == highlightRow) {
            highlightRow = -1;
        } else if (row < highlightRow) {
            highlightRow--;
        }
        reload();
    }

    private void reload() {
        tableModel.setScores(dao.findByDifficulty(difficulty));
    }

    // ---- helpers

    private static String labelOf(DifficultyLevel d) {
        switch (d) {
            case NORMAL: return "正常";
            case HARD:   return "困难";
            case EXPERT: return "专家";
            default:     return d.name();
        }
    }

    // ---- inner types

    private final class HighlightRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                                                       boolean sel, boolean foc,
                                                       int row, int col) {
            Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            if (!sel) {
                c.setBackground(row == highlightRow ? HIGHLIGHT_BG : Color.WHITE);
            }
            return c;
        }
    }

    private final class ScoreTableModel extends AbstractTableModel {
        private final String[] columns = { "排名", "姓名", "得分", "时间" };
        private List<PlayerScore> rows = new ArrayList<>();

        void setScores(List<PlayerScore> list) {
            this.rows = list != null ? list : new ArrayList<>();
            fireTableDataChanged();
        }

        @Override public int getRowCount()                { return rows.size(); }
        @Override public int getColumnCount()             { return columns.length; }
        @Override public String getColumnName(int c)      { return columns[c]; }
        @Override public boolean isCellEditable(int r, int c) { return false; }

        @Override
        public Object getValueAt(int r, int c) {
            PlayerScore p = rows.get(r);
            switch (c) {
                case 0: return r + 1;
                case 1: return p.getPlayerName();
                case 2: return p.getScore();
                case 3: return TIME_FMT.format(new Date(p.getTimestamp()));
                default: return "";
            }
        }
    }
}
