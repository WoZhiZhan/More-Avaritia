package net.wzz.more_avaritia.init;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.wzz.more_avaritia.MoreAvaritiaMod;
import net.wzz.more_avaritia.client.comic.CCShaderInstance;

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

    public static void onRegisterShaders(RegisterShadersEvent event) {
        COSMIC_SHADER = CCShaderInstance.create(event.getResourceProvider(), new ResourceLocation(MoreAvaritiaMod.MODID, "cosmic"), DefaultVertexFormat.BLOCK);
        event.registerShader(COSMIC_SHADER, ModShaders::cosmicShader);
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
