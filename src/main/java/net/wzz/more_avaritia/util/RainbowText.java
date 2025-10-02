package net.wzz.more_avaritia.util;


import net.minecraft.ChatFormatting;
import net.minecraft.Util;

public class RainbowText {
  private static final ChatFormatting[] fabulousness;
  private static final ChatFormatting[] sanic;
  private static final ChatFormatting[] colour = new ChatFormatting[] {
          ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE };
  private static String formatting(String input, ChatFormatting[] colours, double delay)
  {
    StringBuilder sb = new StringBuilder(input.length() * 3);
    if (delay <= 0.0D) {
      delay = 0.001D;
    }
    int offset = (int)Math.floor((System.currentTimeMillis() & 0x3FFF) / delay) % colours.length;
    for (int i = 0; i < input.length(); i++)
    {
      char c = input.charAt(i);
      sb.append(colours[((colours.length + i - offset) % colours.length)].toString());
      sb.append(c);
    }
    return sb.toString();
  }
  public static String makeFabulous(String input) {
    return ludicrousFormatting(input, fabulousness, (double)80.0F, 1, 1);
  }
  public static String makeSANIC(String input) {
    return ludicrousFormatting(input, sanic, (double)50.0F, 2, 1);
  }
  public static String ludicrousFormatting(String input, ChatFormatting[] colours, double delay, int step, int posstep) {
    StringBuilder sb = new StringBuilder(input.length() * 3);
    if (delay <= (double)0.0F) {
      delay = 0.001;
    }

    int offset = (int)Math.floor((double) Util.getMillis() / delay) % colours.length;

    for(int i = 0; i < input.length(); ++i) {
      char c = input.charAt(i);
      int col = (i * posstep + colours.length - offset) % colours.length;
      sb.append(colours[col].toString());
      sb.append(c);
    }

    return sb.toString();
  }
  public static String makeColour(String input)
  {
    return formatting(input, colour, 80.0D);
  }
  static {
    sanic = new ChatFormatting[]{ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.RED, ChatFormatting.WHITE, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY};
    fabulousness = new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE};
  }
}