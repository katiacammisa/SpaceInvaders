package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EndingPanel extends JPanel implements ActionListener, Commons {

    private JFrame frame;
    private JPanel buttonPanel, fieldsPanel;
    private JButton restart;
    private JButton end;
    private boolean active;

    public EndingPanel(SpaceInvaders spaceInvaders) {

        frame = new JFrame("Space Invaders");
        buttonPanel = new JPanel();
        fieldsPanel = new JPanel();
        restart = new JButton("Back To Menu");
        restart.setPreferredSize(new Dimension(600, 30));
        end = new JButton("End Game");
        end.setPreferredSize(new Dimension(600, 30));


        fieldsPanel.add(restart);
        buttonPanel.add(end);
        frame.add(buttonPanel, BorderLayout.PAGE_END);
        frame.add(fieldsPanel, BorderLayout.PAGE_START);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBounds(542, 230, BOARD_WIDTH, BOARD_HEIGHT);

        frame.getContentPane().setBackground(Color.black);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spaceInvaders.restart();
                frame.setVisible(false);
            }
        });
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

    public static void main(String args[]) {
        new PanelStarter();
    }


    public boolean isActive() {
        return active;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
