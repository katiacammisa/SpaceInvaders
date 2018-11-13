package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Drawer extends JPanel implements Commons {

    private ArrayList<Alien> aliens;
    private Player player;
    private ArrayList<Shield> shields;
    private Shot shot;
    private Shot shot2;
    private Alien UFO;
    private Dimension d;
    private boolean ingame;
    private boolean doubleDamage;
    private boolean immunity;
    private boolean freeze;
    private int scoring;
    private int levelCounter;

    public Drawer(){

    }

    public void setAliens(ArrayList<Alien> aliens) {
        this.aliens = aliens;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setShields(ArrayList<Shield> shields) {
        this.shields = shields;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }

    public void setShot2(Shot shot2) {
        this.shot2 = shot2;
    }

    public void setUFO(Alien UFO) {
        this.UFO = UFO;
    }

    public void setD(Dimension d) {
        this.d = d;
    }

    public void setIngame(boolean ingame) {
        this.ingame = ingame;
    }

    public void setDoubleDamage(boolean doubleDamage) {
        this.doubleDamage = doubleDamage;
    }

    public void setImmunity(boolean immunity) {
        this.immunity = immunity;
    }

    public void setFreeze(boolean freeze) {
        this.freeze = freeze;
    }

    public void setScoring(int scoring) {
        this.scoring = scoring;
    }

    public void setLevelCounter(int levelCounter) {
        this.levelCounter = levelCounter;
    }

    public void drawAliens(Graphics g) {

        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()) {
            Alien alien = it.next();
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying() && !alien.isDead()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
        }
    }

    public void drawShield(Graphics g) {

        Iterator<Shield> it = shields.iterator();
        while (it.hasNext()) {
            Shield shield = it.next();
            if (shield.isVisible()) {
                g.drawImage(shield.getImage(), shield.getX(), shield.getY(), this);
            }
            if (shield.isDying()) {
                shield.die();
                it.remove();
            }
        }
    }

    public void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
        if (shot2.isVisible()) {

            g.drawImage(shot2.getImage(), shot2.getX(), shot2.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {

        for (Alien a : aliens) {

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    public void drawUFO(Graphics g) {
        if (UFO.isVisible()) {
            g.drawImage(UFO.getImage(), UFO.getX(), UFO.getY(), this);
        }
        if (UFO.isDying()) {
            UFO.die();
            UFO.setDying(false);
        }
        if (UFO.getX() > BOARD_WIDTH) {
            UFO.die();
        }
    }

    public void drawLevelPass() {

        Graphics g = this.getGraphics();
        Font small = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Level Passed! :)", (BOARD_WIDTH - metr.stringWidth("Level Passed! :)")) / 2, BOARD_WIDTH / 2 - 5);

    }

    public void gameOver(Graphics g, int scoring, String message){
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        Image cat;

        if(message.equals("Game Over :(")|| message.equals("Invasion!")) {
            cat = new ImageIcon("src/images/gatito.jpg").getImage();
            g.drawImage(cat, 0, 0, null);
        } else {
            cat = new ImageIcon("src/images/gatito2.jpg").getImage();
            g.drawImage(cat, 0, 0, null);
        }

        g.setColor(new Color(64, 255, 21));
        g.fillRect(50, BOARD_WIDTH / 2 - 40, BOARD_WIDTH - 100, 80); //30
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 40, BOARD_WIDTH - 100, 80);

        Font small = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(small);
        Font smalleano = new Font("Monospaced", Font.BOLD, 16); //Lo cambie a bold

        g.setColor(Color.black);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2 - 5);
        g.setFont(smalleano);

        g.drawString("Score: " + scoring, (BOARD_WIDTH - metr.stringWidth("Score: " + scoring)) / 2, BOARD_WIDTH / 2 + 20);
    }

    public void inGame(Graphics g, int scoring, int level){
        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        g.drawString("Score: " + scoring , 10, 12);
        g.drawString("Lives: " + player.getLives(), 300, 12);
        Font small = new Font("Helvetica", Font.PLAIN, 17);
        g.setFont(small);
        g.drawString("Level: " + level,147,14);
        drawAliens(g);
        drawPlayer(g);
        drawShield(g);
        drawShot(g);
        drawBombing(g);
        drawUFO(g);
    }

    public void doubleDamage(Graphics g){
        Font small = new Font("Helvetica", Font.PLAIN, 17);
        FontMetrics metrics = this.getFontMetrics(small);
        g.setColor(new Color(151, 14, 179));
        g.drawString("Double Damage On!", (BOARD_WIDTH - metrics.stringWidth("Double Damage On!"))/ 2 , GROUND+25);
    }

    public void immunity(Graphics g){
        Font small = new Font("Helvetica", Font.PLAIN, 17);
        FontMetrics metrics = this.getFontMetrics(small);
        g.setColor(new Color(151, 14, 179));
        g.drawString("Immunity On!", (BOARD_WIDTH - metrics.stringWidth("Immunity On!"))/ 2 , GROUND+25);

    }

    public void freeze(Graphics g){
        Font small = new Font("Helvetica", Font.PLAIN, 17);
        FontMetrics metrics = this.getFontMetrics(small);
        g.setColor(new Color(151, 14, 179));
        g.drawString("Freeze!", (BOARD_WIDTH - metrics.stringWidth("Freeze!"))/ 2 , GROUND+25);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {

            inGame(g, scoring, levelCounter);

            if(doubleDamage)
                doubleDamage(g);
            if(immunity)
                immunity(g);
            if(freeze)
                freeze(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
}