package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.engine.GameLoop;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.PassBinding;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderPass;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.game.DemoAssets;
import it.unicam.cs.mpgc.rpg130396.game.DemoSceneFactory;
import it.unicam.cs.mpgc.rpg130396.javaFX.Input.JavaFXInputAdapter;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXBloomPass;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXNativeBloomPass;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.unicam.cs.mpgc.rpg130396.game.DemoConfig.LOGICAL_HEIGHT;
import static it.unicam.cs.mpgc.rpg130396.game.DemoConfig.LOGICAL_WIDTH;

public class JavaFXLauncher extends Application {

    private static final int BACKGROUND_TARGET_SCALE = 2;
    private static final double BLOOM_THRESHOLD = 0.78;
    private static final int BENCHMARK_WARMUP_FRAMES = 120;
    private static final int BENCHMARK_SAMPLE_FRAMES = 300;

    private Window window;
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> options = getParameters().getNamed();
        String assetMode = options.getOrDefault("assets", "procedural");
        String bloomMode = options.getOrDefault("bloom", "software");
        RenderPass bloom = switch (bloomMode) {
            case "off" -> null;
            case "software" -> new JavaFXBloomPass(BLOOM_THRESHOLD, 8, 0.65, true);
            case "native" -> new JavaFXNativeBloomPass(BLOOM_THRESHOLD);
            default -> throw new IllegalArgumentException("Usa --bloom=off, software o native");
        };

        System.out.printf("Demo assets=%s bloom=%s%n", assetMode, bloomMode);
        DemoAssets assets = switch (assetMode) {
            case "procedural" -> JavaFXDemoTextures.create();
            case "real" -> new JavaFXDemoAssets().load();
            default -> throw new IllegalArgumentException("Usa --assets=procedural o real");
        };
        Scene scene = new DemoSceneFactory().create(assets);
        window = new JavaFXWindow(primaryStage, "Kirby [" + assetMode + "]")
                .setWidth(1920).setHeight(1080).setResizable(false);

        boolean benchmark = Boolean.parseBoolean(options.getOrDefault("benchmark", "false"));
        PolledInput input = new PolledInput();
        if (!benchmark) {
            new JavaFXInputAdapter(primaryStage.getScene(), input);
            primaryStage.focusedProperty().addListener((observable, wasFocused, focused) -> {
                if (!focused) {
                    input.releaseAll();
                }
            });
        }

        GameLoop gameLoop = new GameLoop(createRenderer(window, bloom), scene, input);
        timer = benchmark ? benchmarkTimer(gameLoop, bloomMode) : new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameLoop.update(now);
            }
        };
        primaryStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> timer.stop());
        window.open();
        timer.start();
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        if (window != null) {
            window.close();
        }
    }

    private static AnimationTimer benchmarkTimer(GameLoop gameLoop, String bloomMode) {
        return new AnimationTimer() {
            private int pulses;
            private long sampleStart;
            private long updateNanos;

            @Override
            public void handle(long now) {
                if (++pulses <= BENCHMARK_WARMUP_FRAMES) {
                    gameLoop.update(now);
                    sampleStart = now;
                    return;
                }

                long start = System.nanoTime();
                gameLoop.update(now);
                updateNanos += System.nanoTime() - start;
                if (pulses == BENCHMARK_WARMUP_FRAMES + BENCHMARK_SAMPLE_FRAMES) {
                    double samplesToMillis = BENCHMARK_SAMPLE_FRAMES * 1_000_000.0;
                    System.out.printf("Bloom %s, %d frames: update/pipeline %.3f ms, pulse interval %.3f ms%n",
                            bloomMode, BENCHMARK_SAMPLE_FRAMES, updateNanos / samplesToMillis,
                            (now - sampleStart) / samplesToMillis);
                    stop();
                    Platform.exit();
                }
            }
        };
    }

    private static Renderer createRenderer(Window window, RenderPass bloom) {
        RenderTarget background = new JavaFXRenderTarget(
                LOGICAL_WIDTH * BACKGROUND_TARGET_SCALE, LOGICAL_HEIGHT * BACKGROUND_TARGET_SCALE);
        RenderTarget bridge = new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT);
        RenderTarget kirby = new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT);
        RenderTarget screen = window.getScreenTarget();
        Compositor compositor = new Compositor();
        List<PassBinding> passes = new ArrayList<>();
        RenderTarget backgroundOutput = background;
        if (bloom != null) {
            backgroundOutput = new JavaFXRenderTarget(background.width(), background.height());
            passes.add(new PassBinding(bloom, background, backgroundOutput, true));
        }
        passes.add(new PassBinding(compositor, backgroundOutput, screen, true));
        passes.add(new PassBinding(compositor, bridge, screen, false));
        passes.add(new PassBinding(compositor, kirby, screen, false));

        return new Renderer(window.getDrawContext(), Map.of(0, background, 1, bridge, 2, kirby),
                screen, LOGICAL_WIDTH, LOGICAL_HEIGHT, passes);
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
