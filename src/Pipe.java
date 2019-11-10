//Pipe.java
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Pipe extends GameObject{
    private GamePanel game;
    private BufferedImage imgHor;
    private BufferedImage imgVert;
    private int length,orientation,finalLength,dir,initialLength;
    private HackBox h;
    private int growthTime = -1;
    
    public Pipe(int x, int y, int l, int l2, int dir, int o, HackBox h, GamePanel game){
        this.x=x*64;
        this.y=y*64;
        this.sx=64;
        this.sy=64;
        
        this.game=game;
        this.length=l;
        this.finalLength=l2;
        this.initialLength=length;
        this.orientation=o;
        this.h=h;
        this.dir=dir;
        
        try{
            File f = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\pipe\\0.png");
            imgHor = ImageIO.read(f);
            File g = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\pipe\\1.png");
            imgVert = ImageIO.read(g);
           
        }catch(IOException e){
            System.out.println(e);
        }
        
        if(o==0){
            img = cropImage((BufferedImage)imgHor, new Rectangle(0,0,64*length,64));
            this.sx=length*64;
        }
        else{
            img = cropImage((BufferedImage)imgVert, new Rectangle(0,0,64,64*length));
            this.sy=length*64;
        }
    }
    
    @Override
    public void update(){
        if(h.getLocked()==false){
            if(length!=finalLength){//if its not at its final length, it will increase/decrease the length one block at a time until it is
                if(length<finalLength){
                    if(growthTime<0){
                        growthTime=20;
                    }
                    else{
                        growthTime--;
                        if(growthTime==0){
                            extend(1*dir*-1);
                        }
                    }
                }
                if(length>finalLength){
                    if(growthTime<0){
                        growthTime=20;
                    }
                    else{
                        growthTime--;
                        if(growthTime==0){
                            extend(1*dir);
                        }
                    }
                }
            }
        }
        else{//if its locked, it should be n its original position
            if(length!=initialLength){
                if(length<initialLength){
                    if(growthTime<0){
                        growthTime=20;
                    }
                    else{
                        growthTime--;
                        if(growthTime==0){
                            extend(1*dir*-1);
                        }
                    }
                }
                if(length>initialLength){
                    if(growthTime<0){
                        growthTime=20;
                    }
                    else{
                        growthTime--;
                        if(growthTime==0){
                            extend(1*dir);
                        }
                    }
                }
            }
            else{
                growthTime=-1;
            }
        }
    }
    
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {//crops image to desired length
      BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
      return dest; 
    }
    
    public void extend(int l){
        if(orientation==0){//if its horizontal, changes the x size
            x-=64*l;
            length+=l;
            sx = length*64;
            int wid = 64*length;
            img = cropImage((BufferedImage)imgHor, new Rectangle(0,0,wid,64));
        }
        else{//if its vertical, changes the y size
            y-=64*l;
            length+=l;
            sy = length*64;
            int hei = 64*length;
            img = cropImage((BufferedImage)imgVert, new Rectangle(0,0,64,hei));
        }
    }
}
