//Hamza Sayed-Ali
//GamePanel.java
//this is the maost important class in the program, it keeps track of
//all of the different game objects and updates and renders them

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class GamePanel extends JPanel implements ActionListener{
    
    //Class wide variables
    private boolean[]keys;//keeps track of which keys are pressed
    private boolean mousePressed=false;//keeps track of if the mouse was clicked or released
    private boolean mouseReleased=false;
    private int mousex,mousey;//keep track of mouse pos
    
    private boolean toggleable=true;//for switching between the players phone and gun only once per click
    
    //Menu Variables
    private Image menuImage = new ImageIcon("menu.png").getImage();//back images for each screen
    private Image helpImage = new ImageIcon("help.png").getImage();
    private Image loreImage = new ImageIcon("lore.png").getImage();
    private Image hackImage = new ImageIcon("hack.png").getImage();
    private Image overImage = new ImageIcon("over.png").getImage();
    private Image infoImage = new ImageIcon("info.png").getImage();
    private Button playButton = new Button(340,220,40,40,"",Color.GREEN,this);//buttons for each screen
    private Button helpButton = new Button(305,300,40,40,"",Color.GREEN,this);
    private Button loreButton = new Button(305,380,40,40,"",Color.GREEN,this);
    private Button exitButton = new Button(305,460,40,40,"",Color.GREEN,this);
    private Button exitButton2 = new Button(355,275,40,40,"",Color.GREEN,this);
    private Button menuButton = new Button(595,610,40,40,"",Color.GREEN,this);
    private Button restartButton = new Button(530,200,40,40,"",Color.GREEN,this);
    private Button closeButton = new Button(470,525,40,40,"",Color.GREEN,this);
    
    //Game Variables
    private boolean exit = false, restart = false;
    
    private Image background = new ImageIcon("background.png").getImage();
    private Image textureImage;
    
    private int nextLevel=-1;//advances to the next level when >= 0
    private Level[]levels = new Level[8];//stores all the levels
    private final int numLevels = 7;//number of levels to load
    
    private int level = 0;//current level
    
    private ArrayList<GameObject> terrain = new ArrayList<GameObject>();//Arrays for each type of object
    private ArrayList<GameObject>hackables = new ArrayList<GameObject>();
    private ArrayList<HackBox>hackboxes = new ArrayList<HackBox>();
    private ArrayList<Collectable>collectables = new ArrayList<Collectable>();
    private ArrayList<Bullet>bullets = new ArrayList<Bullet>();
    private ArrayList<Bullet>enemyBullets = new ArrayList<Bullet>();
    private ArrayList<Enemy1>enemies = new ArrayList<Enemy1>();
    private ArrayList<GameObject>doors = new ArrayList<GameObject>();
    private ArrayList<Laser>lasers = new ArrayList<Laser>();
    
    private ArrayList<PuzzleMap>puzzles = new ArrayList<PuzzleMap>();//Array to store the hacking puzzles
    private int currentPuzzle;//index of current puzzle
    private int numPuzzles = 11;//number of puzzles to load
    
    //Player Variables
    public Player p;//player
    private boolean line = false;//if the mouse is pressed, it draws a line between the player and mouse
    
    private int offset = 0;//keeps track of screen offset
    private int end;//keeps track of the end of the level
    
    private StatBar stats = new StatBar(this);//creates the stats bar at the bottom of the screen
    
    private String state = "menu";//keeps track of gameState
    
    //theme song
    private File themeFile = new File("C:\\Users\\Hamza\\Documents\\NetBeansProjects\\Grade12FSE\\sounds\\themeSong.wav");
    private InputStream in;

    // create an audiostream from the inputstream
    private AudioStream audioStream;
    
    
    GamePanel(){
        
        initLevels();//inits levels
        
        p = new Player(128,400,this);//inits player
        
        keys = new boolean[KeyEvent.KEY_LAST+1];//inits the key boolean array
 
        //manually adding things to the levels like doors and other features
        
        //Level 0
        levels[0].setX(3);
        levels[0].setY(8);

        Door door = new Door(54*64,8*64,1,null,this);

        levels[0].add("doors", door);

        //Level1
        
        levels[1].setX(4);
        levels[1].setY(8);
        
        HackBox hack1 = new HackBox(41,12,0,this);
        Door d1 = new Door(20*64,11*64,2,hack1,this);
        
        levels[1].add("doors",d1);
        levels[1].add("hackboxes",hack1);
        
        
        levels[2].setX(3);
        levels[2].setY(7);
        
        
        
        HackBox hack2 = new HackBox(25,10,1,this);
        Door d2 = new Door(10*64,13*64,3,hack2,this);
        levels[2].add("doors",d2);
        levels[2].add("hackboxes",hack2);

        //Level2
        
        
        
        
        //Level3
        
        
        HackBox hack3 = new HackBox(10,2,2,this);
        HackBox hack4 = new HackBox(26,140,3,this);
        Pipe pipe = new Pipe(12,12,1,3,-1,1,hack3,this);
        Pipe pipe2 = new Pipe(26,11,1,3,-1,0,hack4,this);
        
        HackBox hack5 = new HackBox(24,1,4,this);
        Pipe pipe3 = new Pipe(46,7,3,1,-1,1,hack5,this);
        
        HackBox hack6 = new HackBox(50,7,5,this);
        Door door3 = new Door(46*64,5*64,4,hack6,this);
        
        levels[3].setX(4);
        levels[3].setY(7);
        
        levels[3].add("hackables",pipe);
        levels[3].add("hackables",pipe2);
        levels[3].add("hackboxes",hack3);
        levels[3].add("hackboxes",hack4);
        levels[3].add("hackables",pipe3);
        levels[3].add("hackboxes",hack5);
        levels[3].add("doors",door3);
        levels[3].add("hackables",hack6);
        
        //Level4
        
        levels[4].setX(4);
        levels[4].setY(7);
        
        Collectable gun = new Collectable(43,5,0,this);
        HackBox hack7 = new HackBox(40,10,6,this);
        Door door4 = new Door(43*64,12*64,5,hack7,this);
        
        levels[4].add("collectables",gun);
        levels[4].add("hackboxes",hack7);
        levels[4].add("doors",door4);
        
        //Level 5
        
        HackBox hack8 = new HackBox(2,8,7,this);
        HackBox hack9 = new HackBox(9,12,8,this);
        HackBox hack10 = new HackBox(12,10,9,this);
        HackBox hack11 = new HackBox(37,11,10,this);
        HackBox hack12 = new HackBox(49,8,11,this);
        Pipe pipe4 = new Pipe(6,11,1,3,-1,1,hack8,this);
        Pipe pipe5 = new Pipe(10,9,1,3,-1,1,hack9,this);
        Pipe pipe6 = new Pipe(13,6,1,3,-1,1,hack10,this);
        Pipe pipe7 = new Pipe(41,10,4,1,-1,1,hack11,this);
        Door door5 = new Door(48*64,13*64,6,hack12,this);
        
        
        levels[5].add("hackables",pipe4);
        levels[5].add("hackables",pipe5);
        levels[5].add("hackables",pipe6);
        levels[5].add("hackables",pipe7);
        
        
        levels[5].add("hackboxes",hack12);
        levels[5].add("hackboxes",hack8);
        levels[5].add("hackboxes",hack9);
        levels[5].add("hackboxes",hack10);
        levels[5].add("hackboxes",hack11);
        
        levels[5].add("doors",door5);
        
        levels[5].setX(4);
        levels[5].setY(7);
        
        //Level 6
        
        levels[6].setX(4);
        levels[6].setY(7);
        
        
        loadPuzzles();//loads all puzzles
        
        loadLevel(level);//loads the first level
        
        try{in = new FileInputStream(themeFile);
        audioStream = new AudioStream(in);
        }
        catch(Exception e){e.printStackTrace();}
        
        AudioPlayer.player.start(audioStream);//plays song in background
        
        
    }
    
    public void loadPuzzles(){//creates new puzzles based on the file number given
        for(int i = 0; i<numPuzzles; i++){
            PuzzleMap p = new PuzzleMap(Integer.toString(i),this);
            puzzles.add(p);
        }
    }
    
    public void loadLevel(int level){
        Level l = levels[level];//gets level from list
        
        end = l.getEnd();//updates the end of the level
        
        p.x=l.getX();//resets player coords
        p.y=l.getY();
        
        //updates all arrays to be based on the level
        textureImage = l.getTextureImage();
        terrain = l.getTerrain();
        hackables = l.getHackables();
        collectables = l.getCollectables();
        hackboxes = l.getHackBoxes();
        enemies = l.getEnemies();
        doors = l.getDoors();
        lasers = l.getLasers();
        bullets = new ArrayList<Bullet>();
        enemyBullets = new ArrayList<Bullet>();
    }
    
    public void initLevels(){
        for(int i = 0; i < numLevels; i++){
            levels[i]=new Level(i,0,0,this);//creates a new level based on the file number
        }
    }
    
    public void refresh(){
        if(state=="menu"){//updates all buttons on specified menu screen
            playButton.update(mousePressed);
            helpButton.update(mousePressed);
            loreButton.update(mousePressed);
            exitButton.update(mousePressed);
            
            if(playButton.getPressed()){
                state = "game";
            }
            else if(helpButton.getPressed()){
                state = "help";
            }
            else if(loreButton.getPressed()){
                state = "lore";
            }
            else if(exitButton.getPressed()){
                exit = true;
            }
        }
        else if(state == "help"){
            menuButton.update(mousePressed);
            if(menuButton.getPressed()){
                state = "menu";
            }
        }
        else if(state == "lore"){
            menuButton.update(mousePressed);
            if(menuButton.getPressed()){
                state = "menu";
            }
        }
        
        else if(state=="hack"){//updates the current puzzle if hacking
            puzzles.get(currentPuzzle).update(mousePressed);
        }
        
        else if(state=="over"){
            restartButton.update(mousePressed);
            
            exitButton2.update(mousePressed);
            if(restartButton.getPressed()){
                AudioPlayer.player.stop(audioStream);//stops music if restarting game
                restart=true;//indicates that game should restart
            }
            if(exitButton2.getPressed()){
                exit=true;//indicates that game should be exited
            }
        }
        
        else if(state=="info"){
            closeButton.update(mousePressed);
            if(closeButton.getPressed()){
                state="game";
            }
        }
        
        else if(state=="game"){
            if(level==6){
                state = "over";
            }
            
            if(nextLevel>=0){//checks if the next level need to be loaded
                level=nextLevel;
                loadLevel(level);
                nextLevel=-1;
            }
            offset = levels[level].getOffset();//updates the offset

            if(keys[KeyEvent.VK_D]){
                p.move(1);//moves player right
            }
            if(keys[KeyEvent.VK_A]){
                p.move(-1);//moves player left
            }
            if(keys[KeyEvent.VK_Q]&&toggleable){
                p.toggleState();//switches between phone and gun if possible
                toggleable = false;
            }
            if(!keys[KeyEvent.VK_Q]){
                toggleable = true;//resets the toggle for phone/gun
            }
            
            if(keys[KeyEvent.VK_SPACE]){
                p.jump();//player jumps
            }

            if(p.getY()>1500){//checks if the player fell and kills them
                offset=0;
                p.kill();
            }
            //goes through each set of game objects and updates them
            for(GameObject go : hackables){
                go.update();
                if(checkCollision(go,p)){//checks if player touched the object
                    go.touchedByPlayer(true);
                }
                else{
                    go.touchedByPlayer(false);
                }
            }
            ArrayList<Collectable>toRemoveC=new ArrayList<Collectable>();//makes a list of items to remover
            for(Collectable c : collectables){
                c.update();
                if(c.getFree()==false){
                    toRemoveC.add(c);//adds to the list of items to remove
                }
            }
            collectables.removeAll(toRemoveC);//removes the items
            for(GameObject go : doors){
                go.update();
                if(checkCollision(go,p)){
                    go.touchedByPlayer(true);
                }
                else{
                    go.touchedByPlayer(false);
                }
            }
            ArrayList<Enemy1>toRemoveE=new ArrayList<Enemy1>();
            for(Enemy1 e : enemies){
                e.update();
                if(e.dead()==true){
                    toRemoveE.add(e);
                }
                if(e.shot()==true){//creates a new bullet if enemy shoots
                    enemyBullets.add(e.shoot(p.getX(), p.getY()));
                }
            }
            enemies.removeAll(toRemoveE);
            
            ArrayList<Bullet>toRemoveEB=new ArrayList<Bullet>();
            for(Bullet b : enemyBullets){
                b.update();
                b.collisionPlayerDetected();//checks if the bullet hit the player
                if(b.hit()){
                    toRemoveEB.add(b);
                }
            }
            enemyBullets.removeAll(toRemoveEB);

            ArrayList<Bullet>toRemove=new ArrayList<Bullet>();
            for(Bullet b : bullets){
                b.update();
                b.collisionEnemyDetected();
                if(b.hit()){
                    toRemove.add(b);
                }
            }
            bullets.removeAll(toRemove);

            for(HackBox h : hackboxes){
                h.update();
            }

            for(Laser l : lasers){
                l.update();
            }

            playerCollectableInteractions();//checks if the player touched any collectables
            p.update();//updates player

            if(mousePressed){//checks if the mouse was just pressed
                checkPlayerShooting();
                line = true;
            }

            if(mouseReleased){//checks if the mouse was just released
                line = false;
            }
            stats.update();//updates the stats bar
        }
        
        mousePressed=false;//resets the mouse pressed and released indicators
        mouseReleased=false;
        
        if(p.isDead()){//ends the game if the player is dead
            state = "over";
        }
    }
    
    public void paintComponent(Graphics g){
        if(state=="menu"){//depending on the state, draws the appropriate back image for each menu and renders and components and buttons
            g.drawImage(menuImage, 0, 0, this);
            playButton.render(g);
            helpButton.render(g);
            loreButton.render(g);
            exitButton.render(g);
            
        }
        else if(state == "help"){
            g.drawImage(helpImage, 0, 0, this);
            menuButton.render(g);
        }
        else if(state == "lore"){
            g.drawImage(loreImage, 0, 0, this);
            menuButton.render(g);
        }
        else if(state == "hack"){
            g.drawImage(hackImage, 0, 0, this);
            puzzles.get(currentPuzzle).render(g);//renders the current puzzle if hacking 
        }
        else if(state == "over"){
            g.drawImage(overImage, 0, 0, this);
            restartButton.render(g);
            exitButton2.render(g);
        }
        else if(state=="info"){
            g.drawImage(infoImage,0,0,this);
            closeButton.render(g);
        }
        
        else if(state=="game"){
        
            g.drawImage(background,offset/7,0,this);//draws the background first
            //goes through each arraylist of game objects and draws their images at the appropriate spot
            
            for(GameObject go : hackables){
                g.drawImage(go.getImage(), (int)go.getX()+offset, (int)go.getY(), this);  
            }

            for(GameObject go : doors){
                g.drawImage(go.getImage(), (int)go.getX()+offset, (int)go.getY(), this);  
            }

            for(GameObject go : collectables){
                g.drawImage(go.getImage(), (int)go.getX()+offset, (int)go.getY(), this);  
            }

            for(Enemy1 go : enemies){
                g.drawImage(go.getImage(), (int)go.getX()+offset, (int)go.getY(), this);  
            }

            for(Bullet b : bullets){
                g.drawImage(b.getImage(), (int)b.getX()+offset, (int)b.getY(),this);  
            }
            
            for(Bullet b : enemyBullets){
                g.drawImage(b.getImage(), (int)b.getX()+offset, (int)b.getY(),this);  
            }

            for(HackBox h : hackboxes){
                g.drawImage(h.getImage(), (int)h.getX()+offset, (int)h.getY(),this);  
            }

            for(Laser l : lasers){
                l.render(g);
            }

            g.drawImage(p.getImage(), p.spriteX()+offset, p.spriteY(), this);//draws player

            g.drawImage(textureImage,offset,0,this);//draws textures

            if(line&&p.getState()=="phone"){//draws a line between the player and mouse to show if you can access a hackbox
                float px = p.getCenterX();
                float py = p.getY();
                Line2D l1 = new Line2D.Float(px, py, mousex-offset, mousey-40);
                if(mouseIsConnected(l1)){//checks if connected
                    g.setColor(Color.GREEN);//green if accessable
                }
                else{
                    g.setColor(Color.RED);//red if sightline is blocked
                }
                g.drawLine((int)px+offset,(int)py,mousex,mousey-40);

            }

            stats.render(g);
        }
    }
    
    public void checkPlayerShooting(){//checks if the player is shooting and creates a new bullet going towards the mouse
        String type = p.getState();
        if(type=="gun"){
            if(p.getAmmo()>0){//doesn't shoot if theres no ammo
                bullets.add(p.shoot(mousex,mousey));
            }
        }
    }
    
    public void nextLevel(int i){
        nextLevel = i;//changes the next level to the inputted one
    }
    
    public void addOffset(int i){
        levels[level].addOffset(i);//changes the offset of the game
    }
    
    public void actionPerformed(ActionEvent evt){
        
    }
    
    public boolean mouseIsConnected(Line2D l1){//checks if theres a collision between the given line
        if(collisionLineDetected(l1)){
            return false;
        }
        return true;
    }
    
    public boolean collisionLineDetected(Line2D line){
        for(GameObject go : terrain){//goes thru all gameobjects and sees if their rectangles intersect with the line, thus blocking it off
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        for(GameObject go : hackables){
            Rectangle r1 = new Rectangle((int)go.getX(), (int)go.getY(), (int)go.getSX(), (int)go.getSY());
            if(line.intersects(r1)){
                return true;
            }
        }
        
        return false;//returns false if nothing is blocking
    }
    
    public void setKey(int code, boolean state){
        keys[code] = state;//takes in a key from the frame and changes the boolean in the array of keys
    }
    
    public void setMouse(Point p){//takes in mouse point from the frame and updates the mousex and mousey variables
        mousex=p.x;
        mousey=p.y;
    }
    public void pressed(){//sets mouse to be pressed
        mousePressed=true;
    }
    public void released(){//sets mouse to be released
        mouseReleased=true;
    }
    public int mouseX(){return mousex;}
    public int mouseY(){return mousey-40;}
    public boolean getMousePressed(){return mousePressed;}
    
    public boolean checkCollision(GameObject go1, GameObject go2){//checks if 2 GameObjects collide by comparing their boundaries
        if(go1.getTop()<go2.getBottom() && go1.getBottom()>go2.getTop() && go1.getLeft()<go2.getRight() && go1.getRight()>go2.getLeft()){
            return true;
        }
        return false;
    }
    
    public boolean checkCollision(Rectangle r, GameObject go){
        if(r.y<go.getBottom() && r.y+r.height>go.getTop() && r.x<go.getRight() && r.x+r.width>go.getLeft()){
            return true;
        }
        return false;
    }
    
    public void playerCollectableInteractions(){//checks if the player intersects with any collectables to be picked up
        for(Collectable c : collectables){
            if(playerCollides(c)){
                c.pickUp();
                p.pickUp(c);
            }
        }
    }
    
    public boolean playerCollides(GameObject go){//same as checkCollision
        return checkCollision(go,p);
    }
    
    public boolean exit(){return exit;}//returns the state of exit
    public boolean restart(){return restart;}//returns the state of restart
    
    //returns the desired array of GameObjects
    public ArrayList<GameObject> getTerrain(){
        return terrain;
    }
    public ArrayList<Enemy1> getEnemies(){
        return enemies;
    }
    public ArrayList<GameObject> getHackables(){
        return hackables;
    }
    public ArrayList<Laser> getLasers(){
        return lasers;
    }
    
    
    
    public float getPlayerX(){return p.getX();}
    public float getPlayerCenterX(){return p.getCenterX();}
    public float getPlayerY(){return p.getY();}
    public Player getPlayer(){return p;}
    
    public int getEnd(){return end;}//indicates the end of the level to stop changing the offset
    
    public void setOffset(int i){
        levels[level].setOffset(0);
    }
    public int getOffset(){return offset;}
    public Level getLevel(){return levels[level];}
    
    public void setCurrentPuzzle(int i){currentPuzzle = i;}//sets the current puzzle at the index
    public PuzzleMap getPuzzle(int i){return puzzles.get(i);}
    public void setGameState(String state){this.state = state;}//sets the state of the game
}
