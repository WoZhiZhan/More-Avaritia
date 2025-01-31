package net.wzz.more_avaritia.util;

import net.minecraft.util.math.MathHelper;

public class ColorHelper {
  public static int getA(int col) {
    return (col & 0xFF000000) >>> 24;
  }
  
  public static int getR(int col) {
    return (col & 0xFF0000) >> 16;
  }
  
  public static int getG(int col) {
    return (col & 0xFF00) >> 8;
  }
  
  public static int getB(int col) {
    return col & 0xFF;
  }
  
  public static float getRF(int col) {
    return getR(col) / 255.0F;
  }
  
  public static float getGF(int col) {
    return getG(col) / 255.0F;
  }
  
  public static float getBF(int col) {
    return getB(col) / 255.0F;
  }
  
  public static float getAF(int col) {
    return getA(col) / 255.0F;
  }
  
  public static int makeAlphaWhite(int alpha) {
    if (alpha <= 0)
      return 0; 
    if (alpha >= 255)
      return -1; 
    return (alpha & 0xFF) << 24 | 0xFFFFFF;
  }
  
  public static int color(int r, int g, int b, int a) {
    return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
  }
  
  public static float[] colToFloat(int rgb) {
    return new float[] { getA(rgb) / 255.0F, 
        getR(rgb) / 255.0F, 
        getG(rgb) / 255.0F, 
        getB(rgb) / 255.0F };
  }
  
  public static int floatsToCol(float[] rgb) {
    return color((int)(rgb[1] * 255.0F), (int)(rgb[2] * 255.0F), (int)(rgb[3] * 255.0F), (int)(rgb[0] * 255.0F));
  }
  
  public static int brightness(int col) {
    return brightness(getR(col), getG(col), getB(col));
  }
  
  public static int brightness(int r, int g, int b) {
    return r * 109 + g * 366 + b * 37 >> 9;
  }
  
  public static int makeGray(int b) {
    return color(b, b, b, 255);
  }
  
  public static int colorClamp(float r, float g, float b, float a) {
    return color(clamp(r), clamp(g), clamp(b), clamp(a));
  }
  
  public static int clamp(float f) {
    return MathHelper.clamp((int)(f * 255.0F), 0, 255);
  }
  
  public static int multShade(int input, float perc) {
    if (perc >= 1.0F || input == 0)
      return input; 
    if (perc <= 0.0F)
      return input & 0xFF000000; 
    return color(
        Math.round(getR(input) * perc), 
        Math.round(getG(input) * perc), 
        Math.round(getB(input) * perc), 
        getA(input));
  }
  
  public static int alpha(int color, float alpha) {
    if (alpha == 0.0F)
      return 0; 
    float af = getAF(color);
    if (af == 0.0F)
      return 0; 
    return colorClamp(
        getRF(color), 
        getGF(color), 
        getBF(color), af * alpha);
  }
}
