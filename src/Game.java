
import java.awt.MouseInfo;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import sun.audio.AudioPlayer;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamza
 */
public class Game extends JFrame implements ActionListener,MouseListener,KeyListener{
    
    GamePanel game;
    
    javax.swing.Timer myTimer;
    
    public Game(){
        super("Watch_dogs");//inits frame
        setSize(1620, 1080+88);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        
    	myTimer = new javax.swing.Timer(33,this);//new timer at 30fps
        
    	setResizable(false);
        start();
    }
    
    public void start(){
        addKeyListener(this);
        addMouseListener(this);
        myTimer.start();//starts timer and creates a new game
        
        game=new GamePanel();
        
        add(game);
        
        
        setVisible(true);
    }
    
    
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource().equals(myTimer)){//updates game every timer tick 
            if (game != null){
                if(game.restart()){
                    this.remove(game);//deletes and creates a new game 
                    start();
                }
                if(game.exit()){
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));//closes window on exit
                }
                
                game.setMouse(MouseInfo.getPointerInfo().getLocation());
                game.refresh();
                game.repaint();
            }
        }
    }
    
    public void paintComponent(){
    
    }
    public void mousePressed(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1){
            game.pressed();
       }
    }	
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){
        if(e.getButton()==MouseEvent.BUTTON1){
            game.released(); 
        }
    }
    public void mouseClicked(MouseEvent e){
       
    }
    
    public void keyPressed(KeyEvent e){
        game.setKey(e.getKeyCode(),true);
        
    }
    
    public void keyReleased(KeyEvent e){
        game.setKey(e.getKeyCode(),false);
    }
    
    public void keyTyped(KeyEvent e){}
    
}
