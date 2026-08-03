package net.wzz.more_avaritia.client.screens;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.wzz.more_avaritia.config.ModConfig;

public class ModConfigScreen extends Screen {
    private final Screen lastScreen;

    public ModConfigScreen(Screen lastScreen) {
        super(Component.literal("More Avaritia Config"));
        this.lastScreen = lastScreen;
    }

    @Override
    public void init() {
        int centerX = width / 2;
        int startY = 60;
        int spacing = 30;
        addTitle(centerX, startY - 30);
        addToggleOption(
                Component.literal("Kill Animals: "),
                ModConfig.KILL_ANIMALS.get(),
                centerX,
                startY,
                (newValue) -> {
                    ModConfig.KILL_ANIMALS.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        addToggleOption(
                Component.literal("Kill Other Entities: "),
                ModConfig.KILL_OTHER_ENTITIES.get(),
                centerX,
                startY + spacing,
                (newValue) -> {
                    ModConfig.KILL_OTHER_ENTITIES.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        addToggleOption(
                Component.literal("Kill Players: "),
                ModConfig.KILL_PLAYERS.get(),
                centerX,
                startY + spacing * 2,
                (newValue) -> {
                    ModConfig.KILL_PLAYERS.set(newValue);
                    ModConfig.CONFIG_SPEC.save();
                }
        );
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                (button) -> minecraft.setScreen(lastScreen)
        ).bounds(centerX - 100, height - 40, 200, 20).build());
    }

    private void addTitle(int x, int y) {
        this.addRenderableWidget(new net.minecraft.client.gui.components.EditBox(
                font, x - 100, y, 200, 20, Component.literal("Title")) {{
            setValue("More Avaritia Configuration");
            setEditable(false);
            setBordered(false);
            moveCursorToStart();
        }});
    }

    private void addToggleOption(Component label, boolean currentValue, int centerX, int y, java.util.function.Consumer<Boolean> onChange) {
        String status = currentValue ? "ON" : "OFF";

        this.addRenderableWidget(Button.builder(
                Component.literal(label.getString() + status),
                (button) -> {
                    boolean newValue = !currentValue;
                    button.setMessage(Component.literal(label.getString() + (newValue ? "ON" : "OFF")));
                    onChange.accept(newValue);
                }
        ).bounds(centerX - 150, y, 300, 20).build());
    }

    @Override
    public void onClose() {
        minecraft.setScreen(lastScreen);
    }
}