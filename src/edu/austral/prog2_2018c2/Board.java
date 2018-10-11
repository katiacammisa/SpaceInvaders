package edu.austral.prog2_2018c2;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private ArrayList<Alien> aliens;
    private ArrayList<Shield> shields;
    private Player player = new Player();
    private Shot shot;
    private Scoring scoring;

    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 5;
    private final int SHIELD_INIT_X = 50;
    private final int SHIELD_INIT_Y = 230;
    private int direction = -1;
    private int deaths = 0;
    private int levels = 5;
    private int counter = 1;
    private int delay = 17;

    private boolean ingame = true;
    private final String explImg = "src/images/explosion.png";
    private String message = "Game Over :(";

    private Thread animator;

    public Board() {

        initBoard();
        scoring = new Scoring();
    }

    public int getLevels() {
        return levels;
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        aliens = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                Random rn = new Random();
                int answer = rn.nextInt((3-1)+1) + 1;
                switch(answer){
                    case 1:
                        Alien smallAlien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, AlienType.SmallAlien);
                        aliens.add(smallAlien);
                        break;
                    case 2:
                        Alien mediumAlien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, AlienType.MediumAlien);
                        aliens.add(mediumAlien);
                        break;
                    case 3:
                        Alien bigAlien = new Alien(ALIEN_INIT_X + 18 * j, ALIEN_INIT_Y + 18 * i, AlienType.BigAlien);
                        aliens.add(bigAlien);
                        break;
                }
            }
        }

        shields = new ArrayList<Shield>();
        for (int i = 0; i < 4; i++) {
            Shield shield = new Shield(SHIELD_INIT_X + 80 * i,SHIELD_INIT_Y);
            shields.add(shield);
        }

        shot = new Shot();

        if (animator == null || !ingame) {

            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) {

        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()){
            Alien alien = it.next();
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
                it.remove();
                scoring.sumPoints(alien.getPoints());
                System.out.println(scoring.getScore());
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            if (player.hasLivesLeft()) {
                    player.reduceLives();
                    player.setDying(false);
                    player.initPlayer();
                    player.setVisible(true);
                }
            else {
                player.die();
                message = "Game Over :(";
                ingame = false;
            }
        }
    }

    public void drawShield(Graphics g) {

        for(Shield shield : shields) {
            if (shield.isVisible()) {
                g.drawImage(shield.getImage(), shield.getX(), shield.getY(), this);
            }
        }

    }

    public void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {

            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            g.drawString("Score: " + scoring.getScore(), 10, 12);
            g.drawString("Lives: " + player.getLives(), 300, 12);
            Font small = new Font("Helvetica", Font.PLAIN, 17);
            g.setFont(small);
            g.drawString("Level: " + counter,147,14);
            drawAliens(g);
            drawPlayer(g);
            drawShield(g);
            drawShot(g);
            drawBombing(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

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
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.black);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);


    }

    public void animationCycle() {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            counter++;
            if(counter <= levels){
                System.out.println(message = "Level pass! :)");
                delay -= 2;
                initBoard();
                deaths = 0;
            }
            else{
                ingame = false;
                message = "Game won!";
            }
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien: aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + ALIEN_HEIGHT)) {
                        ImageIcon ii
                                = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens

        for (Alien alien: aliens) {

            int x = alien.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {

                direction = -1;
                Iterator i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator i2 = aliens.iterator();

                while (i2.hasNext()) {

                    Alien a = (Alien) i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }

        Iterator it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    ingame = false;
                    message = "Invasion!";
                }

                alien.act(direction);
            }
        }

        // bombs
        Random generator = new Random();

        for (Alien alien: aliens) {

            int shots = generator.nextInt(15);
            Alien.Bomb b = alien.getBomb();

            if (shots == CHANCE && alien.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(alien.getX());
                b.setY(alien.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            int shotX = shot.getX();
            int shotY = shot.getY();

            if (player.isVisible() && !b.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + PLAYER_HEIGHT)) {
                    ImageIcon ii = new ImageIcon(explImg);
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    b.setDestroyed(true);
                }
            }

            for(Shield shield : shields)
            {
                int shieldX = shield.getX();
                int shieldY = shield.getY();

                if (shield.isVisible() && !b.isDestroyed()) {

                    if (bombX >= (shieldX)
                            && bombX <= (shieldX + shield.getWidth())
                            && bombY >= (shieldY - SHIELD_HEIGHT)
                            && bombY <= (shieldY + SHIELD_HEIGHT)) {
                        shield.reduceLives();
                        b.setDestroyed(true);
                    }
                }
                    if(shotX >= (shieldX) && shield.isVisible()
                            && shotX <= (shieldX + shield.getWidth())
                            && shotY <= (shieldY + 3.5 * (SHIELD_HEIGHT))
                            && shotY > (shieldY)
                            && shot.isVisible()) {
                        shield.reduceLives();
                        shot.die();
                }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = delay - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }


        gameOver();
    }

    private class TAdapter extends KeyAdapter {

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
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}