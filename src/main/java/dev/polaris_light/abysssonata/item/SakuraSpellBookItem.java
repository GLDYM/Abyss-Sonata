package dev.polaris_light.abysssonata.item;

import com.google.common.collect.Multimap;
import dev.polaris_light.abysssonata.config.AbyssSonataConfig;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public final class SakuraSpellBookItem extends SpellBook {
    private static final int MAX_SPELL_SLOTS = 15;

    public SakuraSpellBookItem(Properties properties) {
        super(MAX_SPELL_SLOTS, properties);
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext,
            ResourceLocation uuid,
            ItemStack stack
    ) {
        if (!Curios.SPELLBOOK_SLOT.equals(slotContext.identifier())) {
            return super.getAttributeModifiers(slotContext, uuid, stack);
        }

        AttributeContainer[] attributes = AttributeConfigHelper.parseAttributeContainers(
                AbyssSonataConfig.sakuraSpellBookAttributes,
                "Sakura Spell Book"
        );
        return AttributeConfigHelper.buildCurioAttributeMap(
                attributes,
                String.format("%s_%s", slotContext.identifier(), slotContext.index())
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, Item.TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        lines.add(Component.translatable("item.abyss_sonata.sakura_spell_book.tooltip").withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(itemStack, context, lines, flag);
    }
}
