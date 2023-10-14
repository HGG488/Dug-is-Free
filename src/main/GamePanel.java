package main;

import Enemy.Enemy;
import Enemy.EnemyEnemy;
import Tiles.TileManager;
import entity.Entity;
import entity.Player;
import Object.SuperObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import Object.CombatObject;
import Object.*;

public class GamePanel extends JPanel implements Runnable {
    static final int originalTileSize = 66;
    static final int scale = 1;
    public static final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int maxWorldCol = 65;
    public final int maxWorldRow = 35;
    private static GamePanel singleInstance = null;
    Thread gameThread;
    public KeyHandler keyH = new KeyHandler(this);
    public MouseHandler mouseH = new MouseHandler(this);
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public SuperObject[] superObj = new SuperObject[40];
    public AssetSetter assetSetter = new AssetSetter(this);
    public DBoperator dbOperator = new DBoperator(this);
    public Entity[] monster = new Entity[20];
    public ArrayList<Projectile1> projectileList = new ArrayList<>();
    public ArrayList<CombatObject> combatObjects = new ArrayList<>();
    public UI ui = new UI(this);
    public int gameState;
    public int score = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int gameOverState = 3;
    public final int titleState = 0;
    public final int endGameState = 4;
    public LevelManager levelManager = new LevelManager(this);
    public Player player = new Player(this, keyH);
    public EnemyEnemy enemyEnemy;


    private GamePanel() {
        super();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.white);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }


    // Sablon de proiectare: GamePanel este Singleton , functia getInstance returnand o instanta a singurului obiect de tip GamePanel
    public static GamePanel getInstance()  {
        if (singleInstance == null)
        {
            singleInstance = new GamePanel();

        }


        return singleInstance;
    }

    public void setupGame() {
        levelManager.curentLevel = 0;
        dbOperator.create_table();
        score = dbOperator.getScore();
        player.setDefaultValues();
        addMouseListener(mouseH);
        levelManager.setUp();
        gameState = titleState;
        assetSetter.setObject();
        assetSetter.setMonster();
        assetSetter.setCombatObjects();

    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    //Algoritm folosit pentru a afisa exact 60 de FPS
    @Override
    public void run() {
        double drawInterval = 1000000000 / 60.;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            try {
                update();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if ((long) remainingTime > 0)
                    Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() throws IOException {
        if (gameState == playState) {
            player.update();
            tileM.update();
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive)
                        monster[i].update();
                    else {
                        ((Enemy) monster[i]).discardMonster();
                        levelManager.killEnemy();
                    }
                }
            }
            if (enemyEnemy != null) {
                if (CombinationChecker.ENEMY_HAS_ENEMY)
                    enemyEnemy.update();
            }

            levelManager.update();
            for (int i = 0; i < combatObjects.size(); i++) {
                if (combatObjects.get(i) != null) {
                    if (combatObjects.get(i).owner == CombatObject.Owner.PLAYER) {
                        if (combatObjects.get(i).type == CombatObject.Type.GUN) {
                            if (CombinationChecker.DUG_HAS_GUN && !CombinationChecker.GUN_IS_NOTHING && !CombinationChecker.DUG_HAS_NOTHING)
                                combatObjects.get(i).update(this);
                        }
                        if (combatObjects.get(i).type == CombatObject.Type.SHIELD) {
                            if (CombinationChecker.DUG_HAS_SHIELD && !CombinationChecker.SHIELD_IS_NOTHING && !CombinationChecker.DUG_HAS_NOTHING)
                                combatObjects.get(i).update(this);
                        }

                    } else {
                        if (combatObjects.get(i).type == CombatObject.Type.GUN) {
                            if (CombinationChecker.ENEMY_HAS_GUN && !CombinationChecker.GUN_IS_NOTHING && !CombinationChecker.ENEMY_HAS_NOTHING)
                                combatObjects.get(i).update(this);
                        }
                        if (combatObjects.get(i).type == CombatObject.Type.SHIELD) {
                            if (CombinationChecker.ENEMY_HAS_SHIELD && !CombinationChecker.SHIELD_IS_NOTHING && !CombinationChecker.ENEMY_HAS_NOTHING)
                                combatObjects.get(i).update(this);
                        }

                    }

                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive) {
                        projectileList.get(i).update();

                    }
                    if (!projectileList.get(i).alive) {
                        projectileList.remove(i);
                        ;
                    }
                }
            }
        }



    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (gameState == titleState) {
            ui.draw(g2);

        } else {
            tileM.draw(g2);
            for (int i = 0; i < superObj.length; i++) {
                if (superObj[i] != null)
                    superObj[i].draw(g2, this);

            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null)
                    monster[i].draw(g2);

            }
            if (enemyEnemy != null) {
                if (CombinationChecker.ENEMY_HAS_ENEMY)
                    enemyEnemy.draw(g2, this);
            }

            player.draw(g2);
            for (int i = 0; i < combatObjects.size(); i++) {
                if (combatObjects.get(i) != null) {

                    if (combatObjects.get(i).owner == CombatObject.Owner.PLAYER) {
                        if (combatObjects.get(i).type == CombatObject.Type.GUN) {
                            if (CombinationChecker.DUG_HAS_GUN && !CombinationChecker.GUN_IS_NOTHING && !CombinationChecker.DUG_HAS_NOTHING)
                                combatObjects.get(i).draw(g2, this);
                        }
                        if (combatObjects.get(i).type == CombatObject.Type.SHIELD) {
                            if (CombinationChecker.DUG_HAS_SHIELD && !CombinationChecker.SHIELD_IS_NOTHING && !CombinationChecker.DUG_HAS_NOTHING)
                                combatObjects.get(i).draw(g2, this);
                        }

                    } else {
                        if (combatObjects.get(i).type == CombatObject.Type.GUN) {
                            if (CombinationChecker.ENEMY_HAS_GUN && !CombinationChecker.GUN_IS_NOTHING && !CombinationChecker.ENEMY_HAS_NOTHING)
                                combatObjects.get(i).draw(g2, this);
                        }
                        if (combatObjects.get(i).type == CombatObject.Type.SHIELD) {
                            if (CombinationChecker.ENEMY_HAS_SHIELD && !CombinationChecker.SHIELD_IS_NOTHING && !CombinationChecker.ENEMY_HAS_NOTHING)
                                combatObjects.get(i).draw(g2, this);
                        }

                    }
                }
            }

            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    projectileList.get(i).draw(g2);

                }

            }
            ui.draw(g2);
        }
        g2.dispose();
    }


    public void restart() {


        player.setDefaultValues();
        assetSetter.reset();
        assetSetter.loadBlockMatrix();
        assetSetter.setObject();
        assetSetter.setMonster();
        assetSetter.setCombatObjects();


    }

    //functie care parcurge lista de obiecte din GamePanel si returneaza un obiect ( daca exista) in functie de coordanatele date ca parametri
    public SuperObject returnSuperObjectByPosition(int x, int y) {
        for (int i = 0; i < superObj.length; i++)
            if (superObj[i].worldX / tileSize == x && superObj[i].worldY / tileSize == y)
                return superObj[i];
        return null;
    }

}
