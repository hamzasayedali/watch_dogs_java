
import java.awt.Graphics;
import java.awt.*;
import javax.swing.ImageIcon;

public class Laser extends GameObject{
    
    Image head = new ImageIcon("laser\\0.png").getImage();//base of laser
    Image laser = new ImageIcon("laser\\1.png").getImage();//actual beam
    Image scaledLaser = laser;
    
    private int length = 0;
    private int firing = 1;//1 if on, -1 if off
    private int fireTime = -1;//if greater than 1, it fires
    
    public Laser(int x, int y, int h, GamePanel game){
        this.x=x*64+24;
        this.y=y*64;
        this.sx=16;
        this.sy=h*64;//length of laser
        this.game=game;
        
        scaledLaser = laser.getScaledInstance(16, h*64, Image.SCALE_SMOOTH);
    }
    
    @Override
    public void update(){
        if(fireTime<0){//if the timer runs out, it changes state and resets timer
            firing*=-1;
            fireTime = (int)(Math.random() * 100);
        }
        else{
            fireTime--;//decreases timer
        }  
    }
    
    public void render(Graphics g){
        if(firing>=0){//draws the beam if firing
            g.drawImage(scaledLaser, (int)x+game.getOffset(), (int)y, game);
        }
        g.drawImage(head, (int)x-24+game.getOffset(), (int)y, game);
    }
    
    public boolean isFiring(){
        if(firing>0){return true;}
        return false;
    }
}
