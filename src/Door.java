/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamza
 */
public class Door extends GameObject{
    private GamePanel game;
    private boolean open,locked;
    private SpriteManager sprites;
    private String[] folders;
    private int[] nums;
    private int openTime;
    private boolean lastTouched = false;
    private int leadsTo;//level that enters from the door
    private HackBox hack;//hack thats linked
    
    public Door(int x, int y, int leadsTo, HackBox h, GamePanel game){
        this.x=x-18;
        this.y=y-36;
        this.sx=100;
        this.sy=100;
        this.game=game;
        this.open=false;
        this.leadsTo=leadsTo;
        
        this.hack=h;
        
        if(hack==null){//if theres no hackbox, the door is unlocked
            unlock();
        }
        else{
            locked=true;
        }
        
        openTime = -1;
        
        folders = new String[]{"door\\locked\\","door\\unlocked\\","door\\opening\\","door\\open\\"};
        nums = new int[]{1,1,12,1};
        
        sprites = new SpriteManager(folders,nums,100,100);
        sprites.setSpriteGroup(0);
        this.img=sprites.getCurrentSprite();
    }
    
    @Override
    public void update(){
        
        if(hack!=null&&hack.getLocked()==false&&locked==true){//unlocks door if box is unlocked
            unlock();
        }
        else if(hack!=null&&hack.getLocked()==true&&locked==false){//locks door if box is locked
            lock();
        }
        
        if(!locked && lastTouched == false && touchedByPlayer == true){//if its unlocked and touched by player then opens
            open();
            lastTouched = true;
        }
        
        if(openTime>0){
            openTime-=1;
        }
        else if (openTime==0){//if it has opened all the way it sets it to open
            sprites.setSpriteGroup(3);
            open=true;
        }
        
        if(open && touchedByPlayer){//sends to next level if player goes through the door
            game.nextLevel(leadsTo);
        }
        
        
        
        sprites.update();
        this.img = sprites.getCurrentSprite();
    }
    
    public void unlock(){
        locked=false;
        if(sprites!=null){
            sprites.setSpriteGroup(1);
            img=sprites.getCurrentSprite();
        }
    }
    public void lock(){
        locked = true;
        sprites.setSpriteGroup(0);
        img=sprites.getCurrentSprite();
    }
    
    public void open(){
        sprites.setSpriteGroup(2);
        
        openTime = 30;//takes 30 frames to open
    }
}
