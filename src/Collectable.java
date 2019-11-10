
import javax.swing.ImageIcon;

public class Collectable extends GameObject{
    private boolean free = true;
    private int startY;
    private int vy = -2;
    
    private int type;
    
    private GamePanel game;
    
    public Collectable(int x, int y, int type, GamePanel game){
        this.x=x*64+20;
        this.y=y*64+32;
        this.sy=20;
        this.sx=40;
        this.startY=(int)this.y;
        this.game=game;
        this.type=type;
        
        
        this.img = new ImageIcon("collectables\\"+type+".png").getImage();//loads desired icon
    }
    @Override
    public void update(){
        if(free){//makes collectable bounce up and down
            if(y<startY-32){vy*=-1;}
            if(y>startY){vy*=-1;} 
            y+=vy;
        }
    }
    
    public void pickUp(){
        free=false;
    }
    
    public boolean getFree(){return this.free;}
    
    public int getType(){return type;}
}