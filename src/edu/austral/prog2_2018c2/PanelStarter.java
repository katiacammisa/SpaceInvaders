package edu.austral.prog2_2018c2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PanelStarter implements ActionListener, Commons {

    private JFrame frame;
    private JPanel buttonPane, fieldsPanel;
    private JButton start;
    private JButton highScore;
    private boolean active;

    public PanelStarter(){

        frame = new JFrame("Space Invaders");
        buttonPane = new JPanel();
        fieldsPanel = new JPanel();
        start = new JButton("Start Game");
        highScore = new JButton("View HighScores");

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, 2));
        buttonPane.setLayout(new FlowLayout());

        buttonPane.add(start);
        fieldsPanel.add(highScore);
        frame.add(fieldsPanel, BorderLayout.PAGE_START);
        frame.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(542, 230, BOARD_WIDTH, BOARD_HEIGHT);

        start.addActionListener(this);
    }
    public static void main(String args[]) {
        new PanelStarter();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        active = true;
    }

    public boolean isActive() {
        return active;
    }
}
