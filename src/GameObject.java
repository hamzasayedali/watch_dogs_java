//abstract class for all objects in the game because all objects will have an x,y,sx,sy and getters for all variables
import java.awt.*;
import java.util.*;

public abstract class GameObject 
{
    protected float x;
    protected float y;
    protected float sx;
    protected float sy;
    protected float rot;
    protected Image img;
    protected boolean touchedByPlayer;
    protected GamePanel game;
    
    abstract void update();
    
    public Image getImage()
    {
        return img;
    }
    
    public float getX()
    {
        return x;
    }
    public float getY()
    {
        return y;
    }
    public float getSX()
    {
        return sx;
    }
    public float getSY()
    {
        return sy;
    }
    
    public float getLeft()
    {
        return x;
    }
    public float getTop()
    {
        return y;
    }
    public float getRight()
    {
        return sx + x;
    }
    public float getBottom()
    {
        return sy + y;
    }
       
    public float getCenterX()
    {
        return x+(sx/2);
    }
       
    public float getCenterY()
    {
        return y+(sy/2);
    }
    
    public void touchedByPlayer(boolean state){
        touchedByPlayer=state;
    }
    
    
}
