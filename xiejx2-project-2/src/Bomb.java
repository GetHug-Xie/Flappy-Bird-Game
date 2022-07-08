import bagel.Image;
import bagel.Input;

/**
 * Bomb class extends from abstract class Weapon.
 */
public class Bomb extends Weapon {
    private final int SHOOT_RANGE = 50;

    /**
     * Inherits constructor from its abstract class.
     */
    public Bomb(Image bombImage) {
        super(bombImage);
    }

    /**
     * Inherits the update method from its abstract class.
     */
    public void update(Input input, Bird bird, double timeScale) {
        super.update(input, bird, timeScale);
        super.renderWeapon(SHOOT_RANGE);
    }
}
