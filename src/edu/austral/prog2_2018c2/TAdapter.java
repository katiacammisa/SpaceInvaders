package edu.austral.prog2_2018c2;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {

    private Board board;

    public TAdapter(Board board) {
        this.board = board;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        board.getPlayer().keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        board.getPlayer().keyPressed(e);

        int x = board.getPlayer().getX();
        int y = board.getPlayer().getY();

        int key = e.getKeyCode();


        if (key == KeyEvent.VK_SPACE) {

            if (board.isIngame()) {
                if (!board.getShot().isVisible() && !board.isDoubleDamage()) {
                    board.setShot(new Shot(x+6, y));
                }
                if(board.isDoubleDamage() && !board.getShot2().isVisible()){
                    board.setShot(new Shot(x-5, y));
                    board.setShot2(new Shot(x+18, y));
                }
            }
        }
    }
}

