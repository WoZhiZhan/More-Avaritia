package net.wzz.more_avaritia.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.function.Function;

public class InfinityFont extends Font {
    public InfinityFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_) {
        super(p_243253_, p_243245_);
    }

    public static InfinityFont getFont() {
        return new InfinityFont(Minecraft.getInstance().font.fonts, false);
    }

    public int drawInBatch(@NotNull FormattedCharSequence formattedCharSequence, float x, float y, int rgb, boolean b1, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource multiBufferSource, @NotNull DisplayMode mode, int i, int i1) {
        StringBuilder stringBuilder = new StringBuilder();
        formattedCharSequence.accept((index, style, codePoint) -> {
            stringBuilder.appendCodePoint(codePoint);
            return true;
        });
        String text = ChatFormatting.stripFormatting(stringBuilder.toString());
        if (text != null && !text.equals(I18n.get("item.more_avaritia.infinity_god_sword")) && !text.equals(I18n.get("item.more_avaritia.universe_heart")))
            return super.drawInBatch(formattedCharSequence,x,y,rgb,b1,matrix4f,multiBufferSource,mode,i,i1);
        if (text != null) {
            float hueOffset = (float) net.minecraft.Util.getMillis() / 3000.0F;
            int textLength = text.length();
            float hueIncrement = 1.0F / textLength;
            for (int index = 0; index < textLength; index++) {
                String s = String.valueOf(text.charAt(index));
                float hue = (hueOffset + hueIncrement * index) % 1.0F;
                int c = Mth.hsvToRgb(hue, 0.8F, 1.0F);
                int red = (c >> 16) & 0xFF;
                int green = (c >> 8) & 0xFF;
                int blue = c & 0xFF;
                int finalRGB = (red << 16) | (green << 8) | blue;
                super.drawInBatch(s, x, y, finalRGB * c, b1, matrix4f, multiBufferSource, mode, i, i1);
                super.drawInBatch(s, x, y+1, finalRGB, b1, matrix4f, multiBufferSource, mode, i, i1);
                super.drawInBatch(s, x, y-0.5f, -finalRGB, b1, matrix4f, multiBufferSource, mode, i, i1);
                x += this.width(s);
            }
        }
        return (int) x;
    }


    public int drawInBatch(@NotNull String string, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1) {
        return this.drawInBatch(Component.literal(string).getVisualOrderText(), x, y, rgb, b, matrix4f, source, mode, i, i1);
    }

    public int drawInBatch(@NotNull Component component, float x, float y, int rgb, boolean b, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource source, @NotNull DisplayMode mode, int i, int i1) {
        return this.drawInBatch(component.getVisualOrderText(), x, y, rgb, b, matrix4f, source, mode, i, i1);
    }
    public static long milliTime() {
        return System.nanoTime() / 1000000L;
    }
    public static int getColor(float index) {
        return (Color.HSBtoRGB((float) milliTime() / 700.0F % 1.0F, 0.8f, 0.8f));
    }
}