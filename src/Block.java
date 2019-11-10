
import java.awt.*;
/**
 *
 * @author Hamza
 */
public class Block extends GameObject{
    
    private GamePanel game;
    
    public Block(int x, int y, int sx, int sy, GamePanel game){
        this.x=x;
        this.y=y;
        this.sx=sx;
        this.sy=sy;
        
        this.game=game;
    }
    
    @Override
    public void update(){
    
    }
}
