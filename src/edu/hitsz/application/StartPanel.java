package edu.hitsz.application;

import edu.hitsz.gameConfig.DifficultyLevel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 游戏启动前的难度选择面板。单选按钮实时预览对应背景图。
 */
public class StartPanel extends JPanel {

    private DifficultyLevel selected = DifficultyLevel.NORMAL;
    private BufferedImage preview = ImageManager.loadBackground(selected);

    private Consumer<DifficultyLevel> onStart = d ->
            System.out.println("start game with difficulty = " + d);

    public StartPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));

        add(buildTitle(),    BorderLayout.NORTH);
        add(buildOptions(),  BorderLayout.CENTER);
        add(buildStartBar(), BorderLayout.SOUTH);
    }

    public DifficultyLevel getSelectedDifficulty() {
        return selected;
    }

    public void setOnStart(Consumer<DifficultyLevel> handler) {
        if (handler != null) {
            this.onStart = handler;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (preview != null) {
            g.drawImage(preview, 0, 0, getWidth(), getHeight(), null);
        }
    }

    // ---------------------------------------------------------- layout parts

    private JComponent buildTitle() {
        JLabel title = new JLabel("Aircraft War", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));
        title.setOpaque(false);
        return wrapTransparent(title);
    }

    private JComponent buildOptions() {
        Map<DifficultyLevel, String> labels = new LinkedHashMap<>();
        labels.put(DifficultyLevel.NORMAL, "正常");
        labels.put(DifficultyLevel.HARD,   "困难");
        labels.put(DifficultyLevel.EXPERT, "专家");

        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

        ButtonGroup group = new ButtonGroup();
        for (Map.Entry<DifficultyLevel, String> e : labels.entrySet()) {
            JRadioButton rb = new JRadioButton(e.getValue());
            rb.setFont(new Font("SansSerif", Font.BOLD, 22));
            rb.setForeground(Color.WHITE);
            rb.setOpaque(false);
            rb.setAlignmentX(Component.CENTER_ALIGNMENT);
            rb.setFocusPainted(false);

            DifficultyLevel level = e.getKey();
            rb.setSelected(level == selected);
            rb.addActionListener(ev -> onDifficultyChange(level));

            group.add(rb);
            box.add(Box.createVerticalStrut(12));
            box.add(rb);
        }
        return box;
    }

    private JComponent buildStartBar() {
        JButton start = new JButton("开始游戏");
        start.setFont(new Font("SansSerif", Font.BOLD, 20));
        start.setFocusPainted(false);
        start.addActionListener(e -> onStart.accept(selected));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        bar.setOpaque(false);
        bar.add(start);
        return bar;
    }

    private JComponent wrapTransparent(JComponent inner) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(inner, BorderLayout.CENTER);
        return wrap;
    }

    // ---------------------------------------------------------- state change

    private void onDifficultyChange(DifficultyLevel level) {
        selected = level;
        preview = ImageManager.loadBackground(level);
        repaint();
    }
}
