
import java.awt.Image;
import javax.swing.ImageIcon;

public class SpriteManager {
    
    private Image currentImage;//current image to be displayed
    private Image[][] spriteGroups;//array of sprite groups
    private int currentGroupIndex;//current sprite group
    private int currentSpriteIndex;
    private int frame;
    private int sx,sy;
    
    public SpriteManager(String[] folders, int[] nums, int sx, int sy){
        currentGroupIndex = 0;
        currentSpriteIndex = 0;
        frame = 0;
        
        this.sx=sx;
        this.sy=sy;
        
        spriteGroups = new Image[folders.length][nums.length];
        
        
        
        for(int i = 0; i < folders.length; i++){
            spriteGroups[i]=sprites(folders[i],nums[i]);
        }
        
        currentImage = spriteGroups[currentGroupIndex][currentSpriteIndex];
    }
    
    
    
    public Image[] sprites(String fileName, int n){
        Image[] imgs = new Image[n];
        for(int i = 0; i<n; i++){//loads all images from filename
            Image img = new ImageIcon(fileName + i + ".png").getImage();
            img = img.getScaledInstance(sx, sy, 0);
            
            imgs[i] = img;
        }
        return imgs;
    }
    
    public void setSpriteGroup(int i){
        if(currentGroupIndex!=i){//updates the sprite group
            currentGroupIndex=i;
            frame=0;
            currentSpriteIndex=0;
            currentImage = spriteGroups[currentGroupIndex][currentSpriteIndex];
        }
        
    }
    
    public Image getCurrentSprite(){
        return currentImage;
    }
    
    public void update(){
        frame+=1;
        if(frame>=3){//changes the sprite every 3 frames
            frame=0;
            currentSpriteIndex+=1;
            if(currentSpriteIndex>=spriteGroups[currentGroupIndex].length){//if its at the end of the group, it loops back to the start
                currentSpriteIndex=0;
            }
            currentImage = spriteGroups[currentGroupIndex][currentSpriteIndex];//updates current image
        }
    }
}
