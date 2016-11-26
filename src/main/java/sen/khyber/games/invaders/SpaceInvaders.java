package sen.khyber.games.invaders;

import sen.khyber.tuples.Pair;
import sen.khyber.util.SuperList;
import sen.khyber.util.SuperListMapMixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SpaceInvaders extends PApplet {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    
    // keyCodes
    private static final int SPACE_BAR = 32;
    private static final int RIGHT_ARROW = 39;
    private static final int LEFT_ARROW = 37;
    private static final int A_KEY = 65;
    private static final int D_KEY = 68;
    private static final int W_KEY = 87;
    
    private static String SPRITE_DIRECTORY = "src/games/invaders/sprites/";
    
    private final List<Agent> friendlyTargets = new ArrayList<>();
    private final Map<Integer, Player> playerIdMap = new HashMap<>();
    
    // see SuperListMapMixin for more info
    // extends SuperList
    // extra method addWithKey(key, value) that adds value to list
    // and also adds key, value pair to map
    // remove is overloaded to remove the value from the list and map
    private final SuperListMapMixin<Integer, Player> players = new SuperListMapMixin<>(
            Arrays.asList(friendlyTargets),
            Arrays.asList(playerIdMap));
    
    private final List<Agent> enemyTargets = new ArrayList<>();
    private final List<Enemy> enemies = new SuperList<>(enemyTargets);
    // SuperList overloads add to also add everything to another list
    
    private final List<Missile> playerMissiles = new ArrayList<>();
    private final List<Missile> enemyMissiles = new SuperList<>(enemyTargets);
    
    private Sprite playerMouse;
    private Sprite catShipA;
    private Sprite catShipB;
    private Sprite cheeseMissile;
    private Sprite bossMissile;
    
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }
    
    @Override
    public PImage loadImage(final String filename) {
        return super.loadImage(SPRITE_DIRECTORY + filename);
    }
    
    @Override
    public void setup() {
        
        imageMode(CENTER);
        
        playerMouse = new Sprite("PlayerMouse.png", 60, 120);
        catShipA = new Sprite("CatShip_A.png", 100, 70);
        catShipB = new Sprite("CatShip_B.png", 100, 70);
        cheeseMissile = new Sprite("CheeseMissile.png", 30, 35);
        bossMissile = new Sprite("BossMissile.png", 30, 35);
        
        final Player arrowPlayer = new Player(width / 2.0f, height - 70);
        final Player awsdPlayer = new Player(width / 2.0f + 20, height - 70);
        
        players.addWithKey(1, arrowPlayer);
        players.addWithKey(2, awsdPlayer);
        
        // add enemies to the game
        // add catShipA enemies
        for (int i = 0; i < 8; i++) {
            enemies.add(new Enemy(100.0f + 150 * i, 50.0f, catShipA));
        }
        
        // add catShipB enemies
        for (int i = 0; i < 8; i++) {
            enemies.add(new Enemy(100.0f + 150 * i, 150.0f, catShipB));
        }
        // enemies.get(enemies.size()/2).toggleDebug();
        
        //enemies.forEach(enemy -> enemy.setSpeed(10));
        
        //enemyTargets.forEach(System.out::println);
        //System.exit(1);
        
    }
    
    /**
     * There is no game menu right now, just start to play.
     */
    @Override
    public void draw() {
        background(255);
        updateMissiles();
        updatePlayers();
        updateEnemies();
    }
    
    // removes all ships with deleteMe == true
    // updates and displays
    // runs actionAfterUpdate on the non-deleted ships
    private static <AgentType extends Agent> void updateAgents(final List<AgentType> agents,
            final Consumer<? super AgentType> actionAfterUpdate) {
        for (int i = agents.size() - 1; i >= 0; i--) {
            final AgentType agent = agents.get(i);
            if (agent.isToBeDeleted()) {
                if (agent instanceof Player) {
                    System.out.println(agent);
                }
                agents.remove(agent);
            } else {
                agent.updateAndDisplay();
                actionAfterUpdate.accept(agent);
            }
        }
    }
    
    private static void updateShips(final List<? extends Agent> agents) {
        updateAgents(agents, agent -> {});
    }
    
    public void updateMissiles() {
        updateShips(playerMissiles);
        updateShips(enemyMissiles);
    }
    
    private void updateEnemies() {
        updateAgents(enemies, enemy -> {
            if (enemy.canShoot()) {
                enemyMissiles.add(enemy.shoot(friendlyTargets));
            }
        });
    }
    
    private void updatePlayers() {
        updateAgents(players, player -> {
            float speed;
            if ((speed = player.getSpeed()) > 0.000001f) {
                player.setSpeed(speed * 0.98f);
            }
        });
    }
    
    // key press actions
    
    private final Consumer<Player> moveLeft = Player::moveLeft;
    
    private final Consumer<Player> moveRight = Player::moveRight;
    
    private final Consumer<Player> shoot = player -> {
        if (player.canShoot()) {
            playerMissiles.add(player.shoot(enemyTargets));
        }
    };
    
    private static final Map<Integer, Pair<Integer, Consumer<Player>>> keyPressActions = new HashMap<>();
    
    private void addKeyPressAction(final int keyCode, final int id, final Consumer<Player> action) {
        final Pair<Integer, Consumer<Player>> pair = new Pair<>(id, action);
        keyPressActions.put(keyCode, pair);
    }
    
    // add key press actions for different keyCodes
    // action is actually a Pair of the id in the playerIdMap
    // and the actual Consumer<Player> action
    {
        addKeyPressAction(LEFT_ARROW, 1, moveLeft);
        addKeyPressAction(RIGHT_ARROW, 1, moveRight);
        addKeyPressAction(SPACE_BAR, 1, shoot);
        addKeyPressAction(A_KEY, 2, moveLeft);
        addKeyPressAction(D_KEY, 2, moveRight);
        addKeyPressAction(W_KEY, 2, shoot);
    }
    
    @Override
    public void keyPressed() {
        // key press actions set up to have these indices
        final Pair<Integer, Consumer<Player>> pair = keyPressActions.get(keyCode);
        if (pair != null) {
            final int id = pair.getFirst();
            final Consumer<Player> action = pair.getSecond();
            final Player player = playerIdMap.get(id);
            if (player != null) {
                action.accept(player);
            }
        }
    }
    
    public static void main(final String[] passedArgs) {
        final String className = SpaceInvaders.class.getName();
        if (passedArgs != null) {
            PApplet.main(className, passedArgs);
        } else {
            PApplet.main(className);
        }
    }
    
    public abstract class Agent {
        
        private final Sprite sprite;
        private final PVector position;
        private PVector velocity;
        private final PVector acceleration;
        private final float size;
        private final float topSpeed;
        private boolean rotates, debug, deleteMe;
        
        /**
         */
        public Agent(final PVector position, final PVector velocity, final PVector acceleration,
                final float size, final float topSpeed, final Sprite spriteImg) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.size = size;
            this.topSpeed = topSpeed;
            sprite = spriteImg;
            rotates = spriteImg == null;
            deleteMe = false;
        }
        
        /**
         * Change the deleteMe boolean to true. somewhere in the code the
         * program will remove all the agents that want to be deleted
         */
        public void deleteMe() {
            deleteMe = true;
        }
        
        /**
         */
        public boolean isToBeDeleted() {
            return deleteMe;
        }
        
        /**
         */
        public float getAngle() {
            return atan2(velocity.y, velocity.x);
        }
        
        /**
         */
        public void setAngle(final float angle) {
            final float mag = velocity.mag();
            final float x = cos(angle);
            final float y = sin(angle);
            velocity = new PVector(x, y);
            velocity.mult(mag);
        }
        
        /**
         * Accelerate the object to the left, this will make it start to move in
         * that direction
         */
        public void moveLeft() {
            setAngle(PI);
            setSpeed(4);
        }
        
        /**
         * Accelerate the object to the right, this will make it start to move
         * in that direction
         */
        public void moveRight() {
            setAngle(0);
            setSpeed(4);
        }
        
        /**
         * Accelerate the object to the bottom of the screen, this will make it
         * start to move in that direction
         */
        public void moveDown() {
            setAngle(PI / 2);
            setSpeed(4);
        }
        
        /**
         * Accelerate the object to top of the screen, this will make it start
         * to move in that direction
         */
        public void moveUp() {
            setAngle(3 * PI / 2);
            setSpeed(4);
        }
        
        /**
         * Change the x,y position of the object
         * 
         * @param x the specified value to change position.x to
         * @param y the specified value to change position.y to
         */
        public void setXY(final float x, final float y) {
            position.x = x;
            position.y = y;
        }
        
        /**
         * @return the x-position
         */
        public float getX() {
            return position.x;
        }
        
        /**
         * Change the x position of the object to the specified value
         * 
         * @param x the specified value to change to
         */
        public void setX(final float x) {
            position.x = x;
        }
        
        /**
         * @return the y-position
         */
        public float getY() {
            return position.y;
        }
        
        /**
         * Change the x position of the object
         * 
         * @param y the specified value to change to
         */
        public void setY(final float y) {
            position.y = y;
        }
        
        /**
         * @return the x-velocity
         */
        public float getDX() {
            return velocity.x;
        }
        
        /**
         * @return the y-velocity
         */
        public float getDY() {
            return velocity.y;
        }
        
        /**
         * @return the x-size
         */
        public float getWidth() {
            if (sprite == null) {
                return size;
            } else {
                return sprite.width();
            }
        }
        
        /**
         * @return the y-size
         */
        public float getHeight() {
            if (sprite == null) {
                return size;
            } else {
                return sprite.height();
            }
        }
        
        /**
         */
        public void toggleRotate() {
            rotates = !rotates;
        }
        
        /**
         */
        public void toggleDebug() {
            debug = !debug;
        }
        
        /**
         */
        public float getSpeed() {
            return velocity.mag();
        }
        
        /**
         */
        public void setSpeed(final float speed) {
            velocity.normalize();
            velocity.mult(speed);
        }
        
        /**
         */
        public void turn(final float angle) {
            setAngle(getAngle() + angle);
        }
        
        /**
         */
        private void arrow() {
            stroke(0);
            line(0, 0, 10 * velocity.mag(), 0);
            ellipse(10 * velocity.mag(), 0, 4, 4);
        }
        
        public boolean isAtTop() {
            return getY() < getHeight() / 2;
        }
        
        public boolean isAtBottom() {
            return getY() > height - getHeight() / 2;
        }
        
        public boolean isAtLeft() {
            return getX() < getWidth() / 2;
        }
        
        public boolean isAtRight() {
            return getX() > width - getWidth() / 2;
        }
        
        /**
         */
        public void display() {
            pushMatrix();
            translate(position.x, position.y);
            if (debug) {
                fill(0);
                text("Angle: " + degrees(getAngle()) + "\n"
                        + "Speed: " + getSpeed(), -20, 40);
                text("Pos: " + position + " ", -20, 100);
                text("Vel: " + velocity + " ", -20, 120);
                text("Acc: " + acceleration + " ", -20, 140);
                if (sprite != null) {
                    text("size: " + sprite.width() + ","
                            + sprite.height(), -20, 160);
                }
            }
            if (rotates) {
                rotate(getAngle());
            }
            if (sprite == null) {
                fill(255);
                rectMode(CENTER);
                rect(0, 0, size, size);
                arrow();
            } else {
                sprite.display();
            }
            popMatrix();
        }
        
        /**
         */
        private void updatePhysics() {
            position.add(velocity);
            velocity.add(acceleration);
            velocity.limit(topSpeed);
            acceleration.mult(0);
        }
        
        /**
         * This method is called in the update, it is meant to handle how agents
         * behave when they go off the screen Unfortunately you don't know
         * abstract methods yet, but this would be abstract.
         */
        public abstract void keepInBounds();
        
        /**
         */
        public void update() {
            updatePhysics();
            keepInBounds();
        }
        
        public void updateAndDisplay() {
            update();
            display();
        }
        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Agent [sprite=" + sprite + ", position=" + position
                    + ", velocity=" + velocity + ", acceleration="
                    + acceleration + ", size=" + size + ", topSpeed=" + topSpeed
                    + ", rotates=" + rotates + ", deleteMe=" + deleteMe + "]";
        }
        
    }
    
    public class Ship extends Agent {
        
        private static final int DEFAULT_TIME_BETWEEN_MISSILES = 60;
        
        private int timeSinceLastMissile;
        protected Sprite myMissileSprite;
        
        public Ship(final float x, final float y, final Sprite img, final Sprite missileImg) {
            super(new PVector(x, y), new PVector(0.5f, 0.0f), new PVector(0, 0),
                    100, 10, img);
            myMissileSprite = missileImg;
            timeSinceLastMissile = getTimeBetweenMissiles();
        }
        
        protected int getTimeBetweenMissiles() {
            return DEFAULT_TIME_BETWEEN_MISSILES;
        }
        
        public boolean isHit(final Missile p) {
            return SpaceInvaders.dist(getX(), getY(), p.getX(), p.getY()) < 50;
        }
        
        public void hit() {
            deleteMe();
        }
        
        @Override
        public void update() {
            super.update();
            timeSinceLastMissile++;
        }
        
        public void display(final boolean debug) {
            super.display();
            if (debug) {
                if (canShoot()) {
                    text("CAN SHOOT!", width / 2, height / 2);
                }
            }
        }
        
        @Override
        public void keepInBounds() {
            setX(constrain(getX(), 0, width));
            setY(constrain(getY(), 0, height));
        }
        
        public boolean canShoot() {
            return timeSinceLastMissile > getTimeBetweenMissiles();
        }
        
        /**
         * shoot returns a new projectile with properties defined by the ship
         * that creates it
         */
        public Missile shoot(final List<? extends Agent> targets) {
            return new Missile(getX(), getY() - 30, 0.0f, -5.0f,
                    myMissileSprite, targets);
        }
        
    }
    
    public class Player extends Ship {
        
        public Player(final float x, final float y, final Sprite img, final Sprite missileImg) {
            super(x, y, img, missileImg);
        }
        
        public Player(final float x, final float y) {
            this(x, y, playerMouse, cheeseMissile);
        }
        
        @Override
        public void keepInBounds() {
            if (isAtRight()) {
                setX(getWidth() / 2);
            } else if (isAtLeft()) {
                setX(width - getWidth() / 2);
            }
        }
        
    }
    
    public class SuperPlayer extends Player {
        
        public SuperPlayer(final float x, final float y, final Sprite img, final Sprite missileImg) {
            super(x, y, img, missileImg);
        }
        
        public SuperPlayer(final float x, final float y) {
            super(x, y);
        }
        
        @Override
        protected int getTimeBetweenMissiles() {
            return 0;
        }
        
    }
    
    public class Enemy extends Ship {
        
        private static final float DEFAULT_MOVE_DOWN_DIST = 100;
        
        private float oldY;
        private boolean isMovingDown = false;
        
        public Enemy(final float x, final float y, final Sprite img, final Sprite missileImg) {
            super(x, y, img, missileImg);
            oldY = y;
        }
        
        public Enemy(final float x, final float y, final Sprite img) {
            this(x, y, img, bossMissile);
        }
        
        private float getDistToMoveDown() {
            //return DEFAULT_MOVE_DOWN_DIST;
            //return getHeight() * 3;
            return getHeight() / 2;
        }
        
        private boolean hasMovedDown() {
            return getY() - oldY > getDistToMoveDown();
        }
        
        private void turnAtEdge() {
            if (isAtLeft()) {
                turn(-PI / 2);
            } else if (isAtRight()) {
                turn(PI / 2);
            }
        }
        
        @Override
        public void keepInBounds() {
            if (!isMovingDown) {
                turnAtEdge();
                if (isAtLeft() || isAtRight()) {
                    isMovingDown = true;
                }
            } else if (hasMovedDown()) {
                if (isAtBottom()) {
                    deleteMe();
                }
                turnAtEdge();
                isMovingDown = false;
                oldY = getY();
            }
        }
        
        protected int getShootChanceScaleFactor() {
            return 500;
        }
        
        /**
         * @returns true 1/500th of the time
         */
        @Override
        public boolean canShoot() {
            return Math.random() * getShootChanceScaleFactor() < 1;
        }
        
        // shoot returns a new projectile with properties defined by the ship
        // that creates it.
        @Override
        public Missile shoot(final List<? extends Agent> targets) {
            return new Missile(getX(), getY() + 30, 0.0f, 4.0f,
                    myMissileSprite, targets);
        }
        
        @Override
        public void update() {
            super.update();
            // TODO add logic to change how to behave!
        }
        
    }
    
    public class Missile extends Agent {
        
        private final List<Agent> targets;
        
        public Missile(final float x, final float y, final float dx, final float dy, final Sprite img,
                final List<? extends Agent> targets) {
            super(new PVector(x, y), new PVector(dx, dy), new PVector(0, 0),
                    100, 10, img);
            this.targets = new ArrayList<>(targets);
        }
        
        @Override
        public void update() {
            super.update();
            for (final Agent target : targets) {
                if (!isToBeDeleted() && dist(getX(), getY(),
                        target.getX(), target.getY()) < 50) {
                    target.deleteMe();
                    if (!(target instanceof Missile)) {
                        deleteMe();
                    }
                }
            }
        }
        
        @Override
        public void keepInBounds() {
            if (getY() < -10 || getY() > height) {
                deleteMe();
            }
        }
        
    }
    
    /**
     * this is a wrapper class to simplify the PImage to a single line filename
     * + dimensions.
     */
    public class Sprite {
        
        private final PImage img;
        
        public Sprite(final String name, final int width, final int height) {
            img = loadImage(name);
            img.resize(width, height);
        }
        
        public int width() {
            return img.width;
        }
        
        public int height() {
            return img.height;
        }
        
        public void display() {
            image(img, 0, 0);
        }
        
        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Sprite [img=" + img + "]";
        }
        
    }
    
}