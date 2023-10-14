package entity;

import main.AssetSetter;
import main.CombatObjectFactory;
import main.CombinationChecker;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import Object.*;
public class Entity {
     public GamePanel gp;

    public enum Direction {up, down, left, right}

    public int worldX, worldY;
    public int speed;
    public int maxLife;
    public int life;
    public BufferedImage up1, up2, up3, up4,up5;
    public BufferedImage down1, down2, down3, down4,down5;
    public BufferedImage left1, left2, left3, left4,left5;
    public BufferedImage right1, right2, right3, right4,right5;
    public Direction direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX;
    public int solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter=0;
    public int index;
    public boolean moving = false;
    public int remaining;
    public int pixelCounter = 0;

    //PLAYER=0 ENEMY=1 FOLOSIT CA SI ENUM

    public int type;
    final public int PLAYER=0;
    final public int ENEMY=1;


    public boolean invincible=false;
    public int invincibleCounter;
    protected boolean hpBarOn=false;
    public int hpBarcounter=0;
    public  boolean alive=true;
    public boolean dying=false;
    public int dyingCounter=0;

    public CombatObject gun;
    public CombatObject shield;
    public Projectile1 projectile;

    public  Entity(GamePanel gp) {
        this.gp = gp;
        this.direction=Direction.down;
        moving=true;

    }
    public void update(){}
    public void draw(Graphics2D g2 ){}

}
