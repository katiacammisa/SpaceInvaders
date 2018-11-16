package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class Shield extends Sprite implements Commons {

    private int life = 100;
    private String shieldImg = "src/images/Shield3.png";
    private int width;
    private boolean visible = true;

    public Shield(int x, int y) {
        initShield(x, y);
    }

    public void initShield(int x, int y) {

        ImageIcon ii = new ImageIcon(shieldImg);

        width = ii.getImage().getWidth(null);

        setImage(ii.getImage());
        setX(x);
        setY(y);
    }

    public int getLife() {
        return life;
    }

    public int getWidth() {
        return width;
    }

    public void reduceLives(){

        if(life > 2) {
            life -= 2;
        }
        else
        {
            life = 0;
            die();
        }
    }

    public boolean hasShieldLeft()
    {
        if(life >= 1)
        {
            return true;
        }
        return false;
    }
}