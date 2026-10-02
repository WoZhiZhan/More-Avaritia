package net.wzz.more_avaritia.client.comic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.wzz.more_avaritia.client.model.InfinityEnergyModel;

import java.util.function.Function;

public class InfinityEnergyModelLoader implements IGeometryLoader<InfinityEnergyModelLoader.EnergyGeometry> {
    public static final InfinityEnergyModelLoader INSTANCE = new InfinityEnergyModelLoader();

    @Override
    public EnergyGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject clean = modelContents.deepCopy();
        clean.remove("loader");
        BlockModel baseModel = deserializationContext.deserialize(clean, BlockModel.class);
        return new EnergyGeometry(baseModel);
    }

    public static class EnergyGeometry implements IUnbakedGeometry<EnergyGeometry> {
        private final BlockModel baseModel;

        public EnergyGeometry(BlockModel baseModel) {
            this.baseModel = baseModel;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel baseBakedModel = this.baseModel.bake(baker, this.baseModel, spriteGetter, modelState, modelLocation, true);
            return new InfinityEnergyModel(baseBakedModel);
        }

        @Override
        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            this.baseModel.resolveParents(modelGetter);
        }
    }
}
