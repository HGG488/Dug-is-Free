package main;
import Object.*;
import entity.Entity;

import java.io.IOException;

public class CombatObjectFactory {
    public enum type{GUN,SHIELD};
    GamePanel gp;
    public CombatObjectFactory(GamePanel gp)
    {
        this.gp=gp;
    }
    public CombatObject createCombatObject(type type, CombatObject.Owner owner, Entity entity,int index)  {
        switch (type){
            case GUN:
                Gun tmp= new Gun(gp,owner);
                tmp.ownerIndex=index;
                gp.combatObjects.add(tmp);

                return tmp;


            case SHIELD:
                Shield tmp1= new Shield(gp,owner);
                tmp1.ownerIndex=index;
                gp.combatObjects.add(tmp1);
                return tmp1;
            default: return null;


        }
    }

}
