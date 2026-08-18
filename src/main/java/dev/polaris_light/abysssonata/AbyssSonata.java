package dev.polaris_light.abysssonata;

import dev.polaris_light.abysssonata.client.ClientEvent;
import dev.polaris_light.abysssonata.config.AbyssSonataConfig;
import dev.polaris_light.abysssonata.data.DataGenerators;
import dev.polaris_light.abysssonata.network.ModPayloads;
import dev.polaris_light.abysssonata.registry.ModItems;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(AbyssSonata.MODID)
public final class AbyssSonata {
    public static final String MODID = "abyss_sonata";

    public AbyssSonata(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, AbyssSonataConfig.SPEC);
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreativeTabItems);
        modEventBus.addListener(DataGenerators::gatherData);
        modEventBus.addListener(ModPayloads::register);
        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientEvent::clientSetup);
            ClientEvent.registerConfigScreen(modContainer);
        }
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeTabRegistry.EQUIPMENT_TAB.getKey()) {
            event.accept(ModItems.ABYSS_SONATA.get());
            event.accept(ModItems.SAKURA_SPELL_BOOK.get());
        }
    }
}
