public class PowerUp extends Entity implements CollisionReactive, AutoScroller {
    
    private static final String POWERUP_IMAGE_FILE = "media_files/powerup.gif";
    private static final int POWERUP_WIDTH = 40;
    private static final int POWERUP_HEIGHT = 40;
    private static final int POWERUP_DEFAULT_SCROLL_SPEED = 4;
    private static final int POWERUP_DURATION = 150;
    private PowerUpType type;
    private int scrollSpeed = POWERUP_DEFAULT_SCROLL_SPEED;
    
    //enum subs for final vars, allows for cleaner code and easy switch case usage. good for if i want to add more powerups
    public enum PowerUpType {
        SHIELD, MAGNET, SCORE_BOOST 
    }
    
    public PowerUp() {
        this(0, 0);
    }
    
    public PowerUp(int x, int y) {
        this(x, y, POWERUP_IMAGE_FILE);
    }
    
    public PowerUp(int x, int y, String imageName) {
        this(x, y, POWERUP_WIDTH, POWERUP_HEIGHT, imageName);
    }
    
    public PowerUp(int x, int y, int width, int height, String imageName) {
        super(x, y, width, height, imageName);
        
        //randomly choose a power up to assign to the next spawn value
        PowerUpType[] types = PowerUpType.values();
        this.type = types[SimpleGame.rand.nextInt(types.length)];
        
        //diff immages for diff powerups
        switch(this.type) {
            case SHIELD:
                this.setImg("media_files/shield_powerup.gif");
                break;
            case MAGNET:
                this.setImg("media_files/magnet_powerup.gif");
                break;
            case SCORE_BOOST:
                this.setImg("media_files/score_powerup.gif");
                break;
        }
    }
    
    public PowerUpType getType() {
        return this.type;
    }
    
    public int getDuration() {
        return POWERUP_DURATION;
    }
    
    public int getScrollSpeed() {
        return this.scrollSpeed;
    }
    
    
    public void setScrollSpeed(int newSpeed) {
        this.scrollSpeed = newSpeed;
    }
    
    public void scroll() {
        setX(getX() - this.scrollSpeed);
    }
    
    public int getScoreChange() {
        return 10; // 10 for each powerup
    }
    
    public int getHPChange() {
        return 0;
    }
}