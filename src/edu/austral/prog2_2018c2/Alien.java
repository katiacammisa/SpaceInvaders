package edu.austral.prog2_2018c2;

import javax.swing.ImageIcon;

public class Alien extends Sprite {

    private Bomb bomb;
    private final String alienImg;
    private int points;
    private AlienType alientype;
    private boolean dead;


    public Alien(int x, int y, AlienType alienType) {

        this.alienImg = alienType.getImage();
        initAlien(x, y);
        this.points = alienType.getPoints();
        alientype = alienType;

    }

    @Override
    public void die() {
        super.die();
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public int getPoints() {
        return points;
    }

    public AlienType getAlientype() {
        return alientype;
    }

    private void initAlien(int x, int y) {

        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(alienImg);
        setImage(ii.getImage());
    }

    public void act(int direction) {

        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bombImg = "src/images/bomb.png";
        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(bombImg);
            setImage(ii.getImage());

        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
    }
}