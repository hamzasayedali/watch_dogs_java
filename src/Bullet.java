
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;




public class Bullet extends GameObject{
    
    private double vx,vy;
    private GamePanel game;
    private int speed = 20;
    private Image bulletImage = new ImageIcon("collectables/bullet.png").getImage();
    private BufferedImage bImg = null;
    private boolean hit = false;
    
    File wavFile = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\sounds\\shoot.wav");
    AudioClip sound;
    
    public Bullet(int x, int y, double vx, double vy, GamePanel game){
        
        this.x=x;
        this.y=y;
        this.sx=16;
        this.sy=16;
        
        this.vx=vx*speed;
        this.vy=vy*speed;
        this.game=game;
        
        try{
            File f = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\collectables\\bullet.png");
            bImg = ImageIO.read(f);
        }catch(IOException e){
            System.out.println(e);
        }
        
        bulletImage= bulletImage.getScaledInstance(16, 16, 0);
        render();
        img = bImg;
        
        try{sound = Applet.newAudioClip(wavFile.toURI().toURL());}
        catch(Exception e){e.printStackTrace();}
        sound.play();
    }
    
    @Override
    public void update(){
        for(int i = 0; i<Math.abs(vx);i++){//checks if the bullet hits something and changes hit to true
            if(vx<0){
                x-=1;
                if(collisionDetected()){
                    x+=1;
                    vx*=-1;
                    hit=true;
                    break;
                }
            }
            else if(vx>0){

                x+=1;
                if(x>400)

                if(collisionDetected()){
                    x-=1;
                    vx*=-1;
                    hit=true;
                    break;
                }
            }
        }
        for(int i = 0; i<Math.abs(vy); i++){
                if(vy<0){
                    y-=1;
                    if(collisionDetected()){
                        y+=1;
                        hit=true;
                        break;
                    }
                }
                else if(vy>0){
                    y+=1;
                    if(collisionDetected()){
                        y-=1;
                        hit=true;
                        break;
                    }
                }
            }
        
        
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
    public void collisionEnemyDetected(){
        for(Enemy1 go : game.getEnemies()){//damages enemy if hit
            
            if(game.checkCollision(this,go)){
                go.damage(10);
                this.hit=true;
                break;
            }
        }
    }
    
    public void collisionPlayerDetected(){//damages player if hit
            if(game.checkCollision(this,game.getPlayer())){
                game.getPlayer().damage(10);
                this.hit=true;
            }
    }
    
    public void render(){//updates bullet image before drawing
        AffineTransform tx = new AffineTransform();
        double ang = Math.atan(vy/vx);
        if(vx<0){
            ang=Math.PI+ang;
        }
        if(ang==0){
            ang=1;
        }
        tx.rotate(ang, bImg.getWidth() / 2, bImg.getHeight() / 2);//rotates sprite to correct angle

        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        bImg = op.filter(bImg, null);	
    }
    
    public boolean hit(){
        return hit;
    }
}
