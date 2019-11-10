//Button.java
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import javax.swing.*;

public class Button {
    
    private int x,y,sx,sy;
    private Color c,c2,color;
    private String text;
    private JPanel game;
    private boolean pressed = false;
    private Point mousePos;
    private Rectangle rect;
    private File wavFile = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\sounds\\click.wav");
    private AudioClip sound;
    
    
    public Button(int x, int y, int sx, int sy, String text, Color c, JPanel g){
        this.x=x;
        this.y=y;
        this.sx=sx;
        this.sy=sy;
        this.text=text;
        this.c=c;
        this.c2 = Color.GRAY;
        this.color = c;
        this.game=g;
        rect = new Rectangle(x,y,sx,sy);
        this.mousePos = g.getMousePosition();
        
        try{sound = Applet.newAudioClip(wavFile.toURI().toURL());}
        catch(Exception e){e.printStackTrace();}
        
        
    }
    
    public void update(boolean pressed){
        this.pressed = false;
        mousePos = game.getMousePosition();
        if(mousePos!=null){
            if(rect.contains(mousePos)){
                color = c2;//changes color to gray if mouse is over it
                if(pressed){
                    this.pressed = true;
                    sound.play();
                }
            }
            else{
                color = c;
            }
        }
    }
    
    public void render(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, sx, sy);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Ariel",Font.PLAIN,32));
        g.drawString(text,x+100,y+100);
    }
    
    public boolean getPressed(){return pressed;}
    
}
