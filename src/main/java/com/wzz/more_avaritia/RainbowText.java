package com.wzz.more_avaritia;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class RainbowText {
    public static final EnumChatFormatting[] Rainbow = new EnumChatFormatting[] { EnumChatFormatting.RED,
            EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA,
            EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE };

    public static String Format(String Input, EnumChatFormatting[] Style, double Delay, int Step) {
        StringBuilder Builder = new StringBuilder(Input.length() * 3);
        int Offset = (int) Math.floor(Minecraft.getSystemTime() / Delay) % Style.length;
        for (int i = 0; i < Input.length(); i++) {
            char c = Input.charAt(i);
            int col = (i * Step + Style.length - Offset) % Style.length;
            Builder.append(Style[col]);
            Builder.append(c);
        }
        return Builder.toString();
    }

    public static String Rainbow(String Input) {
        return Format(Input, Rainbow, 80.0D, 1);
    }
}
