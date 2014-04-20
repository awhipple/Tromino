/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tromino;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Aaron
 */
public class Tromino extends BasicGame {
    
    //Edit these
    private static final int SIZE_ROOT = 5;
    private static final int SCREEN_W = 800, SCREEN_H = 600;
    private static final int MIN_SCREEN_PADDING = 20;

    //Do not edit these
    private static final int GRID_SIZE = (int)Math.pow(2,SIZE_ROOT);
    private static final int GRID_TARGET_SIZE = Math.min(SCREEN_W, SCREEN_H)-MIN_SCREEN_PADDING*2;
    private static final int SQUARE_DISPLAY_SIZE = (int)(GRID_TARGET_SIZE/GRID_SIZE);
    private static final int GRID_ACTUAL_SIZE = SQUARE_DISPLAY_SIZE * GRID_SIZE;
    private static final int GRID_START_X = Math.max(MIN_SCREEN_PADDING, SCREEN_W/2-GRID_ACTUAL_SIZE/2);
    private static final int GRID_START_Y = Math.max(MIN_SCREEN_PADDING, SCREEN_H/2-GRID_ACTUAL_SIZE/2);
    
    private Color[][] grid = new Color[GRID_SIZE][GRID_SIZE];
    
    @Override
    public void init(GameContainer gc) throws SlickException {
        
        for(int dx = 0; dx < GRID_SIZE; dx++) {
            for(int dy = 0; dy < GRID_SIZE; dy++) {
                trominoRun(dx, dy);
            }
        }
        
        trominoRun((int)(Math.random()*GRID_SIZE),(int)(Math.random()*GRID_SIZE));
    }
    
