import bagel.Image;

/**
 * LifeBar class to record the health status of the bird.
 */
public class LifeBar {
    private final Image FULL_HEART = new Image("res/level/fullLife.png");
    private final Image EMPTY_HEART = new Image("res/level/noLife.png");
    private int fullHealth;
    private int currLife;
    private final int LEVEL0_HEALTH = 3;
    private final double HEART_Y = 15;
    private double initHeartX = 100;
    private final double DIST_HEART = 50;

    /**
     * Constructor of the LifeBar class
     */
    public LifeBar() {
        this.fullHealth = LEVEL0_HEALTH;
        this.currLife = LEVEL0_HEALTH;
    }

    /**
     * Displays the life status at the top left window.
     */
    public void update() {
        double heartX = initHeartX;
        for (int i=0; i<currLife; i++) {
            FULL_HEART.drawFromTopLeft(heartX, HEART_Y);
            heartX += DIST_HEART;
        }
        for (int j=0; j<(fullHealth-currLife); j++) {
            EMPTY_HEART.drawFromTopLeft(heartX, HEART_Y);
            heartX += DIST_HEART;
        }
    }

    /**
     * Gets the current remaining life.
     */
    public int getCurrLife() {
        return this.currLife;
    }

    /**
     * Sets the current remaining life.
     */
    public void setCurrLife(int currLife) {
        this.currLife = currLife;
    }

    /**
     * Resets to the full life of the bird.
     */
    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }
}
