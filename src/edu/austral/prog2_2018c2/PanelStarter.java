package edu.austral.prog2_2018c2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PanelStarter extends JPanel implements ActionListener, Commons {

    private JFrame frame;
    private JPanel buttonPanel, fieldsPanel;
    private JButton start;
    private JButton highScore;
    private boolean active;

    public PanelStarter() {

        frame = new JFrame("Space Invaders");
        buttonPanel = new JPanel();
        fieldsPanel = new JPanel();
        start = new JButton("Start Game");
        start.setPreferredSize(new Dimension(600, 30));
        highScore = new JButton("View HighScores");
        highScore.setPreferredSize(new Dimension(600, 30));

//        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, 2));
//        buttonPanel.setLayout(new FlowLayout());
//        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, 1));
//        buttonPanel.setLayout(new FlowLayout());

        fieldsPanel.add(start);
        buttonPanel.add(highScore);
        frame.add(buttonPanel, BorderLayout.PAGE_END);
        frame.add(fieldsPanel, BorderLayout.PAGE_START);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(542, 230, BOARD_WIDTH, BOARD_HEIGHT);

        frame.getContentPane().setBackground(Color.black);

        start.addActionListener(this);
        highScore.addActionListener(this);
    }

    public static void main(String args[]) {
        new PanelStarter();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == start) {
            frame.setVisible(false);
            active = true;
        }

        if (e.getSource() == highScore) {
            PanelHighScores hs = new PanelHighScores();
        }
    }

    public boolean isActive() {
        return active;
    }
}

