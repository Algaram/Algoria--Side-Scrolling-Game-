import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RamirezGame extends SimpleGame {
    
    private static final String GAME_SPLASH_FILE = "media_files/ramirez_splash.gif";
    private static final String INSTRUCTIONS_SPLASH_FILE = "media_files/instructions_splash.gif";
    private static final String GAME_TITLE = "Algoria";
    private boolean firstSplashShown = false;
    
    private static final int SCORE_TO_WIN = 400;
    private static final int SPAWN_INTERVAL = 45;
    
    //spawn parameters
    private static final int MAX_ENTITIES_PER_SPAWN = 4;
    private static final int MAX_SPAWN_ATTEMPTS = 5;
    private static final int MIN_SPAWN_Y = 25;
    
    //entitie
    private static final int POWERUP_SPAWN_RATE = 10; // 10% chance
    private static final int ASTEROID_SPAWN_RATE = 40; // 40% chance
    private static final int MY_AVOID_SPAWN_RATE = 40; // 40% chance
    private static final int MY_COLLECT_SPAWN_RATE = 40; // 40% chance
    private static final int MY_SPECIAL_AVOID_SPAWN_RATE = 10; // 10% chance
    private static final int MY_SPECIAL_COLLECT_SPAWN_RATE = 10; // 10% chance
    
    private static final int TOTAL_SPAWN_RATE = MY_AVOID_SPAWN_RATE + MY_COLLECT_SPAWN_RATE + MY_SPECIAL_AVOID_SPAWN_RATE + MY_SPECIAL_COLLECT_SPAWN_RATE + POWERUP_SPAWN_RATE + ASTEROID_SPAWN_RATE;
    
    //powerup updaters
    private boolean isPowerUpActive = false;
    private PowerUp.PowerUpType activePowerUpType = null;
    private int powerUpDuration = 0;
    private int powerUpTickCounter = 0;
    
    //mouse click
    private int lastClickX = 0;
    private int lastClickY = 0;
    private boolean targetActive = false;
    
    //manipulate shooting cooldowns
    private int projectileCooldown = 0;
    private static final int MAX_PROJECTILE_COOLDOWN = 100; //ticks
    
    //manipulate score multipliers
    private int scoreMultiplier = 1;
    
    
    public RamirezGame() {
        super();
    }
    
    public RamirezGame(int gameWidth, int gameHeight) {
        super(gameWidth, gameHeight);
    }
    
    
    protected void pregame() {
        this.setBackgroundColor(Color.BLACK);
        super.pregame();
        super.setSplashImg(GAME_SPLASH_FILE);
        super.setBackgroundImg("media_files/space_background.gif");
        player.setImg("media_files/spaceship.gif");
    }
        
    protected void gameUpdate() {
        super.gameUpdate();
        // add own tick rates
        // check for active powerup
        if (isPowerUpActive) {
            powerUpTickCounter++;
            applyPowerUpEffects();
            
            // check coldown if time is over
            if (powerUpTickCounter >= powerUpDuration) {
                deactivatePowerUp();
            }
        }
        
        //projectile cooldown
        if (projectileCooldown > 0) {
            projectileCooldown--;
        }
    
        checkProjectileCollisions();
    
        if (isPowerUpActive && activePowerUpType == PowerUp.PowerUpType.MAGNET) {
            applyMagnetEffect();
        }
    }
    

    private void applyPowerUpEffects() {
        if (activePowerUpType == null) return;
        //switch case for efficiency, basically better if/else if statements
        // switch is our start, case separates the if statements
        switch (activePowerUpType) {
            case SHIELD:
                //logic in collision detection
                break;
            case SCORE_BOOST:
                //logic in collision with collectibles
                scoreMultiplier = 2;
                break;
            case MAGNET:
                //magnet has own logic
                break;
        }
    }
    
    //magnet effect
    private void applyMagnetEffect() {
        for (int i = 0; i < toDraw.size(); i++) {
            Entity e = toDraw.get(i);
            
            //only affecting collect entities
            if (e instanceof Collect) {
                //check distance
                int dx = player.getX() + player.getWidth()/2 - (e.getX() + e.getWidth()/2);
                int dy = player.getY() + player.getHeight()/2 - (e.getY() + e.getHeight()/2);
                double distance = Math.sqrt(dx*dx + dy*dy);
                
                //if in proper range = pull
                if (distance < 150) {
                    double dirX = dx / distance;
                    double dirY = dy / distance;
                    
                    // increasing speed as it gets closer to us
                    int magnetSpeed = (int)(15 * (1 - distance/150));
                    e.setX(e.getX() + (int)(dirX * magnetSpeed));
                    e.setY(e.getY() + (int)(dirY * magnetSpeed));
                }
            }
        }
    }
    
    
    private void checkProjectileCollisions() {
        for (int i = 0; i < toDraw.size(); i++) {
            Entity e = toDraw.get(i);
            
            if (e instanceof Projectile) {
                Collection<Entity> collisions = findAllCollisions(e);
                
                for (Entity hit : collisions) {
                    //only destroys avoid entities
                    if (hit instanceof Avoid) {
                        score += 5;
                        hit.flagForGC(true); // flags the avoid entity that it has to be removed
                        e.flagForGC(true); // flags projectile to be removed
                        createExplosion(hit.getX(), hit.getY()); // explosion start when collides 
                        break; //ensures each projectile only hits one entity
                    }
                }
            }
        }
    }
    
   
    private void createExplosion(int x, int y) {
        // instantiates temp entity for animation
        ExplosionEffect explosion = new ExplosionEffect(x, y);
        toDraw.add(explosion);
    }
    
   
    
    protected void reactToKeyPress(int key) {
        // Check if a splash screen is showing
        if (getSplashImg() != null) {
            // If the advance key is pressed
            if (key == ADVANCE_SPLASH_KEY) {
                // If we're on the first splash screen
                if (!firstSplashShown) {
                    // Show the instructions splash screen
                    super.setSplashImg(INSTRUCTIONS_SPLASH_FILE);
                    firstSplashShown = true;
                    return; // Return early to prevent other key processing
                }
                // Otherwise, continue with normal splash handling
            }
        }
        
        // Continue with the existing method implementation
        super.reactToKeyPress(key);
        if (!isPaused && getSplashImg() == null) {
            //space fires projectile
            if (key == KeyEvent.VK_SPACE) {
                if (projectileCooldown <= 0) {
                    fireProjectile(player.getX() + player.getWidth(), player.getY() + player.getHeight()/2, player.getX() + player.getWidth() + 100, player.getY() + player.getHeight()/2);
                    projectileCooldown = MAX_PROJECTILE_COOLDOWN;
                }
            }
            
            // F activates shield
            if (key == KeyEvent.VK_F) {
                if (!isPowerUpActive) {
                    activatePowerUp(PowerUp.PowerUpType.SHIELD, 150);
                }
            }
            
            // G activates magnet
            if (key == KeyEvent.VK_G) {
                if (!isPowerUpActive) {
                    activatePowerUp(PowerUp.PowerUpType.MAGNET, 150);
                }
            }
        }
    }
    
    
    protected MouseEvent reactToMouseClick(MouseEvent click) {
        if (!isPaused && getSplashImg() == null) {
            //sotores position of mouse click
            lastClickX = click.getX();
            lastClickY = click.getY();
            createTargetIndicator(lastClickX, lastClickY);
            
            //fire if cooldown fixed
            if (projectileCooldown <= 0) {
                fireProjectile(player.getX() + player.getWidth(), player.getY() + player.getHeight()/2, lastClickX, lastClickY);
                projectileCooldown = MAX_PROJECTILE_COOLDOWN;
            }
        }
        return click;
    }
    
    private void createTargetIndicator(int x, int y) {
        //temp entity to create targeting animation
        TargetIndicator target = new TargetIndicator(x, y);
        super.toDraw.add(target);
    }
    
    //fires projectile towards target indicator
    private void fireProjectile(int startX, int startY, int targetX, int targetY) {
        Projectile proj = new Projectile(startX, startY, targetX, targetY);     
        super.toDraw.add(proj);
    }
    
   
    private void activatePowerUp(PowerUp.PowerUpType type, int duration) {
        isPowerUpActive = true;
        activePowerUpType = type;
        powerUpDuration = duration;
        powerUpTickCounter = 0;
        //changes appearance of the ship based off of what powerup has been used
        //again swtich case for efficiency and cleaner code
        if (type != null) {
            switch (type) {
                case SHIELD:
                    player.setImg("media_files/shielded_spaceship.gif");
                    break;
                case MAGNET:
                    player.setImg("media_files/magnet_spaceship.gif");
                    break;
                case SCORE_BOOST:
                    player.setImg("media_files/boost_spaceship.gif");
                    scoreMultiplier = 2;
                    break;
            }
        }
    }
    
    private void deactivatePowerUp() {
        isPowerUpActive = false;
        activePowerUpType = null;
        //resets ship appearance
        player.setImg("media_files/spaceship.gif");
        //resets attributes
        scoreMultiplier = 1;
    }
    

    protected void checkAndHandleCollisions() {
        Collection<Entity> collisions = findAllCollisions(player);
        
        for (Entity e : collisions) {
            //power up collision
            if (e instanceof PowerUp) {
                PowerUp powerUp = (PowerUp) e;
                activatePowerUp(powerUp.getType(), powerUp.getDuration());
                score += powerUp.getScoreChange();
                if (score < 0) score = 0;
                // Remove the power-up
                e.flagForGC(true);
            } 
            else if (e instanceof CollisionReactive) {
                CollisionReactive cr = (CollisionReactive) e;
                //checks for immunity granted if shield is on
                if (isPowerUpActive && activePowerUpType == PowerUp.PowerUpType.SHIELD && 
                    e instanceof Avoid) {
                    e.flagForGC(true); //remove avoid entity if collided wiht shield
                    continue;
                }
                
                // Apply score changes with multiplier if applicable
                int scoreChange = cr.getScoreChange();
                if (scoreChange > 0 && isPowerUpActive && 
                    activePowerUpType == PowerUp.PowerUpType.SCORE_BOOST) {
                    scoreChange *= scoreMultiplier;
                }
                
                score += scoreChange;
                if (score < 0) score = 0;
                
                // Apply HP changes
                player.modifyHP(cr.getHPChange());
                
                // Remove entity if collided
                e.flagForGC(true);
            }
        }
    }
    
    //create shield break animation here?
    
    
    protected void updateTitleText() {
        //constantly update based on the game board
        String titleText = GAME_TITLE + " | Score: " + score + " / " + SCORE_TO_WIN + " | HP: " + player.getHP();
        if (isPowerUpActive && activePowerUpType != null) {
            titleText += " | " + activePowerUpType + " ACTIVE: " + (powerUpDuration - powerUpTickCounter) + " ticks remaining";
        }
        if (projectileCooldown > 0) {
            titleText += " | Weapon Cooldown: " + projectileCooldown;
        } 
        else {
            titleText += " | Weapon Ready";
        }
        setTitle(titleText);
    }
    
    
    protected void performSpawning() {
        int spawnX = super.getWindowWidth(); // spawn side for all entities
        int entitiesToSpawn = rand.nextInt(MAX_ENTITIES_PER_SPAWN) + 1; // random # of spawned entities (1 to MAX_ENTITIES_PER_SPAWN)
        ArrayList<Entity> newEntities = new ArrayList<>(); // accounts for collisions
        
        //spawning logic
        for (int i = 0; i < entitiesToSpawn; i++) {
            boolean foundValidPosition = false; // checks all spots for open space
            for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS && !foundValidPosition; attempt++) {
                int maxY = super.getWindowHeight() - 100; //ensure space is still at bottom
                int spawnY = rand.nextInt(maxY - MIN_SPAWN_Y) + MIN_SPAWN_Y;
                
                //spawn entity when all conditions created
                Entity newEntity = createRandomEntity(spawnX, spawnY);
                //checks if spawned entities are already there
                boolean hasCollision = false;
                for (Entity existing : newEntities) {
                    if (newEntity.isCollidingWith(existing)) {
                        hasCollision = true;
                        break;
                    }
                }
                
                //cycles to next iteration if collision isn't a problem
                if (!hasCollision) {
                    newEntities.add(newEntity);
                    super.toDraw.add(newEntity);
                    foundValidPosition = true;
                }
            }
        }
    }

    private Entity createRandomEntity(int x, int y) {
        int randomValue = rand.nextInt(TOTAL_SPAWN_RATE);
        int currentThreshold = 0;
        //uses 0 to TOTAL_SPAWN_RATE to determine the random rate
        //determines which entity to spawn based on random value given
        currentThreshold += MY_AVOID_SPAWN_RATE;
        if (randomValue < currentThreshold) {
            return new Avoid(x, y);
        }
        currentThreshold += MY_COLLECT_SPAWN_RATE;
        if (randomValue < currentThreshold) {
            return new Collect(x, y);
        }  
        currentThreshold += MY_SPECIAL_AVOID_SPAWN_RATE;
        if (randomValue < currentThreshold) {
            return new SpecialAvoid(x, y);
        }
        currentThreshold += MY_SPECIAL_COLLECT_SPAWN_RATE;
        if (randomValue < currentThreshold) {
            return new SpecialCollect(x, y);
        }
        currentThreshold += POWERUP_SPAWN_RATE;
        if (randomValue < currentThreshold) {
            return new PowerUp(x, y);
        } 
        else {
            //asteroid is spawned as an else (moving special avoid)
            Avoid asteroid = new Avoid(x, y, "media_files/asteroid.gif");
            asteroid.setScrollSpeed(3 + rand.nextInt(4)); //diff speeds
            return asteroid;
        }
    }
    
    
    //game over conditions!!!
    protected boolean checkForGameOver() {
        //win by target score
        if (score >= SCORE_TO_WIN) {
            return true;
        }
        
        //lose by being bad lol
        if (player.getHP() <= 0) {
            return true;
        }
        
        return false;
    }
    
    protected void postgame() {
        if (score >= SCORE_TO_WIN) {
            super.setTitle(GAME_TITLE + " - You Win! Final Score: " + score);
            super.setSplashImg("media_files/victory_splash.gif");
        } 
        else {
            super.setTitle(GAME_TITLE + " - Game Over! Final Score: " + score);
            super.setSplashImg("media_files/gameover_splash.gif");
        }
    }
    

    //target indicator animation class
    private class TargetIndicator extends Entity implements AutoScroller {
        private static final int TARGET_WIDTH = 50;
        private static final int TARGET_HEIGHT = 50;
        private static final String TARGET_IMAGE = "media_files/target.gif";
        private static final int TARGET_DURATION = 30; // ticks
        private int ticksExisted = 0;
        
        public TargetIndicator(int x, int y) {
            super(x - TARGET_WIDTH/2, y - TARGET_HEIGHT/2, TARGET_WIDTH, TARGET_HEIGHT, TARGET_IMAGE);
        }
        
        public int getScrollSpeed() {
            return 0;
        }
        
        public void setScrollSpeed(int newSpeed) {
            // placeholder
        }   
        
        public void scroll() {
            ticksExisted++;
            
            //remove after animation
            if (ticksExisted >= TARGET_DURATION) {
                this.flagForGC(true);
            }
        }
    }
    
    //explosion animation class
    private class ExplosionEffect extends Entity implements AutoScroller {
        private static final int EXPLOSION_WIDTH = 75;
        private static final int EXPLOSION_HEIGHT = 75;
        private static final String EXPLOSION_IMAGE = "media_files/explosion.gif";
        private static final int EXPLOSION_DURATION = 20; // ticks
        private int ticksExisted = 0;
        
        public ExplosionEffect(int x, int y) {
            super(x, y, EXPLOSION_WIDTH, EXPLOSION_HEIGHT, EXPLOSION_IMAGE);
        }
        
        public int getScrollSpeed() {
            return 0;
        }
        
        public void setScrollSpeed(int newSpeed) {
            // placeholder
        }
        
        public void scroll() {
            ticksExisted++;
            // remove after animation is done
            if (ticksExisted >= EXPLOSION_DURATION) {
                this.flagForGC(true);
            }
        }
    }
    
    // shield effect animation class
    private class ShieldEffect extends Entity implements AutoScroller {
        private static final int SHIELD_EFFECT_WIDTH = 75;
        private static final int SHIELD_EFFECT_HEIGHT = 75;
        private static final String SHIELD_EFFECT_IMAGE = "media_files/shield_impact.gif";
        private static final int SHIELD_EFFECT_DURATION = 50; // ticks
        private int ticksExisted = 0;
        
        public ShieldEffect(int x, int y) {
            super(x, y, SHIELD_EFFECT_WIDTH, SHIELD_EFFECT_HEIGHT, SHIELD_EFFECT_IMAGE);
        }
         
        public int getScrollSpeed() {
            return 0; 
        }
           
        public void setScrollSpeed(int newSpeed) {
            // placeholder
        }
        
        public void scroll() {
            ticksExisted++;
            // remove after animation
            if (ticksExisted >= SHIELD_EFFECT_DURATION) {
                this.flagForGC(true);
            }
        }
    }
}