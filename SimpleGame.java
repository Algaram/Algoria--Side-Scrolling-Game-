import java.awt.*;
import java.awt.event.*;
import java.util.*;

//A Simple version of the scrolling game, featuring Avoids, Collects, SpecialAvoids, and SpecialCollects
//Players must reach a score threshold to win.
//If player runs out of HP (via too many Avoid/SpecialAvoid collisions) they lose.
public class SimpleGame extends SSGEngine {
    
    
    //Starting Player coordinates
    protected static final int STARTING_PLAYER_X = 0;
    protected static final int STARTING_PLAYER_Y = 100;
    
    //Score needed to win the game
    protected static final int SCORE_TO_WIN = 300;
    
    //Maximum that the game speed can be increased to
    //(a percentage, ex: a value of 300 = 300% speed, or 3x regular speed)
    protected static final int MAX_GAME_SPEED = 300;
    //Interval that the speed changes when pressing speed up/down keys
    protected static final int SPEED_CHANGE_INTERVAL = 20;    
    
    public static final String INTRO_SPLASH_FILE = "media_files/splash.gif";        
    //Key pressed to advance past the splash screen
    public static final int ADVANCE_SPLASH_KEY = KeyEvent.VK_ENTER;
    
    //Interval that Entities get spawned in the game window
    //ie: once every how many ticks does the game attempt to spawn new Entities
    protected static final int SPAWN_INTERVAL = 45;

    
    //A Random object for all your random number generation needs!
    public static final Random rand = new Random();
    
    //player's current score
    protected int score;
    
    
    //Stores a reference to game's Player object for quick reference (Though this Player presumably
    //is also in the DisplayList, but it will need to be referenced often)
    protected Player player;

    //added vars
    private static final int AVOID_SPAWN_RATE = 40; //40% chance
    private static final int COLLECT_SPAWN_RATE = 40; //40% chance
    private static final int SPECIAL_AVOID_SPAWN_RATE = 10; //10% chance
    private static final int SPECIAL_COLLECT_SPAWN_RATE = 10; //10% chance
    private static final int TOTAL_SPAWN_RATE = AVOID_SPAWN_RATE + COLLECT_SPAWN_RATE + SPECIAL_AVOID_SPAWN_RATE + SPECIAL_COLLECT_SPAWN_RATE;
    private static final int MIN_SPAWN_Y = 25; // min y for spawning
    private static final int MAX_ENTITIES_PER_SPAWN = 4; // max entities
    private static final int MAX_SPAWN_ATTEMPTS = 5; //helps spawn entities

    
    
    public SimpleGame(){
        super();
    }
    
    public SimpleGame(int gameWidth, int gameHeight){
        super(gameWidth, gameHeight);
    }
    
    
    //Performs all of the initialization operations that need to be done before the game starts
    protected void pregame(){
        this.setBackgroundColor(Color.BLACK);
        this.player = new Player(STARTING_PLAYER_X, STARTING_PLAYER_Y);
        this.toDraw.add(player); 
        this.score = 0;
        super.setSplashImg(INTRO_SPLASH_FILE);
    }
    
    //Called on each game tick
    protected void gameUpdate(){
        //scroll all AutoScroller Entities on the game board
        performScrolling();   
        //spawn new per interval
        if (super.getTicksElapsed() % SPAWN_INTERVAL == 0){
            performSpawning();
            gcOffscreenEntities();
        }
        updateTitleText();
        checkAndHandleCollisions();
        
    }
    

    //Update the text at the top of the game window
    protected void updateTitleText(){
        setTitle("Score: " + score + " // HP: " + player.getHP() + " // Speed: " + getUniversalSpeed());
    }
    

    //Scroll all AutoScroller entities per their respective scroll speeds
    protected void performScrolling(){

        //****   Implement me!   ****
        for(int i = 0; i < toDraw.size(); i++){ // lopps through entities
            Entity e = toDraw.get(i);
            //check for scroll
            if(e instanceof AutoScroller){
                ((AutoScroller)e).scroll();
            }
        }
    }

    
    //Handles "garbage collection" of the entities
    //Flags entities in the displaylist that are no longer relevant
    //(i.e. will no longer need to be drawn in the game window).
    protected void gcOffscreenEntities(){

        //****   Implement me!   ****
        for(int i = 0; i < toDraw.size(); i++){ // once entities leave remove
            Entity e = toDraw.get(i);
            if(e != player && e.getX() + e.getWidth() < 0){ // checks if enttiy is player
                e.flagForGC(true); // Flag for garbage collection
            }
        }
    }
    
    
    
