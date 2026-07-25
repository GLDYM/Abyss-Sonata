package dev.polaris_light.abysssonata;

import dev.polaris_light.abysssonata.data.DataGenerators;
import dev.polaris_light.abysssonata.registry.ModItems;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(AbyssSonata.MODID)
public final class AbyssSonata {
    public static final String MODID = "abyss_sonata";

    public AbyssSonata(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreativeTabItems);
        modEventBus.addListener(DataGenerators::gatherData);
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeTabRegistry.EQUIPMENT_TAB.getKey()) {
            event.accept(ModItems.ABYSS_SONATA.get());
        }
    }
}
