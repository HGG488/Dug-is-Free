package main;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import entity.Entity;
import java.util.ArrayList;

public class Level {
    public int levelNumber;
    public boolean completed;
    public boolean running;
    public int numberEnemies;

    public Level(int levelNumber){
        this.levelNumber=levelNumber;
        this.numberEnemies=4;
        completed=false;
        running= false;
    }



}
