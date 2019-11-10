//PuzzleNode.java
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;

public class PuzzleNode extends GameObject{
    private boolean powered;
    private int[] openSides;//indicate which sides are available to connect to, n,e,s,w
    private boolean turnable;//indicates if you can turn it
    private Rectangle rect;
    private File wavFile = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\sounds\\pressNode.wav");
    private AudioClip sound;
    
    public PuzzleNode(int[] openSides, boolean turnable){
        this.openSides = openSides.clone();
        this.turnable=turnable;
        this.rect= new Rectangle(0,0,0,0);
        try{sound = Applet.newAudioClip(wavFile.toURI().toURL());}
        catch(Exception e){e.printStackTrace();}
    }
    
    @Override
    public void update(){
        
        if(openSides[0]==1&&openSides[1]==1&&openSides[2]==1&&openSides[3]==1){//makes the node automatically powered if all sides are available
            power(true);
            
        }
    }
    
    public void render(int x, int y, Graphics g){//draws node
        rect= new Rectangle(x,y,128,128);
        if(turnable){
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x, y, 128, 128);
        }
        if(powered){
            g.setColor(Color.GREEN);
        }
        else{
            g.setColor(Color.GRAY);
        }
        if(openSides[0]==1){
            g.fillRect(x+60, y, 8, 68);
        }
        if(openSides[1]==1){
            g.fillRect(x+60, y+60, 68, 8);
        }
        if(openSides[2]==1){
            g.fillRect(x+60, y+60, 8, 68);
        }
        if(openSides[3]==1){
            g.fillRect(x, y+60, 68, 8);
        }
    }
    
    public void turn(Point p){//turns node if turnable
        if(turnable&&rect.contains(p)){
            sound.play();
            int no = openSides[0];//makes each side the state of the one clockwise to it
            int eo = openSides[1];
            int so = openSides[2];
            int wo = openSides[3];
            openSides[0]=wo;
            openSides[1]=no;
            openSides[2]=eo;
            openSides[3]=so;
        }
    }
    
    public int[] getPoweredSides(){//returns a list of powered sides
        int[] poweredSides = new int[]{0,0,0,0};
        
        if(powered==true){
            
            if(openSides[0]==1){
                poweredSides[0]=1;
            }
            if(openSides[1]==1){
                poweredSides[1]=1;
            }
            if(openSides[2]==1){
                poweredSides[2]=1;
            }
            if(openSides[3]==1){
                poweredSides[3]=1;
            }
        }
        return poweredSides;
    }
    
    public int[] getOpenSides(){
        return openSides;
    }
    
    public void power(boolean state){powered=state;}
    public boolean getPowered(){return powered;}
    
}
