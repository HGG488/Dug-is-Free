package main;
public class CombinationChecker {

    public static boolean DUG_HAS_GUN=false;
    public static boolean DUG_HAS_ENEMY=false;
    public static boolean ENEMY_IS_DEATH=false;
    public static boolean ENEMY_HAS_GUN=false;
    public static boolean DUG_HAS_NOTHING=false;
    public static boolean ENEMY_HAS_NOTHING=false;
    public static boolean DUG_IS_NOTHING=false;
    public static boolean DUG_IS_DEATH=false;
    public static boolean DUG_HAS_DEATH=false;
    public static boolean ENEMY_HAS_DEATH=false;
    public static boolean ENEMY_HAS_SHIELD=false;
    public static boolean DUG_HAS_SHIELD=false;
    public static boolean SHIELD_IS_NOTHING=false;
    public static boolean GUN_IS_NOTHING=false;
    public static boolean ENEMY_HAS_ENEMY=false;
    public static boolean ENEMY_IS_NOTHING=false;

    public static String [] combinationList={"abg","abd","def","dbg","abc","dbc","aec","aef","abf","dbf","dbi","*&#","abi","iec","gec","dbd","dec"};
    GamePanel gp;
    public CombinationChecker(GamePanel gp){
        this.gp=gp;
    }
    // verifica daca combinatia este valida ( ex : DUG HAS GUN este valida)
    public boolean isValidCombination(char a, char b , char c){
        String comb= ""+a +b+ c;

        for(int i=0;i<combinationList.length;i++)
            if(comb.equals(combinationList[i]))
                return true;
        return false;
    }

    //in functie de atributele combinatiilor se seteaza aceastea ca find adevarate daca este cazul
    public void manageCombination(char a,char b,char c){
        String comb= ""+a+b+c;
        System.out.println(comb);
        switch (comb){
            case "abg" : DUG_HAS_GUN=true;break;
            case "abd": DUG_HAS_ENEMY=true;break;
            case "def":ENEMY_IS_DEATH=true;break;
            case "dbg":ENEMY_HAS_GUN=true;break;
            case "abc":DUG_HAS_NOTHING=true;break;
            case "dbc":ENEMY_HAS_NOTHING=true;break;
            case "aec":DUG_IS_NOTHING=true;break;
            case "aef":DUG_IS_DEATH=true;break;
            case "abf":DUG_HAS_DEATH=true;break;
            case "dbf":ENEMY_HAS_DEATH=true;break;
            case "abi":DUG_HAS_SHIELD=true;break;
            case"dbi":ENEMY_HAS_SHIELD=true;break;
            case "iec":SHIELD_IS_NOTHING=true;break;
            case "gec":GUN_IS_NOTHING=true;break;
            case "dbd":ENEMY_HAS_ENEMY=true;break;
            case "dec":ENEMY_IS_NOTHING=true;break;

            case "*&#":
                       ENEMY_IS_DEATH=true;break;

        }
    }
    public void setCombinations(){
        DUG_HAS_GUN=false;
        DUG_HAS_ENEMY=false;
        ENEMY_IS_DEATH=false;
        ENEMY_HAS_GUN=false;
        DUG_HAS_NOTHING=false;
        ENEMY_HAS_NOTHING=false;
        DUG_IS_NOTHING=false;
        DUG_IS_DEATH=false;
        DUG_HAS_DEATH=false;
        ENEMY_HAS_DEATH=false;
        DUG_HAS_SHIELD=false;
       ENEMY_HAS_SHIELD=false;
       SHIELD_IS_NOTHING=false;
       GUN_IS_NOTHING=false;
       ENEMY_HAS_ENEMY=false;
       ENEMY_IS_NOTHING=false;

    }

    public void printCombinations(){
        if(ENEMY_HAS_DEATH)
            System.out.println("ENEMY_HAS_DEATH");
        if(DUG_HAS_GUN)
            System.out.println("DUG_HAS_GUN");
        if(DUG_HAS_ENEMY)
            System.out.println("DUG_HAS_ENEMY");
        if(ENEMY_IS_DEATH)
            System.out.println("ENEMY_IS_DEATH");
        if(ENEMY_HAS_GUN)
            System.out.println("ENEMY_HAS_GUN");
        if(DUG_HAS_NOTHING)
            System.out.println("DUG_HAS_NOTHING");
        if(ENEMY_HAS_NOTHING)
            System.out.println("ENEMY_HAS_NOTHING");
        if(DUG_IS_NOTHING)
            System.out.println("DUG_IS_NOTHING");
        if(DUG_IS_DEATH)
            System.out.println("DUG_IS_DEATH");
        if(DUG_HAS_DEATH)
            System.out.println("DUG_HAS_DEATH");
        if(ENEMY_HAS_SHIELD)
            System.out.println("ENEMY_HAS_SHIELD");
        if(DUG_HAS_SHIELD)
            System.out.println("DUG_HAS_SHIELD");
        if(SHIELD_IS_NOTHING)
            System.out.println("SHIELD_IS_NOTHING");
        if(GUN_IS_NOTHING)
            System.out.println("GUN_IS_NOTHING");
        if(ENEMY_HAS_ENEMY)
            System.out.println("ENEMY_HAS_ENEMY");
        if(ENEMY_IS_NOTHING)
            System.out.println("ENEMY_IS_NOTHING");
        if(DUG_IS_DEATH||DUG_IS_NOTHING||DUG_HAS_DEATH)
        { gp.player.life=0;}
        if(ENEMY_HAS_DEATH||ENEMY_IS_NOTHING)
            for(int i=0;i<gp.monster.length;i++)
            {   if(gp.monster[i]!=null) {
                gp.monster[i].life = 0;
                gp.combatObjects.remove(gp.monster[i].gun);
                gp.combatObjects.remove(gp.monster[i].shield);
                gp.monster[i].dying = true;

            }
            }

    }

    //verifica combinatiile noi create dupa mutarea blocurilor
    public void checkCombinations(){
        this.setCombinations();
        for(int i=0;i<AssetSetter.rows;i++)
        {
            for(int j=0;j<AssetSetter.cols-2;j++){
                char a=Character.toLowerCase(AssetSetter.blockMatrix[i][j]);
                char b=Character.toLowerCase(AssetSetter.blockMatrix[i][j+1]);
                char c=Character.toLowerCase(AssetSetter.blockMatrix[i][j+2]);
                if(!Character.isDigit(a) && !Character.isDigit(b) && !Character.isDigit(c) ){
                    if(isValidCombination(a,b,c))
                  {
                          if(AssetSetter.levelMatrix[i][j]==gp.levelManager.curentLevel+1)
                       this.manageCombination(a,b,c);
                    }
                }
        }   }
        for(int j=0;j<AssetSetter.cols;j++)
        {
            for(int i=0;i<AssetSetter.rows-2;i++){
                char a=Character.toLowerCase(AssetSetter.blockMatrix[i][j]);
                char b=Character.toLowerCase(AssetSetter.blockMatrix[i+1][j]);
                char c=Character.toLowerCase(AssetSetter.blockMatrix[i+2][j]);
                if(!Character.isDigit(a) && !Character.isDigit(b) && !Character.isDigit(c) ){
                    if(isValidCombination(a,b,c))
                    {    if(AssetSetter.levelMatrix[i][j]==gp.levelManager.curentLevel+1)
                        this.manageCombination(a,b,c);
                    }
                }}
        }
        this.printCombinations();
    }

}
