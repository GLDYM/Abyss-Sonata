package dev.polaris_light.abysssonata.network;

import dev.polaris_light.abysssonata.AbyssSonata;
import dev.polaris_light.abysssonata.config.AbyssSonataConfig;
import dev.polaris_light.abysssonata.item.AbyssSonataItem;
import dev.polaris_light.abysssonata.registry.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DamageTypeScrollPayload(int direction) implements CustomPacketPayload {
    public static final Type<DamageTypeScrollPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AbyssSonata.MODID, "damage_type_scroll")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DamageTypeScrollPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, DamageTypeScrollPayload::direction, DamageTypeScrollPayload::new);

    @Override
    public Type<DamageTypeScrollPayload> type() {
        return TYPE;
    }

    public static void handle(DamageTypeScrollPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        if (!AbyssSonataConfig.damageTypeSwitchingEnabled) {
            return;
        }
        ItemStack stack = player.getMainHandItem().is(ModItems.ABYSS_SONATA.get())
                ? player.getMainHandItem()
                : player.getOffhandItem();
        if (!player.isShiftKeyDown() || !stack.is(ModItems.ABYSS_SONATA.get())) {
            return;
        }
        if (payload.direction() == 0) {
            AbyssSonataItem.clearDamageType(stack);
            player.displayClientMessage(Component.translatable("item.abyss_sonata.abyss_sonata.damage_type_cleared"), true);
            return;
        }

        AbyssSonataItem.cycleDamageType(
                stack,
                player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE),
                payload.direction() > 0
        ).ifPresentOrElse(
                id -> player.displayClientMessage(Component.translatable(
                        "item.abyss_sonata.abyss_sonata.selected_damage_type", id.toString()
                ), true),
                () -> player.displayClientMessage(Component.translatable(
                        "item.abyss_sonata.abyss_sonata.damage_type_cleared"
                ), true)
        );
    }
}
