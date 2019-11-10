//Enemy1.png
import java.awt.*;
import java.awt.geom.Line2D;

/**
 *
 * @author Hamza
 */
public class Enemy1 extends GameObject{
    float vx,vy = 0;
    float acc = 3;
    int speed = 3;
    int dir = 0; // 0 == Right 1 == Left
    Rectangle r;
    int health = 20;
    
    int jump = 25;
    
    boolean isOnGround=false;
    
    private boolean targeting = false;//if enemy is targeting player
    
    private int jumpCount = -1;
    private int shootCount = -1;
    
    private GamePanel game;
    private SpriteManager sprites;
    
    private String[] folders;
    private int[] nums;
    
    private boolean dead;
    private int dying = -1;
    private int cooldownTime = -1;
    private boolean shot = false;
    
    public Enemy1(int x, int y, GamePanel game){
        this.x=x*64;
        this.y=y*64;
        this.sx=40;
        this.sy=80;
        this.vx=2;
        
        this.game=game;
        
        dead = false;
        
        folders = new String[]{"enemy1\\walkRight\\","enemy1\\walkLeft\\","enemy1\\death\\"};
        nums = new int[]{6,6,4};
        
        this.sprites = new SpriteManager(folders,nums,60,80);
    }
    
    @Override
    public void update(){
        shot = false;
        
        if(health<=0&&dying<0){die();}
        
        if(dying>0){
            dying--;
            if(dying==0){
                dead=true;
            }
        }
        
        sprites.update();
        this.img = sprites.getCurrentSprite();
        
        
        
        
        vy+=acc;
        if(dying<0){
            if(!targeting && canSeePlayer()){//if the player is not already targeted, starts targeting process
                cooldownTime = -1;
                targeting = true;
                jumpCount = (int)(Math.random()*50);
                shootCount = (int)(Math.random()*100);

            }

            if(targeting){
                vx = (game.getPlayerX()-x)/Math.abs(game.getPlayerX()-x)*speed;
                jumpCount--;
                shootCount--;

                if(jumpCount==0){
                    jumpCount = (int)(Math.random()*50);//jumps randomly when it can see the player
                    jump();
                }
                if(shootCount==0){
                    shootCount = (int)(Math.random()*100);//shoots at player
                    shot = true;
                }
                if(!canSeePlayer()){//waits 200 frames after not seeing player to stop targeting
                    if(cooldownTime<0){
                        cooldownTime=200;
                    }
                    cooldownTime--;
                    if(cooldownTime==0){
                        targeting = false;
                        vx=2;
                        jumpCount=-1;
                        shootCount=-1;
                    }
                }

            }
            //see player.update() for comments
            if(vx<0){
                sprites.setSpriteGroup(1);
                dir=1;
            }
            else if(vx>0){
                sprites.setSpriteGroup(0);
                dir=0;
            }
            else if(vx==0){
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
                    
                    if(collisionDetected()){
                        x+=1;
                        vx*=-1;
                        
                        break;
                    }
                }
                else if(vx>0){

                    x+=1;
                    if(x>400)
                        
                    if(collisionDetected()){
                        
                        x-=1;
                        vx*=-1;
                        
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
                        isOnGround=true;
                        break;
                    }
                }
            }
            
        }
        
            
            
            
            
    }
    
    public void damage(int i){
        health-=i;
    }
    
    public void jump(){
        if(isOnGround){
            isOnGround=false;
            vy = -jump;//sets vy to jump speed
        }
    }
    
    public Bullet shoot(float px, float py){
        
        double xDist = px - x;
        double yDist = py - y;
        double dist = Math.sqrt(xDist*xDist+yDist*yDist);
        double vx = xDist/(dist+1);
        double vy = yDist/(dist+1);
        
        Bullet b = new Bullet((int)x,(int)y,vx,vy,game);//creates a new bullet aiming at the player
        
        return b;
    }
    
    public boolean shot(){return shot;}
    
    public boolean collisionDetected(){
        
        for(GameObject go : game.getTerrain()){
            if(game.checkCollision(this,go)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean collisionLineDetected(Line2D line){
        for(GameObject go : game.getTerrain()){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        
        return false;
    }
    
    public void die(){//starts dying process
        vx=0;
        dying=10;
        sprites.setSpriteGroup(2);
    }
    
    public int spriteX(){
        return (int)x+(int)sx/2-sprites.getCurrentSprite().getWidth(game)/2;
    }
    public int spriteY(){
        return (int)y+(int)sy-sprites.getCurrentSprite().getHeight(game);
    }
    
    public boolean canSeePlayer(){//checks if theres an unobstructed line between this and the player
        
        
            float px = game.getPlayerCenterX();
            float py = game.getPlayerY();
            Line2D l1 = new Line2D.Float(px, py, x, y);

            if(collisionLineDetected(l1)){
                return false;
            }
            return true;
        
    }
    
    public boolean dead(){return dead;}
}