    //Runs the entire puzzle with deficiency block at (dx, dy)
    public void trominoRun(int dx, int dy) {
        for(int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++) {
                grid[x][y] = Color.black;
            }
        }
        try {
            fillTromino(0, 0, GRID_SIZE, dx, dy);
        } catch (TrominoOverlapException ex) {
            System.out.println();
            System.out.println("A SQUARE WAS JUST OVERLAPPED!!!");
        }
        if(!verifyPuzzleIsSolved()) {
            System.out.println();
            System.out.println("A PUZZLE WAS NOT SOLVED CORRECTLY!!!");
        }
    }
    
    /**
     * @param x The starting x position you want to fill
     * @param y The starting y position you want to fill
     * @param s The size of the grid
     * @param dx The x location of the deficiency in the grid
     * @param dy The y location of the deficiency in the grid
     * 
     * Domain:  0 <= x < GRID_SIZE
     *          0 <= y < GRID_SIZE
     *          0 < s <= GRID_SIZE - x; s = 2^n for all n in Z
     *          x <= dx < x + s
     *          y <= dy < y + s
     */
    public void fillTromino(int x, int y, int s, int dx, int dy) throws TrominoOverlapException {
        if(s == 1) return;
        int ns = s/2;
        switch(deficiencyQuadrant(x, y, s, dx, dy)) {
            case 1: placeBLTrominoAtCenter(x, y, s);
                    fillTromino(x, y, ns, x+ns-1, y+ns-1);
                    fillTromino(x+ns, y, ns, dx, dy);
                    fillTromino(x, y+ns, ns, x+ns-1, y+ns);
                    fillTromino(x+ns, y+ns, ns, x+ns, y+ns);
                    break;
            case 2: placeBRTrominoAtCenter(x, y, s);
                    fillTromino(x, y, ns, dx, dy);
                    fillTromino(x+ns, y, ns, x+ns, y+ns-1);
                    fillTromino(x, y+ns, ns, x+ns-1, y+ns);
                    fillTromino(x+ns, y+ns, ns, x+ns, y+ns);
                    break;
            case 3: placeURTrominoAtCenter(x, y, s);
                    fillTromino(x, y, ns, x+ns-1, y+ns-1);
                    fillTromino(x+ns, y, ns, x+ns, y+ns-1);
                    fillTromino(x, y+ns, ns, dx, dy);
                    fillTromino(x+ns, y+ns, ns, x+ns, y+ns);
                    break;
            case 4: placeULTrominoAtCenter(x, y, s);
                    fillTromino(x, y, ns, x+ns-1, y+ns-1);
                    fillTromino(x+ns, y, ns, x+ns, y+ns-1);
                    fillTromino(x, y+ns, ns, x+ns-1, y+ns);
                    fillTromino(x+ns, y+ns, ns, dx, dy);
                    break;
        }
    }
    
    //Returns the (Cartesian) quadrant containing the deficient square
    public int deficiencyQuadrant(int x, int y, int s, int dx, int dy) {
        if(dx < x+s/2) {
            if(dy < y+s/2) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if(dy < y+s/2) {
                return 1;
            } else {
                return 4;
            }
        }
    }
    
    //Places the upper left tromino
    public void placeULTrominoAtCenter(int x, int y, int s) throws TrominoOverlapException {
        Color trominoColor = randomColor();
        addBlock((int)(x+s/2-1),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2-1),(int)(y+s/2),trominoColor);
    }
    
    //Places the upper right tromino
    public void placeURTrominoAtCenter(int x, int y, int s) throws TrominoOverlapException {
        Color trominoColor = randomColor();
        addBlock((int)(x+s/2-1),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2),(int)(y+s/2),trominoColor);
    }
    
    //Places the bottom left tromino
    public void placeBLTrominoAtCenter(int x, int y, int s) throws TrominoOverlapException {
        Color trominoColor = randomColor();
        addBlock((int)(x+s/2-1),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2-1),(int)(y+s/2),trominoColor);
        addBlock((int)(x+s/2),(int)(y+s/2),trominoColor);
    }
    
    //Places the bottom right tromino
    public void placeBRTrominoAtCenter(int x, int y, int s) throws TrominoOverlapException {
        Color trominoColor = randomColor();
        addBlock((int)(x+s/2),(int)(y+s/2-1),trominoColor);
        addBlock((int)(x+s/2-1),(int)(y+s/2),trominoColor);
        addBlock((int)(x+s/2),(int)(y+s/2),trominoColor);
    }

    //Activates a single block on the grid
    public void addBlock(int x, int y, Color col) throws TrominoOverlapException {
        if(grid[x][y] != Color.black) throw new TrominoOverlapException();
        grid[x][y] = col;
    }
    
    @Override
    public void render(GameContainer gc, Graphics grphcs) throws SlickException {
        grphcs.setColor(Color.white);
        grphcs.fillRect(0, 0, SCREEN_W, SCREEN_H);
        for(int y = 0; y < GRID_SIZE; y++) {
            for(int x = 0; x < GRID_SIZE; x++) {
                grphcs.setColor(Color.black);
                grphcs.drawRect(x*SQUARE_DISPLAY_SIZE+GRID_START_X,
                                y*SQUARE_DISPLAY_SIZE+GRID_START_Y,
                                SQUARE_DISPLAY_SIZE,
                                SQUARE_DISPLAY_SIZE);
                grphcs.setColor(grid[x][y]);
                grphcs.fillRect(x*SQUARE_DISPLAY_SIZE+GRID_START_X+1,
                                y*SQUARE_DISPLAY_SIZE+GRID_START_Y+1,
                                SQUARE_DISPLAY_SIZE-1,
                                SQUARE_DISPLAY_SIZE-1);
                if(grid[x][y] == Color.black) {
                    int sl = x*SQUARE_DISPLAY_SIZE+GRID_START_X+1;
                    int sr = sl+SQUARE_DISPLAY_SIZE;
                    int st = y*SQUARE_DISPLAY_SIZE+GRID_START_Y+1;
                    int sb = st+SQUARE_DISPLAY_SIZE;
                    
                    grphcs.setColor(Color.red);
                    grphcs.drawLine(sl, st, sr, sb);
                    grphcs.drawLine(sr, st, sl, sb);
                    
                }
            }
        }
        grphcs.flush();
    }
    
    //This verification only looks to make sure there is only a single deficient square remaining.
    //Overlapping trominoes cause an exception that we display in the console, and the 
    //place tromino functions were manually tested to ensure they always place a single correct tromino.
    //Trominoes placed outside the grid will also cause an ArrayIndexOutOfBounds exception.
    public boolean verifyPuzzleIsSolved() {
        boolean foundDeficientBlock = false;
        for(int x = 0; x < GRID_SIZE; x++) {
            for(int y = 0; y < GRID_SIZE; y++) {
                if(grid[x][y] == Color.black) {
                    foundDeficientBlock = !foundDeficientBlock;
                    if(!foundDeficientBlock) return false;
                }
            }
        }
        return foundDeficientBlock;
    }
    
    public Color randomColor() {
        return new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
    }
    
    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        
    }
    
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Tromino());
            app.setDisplayMode(SCREEN_W, SCREEN_H, false);
            app.setTargetFrameRate(30);
            app.start();
        } catch (SlickException ex) {
            ex.printStackTrace();
        }
    }
    
    public Tromino() {
        super("Tromino");
    }
    
    private class TrominoOverlapException extends Exception {}
}
