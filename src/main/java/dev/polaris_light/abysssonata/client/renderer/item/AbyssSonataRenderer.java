package dev.polaris_light.abysssonata.client.renderer.item;

import dev.polaris_light.abysssonata.AbyssSonata;
import dev.polaris_light.abysssonata.item.AbyssSonataItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public final class AbyssSonataRenderer extends GeoItemRenderer<AbyssSonataItem> {
    public AbyssSonataRenderer() {
        super(new DefaultedItemGeoModel<>(
                ResourceLocation.fromNamespaceAndPath(AbyssSonata.MODID, "abyss_sonata")));

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
