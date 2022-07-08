import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The bird class contains all attributes of the birds and the methods for it to play the game.
 */
public class Bird {
    private final Image WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
    private final Image WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");
    private final Image LEVEL1_WING_DOWN = new Image("res/level-1/birdWingDown.png");
    private final Image LEVEL1_WING_UP = new Image("res/level-1/birdWingUp.png");
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private boolean levelUp = false;
    private boolean loaded = false;

    /**
     * The constructor of the class Bird
     */
    public Bird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
    }

    /**
     * Updates the image and position of the bird.
     */
    public Rectangle update(Input input) {
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            if (!levelUp) {
                WING_DOWN_IMAGE.draw(X, y);
            }
            else {
                LEVEL1_WING_DOWN.draw(X, y);
            }
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            // wing flaps every 10 frames
            if (frameCount % SWITCH_FRAME == 0) {
                if (!levelUp) {
                    WING_UP_IMAGE.draw(X, y);
                    boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
                }
                else {
                    LEVEL1_WING_UP.draw(X, y);
                    boundingBox = LEVEL1_WING_UP.getBoundingBoxAt(new Point(X, y));
                }
            }
            else {
                if (!levelUp) {
                    WING_DOWN_IMAGE.draw(X, y);
                    boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
                }
                else {
                    LEVEL1_WING_DOWN.draw(X, y);
                    boundingBox = LEVEL1_WING_DOWN.getBoundingBoxAt(new Point(X, y));
                }
            }
        }
        frameCount++;
        y += yVelocity;
        return boundingBox;
    }

    /**
     * Gets the y coordinate of the bird
     */
    public double getBirdY() {
        return this.y;
    }

    /**
     * Resets the y coordinate of the bird
     */
    public void setBirdY() {
        this.y = INITIAL_Y;
    }

    /**
     * Get the x coordinate of the bird.
     */
    public double getBirdX() {
        return this.X;
    }

    /**
     * Get the box of the bird for collision detection.
     */
    public Rectangle getBirdBox() {
        return this.boundingBox;
    }


    /**
     * Level up!
     */
    public void setBirdLevelUp(boolean status) {
        this.levelUp = status;
    }

    /**
     * Checks if the bird has weapon currently.
     */
    public boolean getArmedStatus() {
        return this.loaded;
    }

    /**
     * Sets the bird as loaded.
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}