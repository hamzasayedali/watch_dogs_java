
import java.awt.*;
import java.io.*;
import java.util.*;

public class PuzzleMap {
    private PuzzleNode[][] nodes;//arraylist of nodes
    private int x,y;
    private GamePanel game;
    private boolean locked,exit;
    private Button enter;
    
    public PuzzleMap(String fileName, GamePanel game){
        nodes = new PuzzleNode[6][6];//6x6 list
        this.game=game;
        this.x=100;
        this.y=155;
        enter = new Button(295,1010,40,40,"",Color.GREEN,game);
        locked = true;
        
        //combinations of sides available for a node
        int[] west = new int[]{0,0,0,1}; 
        int[] horizontal = new int[]{0,1,0,1};
        int[] vertical = new int[]{1,0,1,0};
        int[] bend1 = new int[]{1,1,0,0};
        int[] bend2 = new int[]{0,1,1,0};
        int[] bend3 = new int[]{0,0,1,1};
        int[] bend4 = new int[]{1,0,0,1};
        int[] tee1 = new int[]{1,0,1,1};
        int[] tee2 = new int[]{1,1,0,1};
        int[] tee3 = new int[]{0,1,1,1};
        int[] tee4 = new int[]{1,1,1,0};
        int[] source = new int[]{1,1,1,1};
        
        int[][] patterns = new int[][]{horizontal,vertical,bend1,bend2,bend3,bend4,tee1,tee2,tee3,tee4,source};
        
        try{
            Scanner layout = new Scanner(new BufferedReader(new FileReader("puzzles\\"+fileName+".txt")));//creates scanner to read pokemon data
            int nodesNum = Integer.parseInt(layout.nextLine());//reads the first number to see how many pokemon
            
            for(int i = 0; i<nodesNum; i++){//loops thru all the lines in the file
                String[]p = layout.nextLine().split(",");//splits all data points into a string array
                int xPos = Integer.parseInt(p[0]);//stores xpos
                int yPos = Integer.parseInt(p[1]); //stores ypos
                int[] pattern = patterns[Integer.parseInt(p[2])];
                boolean turnable = Boolean.parseBoolean(p[3]);//stores type
                
                
                
                PuzzleNode n = new PuzzleNode(pattern,turnable);//creates a new node
                nodes[yPos][xPos]=n;//adds it to array at indicated position
            }
            
        }
        catch(IOException ex){
            System.out.println("File not found");//avoids crash if problem with file
            
        }
        
    }
    
    public void update(boolean pressed){
        resetAll();//resets all nodes to off
        exit = false;
        
        enter.update(pressed);
        if(enter.getPressed()){
            exit=true;//exits hack when button is pressed
        }
        
        for(int y = 0; y<6; y++){
            for(int x = 0; x<6; x++){
                if(nodes[y][x]!=null){
                    nodes[y][x].update();//goes thru all nodes to update
                }
            }
        }
        
        checkMouseClick(pressed);
        
        for(int i = 0; i<36; i ++){//checks for power 36 times to make sure every square gets updates properly
            checkForPower();
        }
        
        locked = false;
        for(int y = 0; y<6; y++){
            for(int x = 0; x<6; x++){
                if(nodes[y][x]!=null&&!nodes[y][x].getPowered()){
                    locked = true;
                }
            }
        }
        
        if(exit){
            game.setGameState("game");
        }
    }
    
    public void checkMouseClick(boolean pressed){//rotates node if pressed
        if(pressed){
            for(int y = 0; y<6; y++){
                for(int x = 0; x<6; x++){
                    
                    PuzzleNode n = nodes[y][x];
                    
                    if(n!=null){
                        
                        Point mouse = new Point(game.mouseX(),game.mouseY());
                        
                        n.turn(mouse);
                    }
                }
            }
        }
    }
    
    public void render(Graphics g){//draws all nodes
        enter.render(g);
        for(int y = 0; y<6; y++){
            for(int x = 0; x<6; x++){
                if(nodes[y][x]!=null){
                    nodes[y][x].render(this.x+x*128, this.y+y*128, g);
                }
            }
        }
    }
    
    public void resetAll(){
        for(int y = 0; y<6; y++){
            for(int x = 0; x<6; x++){
                if(nodes[y][x]!=null){
                    nodes[y][x].power(false);
                }
            }
        }
    }
    
    public void checkForPower(){
        
        PuzzleNode n,nNode;
        
        for(int y = 0; y<6; y++){
            for(int x = 0; x<6; x++){
                int[] possible = new int[]{1,1,1,1};//doesnt check places that are out of bound in the array
                if(x==0){possible[3]=0;}
                if(x==5){possible[1]=0;}
                if(y==0){possible[0]=0;}
                if(y==5){possible[2]=0;}
                
                n = nodes[y][x];
                
                
                
                if(n!=null&&n.getOpenSides()[0]==1&&possible[0]==1){
                    nNode = nodes[y-1][x];
                    if(nNode!=null&&nNode.getPoweredSides()[2]==1){//checks if the neighboring node has power and sends it
                        n.power(true);
                    }
                }
                
                if(n!=null&&n.getOpenSides()[1]==1&&possible[1]==1){
                    nNode = nodes[y][x+1];
                    if(nNode!=null&&nNode.getPoweredSides()[3]==1){
                        n.power(true);
                    }
                }
                
                if(n!=null&&n.getOpenSides()[2]==1&&possible[2]==1){
                    nNode = nodes[y+1][x];
                    if(nNode!=null&&nNode.getPoweredSides()[0]==1){
                        n.power(true);
                    }
                }
                
                if(n!=null&&n.getOpenSides()[3]==1&&possible[3]==1){
                    nNode = nodes[y][x-1];
                    if(nNode!=null&&nNode.getPoweredSides()[1]==1){
                        n.power(true);
                    }
                }
                
            }
        }
    }
    public boolean getLocked(){return locked;}
}
