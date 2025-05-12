public class Projectile extends Entity implements AutoScroller {
    
    private static final String PROJECTILE_IMAGE_FILE = "media_files/projectile.gif";
    private static final int PROJECTILE_WIDTH = 50;
    private static final int PROJECTILE_HEIGHT = 40;
    private static final int PROJECTILE_SPEED = 10;
    private static final int MAX_DISTANCE = 1000; // max travel distance

    private double directionX; // where headed
    private double directionY;
    
    private double exactX; //exact locations
    private double exactY;
    
    private int distanceTraveled = 0;
    
    // takes start and end coords
    public Projectile(int startX, int startY, int targetX, int targetY) {
        super(startX, startY, PROJECTILE_WIDTH, PROJECTILE_HEIGHT, PROJECTILE_IMAGE_FILE);
        this.exactX = startX;
        this.exactY = startY;
        
        double dx = targetX - startX;
        double dy = targetY - startY;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            this.directionX = dx / length;
            this.directionY = dy / length;
        } 
        else {
            //default straight shot if no target indicator
            this.directionX = 1.0;
            this.directionY = 0.0;
        }
    }
    
    public void scroll() {
        //update direction and speed based on where it was told to go
        exactX += directionX * PROJECTILE_SPEED;
        exactY += directionY * PROJECTILE_SPEED;
        super.setX((int) exactX);
        super.setY((int) exactY);
        distanceTraveled += PROJECTILE_SPEED;
        
        if (isOffScreen() || distanceTraveled > MAX_DISTANCE) {
            this.flagForGC(true);
        }
    }
    
    //checks for offscreen out of bounds
    private boolean isOffScreen() {
        return super.getX() < -PROJECTILE_WIDTH || super.getY() < -PROJECTILE_HEIGHT || super.getX() > 1000 || super.getY() > 1000;   
    }
    
    public int getScrollSpeed() {
        return PROJECTILE_SPEED;
    }
    
    public void setScrollSpeed(int newSpeed) {
        // placeholder
    }
}