package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.core.input.InputHandler;
import it.unicam.cs.mpgc.rpg130396.core.input.InputState;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Updatable;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;

import java.util.Objects;

public final class PlayerController implements InputHandler, Updatable {

    public enum PlayerPose { IDLE, WALK, LOOK_UP, CROUCH, JUMP }

    private static final double MOVE_SPEED = 85;
    private static final double JUMP_SPEED = 155;
    private static final double GRAVITY = 420;

    private final Node player;
    private final SpriteAnimator animator;
    private final SpriteAnimation idle;
    private final SpriteAnimation walk;
    private final SpriteAnimation crouchAnimation;
    private final float minX;
    private final float maxX;
    private final float groundY;
    private final boolean walkOnly;

    private boolean left;
    private boolean right;
    private boolean lookUp;
    private boolean crouch;
    private boolean jumpPressed;
    private double velocityY;
    private PlayerPose pose;
    private SpriteAnimation currentAnimation;


    public PlayerController(Node player, SpriteAnimator animator, SpriteAnimation idle,
                            SpriteAnimation walk, SpriteAnimation crouchAnimation,
                            float minX, float maxX, float groundY) {
        this(player, animator, idle, walk, crouchAnimation, minX, maxX, groundY, false);
    }


    public PlayerController(Node player, SpriteAnimator animator, SpriteAnimation idle,
                            SpriteAnimation walk, SpriteAnimation crouchAnimation,
                            float minX, float maxX, float groundY, boolean walkOnly) {
        this.player = Objects.requireNonNull(player);
        this.animator = Objects.requireNonNull(animator);
        this.idle = Objects.requireNonNull(idle);
        this.walk = Objects.requireNonNull(walk);
        this.crouchAnimation = walkOnly ? null : Objects.requireNonNull(crouchAnimation);
        this.minX = minX;
        this.maxX = maxX;
        this.groundY = groundY;
        this.walkOnly = walkOnly;
        update(0);
    }

    public PlayerPose getPose() {
        return pose;
    }

    @Override
    public void handleInput(InputState input) {
        left = input.isDown(Action.LEFT);
        right = input.isDown(Action.RIGHT);
        lookUp = !walkOnly && input.isDown(Action.UP);
        crouch = !walkOnly && input.isDown(Action.DOWN);
        jumpPressed = !walkOnly && input.isPressed(Action.JUMP);
    }

    @Override
    public void update(double dt) {
        Updatable.requireValidDeltaTime(dt);
        Transform2D transform = player.getLocal();
        boolean grounded = walkOnly || (transform.posY() >= groundY && velocityY >= 0);
        if (jumpPressed && grounded && !crouch) {
            velocityY = -JUMP_SPEED;
        }
        jumpPressed = false;

        int direction = Boolean.compare(right, left);
        double x = transform.posX();
        if (direction != 0 && !(grounded && crouch)) {
            x += direction * MOVE_SPEED * dt;
        }
        x = Math.clamp(x, minX, maxX);

        double y = walkOnly ? groundY : transform.posY();
        if (!grounded || velocityY < 0) {
            y += (velocityY + 0.5 * GRAVITY * dt) * dt;
            velocityY += GRAVITY * dt;
        }
        if (y >= groundY && velocityY >= 0) {
            y = groundY;
            velocityY = 0;
        }

        boolean movedHorizontally = (float) x != transform.posX();
        if (movedHorizontally || (float) y != transform.posY()) {
            player.setLocal(transform.withPosition((float) x, (float) y));
        }

        if (walkOnly && direction != 0) {
            animator.setFlippedHorizontally(direction < 0);
        }

        if (y < groundY || velocityY < 0) {
            pose = PlayerPose.JUMP;
        } else if (crouch) {
            pose = PlayerPose.CROUCH;
        } else if (lookUp) {
            pose = PlayerPose.LOOK_UP;
        } else if (direction != 0 && (!walkOnly || movedHorizontally)) {
            pose = PlayerPose.WALK;
        } else {
            pose = PlayerPose.IDLE;
        }

        SpriteAnimation animation = switch (pose) {
            case WALK -> walk;
            case CROUCH -> crouchAnimation;
            case IDLE, LOOK_UP, JUMP -> idle;
        };
        if (animation != currentAnimation) {
            animator.play(animation);
            currentAnimation = animation;
        }
    }
}
