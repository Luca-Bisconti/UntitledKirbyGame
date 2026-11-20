package it.unicam.cs.mpgc.rpg130396.javaFX.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderPass;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.util.Objects;





public final class JavaFXBloomPass implements RenderPass {

    public static final int MAX_RADIUS = 32;

    private final double threshold;
    private final int radius;
    private final double intensity;
    private final float[] kernel;
    private boolean enabled;
    private int width;
    private int height;
    private int[] pixels;
    private float[] bright;
    private float[] horizontal;






    public JavaFXBloomPass(double threshold, int radius, double intensity, boolean enabled) {
        if (!Double.isFinite(threshold) || threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException("Bloom threshold must be in [0, 1]");
        }
        if (radius < 0 || radius > MAX_RADIUS) {
            throw new IllegalArgumentException("Bloom radius must be in [0, " + MAX_RADIUS + "]");
        }
        if (!Double.isFinite(intensity) || intensity < 0) {
            throw new IllegalArgumentException("Bloom intensity must be finite and non-negative");
        }
        this.threshold = threshold;
        this.radius = radius;
        this.intensity = intensity;
        this.enabled = enabled;
        kernel = createKernel(radius);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render(DrawContext context, RenderTarget input, RenderTarget output) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);
        if (input == output || (input instanceof JavaFXRenderTarget source
                && output instanceof JavaFXRenderTarget destination && source.canvas() == destination.canvas())) {
            throw new IllegalArgumentException("Bloom requires distinct input and output targets");
        }
        if (output.width() <= 0 || output.height() <= 0) {
            throw new IllegalArgumentException("Bloom output dimensions must be positive");
        }
        if (!(input.asTexture() instanceof JavaFXTexture texture)) {
            throw new IllegalArgumentException("Bloom input must expose a JavaFX texture");
        }


        WritableImage result;
        if (!enabled || intensity == 0) {
            result = new WritableImage(texture.image().getPixelReader(), texture.width(), texture.height());
        } else {
            prepareBuffers(texture.width(), texture.height());
            var format = PixelFormat.getIntArgbPreInstance();
            texture.image().getPixelReader().getPixels(0, 0, width, height, format, pixels, 0, width);
            if (extractBrightPixels()) {
                if (radius > 0) {
                    blur(bright, horizontal, true);
                    blur(horizontal, bright, false);
                }
                combine();
            }
            result = new WritableImage(width, height);
            result.getPixelWriter().setPixels(0, 0, width, height, format, pixels, 0, width);
        }

        float scale = Math.min(output.width() / (float) texture.width(), output.height() / (float) texture.height());
        float x = (output.width() - texture.width() * scale) / 2;
        float y = (output.height() - texture.height() * scale) / 2;
        context.drawTexture(new TextureRegion(new JavaFXTexture(result)), new Transform2D(x, y, 0, scale));
    }

    private void prepareBuffers(int width, int height) {
        if (this.width == width && this.height == height) {
            return;
        }
        int count = Math.multiplyExact(width, height);
        int channels = Math.multiplyExact(count, 3);
        pixels = new int[count];
        bright = new float[channels];
        horizontal = radius > 0 ? new float[channels] : null;
        this.width = width;
        this.height = height;
    }

    private boolean extractBrightPixels() {
        boolean found = false;
        for (int i = 0, channel = 0; i < pixels.length; i++, channel += 3) {
            int pixel = pixels[i];
            int alpha = pixel >>> 24;
            int red = (pixel >>> 16) & 255;
            int green = (pixel >>> 8) & 255;
            int blue = pixel & 255;


            boolean luminous = alpha != 0 && 2126 * red + 7152 * green + 722 * blue > threshold * alpha * 10_000;
            bright[channel] = luminous ? red : 0;
            bright[channel + 1] = luminous ? green : 0;
            bright[channel + 2] = luminous ? blue : 0;
            found |= luminous;
        }
        return found;
    }

    private void blur(float[] source, float[] destination, boolean alongRows) {
        int stride = alongRows ? 3 : width * 3;
        int last = (alongRows ? width : height) - 1;
        for (int y = 0, channel = 0; y < height; y++) {
            for (int x = 0; x < width; x++, channel += 3) {
                int coordinate = alongRows ? x : y;
                int firstOffset = -Math.min(radius, coordinate);
                int lastOffset = Math.min(radius, last - coordinate);
                float red = 0;
                float green = 0;
                float blue = 0;
                for (int offset = -radius; offset <= radius; offset++) {
                    int sample = channel + Math.clamp(offset, firstOffset, lastOffset) * stride;
                    float weight = kernel[offset + radius];
                    red += source[sample] * weight;
                    green += source[sample + 1] * weight;
                    blue += source[sample + 2] * weight;
                }
                destination[channel] = red;
                destination[channel + 1] = green;
                destination[channel + 2] = blue;
            }
        }
    }

    private void combine() {
        for (int i = 0, channel = 0; i < pixels.length; i++, channel += 3) {
            int pixel = pixels[i];
            int originalAlpha = pixel >>> 24;
            double glowRed = Math.min(255, intensity * bright[channel]);
            double glowGreen = Math.min(255, intensity * bright[channel + 1]);
            double glowBlue = Math.min(255, intensity * bright[channel + 2]);
            double glowAlpha = Math.max(glowRed, Math.max(glowGreen, glowBlue));

            int alpha = (int) Math.round(originalAlpha + glowAlpha * (255 - originalAlpha) / 255);
            int red = Math.min(alpha, ((pixel >>> 16) & 255) + (int) Math.round(glowRed));
            int green = Math.min(alpha, ((pixel >>> 8) & 255) + (int) Math.round(glowGreen));
            int blue = Math.min(alpha, (pixel & 255) + (int) Math.round(glowBlue));
            pixels[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    private static float[] createKernel(int radius) {
        float[] weights = new float[2 * radius + 1];
        double sigma = Math.max(0.5, radius / 2.0);
        double sum = 0;
        for (int offset = -radius; offset <= radius; offset++) {
            float weight = (float) Math.exp(-(offset * offset) / (2 * sigma * sigma));
            weights[offset + radius] = weight;
            sum += weight;
        }
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= sum;
        }
        return weights;
    }
}
