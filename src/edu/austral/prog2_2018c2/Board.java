package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.image.ImageObserver;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends JPanel implements Runnable, Commons {

    private Dimension d;
    private ArrayList<Alien> aliens;
    private ArrayList<Shield> shields;
    private Player player = new Player();
    private Shot shot;
    private Shot shot2;
    private Scoring scoring;
    private Alien UFO;
    private TextFile textFile = new TextFile();
    private Timer timer = new Timer();

    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 45;
    private final int SHIELD_INIT_X = 50;
    private final int SHIELD_INIT_Y = 270;
    private int direction = -1;
    private int deaths = 0;
    private int levels = 5;
    private int levelCounter = 1;
    private int shieldAmount = 4;   //Nueva variable
    private int random = 0;
    private int shotCounter = 0;
    private int delay = 13;

    private long UfoTimer;

    private boolean doubleDamage;
    private boolean inmunity;
    private boolean freeze;
    private boolean ingame = true;
    private boolean ufoOn = false;
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

    private void initBoard() { //Crea el board, lo grafico

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() { //??

        super.addNotify();
        gameInit();
    }

    public int getShotCounter() {
        return shotCounter;
    }

    public void delayInSeconds(int time){ // Para frenar el juego cuando pasamos de nivel
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {}
    }

    public void gameInit() { //inicia el juego, y todas sus componentes

        aliens = new ArrayList<>();
        Graphics g = this.getGraphics();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                Random rn = new Random();
                int answer = rn.nextInt((3-1)+1) + 1;
                switch(answer) {
                    case 1:
                        Alien smallAlien = new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 22 * i, AlienType.SmallAlien);
                        aliens.add(smallAlien);
                        break;
                    case 2:
                        Alien mediumAlien = new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 22 * i, AlienType.MediumAlien);
                        aliens.add(mediumAlien);
                        break;
                    case 3:
                        Alien bigAlien = new Alien(ALIEN_INIT_X + 25 * j, ALIEN_INIT_Y + 22 * i, AlienType.BigAlien);
                        aliens.add(bigAlien);
                        break;
                }
            }
        }

        shields = new ArrayList<Shield>();
        for (int i = 1; i <= shieldAmount; i++) {
            Shield shield = new Shield(((BOARD_WIDTH / shieldAmount)*i - (BOARD_WIDTH/shieldAmount)/2) - 15,SHIELD_INIT_Y);
            shields.add(shield);
        }

        shot = new Shot();
        shot2 = new Shot();

        UFO = new Alien(-25, 25,AlienType.UFO);

        if (animator == null || !ingame) {

            animator = new Thread(this);
            animator.start();
        }

        random = 0;
    }

    public void drawAliens(Graphics g) {

        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()){
            Alien alien = it.next();
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying() && !alien.isDead()) {
                alien.die();
                //it.remove();
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
            } else {
                player.die();
                message = "Game Over :(";
                ingame = false;
            }
        }
    }

    public void drawShield(Graphics g) { //saque el for, para usar iterator

        Iterator<Shield> it = shields.iterator();
        while(it.hasNext())
        {
            Shield shield = it.next();
            if (shield.isVisible()) {
                g.drawImage(shield.getImage(), shield.getX(), shield.getY(), this);
            }
            if(shield.isDying()) //Esto es nuevo
            {
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

    public void drawUFO(Graphics g) {  //Bienvenido sea UFO que no se digna a aparecer
        if(UFO.isVisible()) {
            g.drawImage(UFO.getImage(), UFO.getX(), UFO.getY(), this);
        }
        if(UFO.isDying()) {
            UFO.die();
            scoring.sumPoints(UFO.getPoints());
            UFO.setDying(false);
            UFO.setDying(false);
        }
        if(UFO.getX() > BOARD_WIDTH) {
            UFO.die();
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
            g.drawString("Level: " + levelCounter,147,14);
            drawAliens(g);
            drawPlayer(g);
            drawShield(g);
            drawShot(g);
            drawShot(g);
            drawBombing(g);
            drawUFO(g);

            FontMetrics metrics = this.getFontMetrics(small);
            g.setColor(new Color(151, 14, 179));
            if(doubleDamage){
                g.drawString("Double Damage On!", (BOARD_WIDTH - metrics.stringWidth("Double Damage On!"))/ 2 , GROUND+25);
            }
            if(inmunity){
                g.drawString("Inmunity On!", (BOARD_WIDTH - metrics.stringWidth("Inmunity On!"))/ 2 , GROUND+25);
            }
            if(freeze){
                g.drawString("Freeze!", (BOARD_WIDTH - metrics.stringWidth("Freeze!"))/ 2 , GROUND+25);
            }
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
        g.drawString("Score: " + scoring.getScore(), (BOARD_WIDTH - metr.stringWidth("Score: " + scoring.getScore())) / 2, BOARD_WIDTH / 2 + 20);//esto
        scoring.sumPoints(player.getLives()*100);
        Panel panel = new Panel();
        textFile.run(scoring.getScore());

    }

    public void drawLevelPass(){  // esto es nuevo

        Graphics g = this.getGraphics();

        Font small = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Level Passed! :)", (BOARD_WIDTH - metr.stringWidth("Level Passed! :)")) / 2, BOARD_WIDTH / 2 - 5);

    }

    public void animationCycle() {

        long timing = System.currentTimeMillis() - UfoTimer;

        if(random == 1){
            UFO.act(2);
            UfoTimer = System.currentTimeMillis();
        } else {
            if (timing >= 7000 && timing <= 60000) {
                random = (int) (Math.random() * 30) + 1;
            }
            if (timing > 60000) {
                random = 1;
            }
        }

        if(UFO.getX() > BOARD_WIDTH){
            random = 0;
            UFO.die();
        }

        Iterator alin = aliens.iterator();
        int counter = 0;
        while (alin.hasNext()){
            Alien alien = (Alien) alin.next();
            if(alien.isVisible() == false){
                counter++;
            }
        }

        if (deaths >= NUMBER_OF_ALIENS_TO_DESTROY && counter == NUMBER_OF_ALIENS_TO_DESTROY) {
            levelCounter++;
            if (levelCounter <= levels) {
                drawLevelPass();
                delayInSeconds(1);
                int count = 0;                              //Desde aca
                for (int i = 0; i < shieldAmount; i++) {
                    if (shields.get(i).isVisible()) {
                        count++;
                    }
                }
                shieldAmount = count - 1;                   //Hasta aca hace que vayan bajando los Shields
//                if(direction<0){
//                    direction -=1;
//                }
//                if(direction>0){
//                    direction+=1;
//                }
                delay-=2;
                initBoard();
                deaths = 0;
            } else {
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
            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + ALIEN_HEIGHT)) {
                        alien.setDying(true);
                        if(alien.isDying()) {
                            deaths++;
                            shot.die();
                            shotCounter++;
                        }
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
                shotCounter = 0;
            } else {
                shot.setY(y);
            }
        }

        if (shot2.isVisible()) {

            int shotX = shot2.getX();
            int shotY = shot2.getY();
            for (Alien alien : aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot2.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + ALIEN_HEIGHT)) {
                        ImageIcon ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        if(alien.isDying()) {
                            deaths++;
                            shot2.die();
                        }
                    }
                }
            }

            int y = shot2.getY();
            y -= 4;

            if (y < 0) {
                shot2.die();
                shotCounter = 0;
            } else {
                shot2.setY(y);
            }
        }

        // aliens

        for (Alien alien : aliens) {

            int x = alien.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction > 0) {

                direction = -direction;
                Iterator i1 = aliens.iterator();

                while (i1.hasNext()) {

                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + GO_DOWN);
                }
            }

            if (x <= BORDER_LEFT && direction < 0) {

                direction = -direction;

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

                if(!freeze) {
                    alien.act(direction);
                }
                if(freeze){
                    alien.act(0);
                }
            }
        }

        Random generator = new Random();
        for (Iterator<Alien> iterator = aliens.iterator(); iterator.hasNext(); ) {
            Alien alien = iterator.next();

            int shots = generator.nextInt(15);
            Alien.Bomb b = alien.getBomb();
            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();
            int shotX = shot.getX();
            int shotY = shot.getY();
            int shot2X = shot2.getX();
            int shot2Y = shot2.getY();

            // bombs
            if (!freeze) {
                if (shots == CHANCE && alien.isVisible() && b.isDestroyed()) {

                    b.setDestroyed(false);
                    b.setX(alien.getX());
                    b.setY(alien.getY());
                }

                if (player.isVisible() && !b.isDestroyed()) {

                    if (bombX >= (playerX)
                            && bombX <= (playerX + PLAYER_WIDTH)
                            && bombY >= (playerY)
                            && bombY <= (playerY + PLAYER_HEIGHT)) {
                        if (!inmunity) {
                            player.setDying(true);
                        }
                        b.setDestroyed(true);
                    }
                }

                int ufox = UFO.getX();                          //DESDE ACA
                int ufoy = UFO.getY();

                if (shotX >= (ufox) && UFO.isVisible()
                        && shotX <= (ufox + UFO_WIDTH)
                        && shotY <= (ufoy + (UFO_HEIGHT))
                        && shotY > (ufoy)
                        && shot.isVisible()) {
                    UFO.setDying(true);
                    shot.die();
                    shotCounter++;                              //HASTA ACA DESTROY UFO
                }

                if (!b.isDestroyed()) {
                    b.setY(b.getY() + 1);
                    if (b.getY() >= GROUND - BOMB_HEIGHT) {
                        b.setDestroyed(true);
                    }
                }
            }

            for (Shield shield : shields) {
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
                if (shotX >= (shieldX) && shield.isVisible()
                        && shotX <= (shieldX + shield.getWidth())
                        && shotY <= (shieldY + 3.5 * (SHIELD_HEIGHT))
                        && shotY > (shieldY)
                        && shot.isVisible()) {
                    shield.reduceLives();
                    shot.die();
                }
                if (shot2X >= (shieldX) && shield.isVisible()
                        && shot2X <= (shieldX + shield.getWidth())
                        && shot2Y <= (shieldY + 3.5 * (SHIELD_HEIGHT))
                        && shot2Y > (shieldY)
                        && shot2.isVisible()) {
                    shield.reduceLives();
                    shot2.die();
                }
            }
        }

        //PowerUps
        if (shotCounter == 4) {                                          //DESDE ACA
            shotCounter = 0;
            PowerUp powerUp = new PowerUp();
            player.setPowerUp(powerUp);
            System.out.println(powerUp.getName());
            if (powerUp.getName().equals("Immunity")) {
                inmunity = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        inmunity = false;
                        player.setPowerUp(null);
                    }
                };
                timer.schedule(task, (int)(Math.random()*5000)+3000);

            } else if (powerUp.getName().equals("Double Damage")) {
                doubleDamage = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        doubleDamage = false;
                        player.setPowerUp(null);
                    }
                };
                timer.schedule(task, (int)(Math.random()*5000)+3000);

            } else if (powerUp.getName().equals("Freeze")) {
                freeze = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        freeze = false;
                        player.setPowerUp(null);
                    }
                };
                timer.schedule(task, (int)(Math.random()*5000)+3000);
            }
        }
        if(player.getPowerUp() != null) {
            shotCounter = 0;
        }

    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        UfoTimer = System.currentTimeMillis();
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
}