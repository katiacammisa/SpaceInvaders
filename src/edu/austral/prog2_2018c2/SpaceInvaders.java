package edu.austral.prog2_2018c2;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class SpaceInvaders extends JFrame implements Commons {

    public SpaceInvaders() {

        initUI();
    }

    private void initUI() {
        add(new Board());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        start();

        EventQueue.invokeLater(() -> {
            SpaceInvaders ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }

    public static void start(){
        PanelStarter start = new PanelStarter();
        while (!start.isActive()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}