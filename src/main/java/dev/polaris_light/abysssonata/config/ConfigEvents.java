package dev.polaris_light.abysssonata.config;

import dev.polaris_light.abysssonata.AbyssSonata;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = AbyssSonata.MODID)
public final class ConfigEvents {
    private ConfigEvents() {
    }

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(AbyssSonata.MODID)) {
            AbyssSonataConfig.bake();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(AbyssSonata.MODID)) {
            AbyssSonataConfig.bake();
        }
    }
}
