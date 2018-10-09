package edu.austral.prog2_2018c2;


import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class Player extends Sprite implements Commons {

    private final int START_Y = 280;
    private final int START_X = 270;
    private int lives;
    private int shields;

    private final String playerImg = "src/images/player2.png";
    private int width;

    public Player() {

        shields = 4;
        lives = 3;
        initPlayer();
    }

    public void initPlayer() { //cambie el private de esto por public

        ImageIcon ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }

    public void initPlayerDead() { //agregue esto

        ImageIcon ii = new ImageIcon(playerImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(getX());
        setY(getY());
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

    public boolean hasShieldsLeft()
    {
        if(shields>= 1)
        {
            return true;
        }
        return false;
    }

    public void reduceShields(){
        --shields;
    }

    public int getShields() {
        return shields;
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