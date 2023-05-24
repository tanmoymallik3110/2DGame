
package main;

import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import object.SuperObject;
import tile.TileManager;


public class GamePanel extends JPanel implements Runnable{
     
    //SCREEN SETTING
    
    final int originalTileSize = 16;                      //16 x 16 tiles
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;        //48 x 48 tiles
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;      //768 pixel
    public final int screenHeight = tileSize * maxScreenRow;     //576 pixel
    
    //world setting
    
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    

    //FPS
    int FPS = 60;
    
    //SYSTEM
    
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;
    
    //ENTITY AND OBJECT
    
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];
    
    
    
    
    
    //set player deafult position and speed
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    
    
    public GamePanel(){
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); 
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        }

    
    public void setupGame()
    {
        
        aSetter.setObject();
        
        playMusic(0);
        
    }
    
    
    public void startgameThread(){
        
        gameThread = new Thread((Runnable) this);
        gameThread.start();
    }
    
    public void run()
    {
        
        double drawInterval = 1000000000/FPS;       //0.166666 seconds  
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while(gameThread != null){ 
            
            //Update: Update information such as character position
            update();
            
            //Paint: Draw screen with update information
            repaint();
            
            
            try {
                
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                
                if(remainingTime < 0)
                {
                    
                    remainingTime = 0;
                }
                        
                Thread.sleep((long) remainingTime);
                
                nextDrawTime += drawInterval;
            } 
            catch (InterruptedException ex) {
                
                Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void update(){
        
       player.update();
    }
    
    public void paintComponent(Graphics g){
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        //draw tile
        tileM.draw(g2);
             
        //object
        for(int i = 0; i < obj.length; i++)
        {
            
            if(obj[i] != null)
            {
                
                obj[i].draw(g2, this);
            }
        }
        
        //draw player
        player.draw(g2); 
        
        //draw UI
        ui.draw(g2);
        
        g2.dispose();
    }

    
    public void playMusic(int i)
    {
        
        music.setFile(i);
        music.play();
        music.loop();
    }
    
    public void stopMusic()
    {
        
        music.stop();
    }
    
    public void playerSE(int i)
    {
        
        se.setFile(i);
        se.play();
    }


}







