package main;

import Enemy.Enemy;
import Object.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Enemy.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

public class AssetSetter {
    GamePanel gp;
    public static int rows;
    public static int cols;
    public static Character[][] blockMatrix;
    public static int[][] levelMatrix = new int[100][100];

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
        rows = gp.maxWorldRow;
        cols = gp.maxWorldCol;
        blockMatrix = new Character[gp.maxWorldRow][gp.maxWorldCol];
        this.loadBlockMatrix();
    }

    // functie care citeste din fisierul text matricea ( char ) cu blocuri si o pune intr-o matrice
    public void loadBlockMatrix() {
        try {
            InputStream is = getClass().getResourceAsStream("/matriceBlocuri/blocuri.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    char num = numbers[col].charAt(0);

                    blockMatrix[row][col] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
            InputStream is1 = getClass().getResourceAsStream("/matriceBlocuri/nivele.txt");
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is1));
            int col1 = 0;
            int row1 = 0;
            while (col1 < gp.maxWorldCol && row1 < gp.maxWorldRow) {
                String line1 = br1.readLine();
                while (col1 < gp.maxWorldCol) {
                    String[] numbers1 = line1.split(" ");
                    int num1 = Character.getNumericValue(numbers1[col1].charAt(0));

                    levelMatrix[row1][col1] = num1;
                    col1++;
                }
                if (col1 == gp.maxWorldCol) {
                    col1 = 0;
                    row1++;
                }
            }
            br1.close();
        }
        catch (IOException e){
            System.out.println("da");
        }
    }

    //functie care citeste matricea cu blocuri si populeaza vectorul SuperObj cu obiecte de tip Block
    public void setObject()  {
        // id-urile fiecarui block;
        //a-> DUG_G
        //b-> HAS_G
        //c->NOTHING_G
        //d->ENEMY_G
        //e->IS_G
        //f->DEATH_G
        //g->GUN_G
        //i->SHIELD
        int index = 0;
        for (int i = 0; i < gp.maxWorldRow; i++)
            for (int j = 0; j < gp.maxWorldCol; j++) {
                if (!Character.isDigit(blockMatrix[i][j])) {
                    if (index < gp.superObj.length) {
                        Block.movementType typeM = Block.movementType.G;
                        char curentChar = 'a';
                        if (Character.isLowerCase(blockMatrix[i][j])) {
                            typeM = Block.movementType.G;
                            curentChar = blockMatrix[i][j];

                        } else if (Character.isUpperCase(blockMatrix[i][j])) {
                            typeM = Block.movementType.P;
                            curentChar = blockMatrix[i][j];

                        } else {
                            typeM = Block.movementType.Y;
                            if (blockMatrix[i][j] == '*')
                                curentChar = 'd';
                            else if (blockMatrix[i][j] == '&')
                                curentChar = 'e';
                            else if (blockMatrix[i][j] == '#')
                                curentChar = 'f';

                        }
                        gp.superObj[index] = new Block(curentChar, typeM);
                        gp.superObj[index].worldX = j * GamePanel.tileSize;
                        gp.superObj[index].worldY = i * GamePanel.tileSize;
                        index++;
                    }
                }
            }


    }

    //SETEAZA INAMICII IN FUNCTIE DE NIVEL ( MONSTER ESTE ECHIVALENT CU ENEMY )
    public void setMonster() {
        CombatObjectFactory objectFactory = new CombatObjectFactory(gp);
        if (gp.levelManager.curentLevel == 0) {
            gp.monster[0] = new Enemy.EnemyBuilder().setGP(gp).build();
            gp.monster[0].worldX = GamePanel.tileSize * 11;
            gp.monster[0].worldY = GamePanel.tileSize * 14;
            gp.monster[0].index = 0;

            gp.monster[1] = new Enemy.EnemyBuilder().setGP(gp).build();
            gp.monster[1].worldX = GamePanel.tileSize * 14;
            gp.monster[1].worldY = GamePanel.tileSize * 17;
            gp.monster[1].index = 1;

            gp.monster[2] = new Enemy.EnemyBuilder().setGP(gp).build();
            gp.monster[2].worldX = GamePanel.tileSize * 16;
            gp.monster[2].worldY = GamePanel.tileSize * 17;
            gp.monster[2].index = 2;

            gp.monster[3] = new Enemy.EnemyBuilder().setGP(gp).build();
            gp.monster[3].worldX = GamePanel.tileSize * 12;
            gp.monster[3].worldY = GamePanel.tileSize * 17;
            gp.monster[3].index = 3;

            gp.levelManager.levels.get(gp.levelManager.curentLevel).numberEnemies = 4;
        }
        if (gp.levelManager.curentLevel == 1) {
            gp.monster[0] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 44 / 2,
                            GamePanel.tileSize * 19)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[0],
                                    0))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[0],
                                    0))
                    .build();
            gp.monster[0].gun.worldX = gp.monster[0].worldX + GamePanel.tileSize;
            gp.monster[0].gun.worldY = gp.monster[0].worldY;
            gp.monster[0].shield.worldX = gp.monster[0].worldX - 15;
            gp.monster[0].shield.worldY = gp.monster[0].worldY - 15;
            gp.monster[0].index = 0;
            gp.monster[0].speed += 2;

            gp.monster[1] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 72 / 2,
                            GamePanel.tileSize * 20)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[1],
                                    1))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[1],
                                    1))
                    .build();
            gp.monster[1].index = 1;
            gp.monster[1].speed += 2;
            gp.monster[1].gun.worldX = gp.monster[1].worldX + GamePanel.tileSize;
            gp.monster[1].gun.worldY = gp.monster[1].worldY;
            gp.monster[1].shield.worldX = gp.monster[1].worldX - 15;
            gp.monster[1].shield.worldY = gp.monster[1].worldY - 15;

            gp.monster[2] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 60 / 2,
                            GamePanel.tileSize * 12)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[2],
                                    2))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[2],
                                    2))
                    .build();
            gp.monster[2].index = 2;
            gp.monster[2].speed += 2;
            gp.monster[2].gun.worldX = gp.monster[2].worldX + GamePanel.tileSize;
            gp.monster[2].gun.worldY = gp.monster[2].worldY;
            gp.monster[2].shield.worldX = gp.monster[2].worldX - 15;
            gp.monster[2].shield.worldY = gp.monster[2].worldY - 15;

            gp.monster[3] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 70 / 2,
                            GamePanel.tileSize * 10)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[3],
                                    3))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[3],
                                    3))
                    .build();
            gp.monster[3].index = 3;
            gp.monster[3].speed += 2;
            gp.monster[3].gun.worldX = gp.monster[3].worldX + GamePanel.tileSize;
            gp.monster[3].gun.worldY = gp.monster[3].worldY;
            gp.monster[3].shield.worldX = gp.monster[3].worldX - 15;
            gp.monster[3].shield.worldY = gp.monster[3].worldY - 15;

            gp.levelManager.levels.get(gp.levelManager.curentLevel).numberEnemies = 4;

        }
        if (gp.levelManager.curentLevel == 2) {
            gp.monster[0] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 90 / 2,
                            GamePanel.tileSize * 19)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[0],
                                    0))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[0],
                                    0))
                    .build();

            gp.monster[0].index = 0;
            gp.monster[0].gun.worldX = gp.monster[0].worldX + GamePanel.tileSize;
            gp.monster[0].gun.worldY = gp.monster[0].worldY;
            gp.monster[0].shield.worldX = gp.monster[0].worldX - 15;
            gp.monster[0].shield.worldY = gp.monster[0].worldY - 15;

            gp.monster[1] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 98 / 2,
                            GamePanel.tileSize * 25)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[1],
                                    1))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[1],
                                    1))
                    .build();

            gp.monster[1].index = 1;
            gp.monster[1].gun.worldX = gp.monster[1].worldX + GamePanel.tileSize;
            gp.monster[1].gun.worldY = gp.monster[1].worldY;
            gp.monster[1].shield.worldX = gp.monster[1].worldX - 15;
            gp.monster[1].shield.worldY = gp.monster[1].worldY - 15;

            gp.monster[2] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 106 / 2,
                            GamePanel.tileSize * 13)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[2],
                                    2))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[2],
                                    2))
                    .build();

            gp.monster[2].index = 2;
            gp.monster[2].gun.worldX = gp.monster[2].worldX + GamePanel.tileSize;
            gp.monster[2].gun.worldY = gp.monster[2].worldY;
            gp.monster[2].shield.worldX = gp.monster[2].worldX - 15;
            gp.monster[2].shield.worldY = gp.monster[2].worldY - 15;

            gp.monster[3] = new Enemy.EnemyBuilder().setGP(gp).setPosition(
                            GamePanel.tileSize * 104 / 2,
                            GamePanel.tileSize * 19)
                    .setGun(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.GUN,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[3],
                                    3))
                    .setShield(
                            objectFactory.createCombatObject(
                                    CombatObjectFactory.type.SHIELD,
                                    CombatObject.Owner.ENEMY,
                                    gp.monster[3],
                                    3))
                    .build();

            gp.monster[3].index = 3;
            gp.monster[3].gun.worldX = gp.monster[3].worldX + GamePanel.tileSize;
            gp.monster[3].gun.worldY = gp.monster[3].worldY;
            gp.monster[3].shield.worldX = gp.monster[3].worldX - 15;
            gp.monster[3].shield.worldY = gp.monster[3].worldY - 15;

            gp.levelManager.levels.get(gp.levelManager.curentLevel).numberEnemies = 4;
            gp.enemyEnemy = new EnemyEnemy(gp);
            gp.enemyEnemy.worldX = GamePanel.tileSize * 51;
            gp.enemyEnemy.worldY = GamePanel.tileSize * 16;

        }


    }

    public void reset() {
        gp.projectileList.clear();
        gp.combatObjects.clear();
        for (int i = 0; i < gp.monster.length; i++) {
            if (gp.monster[i] != null)
                gp.monster[i] = null;
        }
    }

    public void setCombatObjects() {

            CombatObjectFactory objectFactory = new CombatObjectFactory(gp);

            gp.player.gun = objectFactory.createCombatObject(CombatObjectFactory.type.GUN, CombatObject.Owner.PLAYER, gp.player, -1);
            gp.player.shield = objectFactory.createCombatObject(CombatObjectFactory.type.SHIELD, CombatObject.Owner.PLAYER, gp.player, -1);



    }
}
