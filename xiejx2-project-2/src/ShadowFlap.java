import bagel.*;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2021
 *
 * Please filling your name below
 * @author: Jie Xie, 1174437
 * Reference: Many methods I used in this project are modified from Betty Lin's solution for Project 1.
 */
public class ShadowFlap extends AbstractGame {
    private final Image LEVEL0_BACKGROUND = new Image("res/level-0/background.png");
    private final Image LEVEL1_BACKGROUND = new Image("res/level-1/background.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String LEVELUP_MSG = "LEVEL UP!";
    private final String SHOOT_MSG = "PRESS 'S' TO SHOOT";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private Bird bird;
    private LifeBar lifeBar;
    private final int FULL_HEALTH = 6;
    private int score;
    private boolean gameOn;
    private boolean pipeCollision;
    private boolean win;
    private boolean dead = false;
    private final int WIN_LEVEL0 = 10;
    private final int WIN_LEVEL1 = 30;
    private final Image PLASTIC_PIPE = new Image("res/level/plasticPipe.png");
    private final Image STEEL_PIPE = new Image("res/level-1/steelPipe.png");
    private boolean isLevelUp = false;
    private final int LEVEL_UP = 150;
    private ArrayList<PipeSet> plasticPipeSets = new ArrayList<>();
    private ArrayList<PipeSet> pipeSets = new ArrayList<>();
    private ArrayList<Weapon> weapons = new ArrayList<>();
    private boolean currWeaponPosition = false;
    private final double WEAPON_RENDER = 150;
    private final Image ROCK = new Image("res/level-1/rock.png");
    private final Image BOMB = new Image("res/level-1/bomb.png");
    private double timeScale = 1;
    private final double TIMESCALE = 1.5;
    private final int MAX_SCALE = 5;
    private final int MIN_SCALE = 1;
    private int frameCount = 0;
    private final double SWITCH_FRAME = 100;
    private boolean levelUp = false;
    private int levelUpDuration = 0;
    private boolean flameCollision = false;

    /**
     * The constructor of the class ShadowFlap
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        lifeBar = new LifeBar();
        score = 0;
        gameOn = false;
        pipeCollision = false;
        win = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        if (!isLevelUp) {
            LEVEL0_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }
        else {
            LEVEL1_BACKGROUND.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            System.exit(0);
        }
        if (!gameOn) {
            renderInstructionScreen(input);
        }
        if (lifeBar.getCurrLife() == 0) {
            renderGameOverScreen();
            dead = true;
        }
        if (birdOutOfBound()) {
            if (lifeBar.getCurrLife() > 0) {
                lifeBar.setCurrLife(lifeBar.getCurrLife()-1);
                bird.setBirdY();
            }
        }
        if (win) {
            renderWinScreen();
        }

        // The shift between level 0 and level 1
        if (!isLevelUp && levelUp) {
            renderLevelUpScreen();
            levelUpDuration++;
            if (levelUpDuration == LEVEL_UP) {
                reloadGame();
            }
        }

        // The game is running
        if (gameOn && !win && !dead) {
            // Enters level 0 here
            if (!levelUp) {
                updateTimeScale(input);
                // Adds new pipe every 100 frames
                if (frameCount % SWITCH_FRAME == 0) {
                    plasticPipeSets.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
                }
                // Counts the frames, checks the interaction between the bird and pipes and updates
                frameCount++;
                bird.update(input);
                updatePipeSets(plasticPipeSets, bird.getBirdBox());
                lifeBar.update();
            }

            // Enters level 1 here
            if (isLevelUp) {
                updateTimeScale(input);
                if (frameCount % SWITCH_FRAME == 0) {
                    pipeRandomRender();
                }
                // Renders weapon at a regular frequency, every 150 frames here
                if (frameCount % WEAPON_RENDER == 0) {
                    weaponRandomRender();
                    currWeaponPosition = detectOverlap(weapons.get(weapons.size()-1).getWeaponBox(), pipeSets);
                    weapons.get(weapons.size()-1).setOverlap(currWeaponPosition);
                }
                frameCount++;
                bird.update(input);
                updatePipeSets(pipeSets, bird.getBirdBox());
                // Similar but needs to update weapon status compared to level 0
                updateWeapons(bird.getBirdBox(), input);
                lifeBar.update();
            }
        }
    }

    /**
     * Randomly renders pipes.
     */
    public void pipeRandomRender() {
        if (new Random().nextBoolean()) {
            pipeSets.add(new SteelPipeSet(STEEL_PIPE, isLevelUp));
        }
        else {
            pipeSets.add(new PlasticPipeSet(PLASTIC_PIPE, isLevelUp));
        }
    }

    /**
     * Randomly renders weapons.
     */
    public void weaponRandomRender() {
        if (new Random().nextBoolean()) {
            weapons.add(new Bomb(BOMB));
        }
        else {
            weapons.add(new Rock(ROCK));
        }
    }

    /**
     * Checks whether the bird is out of window.
     */
    public boolean birdOutOfBound() {
        return (bird.getBirdY() > Window.getHeight()) || (bird.getBirdY() < 0);
    }

    /**
     * Prints the instruction messages.
     */
    public void renderInstructionScreen(Input input) {
        if (!isLevelUp) {
            FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)),
                    (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        }
        // Needs to attach the score when level 0 is passed,
        // and slight change of texts' position to keep it in the middle of screen
        if (isLevelUp) {
            FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)),
                    (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
            FONT.drawString(SHOOT_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOT_MSG)/2.0)),
                    (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        }
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
    }

    /**
     * Prints the game over information.
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Prints the game win information.
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Prints the level up information.
     */
    public void renderLevelUpScreen() {
        FONT.drawString(LEVELUP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVELUP_MSG)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)),
                (Window.getHeight()/2.0-(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * A general method used for detecting the collision between a target and two objects.
     */
    public boolean detectCollision(Rectangle objectBox, Rectangle topBox, Rectangle bottomBox) {
        return objectBox.intersects(topBox) || objectBox.intersects(bottomBox);
    }

    /**
     * Detects whether weapon collides with pipes
     */
    public boolean detectOverlap(Rectangle weaponBox, ArrayList<PipeSet> pipeSets) {
        // Checks through each pipe set if they overlap a weapon
        for (PipeSet pipe : pipeSets) {
            if (detectCollision(weaponBox, pipe.getTopBox(), pipe.getBottomBox())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the timeScale as the game processes.
     */
    public void updateTimeScale(Input input) {
        if (input.wasPressed(Keys.L) && timeScale<MAX_SCALE) {
            timeScale = timeScale * TIMESCALE;
        }
        if (input.wasPressed(Keys.K) && timeScale>MIN_SCALE) {
            timeScale = timeScale / TIMESCALE;
        }
    }

    /**
     * Checks if the bird collides with the pipes and updates the collisions and life bars
     */
    public void updatePipeSets(ArrayList<PipeSet> pipeSets, Rectangle birdBox) {
        for (PipeSet pipeSet : pipeSets) {
            pipeSet.update(timeScale);
            pipeCollision = detectCollision(birdBox, pipeSet.getTopBox(), pipeSet.getBottomBox());
            // if collision occurs and is the first time
            if (pipeCollision && !pipeSet.getCollision()) {
                pipeSet.setCollision(true);
                lifeBar.setCurrLife(lifeBar.getCurrLife()-1);
            }

            // Enters level 1 here as steel pipes only exist in level 1
            if (pipeSet.getPIPE_IMAGE().equals(STEEL_PIPE)) {
                SteelPipeSet steelPipe = (SteelPipeSet) pipeSet;
                flameCollision = detectCollision(birdBox, steelPipe.getTopFlame(), steelPipe.getBottomFlame());
                // if collides flame for the first time, and does not collide pipes in case of
                // collision with flame when pipe is removed after collision with pipe
                if (flameCollision && !steelPipe.getFlameCollision() && !steelPipe.getCollision()) {
                    pipeSet.setFlameCollision(true);
                    steelPipe.setCollideWithFlame(true);
                    lifeBar.setCurrLife(lifeBar.getCurrLife()-1);
                }
            }
            updateScore(pipeSet);
        }
    }

    /**
     * Checks through each weapon and pick it when the bird is unarmed and collides the weapon.
     */
    public void updateWeapons(Rectangle birdBox, Input input) {
        for (Weapon weapon : weapons) {
            if (birdBox.intersects(weapon.getWeaponBox())
                    && !weapon.getCollected() && !weapon.getOverlap()) {
                if (!bird.getArmedStatus()) {
                    bird.setLoaded(true);
                    weapon.setCollected(true);
                }
            }
            weapon.update(input, bird, timeScale);
            checkPipeDestroyed(weapon);
        }
    }

    /**
     * Updates the score on time and changes the game status when score reaches the goal.
     */
    public void updateScore(PipeSet pipeSet) {
        // Scores if the bird passes the pipe without collision
        if (bird.getBirdX() > pipeSet.getTopBox().right()
                && !pipeSet.getPassed() && !pipeSet.getCollision()) {
            pipeSet.setPassed(true);
            score++;
        }
        // Updates the score on time
        FONT.drawString(SCORE_MSG+score, 100, 100);

        if (score == WIN_LEVEL0 && !isLevelUp) {
            levelUp = true;
        }
        if (score == WIN_LEVEL1) {
            win = true;
        }
    }

    /**
     * Updates the weapon's and pipe's status after a weapon hit a pipe.
     */
    public void updateWeaponPipe(Weapon weapon, PipeSet pipe) {
        weapon.setRemoved(true);
        pipe.setCollision(true);
        pipe.setPassed(true);
        score++;
    }

    /**
     * Checks if the current weapon can destroy the current pipe and updates the results.
     */
    public void checkPipeDestroyed(Weapon weapon) {
        for (PipeSet pipe : pipeSets) {
            // Loaded and not fired
            if (!weapon.getRemoved() && weapon.getIsShoot()) {
                // when a rock hits a plastic pipe
                if (weapon.getWeaponImage().equals(ROCK) && pipe.getPIPE_IMAGE().equals(PLASTIC_PIPE)
                        && detectCollision(weapon.getWeaponBox(), pipe.getTopBox(), pipe.getBottomBox())) {
                    updateWeaponPipe(weapon, pipe);
                }
                // when a bomb hits any pipes
                if (weapon.getWeaponImage().equals(BOMB)
                        && detectCollision(weapon.getWeaponBox(), pipe.getTopBox(), pipe.getBottomBox())) {
                    updateWeaponPipe(weapon, pipe);
                }
            }
        }
    }

    /**
     * Resets the key attributes of the game
     */
    public void reloadGame() {
        isLevelUp = true;
        gameOn = false;
        bird.setBirdLevelUp(true);
        bird.setBirdY();
        timeScale = 1;
        lifeBar.setFullHealth(FULL_HEALTH);
        lifeBar.setCurrLife(FULL_HEALTH);
        score = 0;
        frameCount = 0;
    }
}
