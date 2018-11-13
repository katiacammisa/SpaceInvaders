package edu.austral.prog2_2018c2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Board extends Drawer implements Runnable, Commons {

    private Dimension d;
    private ArrayList<Alien> aliens;
    private ArrayList<Shield> shields;
    private Player player;
    private Shot shot;
    private Shot shot2;
    private Alien UFO;
    private Timer timer = new Timer();
    private Audio audio;

    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 45;
    private final int SHIELD_INIT_X = 50;
    private final int SHIELD_INIT_Y = 270;
    private int direction = -1;
    private int deaths = 0;
    private int levels = 5;
    private int delay = 15;
    private int shieldAmount = 4;   //Nueva variable
    private int random = 0;
    private int shotCounter = 0;
    private int levelCounter = 1;
    private int scoring;

    private long UfoTimer;

    private boolean doubleDamage;
    private boolean immunity;
    private boolean freeze;
    private boolean ingame = true;
    private boolean ufoOn = false;
    private final String explImg = "src/images/explosion.png";
    private String message = "Game Over :(";

    private Thread animator;

    public Board() { //hola

        player = new Player();
        audio = new Audio("/sound/cancion.wav");
        initBoard();
    }

    private void initBoard() { //Crea el board, lo grafica

        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        addKeyListener(new TAdapter(this));
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() { //??

        super.addNotify();
        gameInit();
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
        audio.play();

        super.setAliens(aliens);
        super.setPlayer(player);
        super.setShields(shields);
        super.setShot(shot);
        super.setShot2(shot2);
        super.setUFO(UFO);
    }

    @Override
    public void paintComponent(Graphics g){
        super.setIngame(ingame);
        super.setD(d);
        super.setDoubleDamage(doubleDamage);
        super.setImmunity(immunity);
        super.setFreeze(freeze);
        super.setLevelCounter(levelCounter);
        super.setScoring(scoring);
        super.paintComponent(g);
    }

    public void gameOver(){

        scoring += player.getLives()*100;

        Graphics g = this.getGraphics();

        gameOver(g, scoring, message);

        PanelPlayer panel = new PanelPlayer(scoring);
        while (!panel.isOk()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        HighScore.run(panel.getScoreData());
    }

    public void animationCycle() {

        // player
        player.act();

        // shot
        shotLife();

        // aliens
        alienLife();

        //Bombs
        bombsFunction();
        //UFO
        ufoAct();

        //PowerUps
        hasPowerUps();

        canPassLevel();
        sumPoints();
        lives();

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

    public void sumPoints(){
        Iterator<Alien> it = aliens.iterator();
        while (it.hasNext()) {
            Alien alien = it.next();
            if (alien.isDying() && !alien.isDead()) {
                scoring += alien.getPoints();
                System.out.println(scoring);
            }
        }

        if (UFO.isDying()) {
            scoring += UFO.getPoints();
        }
    }

    public void lives(){
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

    public void ufoAct(){
        long timing = System.currentTimeMillis() - UfoTimer;

        if(random == 1){
            UFO.act(2);
            UfoTimer = System.currentTimeMillis();
        } else {
            if (timing >= 45000 && timing <= 60000) {
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
    }

    public void canPassLevel(){

        Iterator alin = aliens.iterator();
        int counter = 0;
        while (alin.hasNext()){
            Alien alien = (Alien) alin.next();
            if(!alien.isVisible()){
                counter++;
            }
        }

        if (deaths >= NUMBER_OF_ALIENS_TO_DESTROY && counter == NUMBER_OF_ALIENS_TO_DESTROY) {
            levelCounter++;
            if (levelCounter <= levels) {
                drawLevelPass();
                delayInSeconds(2);
                int count = 0;
                for (int i = 0; i < shieldAmount; i++) {
                    if (shields.get(i).isVisible()) {
                        count++;
                    }
                }
                shieldAmount = count - 1;
                delay -= 2;
                initBoard();
                deaths = 0;
            } else {
                ingame = false;
                message = "Game won!";
            }
        }
    }

    public void hasPowerUps(){
        if (shotCounter == 4) {                                          //DESDE ACA
            shotCounter = 0;
            PowerUp powerUp = new PowerUp();
            player.setPowerUp(powerUp);
            System.out.println(powerUp.getName());
            if (powerUp.getName().equals("Immunity")) {
                immunity = true;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        immunity = false;
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

    public void shotLife(){
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
    }

    public void alienLife(){
        for (Alien alien : aliens) {

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

                if(freeze){
                    alien.act(0);
                }
                else{
                    alien.act(direction);
                }
            }
        }
    }

    public void bombsFunction(){

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
                        if (!immunity) {
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
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Shot getShot() {
        return shot;
    }

    @Override
    public void setShot(Shot shot) {
        this.shot = shot;
    }

    public Shot getShot2() {
        return shot2;
    }

    @Override
    public void setShot2(Shot shot2) {
        this.shot2 = shot2;
    }

    public Alien getUFO() {
        return UFO;
    }

    @Override
    public void setUFO(Alien UFO) {
        this.UFO = UFO;
    }

    public boolean isDoubleDamage() {
        return doubleDamage;
    }

    public boolean isImmunity() {
        return immunity;
    }

    public boolean isFreeze() {
        return freeze;
    }

    public boolean isIngame() {
        return ingame;
    }
}