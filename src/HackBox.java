
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import javax.swing.ImageIcon;


public class HackBox extends GameObject{
    
    private GamePanel game;
    private Image lockedImg,unlockedImg;//sprites
    private boolean locked = true;//locked or unlocked
    private Point mousePos;
    private Rectangle rect;//for collision detection
    private int puzzle;//corresponds with puzzle in list
    
    private File wavFile = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\sounds\\systemEntered.wav");
    private AudioClip sound;
    
    public HackBox(int x, int y, int puzzle, GamePanel game){
        this.x=x*64;
        this.y=y*64;
        this.game=game;
        this.puzzle = puzzle;
        lockedImg = new ImageIcon("hackbox\\0.png").getImage();
        unlockedImg = new ImageIcon("hackbox\\1.png").getImage();
        lockedImg=lockedImg.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        unlockedImg=unlockedImg.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        rect = new Rectangle(x*64,y*64,64,64);
        img = lockedImg;
        
        try{sound = Applet.newAudioClip(wavFile.toURI().toURL());}
        catch(Exception e){e.printStackTrace();}
    }
    
    @Override
    public void update(){
        rect = new Rectangle((int)x+game.getOffset(),(int)y,64,64);//updates the rectangle's position on screen
        mousePos = game.getMousePosition();//updates mouse position
        boolean pressed = game.getMousePressed();
        if(mousePos!=null){
            if(rect.contains(mousePos) && canSeePlayer()&&pressed&&game.getPlayer().getState()=="phone"){
                game.setCurrentPuzzle(puzzle);//enters the hacking screen if clicked on
                game.setGameState("hack");
                sound.play();
                
                
            }
        }
        locked = game.getPuzzle(puzzle).getLocked();//if the puzzle is locked, then the box will be locked

        if(locked){
            img = lockedImg;
        }
        else if(locked==false){
            img = unlockedImg;
        }
    }
    
    public boolean canSeePlayer(){//checks if the line on sight to the player is clear
            float px = game.getPlayerCenterX();
            float py = game.getPlayerY();
            Line2D l1 = new Line2D.Float(px, py, x+32, y+32);

            if(collisionLineDetected(l1)){
                return false;
            }
            return true;
        
    }
    
    public boolean collisionLineDetected(Line2D line){//checks if anything is obstructing the line betweeen this and the player
        for(GameObject go : game.getTerrain()){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }//goes through every object in terrain and hackables
        for(GameObject go : game.getHackables()){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean getLocked(){return locked;}
}
