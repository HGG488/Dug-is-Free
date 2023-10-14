package main;

import entity.Entity;
import entity.Player;

import Object.SuperObject;
import Object.*;
import Enemy.*;

public class CollisionChecker {
    GamePanel gp;
    CombinationChecker combinationChecker;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
        combinationChecker = new CombinationChecker(this.gp);
    }

    //functie pentru coliziuni cu mapa ( cu tile-uri)
    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.width;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        int tileNum1, tileNum2;
        switch (entity.direction) {
            case up:
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case down:
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case left:
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case right:
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
                tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

        }
    }

    //COLIZIUNI DINTRE ENTITATI SI OBIECTE
    public int checkObject(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gp.superObj.length; i++) {
            if (gp.superObj[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.superObj[i].solidArea.x = gp.superObj[i].worldX + gp.superObj[i].solidArea.x;
                gp.superObj[i].solidArea.y = gp.superObj[i].worldY + gp.superObj[i].solidArea.y;

                switch (entity.direction) {
                    case up:
                        entity.solidArea.y -= GamePanel.tileSize;
                        if (entity.solidArea.intersects(gp.superObj[i].solidArea)) {
                            if (gp.superObj[i].collision)
                                entity.collisionOn = true;
                            if (player)
                                index = i;

                        }
                        break;
                    case down:
                        entity.solidArea.y += GamePanel.tileSize;
                        if (entity.solidArea.intersects(gp.superObj[i].solidArea)) {
                            if (gp.superObj[i].collision)
                                entity.collisionOn = true;
                            if (player)
                                index = i;

                        }


                        break;
                    case left:
                        entity.solidArea.x -= GamePanel.tileSize;
                        if (entity.solidArea.intersects(gp.superObj[i].solidArea)) {
                            if (gp.superObj[i].collision)
                                entity.collisionOn = true;
                            if (player)
                                index = i;

                        }

                        break;
                    case right:
                        entity.solidArea.x += GamePanel.tileSize;
                        if (entity.solidArea.intersects(gp.superObj[i].solidArea)) {
                            if (gp.superObj[i].collision)
                                entity.collisionOn = true;
                            if (player)
                                index = i;

                        }


                        break;

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.superObj[i].solidArea.x = gp.superObj[i].solidAreaDefaultX;
                gp.superObj[i].solidArea.y = gp.superObj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    //COLIZIUNI DINTRE UN INAMIC SI ALTI INAMICI
    public int checkOtherEntities(Entity entity) {
        int index = 999;

        for (int i = 0; i < gp.monster.length; i++)
            if (entity.index != i) {
                if (gp.monster[i] != null) {
                    entity.solidArea.x = entity.worldX + entity.solidArea.x;
                    entity.solidArea.y = entity.worldY + entity.solidArea.y;
                    gp.monster[i].solidArea.x = gp.monster[i].worldX + gp.monster[i].solidArea.x;
                    gp.monster[i].solidArea.y = gp.monster[i].worldY + gp.monster[i].solidArea.y;
                    if (entity.moving) {
                        switch (entity.direction) {
                            case up:
                                entity.solidArea.y -= entity.remaining;
                                break;
                            case down:
                                entity.solidArea.y += entity.remaining;
                                break;
                            case left:
                                entity.solidArea.x -= entity.remaining;
                                break;
                            case right:
                                entity.solidArea.x += entity.remaining;
                                break;
                        }
                    }
                    if (gp.monster[i].moving) {
                        switch (gp.monster[i].direction) {
                            case up:
                                gp.monster[i].solidArea.y -= gp.monster[i].remaining;
                                break;
                            case down:
                                gp.monster[i].solidArea.y += gp.monster[i].remaining;
                                break;
                            case left:
                                gp.monster[i].solidArea.x -= gp.monster[i].remaining;
                                break;
                            case right:
                                gp.monster[i].solidArea.x += gp.monster[i].remaining;
                                break;
                        }
                    }

                    switch (entity.direction) {
                        case up:
                            entity.solidArea.y -= GamePanel.tileSize;
                            if (entity.solidArea.intersects(gp.monster[i].solidArea)) {
                                entity.collisionOn = true;


                            }

                            break;
                        case down:

                            entity.solidArea.y += GamePanel.tileSize;
                            if (entity.solidArea.intersects(gp.monster[i].solidArea)) {
                                entity.collisionOn = true;


                            }


                            break;
                        case left:
                            entity.solidArea.x -= GamePanel.tileSize;
                            if (entity.solidArea.intersects(gp.monster[i].solidArea)) {

                                entity.collisionOn = true;


                            }

                            break;
                        case right:
                            entity.solidArea.x += GamePanel.tileSize;
                            if (entity.solidArea.intersects(gp.monster[i].solidArea)) {

                                entity.collisionOn = true;


                            }


                            break;

                    }
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    gp.monster[i].solidArea.x = gp.monster[i].solidAreaDefaultX;
                    gp.monster[i].solidArea.y = gp.monster[i].solidAreaDefaultY;
                }
            }

        return index;
    }

    //functie pentru coliziuni cu obiecte(pentru a putea muta mai multe obiecte in acelasi timp , se pun toate blocurile care sunt in linie
    // intr-o lista si dupa se parcurge lista pentru a le muta pe toate in acelasi timp
    public void checkBlock(Player player) {
        boolean moveOn = true;
        boolean moveOn1 = true;
        int x = player.mapPositionX;

        int y = player.mapPositionY;

        switch (player.direction) {

            case up:
                if (!Character.isDigit(AssetSetter.blockMatrix[y - 1][x])) {

                    int curentX = x;
                    int curentY = y - 1;
                    SuperObject[] lista = new SuperObject[20];
                    int index = 0;
                    while (!Character.isDigit(AssetSetter.blockMatrix[curentY][curentX])) {

                        lista[index] = gp.returnSuperObjectByPosition(curentX, curentY);
                        if (!((Block) lista[index]).canMove) {
                            moveOn1 = false;
                            break;
                        }
                        index++;
                        curentY--;
                    }
                    if (moveOn1) {
                        Entity tmp = new Enemy(gp);
                        for (int i = lista.length - 1; i >= 0; i--)
                            if (lista[i] != null) {
                                tmp.worldX = lista[i].worldX;
                                tmp.worldY = lista[i].worldY;
                                tmp.solidArea.x = lista[i].solidArea.x;
                                tmp.solidArea.y = lista[i].solidArea.y;
                                tmp.direction = player.direction;
                                checkEntity(tmp, gp.monster);

                                if (tmp.collisionOn) {
                                    moveOn = false;
                                    player.collisionOn = true;
                                }
                                break;

                            }
                        if (moveOn) {
                            if (AssetSetter.blockMatrix[curentY][curentX] == '0') {

                                for (int i = index + 1; i >= 1; i--) {

                                    AssetSetter.blockMatrix[y - i][x] = AssetSetter.blockMatrix[y - i + 1][x];
                                }
                                for (int i = 0; i < index; i++) {

                                    lista[i].move1(Entity.Direction.up);
                                }
                            } else
                                player.collisionOn = true;
                        }
                    }
                }
                break;
            case down:
                if (!Character.isDigit(AssetSetter.blockMatrix[y + 1][x])) {

                    int curentX = x;
                    int curentY = y + 1;
                    SuperObject[] lista = new SuperObject[20];
                    int index = 0;
                    while (!Character.isDigit(AssetSetter.blockMatrix[curentY][curentX])) {

                        lista[index] = gp.returnSuperObjectByPosition(curentX, curentY);
                        if (!(((Block) lista[index]).canMove)) {
                            moveOn1 = false;
                            break;
                        }
                        index++;
                        curentY++;
                    }
                    if (moveOn1) {


                        Entity tmp = new Enemy(gp);
                        for (int i = lista.length - 1; i >= 0; i--)
                            if (lista[i] != null) {
                                tmp.worldX = lista[i].worldX;
                                tmp.worldY = lista[i].worldY;
                                tmp.solidArea.x = lista[i].solidArea.x;
                                tmp.solidArea.y = lista[i].solidArea.y;
                                tmp.direction = player.direction;
                                checkEntity(tmp, gp.monster);

                                if (tmp.collisionOn) {
                                    moveOn = false;
                                    player.collisionOn = true;
                                }
                                break;

                            }
                        if (moveOn) {
                            if (AssetSetter.blockMatrix[curentY][curentX] == '0') {

                                char tempchar = '0';
                                for (int i = index + 1; i >= 1; i--) {

                                    AssetSetter.blockMatrix[y + i][x] = AssetSetter.blockMatrix[y + i - 1][x];
                                }
                                for (int i = 0; i < index; i++) {

                                    lista[i].move1(Entity.Direction.down);
                                }
                            } else
                                player.collisionOn = true;
                        }
                    }
                }
                break;
            case left:
                if (!Character.isDigit(AssetSetter.blockMatrix[y][x - 1])) {

                    int curentX = x - 1;
                    int curentY = y;
                    SuperObject[] lista = new SuperObject[20];
                    int index = 0;
                    while (!Character.isDigit(AssetSetter.blockMatrix[curentY][curentX])) {

                        lista[index] = gp.returnSuperObjectByPosition(curentX, curentY);
                        if (!((Block) lista[index]).canMove) {
                            moveOn1 = false;
                            break;
                        }
                        index++;
                        curentX--;
                    }
                    if (moveOn1) {
                        Entity tmp = new Enemy(gp);
                        for (int i = lista.length - 1; i >= 0; i--)
                            if (lista[i] != null) {
                                tmp.worldX = lista[i].worldX;
                                tmp.worldY = lista[i].worldY;
                                tmp.solidArea.x = lista[i].solidArea.x;
                                tmp.solidArea.y = lista[i].solidArea.y;
                                tmp.direction = player.direction;
                                checkEntity(tmp, gp.monster);

                                if (tmp.collisionOn) {
                                    moveOn = false;
                                    player.collisionOn = true;
                                }
                                break;

                            }
                        if (moveOn) {
                            if (AssetSetter.blockMatrix[curentY][curentX] == '0') {

                                char tempchar = '0';
                                for (int i = index + 1; i >= 1; i--) {

                                    AssetSetter.blockMatrix[y][x - i] = AssetSetter.blockMatrix[y][x - i + 1];
                                }
                                for (int i = 0; i < index; i++) {

                                    lista[i].move1(Entity.Direction.left);
                                }
                            } else
                                player.collisionOn = true;
                        }
                    }
                }
                break;
            case right:
                if (!Character.isDigit(AssetSetter.blockMatrix[y][x + 1])) {
                    int curentX = x + 1;
                    int curentY = y;
                    SuperObject[] lista = new SuperObject[20];
                    int index = 0;
                    while (!Character.isDigit(AssetSetter.blockMatrix[curentY][curentX])) {

                        lista[index] = gp.returnSuperObjectByPosition(curentX, curentY);
                        if (!((Block) lista[index]).canMove) {
                            moveOn1 = false;
                            break;
                        }
                        index++;
                        curentX++;
                    }
                    if (moveOn1) {
                        Entity tmp = new Enemy(gp);
                        for (int i = lista.length - 1; i >= 0; i--)
                            if (lista[i] != null) {
                                tmp.worldX = lista[i].worldX;
                                tmp.worldY = lista[i].worldY;
                                tmp.solidArea.x = lista[i].solidArea.x;
                                tmp.solidArea.y = lista[i].solidArea.y;
                                tmp.direction = player.direction;
                                checkEntity(tmp, gp.monster);

                                if (tmp.collisionOn) {
                                    moveOn = false;
                                    player.collisionOn = true;
                                }
                                break;

                            }
                        if (moveOn) {
                            if (AssetSetter.blockMatrix[curentY][curentX] == '0') {

                                char tempchar = '0';
                                for (int i = index + 1; i >= 1; i--) {
                                    AssetSetter.blockMatrix[y][x + i] = AssetSetter.blockMatrix[y][x + i - 1];
                                }
                                for (int i = 0; i < index; i++) {
                                    lista[i].move1(Entity.Direction.right);
                                }


                            } else {
                                player.collisionOn = true;
                            }


                        }
                    }
                }
                break;

        }
        if (!moveOn1)
            player.collisionOn = true;
        combinationChecker.checkCombinations();//verifica combinatiile create dupa mutarea blocurilor


    }
    //Colziuni dintre Entity si un vector de Entity folosita la player
    public int checkEntity(Entity entity, Entity[] target) {

        int index = 999;
        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
                if (entity.moving) {
                    switch (entity.direction) {
                        case up:
                            entity.solidArea.y -= entity.remaining;
                            break;
                        case down:
                            entity.solidArea.y += entity.remaining;
                            break;
                        case left:
                            entity.solidArea.x -= entity.remaining;
                            break;
                        case right:
                            entity.solidArea.x += entity.remaining;
                            break;
                    }
                }
                if (target[i].moving) {
                    switch (target[i].direction) {
                        case up:
                            target[i].solidArea.y -= target[i].remaining;
                            break;
                        case down:
                            target[i].solidArea.y += target[i].remaining;
                            break;
                        case left:
                            target[i].solidArea.x -= target[i].remaining;
                            break;
                        case right:
                            target[i].solidArea.x += target[i].remaining;
                            break;
                    }
                }


                switch (entity.direction) {
                    case up:
                        entity.solidArea.y -= GamePanel.tileSize;
                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;

                        }
                        break;
                    case down:
                        entity.solidArea.y += GamePanel.tileSize;
                        if (entity.solidArea.intersects(target[i].solidArea)) {

                            entity.collisionOn = true;
                            index = i;

                        }
                        break;
                    case left:
                        entity.solidArea.x -= GamePanel.tileSize;
                        if (entity.solidArea.intersects(target[i].solidArea)) {

                            entity.collisionOn = true;
                            index = i;

                        }
                        break;
                    case right:
                        entity.solidArea.x += GamePanel.tileSize;
                        if (entity.solidArea.intersects(target[i].solidArea)) {

                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }
    //Coliziune dintre Enemy, Player
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        if (entity.moving) {
            switch (entity.direction) {
                case up:
                    entity.solidArea.y -= entity.remaining;
                    break;
                case down:
                    entity.solidArea.y += entity.remaining;
                    break;
                case left:
                    entity.solidArea.x -= entity.remaining;
                    break;
                case right:
                    entity.solidArea.x += entity.remaining;
                    break;
            }
        }
        if (gp.player.moving) {
            switch (gp.player.direction) {
                case up:
                    gp.player.solidArea.y -= gp.player.remaining;
                    break;
                case down:
                    gp.player.solidArea.y += gp.player.remaining;
                    break;
                case left:
                    gp.player.solidArea.x -= gp.player.remaining;
                    break;
                case right:
                    gp.player.solidArea.x += gp.player.remaining;
                    break;
            }
        }
        switch (entity.direction) {
            case up:
                entity.solidArea.y -= GamePanel.tileSize;

                break;
            case down:
                entity.solidArea.y += GamePanel.tileSize;

                break;
            case left:
                entity.solidArea.x -= GamePanel.tileSize;

                break;
            case right:
                entity.solidArea.x += GamePanel.tileSize;

                break;

        }
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        return contactPlayer;
    }
    //COLIZIUNE DINTRE PROIECTIL SI ENTITATI
    public int checkEntityProjectile(Entity entity, Entity[] target) {

        int index = 999;
        for (int i = 0; i < target.length; i++) {


            if (target[i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;


                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                if (entity.projectile.g2 != null) {
                    entity.projectile.g2.fillRect(entity.solidArea.x - gp.player.worldX + gp.player.screenX, entity.solidArea.y - gp.player.worldY + gp.player.screenY, 50, 50);
                }
                if (entity.solidArea.intersects(target[i].solidArea)) {
                    entity.collisionOn = true;
                    index = i;


                }


                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }

        return index;
    }
    //COLIZIUNI PROIECTIL SI OBIECTE
    public int checkObjectProjectile(Entity entity, boolean player) {
        int index = 999;
        for (int i = 0; i < gp.superObj.length; i++) {
            if (gp.superObj[i] != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gp.superObj[i].solidArea.x = gp.superObj[i].worldX + gp.superObj[i].solidArea.x;
                gp.superObj[i].solidArea.y = gp.superObj[i].worldY + gp.superObj[i].solidArea.y;
                entity.solidArea.x += entity.speed * Math.cos(entity.projectile.angle);
                entity.solidArea.y += entity.speed * Math.sin(entity.projectile.angle);

                if (entity.solidArea.intersects(gp.superObj[i].solidArea)) {
                    if (gp.superObj[i].collision)
                        entity.collisionOn = true;
                    if (player)
                        index = i;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.superObj[i].solidArea.x = gp.superObj[i].solidAreaDefaultX;
                gp.superObj[i].solidArea.y = gp.superObj[i].solidAreaDefaultY;

            }
        }

        return index;
    }
    //COLIZIUNI PROIECTIL SI MAPA
    public void checkTileProjectile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.width;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;
        int tileNum1, tileNum2;

        entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }

        entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
        tileNum1 = gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }

        entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityLeftCol];
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }

        entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
        tileNum2 = gp.tileM.mapTileNum[entityBottomRow][entityRightCol];
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionOn = true;
        }


    }


}

