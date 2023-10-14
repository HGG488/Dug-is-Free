package entity;

import main.GamePanel;
import main.KeyHandler;
import main.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import Object.*;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public int screenX;
    public int screenY;
    public boolean moving = false;
    public int remaining;
    int pixelCounter = 0;
    public int mapPositionX;
    public int mapPositionY;

    public int shotAvailableCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;
        this.setDefaultValues();
        this.solidArea = new Rectangle();
        this.type = PLAYER;
        solidArea.x = 1;
        solidArea.y = 1;
        solidArea.height = 64;
        solidArea.width = 64;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }




    public void changeDirection() {
        if (keyH.upPressed) {
            direction = Direction.up;
        }
        if (keyH.downPressed) {
            direction = Direction.down;
        }
        if (keyH.leftPressed) {
            direction = Direction.left;
        }
        if (keyH.rightPressed) {
            direction = Direction.right;
        }

    }

    public void move() {
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
    }

    public void updateSprite() {
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteNum++;
            if (spriteNum == 5)
                spriteNum = 1;
            spriteCounter = 0;
        }
    }


    // se foloseste un algoritm pentru a muta playerul un Tile intreg ( Grid based movement )
    public void update() {

        if (!moving) {
            if ((keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)) {
                this.changeDirection();

                collisionOn = false;
                //    this.changeDirection();

                gp.cChecker.checkTile(this);
                // int objIndex = gp.cChecker.checkObject(this, true);
                gp.cChecker.checkBlock(this);
                int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
                if (CombinationChecker.ENEMY_IS_DEATH && monsterIndex != 999)
                    contactMonster(monsterIndex);

                if (!collisionOn) {
                    moving = true;
                    remaining = GamePanel.tileSize;
                }
                //if(!collisionOn)
                //  this.move();
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
                this.move();
                this.updateSprite();
                speed = tempspeed;
                if (pixelCounter == GamePanel.tileSize) {
                    moving = false;
                    pixelCounter = 0;
                    this.changeMapPosition();

                }
                remaining -= speed;
            }
        }
        //SE SETEAZA COORDONATELE PROIECTILULUI DACA PLAYERUL ARE ARMA SI SE APASA BUTONUL MOUSE-ULUI
        if (gp.mouseH.bulletPressed && !projectile.alive && shotAvailableCounter == 30 && CombinationChecker.DUG_HAS_GUN) {
            projectile.set(worldX, worldY, direction, true, this, this.gun.curentTipX, this.gun.curentTipY, this.gun.mouseCoord);


            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }
        //DUPA CE PRIMESTE UN HIT PLAYER-UL DEVINE INVINCIBIL PENTRU CATEVA SECUNDE
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if (life <= 0)
            gp.gameState = gp.gameOverState;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (direction) {
            case up:
                if (spriteNum == 1)
                    image = up1;
                if (spriteNum == 2)
                    image = up2;
                if (spriteNum == 3)
                    image = up3;
                if (spriteNum == 4)
                    image = up4;
                break;
            case down:
                if (spriteNum == 1)
                    image = down1;
                if (spriteNum == 2)
                    image = down2;
                if (spriteNum == 3)
                    image = down3;
                if (spriteNum == 4)
                    image = down4;
                break;
            case left:
                if (spriteNum == 1)
                    image = left1;
                if (spriteNum == 2)
                    image = left2;
                if (spriteNum == 3)
                    image = left3;
                if (spriteNum == 4)
                    image = left4;
                break;
            case right:
                if (spriteNum == 1)
                    image = right1;
                if (spriteNum == 2)
                    image = right2;
                if (spriteNum == 3)
                    image = right3;
                if (spriteNum == 4)
                    image = right4;
                break;
        }
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        }
        if (g2.getComposite().equals(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g2.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f));
        } else
            g2.drawImage(image, screenX, screenY, GamePanel.tileSize, GamePanel.tileSize, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/sus1.png")));
            up2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/sus2.png")));
            up3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/sus3.png")));
            up4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/sus4.png")));
            down1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/jos1.png")));
            down2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/jos2.png")));
            down3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/jos3.png")));
            down4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/jos4.png")));
            left1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/stanga1.png")));
            left2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/stanga2.png")));
            left3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/stanga3.png")));
            left4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/stanga4.png")));
            right1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/dreapta1.png")));
            right2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/dreapta2.png")));
            right3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/dreapta3.png")));
            right4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesPlayer/dreapta4.png")));
        } catch (IOException e) {
            System.out.println("eroare la citire imagine pentru Player");

        }
    }

    // pozitia pe mapa de blocuri
    private void changeMapPosition() {

        switch (direction) {
            case up:
                this.mapPositionY -= 1;
                break;
            case down:
                this.mapPositionY += 1;
                break;
            case left:
                this.mapPositionX -= 1;
                break;
            case right:
                this.mapPositionX += 1;
                break;
        }

    }
    //Daca atinge un inamic
    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible) {
                life -= 1;
                invincible = true;

            }

        }
    }
    //daca ataca un inamic
    public void damageMonster(int i) {
        if (!gp.monster[i].invincible) {
            gp.monster[i].life -= 1;
            gp.monster[i].invincible = true;
            if (gp.monster[i].life <= 0) {
                gp.monster[i].dying = true;
            }

        }
    }

    public void setDefaultValues() {
        switch (gp.levelManager.curentLevel) {
            case 0:
                mapPositionX = 31;
                mapPositionY = 28;
                break;
            case 1:

                mapPositionX = 9;
                mapPositionY = 7;
                break;
            case 2:

                mapPositionX = 41;
                mapPositionY = 19;
                break;

        }

        screenX = gp.screenWidth / 2 - GamePanel.tileSize / 2;
        screenY = gp.screenHeight / 2 - GamePanel.tileSize / 2;
        worldX = mapPositionX * GamePanel.tileSize;
        worldY = mapPositionY * GamePanel.tileSize;
        speed = 9;
        direction = Direction.down;
        this.getPlayerImage();
        maxLife = 3;
        life = maxLife;
        projectile = new Projectile1(gp, Projectile1.Owner.PLAYER);
        invincible = false;
    }
}



