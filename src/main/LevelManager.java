package main;

import Enemy.Enemy;

import java.io.IOException;
import java.util.ArrayList;

public class LevelManager {
    public int curentLevel=0;
    GamePanel gp;
    int numberOfLevel;
    public ArrayList<Level> levels=new ArrayList<>();

    public LevelManager(GamePanel gp){
        this.curentLevel=0;
        numberOfLevel=0;
        this.gp=gp;
    }
    public void update() throws IOException {

        if (levels.get(curentLevel).numberEnemies == 0  ) {

            levels.get(curentLevel).completed = true;


        }
        if(AssetSetter.levelMatrix[gp.player.mapPositionY][gp.player.mapPositionX]==curentLevel+2 &&levels.get(curentLevel).completed){
            levels.get(curentLevel).running = false;
            curentLevel++;
            for(int i=0;i<gp.monster.length;i++)
                if(gp.monster[i]!=null)
                     ((Enemy)gp.monster[i]).discardMonster();
            levels.get(curentLevel).running = true;
            gp.assetSetter.setMonster();
            gp.assetSetter.setCombatObjects();
        }


    }
    public void killEnemy(){
        levels.get(curentLevel).numberEnemies--;
    }
    public void setUp(){
        levels.clear();
        curentLevel=0;
        levels.add(new Level(numberOfLevel));
        numberOfLevel++;
        levels.add(new Level(numberOfLevel));
        numberOfLevel++;
        levels.add(new Level(numberOfLevel));
        numberOfLevel++;
        levels.add(new Level(numberOfLevel));
        numberOfLevel++;

    }
}
