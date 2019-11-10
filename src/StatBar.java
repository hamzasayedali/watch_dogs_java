
import java.awt.*;
import javax.swing.*;

public class StatBar {
    
    private GamePanel game;
    private String ammo,lives;//keeps track of how much ammo and how many lives
    private int x = 0;
    private int y = 64*16;
    private Image gunImage,cellPhoneImage;
    
    
    public StatBar(GamePanel game){
        this.game=game;
        gunImage = new ImageIcon("gun.png").getImage();
        cellPhoneImage = new ImageIcon("cellPhone.png").getImage();
        cellPhoneImage = cellPhoneImage.getScaledInstance(40, 80, Image.SCALE_SMOOTH);
        gunImage = gunImage.getScaledInstance(120, 70, Image.SCALE_SMOOTH);
        ammo = "";
        lives = "";
    }
    
    public void update(){
        if(game!=null){
            ammo = "Ammo: "+game.getPlayer().getAmmo();//updates numbers from player
            lives = "livesLeft: "+game.getPlayer().getLives();//updates numbers from player
        }
    }
    
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillRect(0, 64*16, 1680, 96);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Consolas",Font.PLAIN,40));
        g.drawString(ammo,x+50,y+55);
        g.drawString("HEALTH:",x+300,y+55);
        float h = game.getPlayer().getHealth();
        if(h>66){g.setColor(Color.GREEN);}
        else if(h>33){g.setColor(Color.YELLOW);}
        else{g.setColor(Color.RED);}
        g.fillRect(x+480, y+20, (int)(game.getPlayer().getHealth()/100*320), 50);
        
        if(game.getPlayer().getState()=="phone"){
            g.drawImage(cellPhoneImage, x+900, y+7, game);
        }
        else if(game.getPlayer().getState()=="gun"){
            g.drawImage(gunImage, x+1000, y+10, game);
        }
        
        g.setColor(Color.GREEN);
        g.setFont(new Font("Consolas",Font.PLAIN,40));
        g.drawString(lives,x+1200,y+55);
        
    }
}
