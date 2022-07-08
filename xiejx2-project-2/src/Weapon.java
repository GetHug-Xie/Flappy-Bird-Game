import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Rectangle;

import java.util.Random;

/**
 * An abstract class contains both rock and bomb.
 */
public abstract class Weapon {
    private final Image WEAPON_IMAGE;
    private double weaponStepSize = 5;
    private final double TIMESCALE = 1.5;
    private final double LOWWEST = 100;
    private final double HIGHEST = 500;
    private double weaponX = Window.getWidth();
    private boolean collected = false;
    private boolean fired = false;
    private boolean removed = false;
    private boolean overlap = false;
    private int distance = 0;
    private double weaponY;

    /**
     * The constructor of the abstract class Weapon.
     */
    public Weapon(Image weaponImage) {
        WEAPON_IMAGE = weaponImage;
        weaponY = LOWWEST + (new Random().nextDouble() * (HIGHEST - LOWWEST));
    }

    /**
     * Updates the position of weapons.
     */
    public void update(Input input, Bird bird, double timeScale) {
        updateSpeed(timeScale);
        // fades out the window if weapon not collected
        if (!collected) {
            this.weaponX -= weaponStepSize;
        }
        else {
            // weapon being fired or stays with the bird
            if (bird.getArmedStatus() && !fired) {
                if (input.wasPressed(Keys.S)) {
                    bird.setLoaded(false);
                    this.fired = true;
                }
                setWeaponX(bird.getBirdBox().right());
                setWeaponY(bird.getBirdY());
            }
        }
    }

    /**
     * Sets the display of the weapon under different conditions.
     */
    public void renderWeapon(int shootRange) {
        if (fired) {
            if (distance <= shootRange) {
                this.weaponX += weaponStepSize;
                this.distance += weaponStepSize;
            }
            // out of shooting range and should disappear
            else {
                this.removed = true;
            }
        }
        // keep displaying the weapon if it does not overlap and still in the window
        if (!removed && !overlap) {
            WEAPON_IMAGE.draw(weaponX, weaponY);
        }
    }

    /**
     * Updates the moving speed of the weapon according to the timescale.
     */
    public void updateSpeed(double timeScale) {
        for (int i=1; i<timeScale; i++) {
            weaponStepSize = weaponStepSize * TIMESCALE;
        }
    }

    /**
     * Sets the x coordinate of the weapon.
     */
    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    /**
     * Sets the y coordinate of the weapon.
     */
    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    /**
     * Gets the overlap status of the weapon.
     */
    public boolean getOverlap() {
        return this.overlap;
    }

    /**
     * Sets the overlap status of the weapon.
     */
    public void setOverlap(boolean isOverlap) {
        this.overlap = isOverlap;
    }

    /**
     * Gets the detection box of the weapon.
     */
    public Rectangle getWeaponBox() {
        return WEAPON_IMAGE.getBoundingBoxAt(new bagel.util.Point(weaponX, weaponY));
    }

    /**
     * Gets the image of the weapon.
     */
    public Image getWeaponImage() {
        return this.WEAPON_IMAGE;
    }

    /**
     * Gets the pickup status of the weapon.
     */
    public boolean getCollected() {
        return this.collected;
    }

    /**
     * Sets the pickup status of the weapon.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    /**
     * Gets the fired status of the weapon.
     */
    public boolean getIsShoot() {
        return this.fired;
    }

    /**
     * Gets the appearing status of the weapon.
     */
    public boolean getRemoved() {
        return this.removed;
    }

    /**
     * Sets the appearing status of the weapon.
     */
    public void setRemoved(boolean disappeared) {
        this.removed = disappeared;
    }
}
