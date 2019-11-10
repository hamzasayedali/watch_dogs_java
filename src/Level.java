
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Level {
    
    private ArrayList<GameObject> terrain = new ArrayList<GameObject>();
    private ArrayList<GameObject>hackables = new ArrayList<GameObject>();
    private ArrayList<Collectable>collectables = new ArrayList<Collectable>();
    private ArrayList<Enemy1>enemies = new ArrayList<Enemy1>();
    private ArrayList<GameObject>doors = new ArrayList<GameObject>();
    private ArrayList<HackBox>hackboxes = new ArrayList<HackBox>();
    private ArrayList<Laser>lasers = new ArrayList<Laser>();
    
    private int end;
   
    private GamePanel game;
    
    private Image texture;
    
    private int x,y,offset;
    
    
    public Level(int index, int x, int y, GamePanel game){
        this.game=game;
        this.offset=0;
        this.x=x*64;
        this.y=y*64;
        
        loadLevel(index);
    }
    
    public void loadLevel(int index){
        BufferedImage mapImage = null;
        try{//reads corresponding file
            File f = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\maps\\"+index+".png");
            mapImage = ImageIO.read(f);
           
        }catch(IOException e){
            System.out.println(e);
        }
        
        texture = new ImageIcon("textures\\"+index+".png").getImage();
        texture = texture.getScaledInstance(128*16*4,16*16*4,Image.SCALE_SMOOTH);//makes texture fit screen
        
        for(int x = 0; x < 128; x++){//goes thru each square on the map image and loads the correct object based on the color
            for(int y = 0; y<16; y++){
                int p = mapImage.getRGB(x*32, y*32);
                int a = (p>>24) & 0xff;
                int r = (p>>16) & 0xff;
                int g = (p>>8) & 0xff;
                int b = p & 0xff;
                Color c = new Color(r,g,b);
                
                if(c.equals(Color.GREEN)){
                    Block bl = new Block(x*64,y*64,64,64,game);
                    terrain.add(bl);
                }
                if(c.equals(Color.YELLOW)){
                    end = x*64;
                }
                if(c.equals(Color.RED)){
                    Laser l = new Laser(x,y,11,game);
                    add(l);
                }
                if(c.equals(Color.CYAN)){
                    Enemy1 e = new Enemy1(x,y,game); 
                    add(e);
                }
                if(c.equals(Color.MAGENTA)){
                    Collectable co = new Collectable(x,y,1,game); 
                    add("collectables",co);
                }
                if(c.equals(new Color(255,165,0))){
                    Collectable co = new Collectable(x,y,2,game); 
                    add("collectables",co);
                }
                
            }
        }
    }
    //overloaded to accomadate for any type of object being added to desired array
    public void add(String array, GameObject go){
        if(array == "terrain"){
            terrain.add(go);
        }
        else if(array == "hackables"){hackables.add(go);}
        else if(array == "doors"){doors.add(go);}
    }
    public void add(String array, Collectable go){
        if(array == "collectables"){collectables.add(go);}
    }
    public void add(String array, HackBox go){
        hackboxes.add(go);
    }
    public void add(Enemy1 enemy){
        enemies.add(enemy);
    }
    public void add(Laser l){
        lasers.add(l);
    }
    
    public void addOffset(int i){
        offset+=i;
    }
    
    public int getOffset(){return offset;}
    public void setOffset(int i){offset=i;}
    
    
    public ArrayList<GameObject> getTerrain(){return terrain;}
    public ArrayList<GameObject> getHackables(){return hackables;}
    public ArrayList<Collectable> getCollectables(){return collectables;}
    public ArrayList<Enemy1> getEnemies(){return enemies;}
    public ArrayList<GameObject> getDoors(){return doors;}
    public ArrayList<HackBox> getHackBoxes(){return hackboxes;}
    public ArrayList<Laser> getLasers(){return lasers;}
    
    public Image getTextureImage(){return texture;}
    
    public void setX(int i){x=i*64;}
    public void setY(int i){y=i*64;}
    public int getX(){return x;}
    public int getY(){return y;}
    public int getEnd(){return end;};
}
