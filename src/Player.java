
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Player extends GameObject{
    private float vx,vy = 0;//speed in x and y directions
    private final float acc = 3;//acceleration due to gravity
    private int speed = 15;//walking speed
    private int dir = 0; // 0 == Right 1 == Left
    private int jump = 26;//jump velocity
    private int ammo = 10;//amount of ammo
    private float health = 100;//health of player
    
    private int dying = -1;//timer for the dying animation of the player
    
    private int lives = 3;//amount of lives left
    
    private boolean dead = false;//indicates if player is dead
    
    private boolean isOnGround=false;//if player is touching the ground
    private GamePanel game;
    
    private String state;//indicates if the player has phone out or gun out 
    
    private boolean playerHasGun=false;//indicates if the player has the gun
    
    private SpriteManager sprites;//manages sprites
    
    private String[] folders;//list of sprite folders
    private int[] nums;//number of sprites in each folder

    public Player(int x, int y, GamePanel game){
        this.x=x;
        this.y=y;
        this.sx=40;
        this.sy=80;
        
        this.vy=5;
        
        this.game=game;
        
        state = "phone";
        
        folders = new String[]{"player\\","player\\runLeft\\","player\\standRight\\","player\\standLeft\\","player\\dying\\"};
        nums = new int[]{8,8,9,9,9};
        
        sprites = new SpriteManager(folders,nums,48,80);//loads all sprites
    }
    @Override
    public void update(){
        sprites.update();//updates sprites
        vy+=acc;//increases player y velocity
        this.img = sprites.getCurrentSprite();//updates image
        
        if(lives<0){dead=true;}//checks if player is dead
        
        if(health<0&&dying<0){die();}//starts dying process
        
        if(dying>0){
            dying--;
            vx=0;
            if(dying==0){
                dying = -1;
                kill();//kills player once finished dying
            }
        }
        
        if(vx<0){//updates sprite group based on direction and movement
            sprites.setSpriteGroup(1);
            dir=1;
        }
        else if(vx>0){
            sprites.setSpriteGroup(0);
            dir=0;
        }
        else if(vx==0&&dying<0){
            if(dir==0){
                sprites.setSpriteGroup(2);
            }
            else{
                sprites.setSpriteGroup(3);
            }
        }

        

        for(int i = 0; i<Math.abs(vx);i++){
            if(vx<0){
                x-=1;
                if(x>400 && x<game.getEnd()-1220){//adds offset if the player is moving
                    game.addOffset(1);
                }
                else{}
                if(collisionDetected()){
                    x+=1;//moves player back if hits wall

                    if(x>400 && x<game.getEnd()-1220){
                        game.addOffset(-1);
                    }
                    break;//stops moving if there is a collision
                }
            }
            else if(vx>0){

                x+=1;
                if(x>400 && x<game.getEnd()-1220){
                    game.addOffset(-1);
                }
                else{}
                if(collisionDetected()){
                    x-=1;
                    if(x>400 && x<game.getEnd()-1220){
                        game.addOffset(1);
                    }
                    break;
                }
            }


        }
        for(int i = 0; i<Math.abs(vy); i++){
            if(vy<0){
                y-=1;
                if(collisionDetected()){
                    y+=1;
                    vy=5;

                    break;
                }
            }
            else if(vy>0){
                y+=1;
                if(collisionDetected()){
                    y-=1;
                    vy=5;
                    isOnGround=true;//resets to true if player touches ground
                    break;
                }
            }
        }

        if(collisionDetected()){//if there is another collision after first checks, it means the player is stuck in a block and should be unstuck
            y-=64;
        }

        vx=0;//resets x velocity
        
        
        for(Laser l : game.getLasers()){//checks if the player hits a laser and does damage
            if(game.playerCollides(l)&&l.isFiring()){
                damage(2);
            }
        }
        
    }
    
    public void die(){//starts dying process
        vx=0;
        dying=27;
        sprites.setSpriteGroup(4);
    }
    
    public void damage(float i){//changes player health
        health-=i;
        if(health>100){
            health=100;
        }
    }
    
    
    public void move(int dir){
        vx=dir*speed;  
    }
    public void jump(){
        if(isOnGround){
            isOnGround=false;
            vy = -jump;
        }
    }
    
    public void toggleState(){//changes between phone and gun
        if(playerHasGun&&state=="phone"){
            state="gun";
        }
        else if(state=="gun"){
            state="phone";
        }
    }
    
    public void kill(){//resets player pos
        this.x=game.getLevel().getX();
        this.y=game.getLevel().getY();
        this.health=100;
        this.lives--;
        game.setOffset(0);
    }
    
    
    public boolean collisionDetected(){
        
        for(GameObject go : game.getTerrain()){
            if(game.checkCollision(this,go)){
                return true;
            }
        }
        for(GameObject go : game.getHackables()){
            if(game.checkCollision(this,go)){
                return true;
            }
        }
        return false;
    }
    
    
    public boolean canSeeMouse(){
            float mx = game.getPlayerCenterX();
            float my = game.getPlayerY();
            Line2D l1 = new Line2D.Float(mx, my, x, y);

            return collisionLineDetected(l1);
    }
    
    public boolean collisionLineDetected(Line2D line){
        for(GameObject go : game.getTerrain()){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        for(GameObject go : game.getHackables()){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        
        return false;
    }
    
    public int getScreenX(){return (int)x+game.getOffset();}
    
    public int spriteX(){
        return (int)x+(int)sx/2-sprites.getCurrentSprite().getWidth(game)/2;
    }
    public int spriteY(){
        return (int)y+(int)sy-sprites.getCurrentSprite().getHeight(game);
    }
    
    public void pickUp(Collectable c){//goes thru collectables and performs the correct corresponding action
        if(c.getType()==0){
            playerHasGun=true;
            game.setGameState("info");
        }
        else if(c.getType()==1){
            damage(-20);
        }
        else if(c.getType()==2){
            ammo+=5;
        }
    }
    
    public Bullet shoot(int mousex, int mousey){//creates new bullet aimed at the mouse
        
        double xDist = mousex - getScreenX();
        double yDist = mousey - 40 - (int)y;
        double dist = Math.sqrt(xDist*xDist+yDist*yDist);
        double vx = xDist/(dist+1);
        double vy = yDist/(dist+1);
        System.out.println("Shot");
        Bullet b = new Bullet((int)x,(int)y,vx,vy,game);
        ammo--;
        return b;
    }
    
    public String getState(){return state;}
    public int getAmmo(){return ammo;}
    public float getHealth(){return health;}
    public int getLives(){return lives;}
    public boolean isDead(){return dead;}
}
