package Object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Heart extends SuperObject{
    GamePanel gp;
    public Heart(GamePanel gp){
        this.gp=gp;
        name="Heart";
        try{
            image= ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/heart_full1.png")));
            image1=ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/texturesMore/heart_blank1.png")));
        } catch (IOException e) {
            System.out.println("eroare la citire imagine inima");
        }
    }
}
