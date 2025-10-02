//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.wzz.more_avaritia.colour;

public class ColorUtils {
    public ColorUtils() {
    }

    public static float[] HSVtoRGB(float h, float s, float v) {
        float[] hsv = new float[3];
        float[] rgb = new float[3];
        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
        if (hsv[0] == -1.0F) {
            rgb[0] = rgb[1] = rgb[2] = hsv[2];
        } else {
            int i = (int)Math.floor((double)hsv[0]);
            float f = hsv[0] - (float)i;
            if (i % 2 == 0) {
                f = 1.0F - f;
            }

            float m = hsv[2] * (1.0F - hsv[1]);
            float n = hsv[2] * (1.0F - hsv[1] * f);
            switch (i) {
                case 0:
                case 6:
                    rgb[0] = hsv[2];
                    rgb[1] = n;
                    rgb[2] = m;
                    break;
                case 1:
                    rgb[0] = n;
                    rgb[1] = hsv[2];
                    rgb[2] = m;
                    break;
                case 2:
                    rgb[0] = m;
                    rgb[1] = hsv[2];
                    rgb[2] = n;
                    break;
                case 3:
                    rgb[0] = m;
                    rgb[1] = n;
                    rgb[2] = hsv[2];
                    break;
                case 4:
                    rgb[0] = n;
                    rgb[1] = m;
                    rgb[2] = hsv[2];
                    break;
                case 5:
                    rgb[0] = hsv[2];
                    rgb[1] = m;
                    rgb[2] = n;
            }
        }

        return rgb;
    }

    public static int HSBToRGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int)(brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float)Math.floor((double)hue)) * 6.0F;
            float f = h - (float)Math.floor((double)h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch ((int)h) {
                case 0:
                    r = (int)(brightness * 255.0F + 0.5F);
                    g = (int)(t * 255.0F + 0.5F);
                    b = (int)(p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int)(q * 255.0F + 0.5F);
                    g = (int)(brightness * 255.0F + 0.5F);
                    b = (int)(p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int)(p * 255.0F + 0.5F);
                    g = (int)(brightness * 255.0F + 0.5F);
                    b = (int)(t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int)(p * 255.0F + 0.5F);
                    g = (int)(q * 255.0F + 0.5F);
                    b = (int)(brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int)(t * 255.0F + 0.5F);
                    g = (int)(p * 255.0F + 0.5F);
                    b = (int)(brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int)(brightness * 255.0F + 0.5F);
                    g = (int)(p * 255.0F + 0.5F);
                    b = (int)(q * 255.0F + 0.5F);
            }
        }

        return -16777216 | r << 16 | g << 8 | b;
    }
}
