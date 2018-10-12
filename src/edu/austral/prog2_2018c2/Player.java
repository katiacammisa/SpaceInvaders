package edu.austral.prog2_2018c2;


import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Sprite implements Commons {

    private final int START_Y = 320;//280
    private final int START_X = 270;
    private int lives;
    private String playerImg = "src/images/Player2.png";

    private int width;

    public Player() {

        lives = 3;
        initPlayer();
    }


    public void initPlayer() {

        ImageIcon ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void initPlayerDead() {

        ImageIcon ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(getX());
        setY(getY());
    }

    public void setPlayerImg(String playerImg) {
        this.playerImg = playerImg;
    }

    public int getLives() {
        return lives;
    }

    public boolean hasLivesLeft()
    {
        if(lives>= 1)
        {
            return true;
        }
        return false;
    }

    public void reduceLives(){
        --lives;
    }

    public void act() {

        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {

            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {

            dx = 0;
        }
    }
}