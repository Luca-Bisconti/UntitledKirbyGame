package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.input.Action;
import it.unicam.cs.mpgc.rpg130396.game.PlayerController;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    @Test
    void clampsMovementAtTheRightEdge() {
        var texture = new Texture() {
            public int width() { return 8; }
            public int height() { return 8; }
        };
        var frame = new TextureRegion(texture);
        var clip = new SpriteAnimation(java.util.List.of(frame), 0.25, true);
        var player = new Sprite("player");
        var controller = new PlayerController(player, new SpriteAnimator(player), clip, clip, 0, 10, 0);
        var input = new PolledInput();
        input.press(Action.RIGHT);
        controller.handleInput(input);
        controller.update(1);
        assertEquals(10f, player.getLocal().posX());
        assertEquals(PlayerController.PlayerPose.WALK, controller.getPose());
    }
}
