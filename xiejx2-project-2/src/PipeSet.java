import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An abstract class contains both plastic pipes and steel pipes.
 */
public abstract class PipeSet {
    private final Image PIPE_IMAGE;
    private final int PIPE_GAP = 168;
    private final int GAP_LOW = 100;
    private final int GAP_MID = 300;
    private final int GAP_HIGH = 500;
    private double pipeStepSize = 5;
    private final double TIMESCALE = 1.5;
    private final double TOP_PIPE_Y;
    private final double BOTTOM_PIPE_Y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth();
    private final double RANDOM_POSITION;
    private List<Integer> pipeTypes = new ArrayList<>();
    private boolean levelUp;
    private boolean collision = false;
    private boolean collideWithFlame = false;
    private boolean passed = false;

    /**
     * Constructor of the PipeSet class.
     */
    public PipeSet(Image pipeImage, boolean isLevelUp) {
        PIPE_IMAGE = pipeImage;
        levelUp = isLevelUp;
        pipeTypes.add(GAP_LOW);
        pipeTypes.add(GAP_MID);
        pipeTypes.add(GAP_HIGH);
        // 3 gap positions in level 0 and many gap positions in level 1
        if (!levelUp) {
            RANDOM_POSITION = pipeTypes.get(new Random().nextInt(pipeTypes.size()));
        }
        else {
            RANDOM_POSITION = GAP_LOW + (new Random().nextDouble() * (GAP_HIGH-GAP_LOW));
        }
        TOP_PIPE_Y = RANDOM_POSITION - PIPE_IMAGE.getHeight()/2.0;
        BOTTOM_PIPE_Y = RANDOM_POSITION + PIPE_GAP + PIPE_IMAGE.getHeight()/2.0;
    }

    /**
     * Updates and display and timescale of pipes.
     */
    public void update(double timeScale) {
        if (!collision && !collideWithFlame) {
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
        for (int i=1; i<timeScale; i++) {
            pipeStepSize = pipeStepSize * TIMESCALE;
        }
        pipeX -= pipeStepSize;
    }

    /**
     * Gets the x coordinate of the pipe.
     */
    public double getPipeX() {
        return this.pipeX;
    }

    /**
     * Gets the top y coordinate of the pipe.
     */
    public double getTOP_PIPE_Y() {
        return this.TOP_PIPE_Y;
    }

    /**
     * Gets the bottom y coordinate of the pipe.
     */
    public double getBOTTOM_PIPE_Y() {
        return this.BOTTOM_PIPE_Y;
    }

    /**
     * Rotates the pipe to draw bottom pipe with the same image as the top pipe.
     */
    public DrawOptions getROTATOR() {
        return this.ROTATOR;
    }

    /**
     * Gets the image of the pipe.
     */
    public Image getPIPE_IMAGE() {
        return this.PIPE_IMAGE;
    }

    /**
     * Gets the box of the top pipe.
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
    }

    /**
     * Gets the box of the bottom pipe.
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }

    /**
     * Gets the collision status of the pipe.
     */
    public boolean getCollision() {
        return this.collision;
    }

    /**
     * Sets the collision status of the pipe.
     */
    public void setCollision(boolean status) {
        this.collision = status;
    }

    /**
     * Sets the flame collision status of the pipe.
     */
    public void setFlameCollision(boolean collideWithFlame) {
        this.collideWithFlame = collideWithFlame;
    }

    /**
     * Gets if the pipe is passed.
     */
    public boolean getPassed() {
        return this.passed;
    }

    /**
     * Sets the pipe as passed.
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}
