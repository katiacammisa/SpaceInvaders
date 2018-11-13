package edu.austral.prog2_2018c2;

import javax.swing.ImageIcon;

public class Shot extends Sprite {

    private final String shotImg = "src/images/shot.png";
    private final int H_SPACE = 6;
    private final int V_SPACE = 1;
    private int delay = 7;

    public Shot() {}

    public Shot(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {
        ImageIcon ii = new ImageIcon(shotImg);
        setImage(ii.getImage());
        setVisible(true);

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    public int getDelay() {
        return delay;
    }

}
