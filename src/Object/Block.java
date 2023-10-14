package Object;

import Exceptions.BlockAttributeException;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
public class Block extends SuperObject {
 public enum movementType{G,P,Y}; //green pink yellow
 public enum attributeType{DUG,HAS,IS,ENEMY,SHIELD,GUN,NOTHING,DEATH};
 public movementType typeM;
 public attributeType typeA;
 public boolean canMove=false;
 public boolean canCombine=false;
 public char id;
 public Block(char id,movementType typeM) {

     this.id=id;
     attributeType typeA;
     collision=true;
     id=Character.toLowerCase(id);
     try {
         switch (id) {
             case 'a':
                 typeA = attributeType.DUG;

                 break;
             case 'b':
                 typeA = attributeType.HAS;

                 break;
             case 'c':
                 typeA = attributeType.NOTHING;

                 break;
             case 'd':
                 typeA = attributeType.ENEMY;

                 break;
             case 'e':
                 typeA = attributeType.IS;

                 break;
             case 'f':
                 typeA = attributeType.DEATH;

                 break;
             case 'g':
                 typeA = attributeType.GUN;

                 break;
             case 'i':
                 typeA = attributeType.SHIELD;
                 break;
             default:

                 throw new BlockAttributeException("eroare la stabilirea atributului unui Block");


         }

     }
     catch (BlockAttributeException e)
     {
         System.out.println(e.getMessage()+", atributul a fost stabilit ca DUG");
         typeA=attributeType.DUG;
     }
     canMove= typeM == movementType.G;
     canCombine= (typeM==movementType.G||typeM==movementType.P);
     name=typeA.name()+"_"+typeM.name();

    try {
        image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesBlock/" + typeA.name() + "_" + typeM.name() + ".png")));
    }
    catch (IOException e){
        System.out.println("Eroare la citire imagine pentru BLOCK : "+name);
    }



 }



}
