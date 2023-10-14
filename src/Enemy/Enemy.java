package Enemy;

import entity.Entity;
import main.AssetSetter;
import main.CombinationChecker;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import Object.Projectile1;
import Object.*;
import Exceptions.*;

public class Enemy extends Entity {
    public int shotAvailableCounter = 0;

    public Enemy(GamePanel gp) {
        super(gp);

        speed = 3;
        maxLife = 4;
        life = maxLife;
        type = ENEMY;
        solidArea.x = 1;
        solidArea.y = 1;
        solidArea.width = 64;
        solidArea.height = 64;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        projectile = new Projectile1(gp, Projectile1.Owner.ENEMY);
        this.type = ENEMY;
    }

    public Enemy(EnemyBuilder builder) { //CONSTRUCTOR PENTRU BUILDER
        super(builder.gp);
        speed = 3;
        maxLife = 4;
        life = maxLife;
        type = ENEMY;
        solidArea.x = 1;
        solidArea.y = 1;
        solidArea.width = 64;
        solidArea.height = 64;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
        type = ENEMY;
        this.gun = builder.gun;
        this.shield = builder.shield;
        this.projectile = builder.projectile;
        this.worldX = builder.worldX;
        this.worldY = builder.worldY;

    }

    public void update() {
        if (!dying) {
            if (!moving) {

                setAction();

                collisionOn = false;

                //VERIFICARE COLIZIUNI

                gp.cChecker.checkTile(this);
                gp.cChecker.checkObject(this, false);
                boolean contactPlayer = gp.cChecker.checkPlayer(this);

                //VIATA PLAYER-ULUI SCADE DACA ESTE ATINS DE INAMIC

                if (this.type == 1 && contactPlayer && CombinationChecker.ENEMY_IS_DEATH)
                    if (!gp.player.invincible) {
                        gp.player.life -= 1;
                        gp.player.invincible = true;
                    }

                gp.cChecker.checkOtherEntities(this);
                if (!collisionOn) {
                    moving = true;
                    remaining = GamePanel.tileSize;
                }

            } else {
                int tempspeed = speed;
                if (pixelCounter + speed > GamePanel.tileSize) {
                    speed = GamePanel.tileSize - pixelCounter;
                    pixelCounter = GamePanel.tileSize;

                } else {
                    pixelCounter += speed;
                }

                if (!collisionOn) {
                    switch (direction) {
                        case up:
                            worldY -= speed;
                            break;
                        case down:
                            worldY += speed;
                            break;
                        case left:
                            worldX -= speed;
                            break;
                        case right:
                            worldX += speed;
                            break;
                    }
                    spriteCounter++;
                    if (spriteCounter > 10) {
                        spriteNum++;
                        if (spriteNum == 5)
                            spriteNum = 1;
                        spriteCounter = 0;
                    }
                    speed = tempspeed;
                    if (pixelCounter == GamePanel.tileSize) {
                        moving = false;
                        pixelCounter = 0;

                    }
                    remaining -= speed;
                    if (invincible) {
                        invincibleCounter++;
                        if (invincibleCounter > 40) {
                            invincible = false;
                            invincibleCounter = 0;
                        }
                    }

                }
            }
            //LANSARE PROIECTIL LA UN ANUMIT INTERVAL DE TIMP
            if (projectile != null && !projectile.alive && shotAvailableCounter == 60 && gun != null && CombinationChecker.ENEMY_HAS_GUN) {
                projectile.set(worldX, worldY, direction, true, this, gun.curentTipX, gun.curentTipY, gun.mouseCoord);

                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            } else
                shotAvailableCounter++;
        }


    }
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + GamePanel.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - GamePanel.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + GamePanel.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - GamePanel.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case up:
                    if (spriteNum == 1)
                        if(up1!=null)image = up1;
                    if (spriteNum == 2)
                        if(up2!=null)  image = up2;
                        else image=up1;
                    if (spriteNum == 3)
                        if(up3!=null) image = up3;
                        else image=up1;
                    if (spriteNum == 4)
                        if(up4!=null)  image = up4;
                        else image=up1;
                    break;
                case down:
                    if (spriteNum == 1)
                        if(down1!=null) image = down1;
                    if (spriteNum == 2)
                        if(down2!=null) image = down2;
                        else image=down1;
                    if (spriteNum == 3)
                        if(down3!=null)  image = down3;
                        else image=down1;
                    if (spriteNum == 4)
                        if(down4!=null) image = down4;
                        else image=down1;
                    break;
                case left:
                    if (spriteNum == 1)
                        if(left1!=null)image = left1;
                    if (spriteNum == 2)
                        if(left2!=null) image = left2;
                    if (spriteNum == 3)
                        if(left3!=null)  image = left3;
                    if (spriteNum == 4)
                        if(left4!=null) image = left4;
                    break;
                case right:
                    if (spriteNum == 1)
                        if(right1!=null)image = right1;
                    if (spriteNum == 2)
                        if(right2!=null)image = right2;
                    if (spriteNum == 3)
                        if(right3!=null) image = right3;
                    if (spriteNum == 4)
                        if(right4!=null) image = right4;
                    break;
            }
            if(type==ENEMY && hpBarOn){
                double oneScale=(double)GamePanel.tileSize/maxLife;
                double hpBarValue=oneScale*life;
                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX-1,screenY-16,GamePanel.tileSize,12);
                g2.setColor(new Color(255,0,30));
                g2.fillRect(screenX,screenY-15,(int)hpBarValue,10);}
            hpBarcounter++;
            if(hpBarcounter>200){
                hpBarcounter=0;
                hpBarOn=false;
            }
            if(invincible)
            { hpBarOn=true;
                hpBarcounter=0;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));

            }

            if(gp.levelManager.curentLevel+1== AssetSetter.levelMatrix[worldY/GamePanel.tileSize][worldX/GamePanel.tileSize])
                g2.drawImage(image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);
            else
                g2.drawImage(gp.tileM.tile[1].image,screenX,screenY, GamePanel.tileSize, GamePanel.tileSize,null);


            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        }
        if(dying) {
            if(type==ENEMY){
                dyingAnimation(g2);

            }

        }}

    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        if(dyingCounter<=5){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0f));
        }
        if(dyingCounter>5 && dyingCounter<=10)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        if(dyingCounter>10 && dyingCounter<=15)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0f));
        if(dyingCounter>15 && dyingCounter<=20)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        if(dyingCounter>20 && dyingCounter<=25)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0f));

        if(dyingCounter>25 && dyingCounter<=30)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        if(dyingCounter>30 && dyingCounter<=35)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0f));
        if(dyingCounter>35 && dyingCounter<=40)
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));

        if(dyingCounter>40)
        {dying = false;
            alive=false;}




    }

    //MONSTER ESTE ECHIVALENT CU ENEMY , DUREAZA PREA MULT SA SCHIMB INAPOI IN ENEMY
    public void discardMonster() {

        this.gp.monster[index] = null;
        gp.score += 5;
        gp.dbOperator.setScore(gp.score);
        gp.combatObjects.remove(this.gun);
        gp.combatObjects.remove(this.shield);

    }

    public void getImage()  {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/sus1.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/jos1.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/stanga1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/stanga2.png")));
            left3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/stanga3.png")));
            left4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/stanga4.png")));
            left5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/stanga5.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/dreapta1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/dreapta2.png")));
            right3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/dreapta3.png")));
            right4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/dreapta4.png")));
            right5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesEnemy/dreapta5.png")));
        } catch (IOException e) {

            System.out.println("eroare la incarcare imagine pentru Enemy "+ e+e.getMessage());
        }

    }

    public void setAction() {
        Random random = new Random();
        int i = random.nextInt(100) + 1;
        if (i <= 25) direction = Direction.up;
        if (i > 25 && i <= 50) direction = Direction.down;
        if (i > 50 && i <= 75) direction = Direction.left;
        if (i > 75) direction = Direction.right;
        actionLockCounter = 0;

    }
    //BUILDER PENTRU ENEMY
    public static class EnemyBuilder {

        public CombatObject gun, shield = null;
        public Projectile1 projectile = null;
        public GamePanel gp = null;
        public int worldX = 0;
        public int worldY = 0;

        public EnemyBuilder() {

        }

        public EnemyBuilder setPosition(int worldX, int worldY) {
            try {
                if (worldX < 0 || worldY < 0)
                    throw new NegativeCoordinatesException("coordonate negative la Enemy-Builder-SetPosition");
                else {
                    this.worldX = worldX;
                    this.worldY = worldY;
                }
            }
            catch (NegativeCoordinatesException e){
                System.out.println(e.getMessage());
            }
            return this;
        }

        public EnemyBuilder setGP(GamePanel gp) {
            this.gp = gp;
            return this;
        }

        public EnemyBuilder setGun(CombatObject gun) {
            try {
                if(gun==null)
                    throw new NullObjectException("Obj Gun null pentru Enemy-Builder-SetGun");
                else{
                this.gun = gun;
                this.setProjectile(new Projectile1(gp, Projectile1.Owner.ENEMY));
                return this;
                }
            }
            catch (NullObjectException e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        public EnemyBuilder setShield(CombatObject shield) {
            try {
                if(shield==null)
                    throw new NullObjectException("Obj Shield null pentru Enemy-Builder-SetGun");
                else{
                    this.shield = shield;
                    return this;
                }
            }
            catch (NullObjectException e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        public EnemyBuilder setProjectile(Projectile1 projectile) {
            try {
                if(projectile==null)
                    throw new NullObjectException("Obj Projectile null pentru Enemy-Builder-SetGun");
                else{
                    this.projectile =projectile;
                    return this;
                }
            }
            catch (NullObjectException e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        public Enemy build() {
            return new Enemy(this);
        }


    }
}
