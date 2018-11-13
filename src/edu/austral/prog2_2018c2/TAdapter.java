package edu.austral.prog2_2018c2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {

    private Player player;
    private Shot shot;
    private Shot shot2;

    private boolean ingame;
    private boolean doubleDamage;

    public TAdapter(Player player, Shot shot, Shot shot2, boolean ingame, boolean doubleDamage) {
        this.player = player;
        this.shot = shot;
        this.shot2 = shot2;
        this.ingame = ingame;
        this.doubleDamage = doubleDamage;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        player.keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        player.keyPressed(e);

        int x = player.getX();
        int y = player.getY();

        int key = e.getKeyCode();


        if (key == KeyEvent.VK_SPACE) {

            if (ingame) {
                if (!shot.isVisible() && !doubleDamage) {
                    shot = new Shot(x+6, y);
                }
                if(doubleDamage && !shot2.isVisible()){
                    shot = new Shot(x-5, y);
                    shot2 = new Shot(x+18, y);
                }
            }
        }
    }
}

