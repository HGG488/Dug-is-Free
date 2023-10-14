package main;

import Enemy.Enemy;
import entity.Entity;
import Object.*;

import java.io.IOException;
import java.sql.*;
import Enemy.*;
public class DBoperator {
    private Connection con;
    private Statement statement;
    GamePanel gp;
    public DBoperator(GamePanel gp){
        this.gp=gp;
    }
    public boolean isEmpty(){
        try{
            ResultSet rs = statement.executeQuery("select count(*) from GAME_INFO");
            int rez = rs.getInt(1);
            if(rez > 0)
                return false;
            if(rez == 0)
                return true;
            con.commit();
        }catch(Exception e)
        {
            System.out.println("Problema la functia isEmpty() !");
        }
        return false;
    }

    public void create_table()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        String url = "jdbc:sqlite:database.db";


        try (Connection conn = DriverManager.getConnection(url))
        {
            String sql = "CREATE TABLE IF NOT EXISTS SCORE_DB (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + " SCORE integer NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            // create a new table
            stmt.execute(sql);
            sql = "INSERT OR IGNORE INTO SCORE_DB(ID,SCORE) " +
                    "VALUES (1, 0 );";
            stmt.execute(sql);
            stmt.close();
            conn.close();



        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    public int getScore(){
        String url = "jdbc:sqlite:database.db";

        try (Connection conn = DriverManager.getConnection(url))
        { Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT SCORE FROM SCORE_DB WHERE ID=1;" );
            int score = rs.getInt("score");



            rs.close();
            stmt.close();
            conn.close();
            return  score;


        } catch (SQLException e) {
            System.out.println(e);
        }
        return -1;
    }
    public void setScore(int score) {
        String url = "jdbc:sqlite:database.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);

            String sql = "UPDATE SCORE_DB set SCORE =" + score + " where ID=1;";
            stmt.executeUpdate(sql);
            conn.commit();
            stmt.close();
            conn.close();


        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    public void saveGame(){
        String url = "jdbc:sqlite:database.db";


        try (Connection conn = DriverManager.getConnection(url))
        {
            String sql = "CREATE TABLE IF NOT EXISTS BLOCK_MATRIX (\n"
                    + " ID integer PRIMARY KEY,\n"
                    + "	ROW integer NOT NULL,\n"
                    + " COL integer NOT NULL,\n"
                    + " VALUE TEXT NOT NULL\n"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            int index=1;
            for(int i=0;i<gp.maxWorldRow;i++)
                for(int j=0;j<gp.maxWorldCol;j++)
                {   if(Character.isLowerCase(AssetSetter.blockMatrix[i][j]))
                       System.out.println(AssetSetter.blockMatrix[i][j]);
                    sql = "INSERT OR REPLACE INTO BLOCK_MATRIX(ID,ROW,COL,VALUE) " +
                    "VALUES ("+index+","+i+","+j+ ",'"+ AssetSetter.blockMatrix[i][j] +"');";

                    stmt.execute(sql);
                    index++;
                }


             sql = "CREATE TABLE IF NOT EXISTS ENEMY (\n"
                    + " ID integer PRIMARY KEY,\n"
                    + "	WORLDX integer NOT NULL,\n"
                    + " WORLDY integer NOT NULL,\n"
                     + " INDEX1 integer NOT NULL\n"
                    + ");";
            stmt.execute(sql);
            sql="DELETE FROM ENEMY;";
            stmt.execute(sql);

            index=0;
            for(int i=0;i<gp.monster.length;i++)
            {
                if(gp.monster[i]!=null){
                    System.out.println(i);
                    int mX=gp.monster[i].worldX;
                    int my=gp.monster[i].worldY;
                    switch(gp.monster[i].direction){
                        case up:my-=gp.monster[i].remaining;break;
                        case down:my+=gp.monster[i].remaining;break;
                        case left:mX-=gp.monster[i].remaining;break;
                        case right:mX+=gp.monster[i].remaining;break;
                    }
                    sql = "INSERT OR REPLACE INTO ENEMY (ID,WORLDX,WORLDY,INDEX1) " +
                            "VALUES ("+index+","+mX+","+my +","+gp.monster[i].index+");";
                    index++;
                    stmt.execute(sql);
                }
            }
            sql = "CREATE TABLE IF NOT EXISTS PLAYER (\n"
                    + " ID integer PRIMARY KEY,\n"
                    + "	WORLDX integer NOT NULL,\n"
                    + " WORLDY integer NOT NULL,\n"
                    + " LIFE integer NOT NULL,\n"
                    + "LEVEL integer NOT NULL ,\n"
                    +"MAPX integer NOT NULL,\n"
                    +"MAPY integer NOT NULL\n"
                    + ");";
            stmt.execute(sql);
            sql="INSERT OR REPLACE INTO PLAYER(ID,WORLDX,WORLDY,LIFE,LEVEL,MAPX,MAPY) "+
                    "VALUES (1,"+ gp.player.worldX+","+gp.player.worldY+" ,"+gp.player.life+","+gp.levelManager.curentLevel+","+gp.player.mapPositionX +","+gp.player.mapPositionY + ");";

        stmt.execute(sql);



            stmt.close();
            conn.close();

    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




        public void loadGame()  {

            gp.assetSetter.reset();
            String url = "jdbc:sqlite:database.db";

            try (Connection conn = DriverManager.getConnection(url))
            { Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT WORLDX FROM PLAYER WHERE ID=1;" );
                gp.player.worldX = rs.getInt("WORLDX");
                 rs = stmt.executeQuery( "SELECT WORLDY FROM PLAYER WHERE ID=1;" );
                 gp.player.worldY=rs.getInt("WORLDY");
                 rs = stmt.executeQuery( "SELECT LEVEL FROM PLAYER WHERE ID=1;" );
                 gp.levelManager.curentLevel=rs.getInt("LEVEL");
                 rs = stmt.executeQuery( "SELECT LIFE FROM PLAYER WHERE ID=1;" );
                 gp.player.life=rs.getInt("LIFE");
                rs = stmt.executeQuery( "SELECT MAPX FROM PLAYER WHERE ID=1;" );
                gp.player.mapPositionX=rs.getInt("MAPX");
                rs = stmt.executeQuery( "SELECT MAPY FROM PLAYER WHERE ID=1;" );
                gp.player.mapPositionY=rs.getInt("MAPY");

                for(int i=0;i<gp.maxWorldRow;i++)
                    for(int j=0;j<gp.maxWorldCol;j++)
                    {
                        rs=stmt.executeQuery("SELECT VALUE FROM BLOCK_MATRIX WHERE ROW="+i+" AND COL="+j+";");
                        AssetSetter.blockMatrix[i][j]= (rs.getString("VALUE")).charAt(0);
                    }
                gp.assetSetter.setObject();
                rs=stmt.executeQuery("SELECT COUNT(*) as nrenemy FROM ENEMY ;");
                int count=rs.getInt("nrenemy");
                System.out.println("count "+count);
                for(int i=0;i<count;i++)
                {   System.out.println(i);
                    rs=stmt.executeQuery("SELECT INDEX1 FROM ENEMY WHERE ID="+i+";");
                    int index=rs.getInt("index1");
                    gp.monster[index]=new Enemy(gp);
                    rs=stmt.executeQuery("SELECT WORLDX FROM ENEMY WHERE ID="+i+";");


                    gp.monster[index].worldX= rs.getInt("worldx");
                    rs=stmt.executeQuery("SELECT WORLDY FROM ENEMY WHERE ID="+i+";");
                    gp.monster[index].worldY= rs.getInt("worldy");

                    gp.monster[index].index=i;

                    gp.monster[index].gun=new Gun(gp, CombatObject.Owner.ENEMY);
                    gp.monster[index].shield=new Shield(gp, CombatObject.Owner.ENEMY);

                    gp.monster[index].gun.worldX=gp.monster[index].worldX+GamePanel.tileSize;
                    gp.monster[index].gun.worldY=gp.monster[index].worldY;

                    gp.monster[index].shield.worldX=gp.monster[index].worldX-15;
                    gp.monster[index].shield.worldY=gp.monster[index].worldY-15;

                    gp.monster[index].gun.ownerIndex=index;
                    gp.combatObjects.add(gp.monster[index].gun);

                    gp.monster[index].shield.ownerIndex=index;
                    gp.combatObjects.add(gp.monster[index].shield);



                }
                gp.levelManager.levels.get(gp.levelManager.curentLevel).numberEnemies=count;
                switch(gp.levelManager.curentLevel){
                    case 0: break;
                    case 1:
                        for(int i=0;i<gp.monster.length;i++)
                        {
                          if(gp.monster[i]!=null)  gp.monster[i].speed+=2;
                        }
                        break;
                    case 2:
                        gp.enemyEnemy=new EnemyEnemy(gp);
                        gp.enemyEnemy.worldX=GamePanel.tileSize*51;
                        gp.enemyEnemy.worldY=GamePanel.tileSize*16;

                }

                rs.close();
                stmt.close();
                conn.close();


            } catch (SQLException e) {
                System.out.println(e);
            }


            gp.player.screenX = gp.screenWidth / 2 - GamePanel.tileSize / 2;
            gp.player.screenY = gp.screenHeight / 2 - GamePanel.tileSize / 2;
            gp.player.speed = 9;
            gp.player.direction = Entity.Direction.down;
            gp.player.getPlayerImage();
            gp.player.maxLife=3;
            gp.player.projectile=new Projectile1(gp, Projectile1.Owner.PLAYER);
            gp.player.invincible=false;
            gp.assetSetter.setCombatObjects();








        }








}
