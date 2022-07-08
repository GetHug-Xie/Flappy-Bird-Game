import bagel.Image;
import bagel.Input;

/**
 * Rock class extends from abstract class Weapon.
 */
public class Rock extends Weapon {
    private final int SHOOT_RANGE = 25;

    /**
     * Inherits constructor from its abstract class.
     */
    public Rock(Image rockImage) {
        super(rockImage);
    }

    /**
     * Inherits the update method from its abstract class.
     */
    public void update(Input input, Bird bird, double timeScale) {
        super.update(input, bird, timeScale);
        super.renderWeapon(SHOOT_RANGE);
    }
}
