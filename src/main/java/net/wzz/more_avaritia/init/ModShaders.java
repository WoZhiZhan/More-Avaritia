package net.wzz.more_avaritia.init;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.wzz.more_avaritia.MoreAvaritiaMod;
import net.wzz.more_avaritia.client.comic.CCShaderInstance;

import java.io.IOException;
import java.util.Objects;

public class ModShaders {
    public static int renderTime;
    public static float renderFrame;
    public static CCShaderInstance COSMIC_SHADER;
    public static Uniform cosmicTime;
    public static Uniform cosmicYaw;
    public static Uniform cosmicPitch;
    public static Uniform cosmicExternalScale;
    public static Uniform cosmicOpacity;
    public static Uniform cosmicUVs;
    public static ShaderInstance boltShader;
    public static CCShaderInstance STAR_LIGHTNING_SHADER;
    public static Uniform starLightningTime;
    public static CCShaderInstance INFINITY_ENERGY_SHADER;
    public static Uniform infinityEnergyTime;
    public static CCShaderInstance INFINITY_ENERGY_GUI_SHADER;

    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        COSMIC_SHADER = CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation(MoreAvaritiaMod.MODID, "cosmic"), DefaultVertexFormat.BLOCK);
        event.registerShader(COSMIC_SHADER, ModShaders::cosmicShader);
        event.registerShader(
                new ShaderInstance(event.getResourceProvider(),
                        new ResourceLocation(MoreAvaritiaMod.MODID, "rendertype_bolt"),
                        DefaultVertexFormat.POSITION_COLOR_TEX),
                shader -> ModShaders.boltShader = shader);
        STAR_LIGHTNING_SHADER = CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation(MoreAvaritiaMod.MODID, "star_lightning"), DefaultVertexFormat.POSITION_COLOR);
        event.registerShader(STAR_LIGHTNING_SHADER, ModShaders::starLightningShader);
        INFINITY_ENERGY_SHADER = CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation(MoreAvaritiaMod.MODID, "infinity_energy"), DefaultVertexFormat.POSITION_COLOR_TEX);
        event.registerShader(INFINITY_ENERGY_SHADER, ModShaders::infinityEnergyShader);
        INFINITY_ENERGY_GUI_SHADER = CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation(MoreAvaritiaMod.MODID, "infinity_energy_gui"), DefaultVertexFormat.POSITION_COLOR_NORMAL);
        event.registerShader(INFINITY_ENERGY_GUI_SHADER, ModShaders::infinityEnergyGuiShader);
    }

    public static void starLightningShader(ShaderInstance shader) {
        STAR_LIGHTNING_SHADER = (CCShaderInstance) shader;
        starLightningTime = STAR_LIGHTNING_SHADER.getUniform("time");
        if (starLightningTime != null) {
            STAR_LIGHTNING_SHADER.onApply(() -> starLightningTime.set((float) renderTime + renderFrame));
        }
    }

    public static void infinityEnergyShader(ShaderInstance shader) {
        INFINITY_ENERGY_SHADER = (CCShaderInstance) shader;
        infinityEnergyTime = INFINITY_ENERGY_SHADER.getUniform("time");
        if (infinityEnergyTime != null) {
            INFINITY_ENERGY_SHADER.onApply(() -> infinityEnergyTime.set((float) renderTime + renderFrame));
        }
    }

    public static void infinityEnergyGuiShader(ShaderInstance shader) {
        INFINITY_ENERGY_GUI_SHADER = (CCShaderInstance) shader;
    }

    public static void cosmicShader(ShaderInstance shader) {
        COSMIC_SHADER = (CCShaderInstance) shader;
        cosmicTime = Objects.requireNonNull(COSMIC_SHADER.getUniform("time"));
        cosmicYaw = Objects.requireNonNull(COSMIC_SHADER.getUniform("yaw"));
        cosmicPitch = Objects.requireNonNull(COSMIC_SHADER.getUniform("pitch"));
        cosmicExternalScale = Objects.requireNonNull(COSMIC_SHADER.getUniform("externalScale"));
        cosmicOpacity = Objects.requireNonNull(COSMIC_SHADER.getUniform("opacity"));
        cosmicUVs = Objects.requireNonNull(COSMIC_SHADER.getUniform("cosmicuvs"));
        cosmicTime.set((float) renderTime + renderFrame);
        COSMIC_SHADER.onApply(() -> cosmicTime.set((float) renderTime + renderFrame));
    }
}