    //Spawn new Entities on the right edge of the game board
    protected void performSpawning() {
        // Determine the right edge of the screen (where entities will spawn)
        int spawnX = super.getWindowWidth();
        
        // Randomly decide how many entities to spawn (1 to MAX_ENTITIES_PER_SPAWN)
        int entitiesToSpawn = rand.nextInt(MAX_ENTITIES_PER_SPAWN);
        
        // Keep track of spawned entities to check for collisions
        ArrayList<Entity> newEntities = new ArrayList<>();
        
        // Try to spawn the requested number of entities
        for (int i = 0; i < entitiesToSpawn; i++) {
            // Try several positions to find one that doesn't collide
            boolean foundValidPosition = false;
            
            for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS && !foundValidPosition; attempt++) {
                // Generate a random Y position within the game window
                int maxY = super.getWindowHeight() - 100; // Leave some space at the bottom
                int spawnY = rand.nextInt(maxY - MIN_SPAWN_Y) + MIN_SPAWN_Y;
                
                // Create a new entity based on random weighted selection
                Entity newEntity = createRandomEntity(spawnX, spawnY);
                
                // Check if this new entity would collide with any previously spawned entities
                boolean hasCollision = false;
                for (Entity existing : newEntities) {
                    if (newEntity.isCollidingWith(existing)) {
                        hasCollision = true;
                        break;
                    }
                }
                
                // If no collision, add the entity and move to the next one
                if (!hasCollision) {
                    newEntities.add(newEntity);
                    super.toDraw.add(newEntity);
                    foundValidPosition = true;
                }
            }
        }
    }

    // Helper method to create a random entity based on weighted probabilities
    private Entity createRandomEntity(int x, int y) {
        // Generate a random number between 0 and TOTAL_SPAWN_RATE
        int randomValue = rand.nextInt(TOTAL_SPAWN_RATE);
        
        // Determine which type of entity to spawn based on the random value
        if (randomValue < AVOID_SPAWN_RATE) {
            return new Avoid(x, y);
        } else if (randomValue < AVOID_SPAWN_RATE + COLLECT_SPAWN_RATE) {
            return new Collect(x, y);
        } else if (randomValue < AVOID_SPAWN_RATE + COLLECT_SPAWN_RATE + SPECIAL_AVOID_SPAWN_RATE) {
            return new SpecialAvoid(x, y);
        } else {
            return new SpecialCollect(x, y);
        }
    }
    
    //Called once the game is over, performs any end-of-game operations
    protected void postgame(){
        
        if(score >= SCORE_TO_WIN){
            super.setTitle("Game Over You Win!");
        }
        else{
            super.setTitle("Game Over You Lose!");
        }
    }
    
    //Returns a boolean indicating if the game is over (true) or not (false)
    //Game can be over due to either a win or lose state
    protected boolean checkForGameOver(){

        //****   placeholder... Implement me!   ****

        // win game
        if(score >= SCORE_TO_WIN){
            return true;
        }
        
        // lost game
        if(player.getHP() <= 0){
            return true;
        }
        
        return false;

    }
    
    //Reacts to a single key press on the keyboard
    protected void reactToKeyPress(int key){
        
        //if a splash screen is up, only react to the advance splash key
        if (getSplashImg() != null){
            if (key == ADVANCE_SPLASH_KEY)
                super.setSplashImg(null);
            return;
        }

        // paiuse game
        if(key == KEY_PAUSE_GAME){
            isPaused = !isPaused;
            return;
        }
        
        // speed change
        if(key == SPEED_UP_KEY){
            int currentSpeed = getUniversalSpeed();
            if(currentSpeed < MAX_GAME_SPEED){
                setUniversalSpeed(currentSpeed + SPEED_CHANGE_INTERVAL);
            }
        }
        else if(key == SPEED_DOWN_KEY){
            int currentSpeed = getUniversalSpeed();
            if(currentSpeed > SPEED_CHANGE_INTERVAL){
                setUniversalSpeed(currentSpeed - SPEED_CHANGE_INTERVAL);
            }
        }
        
        // movements (checks for borders too)
        if(!isPaused){
            if(key == UP_KEY){
                // up
                int newY = Math.max(0, player.getY() - player.getMoveSpeed());
                player.setY(newY);
            }
            else if(key == DOWN_KEY){
                // down
                int newY = Math.min(getWindowHeight() - player.getHeight(), 
                                player.getY() + player.getMoveSpeed());
                player.setY(newY);
            }
            else if(key == LEFT_KEY){
                // left
                int newX = Math.max(0, player.getX() - player.getMoveSpeed());
                player.setX(newX);
            }
            else if(key == RIGHT_KEY){
                // right
                int newX = Math.min(getWindowWidth() - player.getWidth(), 
                                player.getX() + player.getMoveSpeed());
                player.setX(newX);
            }
        }
    }

    private void checkAndHandleCollisions(){
        Collection<Entity> collisions = findAllCollisions(player);
        
        for(Entity e : collisions){
            if(e instanceof CollisionReactive){
                CollisionReactive cr = (CollisionReactive)e;
                
                //change score but not allowing negatives
                score += cr.getScoreChange();
                if(score < 0) score = 0; 
                
                //changes hp
                player.modifyHP(cr.getHPChange());
                
                //removes entity if collided
                e.flagForGC(true);
            }
        }
    }
    
    
    
    //Handles reacting to a single mouse click in the game window
    protected MouseEvent reactToMouseClick(MouseEvent click){
       
        //Mouse functionality is not used at all in the Starter game...
        //you may want to override this function for a CreativeGame feature though!

        return click;//returns the mouse event for any child classes overriding this method
    }

    
    
    
    
}
