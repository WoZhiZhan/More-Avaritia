package net.wzz.more_avaritia.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.Sys;

import javax.annotation.Nonnull;
import java.awt.*;

public class RainbowFont extends FontRenderer {
  private static final Minecraft mc = Minecraft.getMinecraft();
  public static final RainbowFont Instance = new RainbowFont();

  public RainbowFont() {
    super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
  }

  public static long getSystemTime() {
    return Sys.getTime() * 1000L / Sys.getTimerResolution();
  }

  @Override
  public int drawString(String text, int x, int y, int color){
    float time = getSystemTime() / 1000.0f;
    for (int i = 0; i < text.length(); i++) {
      float red = (float)Math.sin(time * 3.5f + i) * 0.5f + 0.5f;
      float blue = (float)Math.sin(time * 3.0f + i) * 0.5f + 0.5f;
      int c = (color & 0xFF000000) | ((int)(red * 255) << 16) | ((int)(blue * 255));
      x = super.drawString(String.valueOf(text.charAt(i)), x, y, c, false);
      int charWidth = getStringWidth(String.valueOf(text.charAt(i)));
      if (text.charAt(i) >= 32 && text.charAt(i) <= 126) {
        x += charWidth + 6;
      } else {
        x += charWidth;
      }
    }
    return x;
  }

  @Override
  public int drawStringWithShadow(String text, float x, float y, int color) {
    long time = getSystemTime();
    float xOffset = 0;
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == '\u00a7') {
        i++;
        continue;
      }
      float red = (float) Math.sin(time / 300.0f + i * 0.5f) * 0.5f + 0.5f;
      float blue = (float) Math.sin(time / 300.0f + i * 0.3f) * 0.5f + 0.5f;
      int c = (color & 0xFF000000) | ((int) (red * 400) << 16) | ((int) (blue * 400));
      String character = String.valueOf(text.charAt(i));
      float charWidth = getStringWidth(character);
      super.drawStringWithShadow(character, x + xOffset, y, c);
      if (text.charAt(i) >= 32 && text.charAt(i) <= 126) {
        xOffset += charWidth + 6;
      } else {
        xOffset += charWidth;
      }
    }
    return (int) (x + xOffset);
  }

  @Override
  public void drawSplitString(String text, int x, int y, int wrapWidth, int textColor) {
    String[] lines = text.split("\n");
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      float yOffset = getLineYOffset(i);
      super.drawStringWithShadow(line, x, y + yOffset, textColor);
      y += getLineYOffset(i);
    }
  }

  private float getLineYOffset(int lineNumber) {
    float time = (float) getSystemTime() / 500.0f;
    float frequency = 1.5f;
    float amplitude = 2.0f;
    return (float) Math.sin(time * frequency + lineNumber) * amplitude;
  }

  public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
    return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
  }
}
