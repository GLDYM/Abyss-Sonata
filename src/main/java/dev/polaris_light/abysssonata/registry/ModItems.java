package dev.polaris_light.abysssonata.registry;

import dev.polaris_light.abysssonata.AbyssSonata;
import dev.polaris_light.abysssonata.item.AbyssSonataItem;
import io.redspace.ironsspellbooks.render.CinderousRarity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, AbyssSonata.MODID);

    public static final DeferredHolder<Item, Item> ABYSS_SONATA = ITEMS.register("abyss_sonata",
            () -> new AbyssSonataItem(new Item.Properties()
                    .stacksTo(1)
                    .fireResistant()
                    .rarity(CinderousRarity.CINDEROUS_RARITY_PROXY.getValue())
                    .attributes(AbyssSonataItem.createAttributes())));

    private ModItems() {
    }
}
