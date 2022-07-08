import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * SteelPipeSet class extends from abstract class PipeSet.
 */
public class SteelPipeSet extends PipeSet {
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final double SWITCH_FRAME = 20;
    private final int FLAMETIME = 30;
    private final double FLAME_TO_PIPE = 10;
    private int frameCount = 0;
    private int flameDuration = 0;
    private boolean flameCollision = false;

    /**
     * Inherits constructor from its abstract class.
     */
    public SteelPipeSet(Image pipeImage, boolean isLevelUp) {
        super(pipeImage, isLevelUp);
    }

    /**
     * Inherits the update method from its abstract class.
     */
    public void update(double timeScale) {
        frameCount++;
        // spits fire every 20 frames
        if (frameCount % SWITCH_FRAME == 0) {
            // Renders the flame normally if no collision occurs
            if (!super.getCollision() && flameDuration < FLAMETIME) {
                if (!flameCollision) {
                    FLAME_IMAGE.draw(super.getPipeX(),
                            super.getTOP_PIPE_Y() + Window.getHeight()/2.0 + FLAME_TO_PIPE);
                    FLAME_IMAGE.draw(super.getPipeX(),
                            super.getBOTTOM_PIPE_Y() - Window.getHeight()/2.0 - FLAME_TO_PIPE, super.getROTATOR());
                }
                frameCount--;
                flameDuration++;
            }
        }
        // Counts the frames to spit fire again
        if (flameDuration == FLAMETIME) {
            resetFlame();
        }
        super.update(timeScale);
    }

    /**
     * Gets the box of the top flame.
     */
    public Rectangle getTopFlame() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(),
                super.getTOP_PIPE_Y() + Window.getHeight()/2.0 + FLAME_TO_PIPE));
    }

    /**
     * Gets the box of the bottom flame.
     */
    public Rectangle getBottomFlame() {
        return FLAME_IMAGE.getBoundingBoxAt(new Point(super.getPipeX(),
                super.getBOTTOM_PIPE_Y() - Window.getHeight()/2.0 - FLAME_TO_PIPE));
    }

    /**
     * Gets the flame collision status of the steel pipe.
     */
    public boolean getFlameCollision() {
        return this.flameCollision;
    }

    /**
     * Sets the flame collision status of the steel pipe.
     */
    public void setCollideWithFlame(boolean collideWithFlame) {
        this.flameCollision = collideWithFlame;
    }

    /**
     * Counts the frames to spit fire again.
     */
    public void resetFlame() {
        flameDuration = 0;
        frameCount += FLAMETIME;
    }
}
