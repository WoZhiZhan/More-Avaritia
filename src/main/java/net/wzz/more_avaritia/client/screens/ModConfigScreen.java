package net.wzz.more_avaritia.client.screens;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.wzz.more_avaritia.config.ModConfig;

public class ModConfigScreen extends Screen {
    private final Screen lastScreen;

    public ModConfigScreen(Screen lastScreen) {
        super(Component.translatable("more_avaritia.config.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    public void init() {
        int centerX = width / 2;
        int startY = 40;
        int spacing = 28;
        addTitle(centerX, startY - 26);
        addToggleOption(
                Component.translatable("more_avaritia.config.kill_animals"),
                ModConfig.KILL_ANIMALS::get,
                centerX,
                startY,
                (newValue) -> {
                    ModConfig.KILL_ANIMALS.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        addToggleOption(
                Component.translatable("more_avaritia.config.kill_other"),
                ModConfig.KILL_OTHER_ENTITIES::get,
                centerX,
                startY + spacing,
                (newValue) -> {
                    ModConfig.KILL_OTHER_ENTITIES.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        addToggleOption(
                Component.translatable("more_avaritia.config.kill_players"),
                ModConfig.KILL_PLAYERS::get,
                centerX,
                startY + spacing * 2,
                (newValue) -> {
                    ModConfig.KILL_PLAYERS.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        addIntSlider(
                Component.translatable("more_avaritia.config.god_sword_range"),
                ModConfig.GOD_SWORD_RANGE,
                1,
                512,
                centerX,
                startY + spacing * 3
        );
        addIntSlider(
                Component.translatable("more_avaritia.config.god_sword_angle"),
                ModConfig.GOD_SWORD_ANGLE,
                10,
                360,
                centerX,
                startY + spacing * 4
        );
        this.addRenderableWidget(Button.builder(
                Component.translatable("more_avaritia.config.done"),
                (button) -> minecraft.setScreen(lastScreen)
        ).bounds(centerX - 100, height - 40, 200, 20).build());
    }

    private static Component optionText(Component label, Component value) {
        return Component.empty().append(label).append(" ").append(value);
    }

    private void addTitle(int x, int y) {
        this.addRenderableWidget(new EditBox(
                font, x - 100, y, 200, 20, Component.empty()) {{
            setValue(Component.translatable("more_avaritia.config.title").getString());
            setEditable(false);
            setBordered(false);
            moveCursorToStart();
        }});
    }

    private void addToggleOption(Component label, java.util.function.Supplier<Boolean> getter, int centerX, int y, java.util.function.Consumer<Boolean> onChange) {
        Button button = Button.builder(
                optionText(label, Component.translatable(getter.get() ? "more_avaritia.config.on" : "more_avaritia.config.off")),
                (b) -> {
                    boolean newValue = !getter.get();
                    b.setMessage(optionText(label, Component.translatable(newValue ? "more_avaritia.config.on" : "more_avaritia.config.off")));
                    onChange.accept(newValue);
                }
        ).bounds(centerX - 150, y, 300, 20).build();
        this.addRenderableWidget(button);
    }

    private void addIntSlider(Component label, ForgeConfigSpec.IntValue configValue, int min, int max, int centerX, int y) {
        AbstractSliderButton slider = new AbstractSliderButton(centerX - 150, y, 300, 20,
                optionText(label, Component.literal(String.valueOf(configValue.get()))),
                (configValue.get() - min) / (double) (max - min)) {
            @Override
            protected void updateMessage() {
                setMessage(optionText(label, Component.literal(String.valueOf(configValue.get()))));
            }

            @Override
            protected void applyValue() {
                configValue.set((int) Math.round(min + this.value * (max - min)));
            }

            @Override
            public void onRelease(double pMouseX, double pMouseY) {
                super.onRelease(pMouseX, pMouseY);
                ModConfig.CONFIG_SPEC.save();
            }
        };
        this.addRenderableWidget(slider);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(lastScreen);
    }
}
