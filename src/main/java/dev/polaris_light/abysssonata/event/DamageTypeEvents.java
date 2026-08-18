package dev.polaris_light.abysssonata.event;

import dev.polaris_light.abysssonata.AbyssSonata;
import dev.polaris_light.abysssonata.config.AbyssSonataConfig;
import dev.polaris_light.abysssonata.item.AbyssSonataItem;
import dev.polaris_light.abysssonata.registry.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = AbyssSonata.MODID)
public final class DamageTypeEvents {
    private DamageTypeEvents() {
    }

    @SubscribeEvent
    public static void replaceDamageType(LivingIncomingDamageEvent event) {
        if (!AbyssSonataConfig.damageTypeSwitchingEnabled || !(event.getSource().getEntity() instanceof Player player)
                || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        ItemStack staff = player.getMainHandItem().is(ModItems.ABYSS_SONATA.get())
                ? player.getMainHandItem()
                : player.getOffhandItem();
        if (!staff.is(ModItems.ABYSS_SONATA.get())) {
            return;
        }

        AbyssSonataItem.getSelectedDamageType(staff).ifPresent(id -> replace(event, serverLevel, id));
    }

    private static void replace(LivingIncomingDamageEvent event, ServerLevel level, ResourceLocation id) {
        Holder.Reference<DamageType> replacement = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(ResourceKey.create(Registries.DAMAGE_TYPE, id)).orElse(null);
        if (replacement == null || event.getSource().typeHolder().equals(replacement)) {
            return;
        }

        DamageSource source = event.getSource();
        event.setCanceled(true);
        event.getEntity().hurt(new DamageSource(replacement, source.getDirectEntity(), source.getEntity()), event.getAmount());
    }
}
