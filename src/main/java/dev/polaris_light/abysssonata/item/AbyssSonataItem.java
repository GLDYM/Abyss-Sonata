package dev.polaris_light.abysssonata.item;

import dev.polaris_light.abysssonata.client.renderer.item.AbyssSonataRenderer;
import dev.polaris_light.abysssonata.config.AbyssSonataConfig;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public final class AbyssSonataItem extends StaffItem implements IPresetSpellContainer, GeoItem {
    private static final String DAMAGE_TYPE_KEY = "AbyssSonataDamageType";
    private static final RawAnimation IDLE_ANIMATION =
            RawAnimation.begin().thenLoop("animation.abyss_sonata.default");
    private static final IronsWeaponTier TIER = new AbyssSonataTier();
    private static final SpellDataRegistryHolder[] PRESET_SPELLS = SpellDataRegistryHolder.of(
            new SpellDataRegistryHolder(SpellRegistry.ECHOING_STRIKES_SPELL, 5)
    );

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private List<SpellData> spellData;

    public AbyssSonataItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomRendering() {
        return true;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ExtendedSwordItem.createAttributes(TIER);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null || ISpellContainer.isSpellContainer(itemStack)) {
            return;
        }

        List<SpellData> spells = getSpells();
        var spellContainer = ISpellContainer.create(spells.size(), true, false).mutableCopy();
        spells.forEach(spellData -> spellContainer.addSpell(spellData.getSpell(), spellData.getLevel(), true));
        ISpellContainer.set(itemStack, spellContainer.toImmutable());
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment)
                || new ItemStack(Items.NETHERITE_SWORD).supportsEnchantment(enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return super.isPrimaryItemFor(stack, enchantment)
                || new ItemStack(Items.NETHERITE_SWORD).isPrimaryItemFor(enchantment);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false; // Disable the enchantment glint effect
    }

    public static Optional<ResourceLocation> getSelectedDamageType(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return Optional.empty();
        }
        String value = customData.copyTag().getString(DAMAGE_TYPE_KEY);
        if (value.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(ResourceLocation.tryParse(value)).filter(id -> !id.getPath().isBlank());
    }

    public static Optional<ResourceLocation> cycleDamageType(ItemStack stack, Registry<DamageType> registry, boolean forward) {
        List<ResourceLocation> damageTypes = registry.keySet().stream().sorted(Comparator.comparing(ResourceLocation::toString)).toList();
        if (damageTypes.isEmpty()) {
            return Optional.empty();
        }

        int currentIndex = getSelectedDamageType(stack).map(damageTypes::indexOf)
                .filter(index -> index >= 0)
                .map(index -> index + 1)
                .orElse(0);
        int nextIndex = Math.floorMod(currentIndex + (forward ? 1 : -1), damageTypes.size() + 1);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (nextIndex == 0) {
            tag.remove(DAMAGE_TYPE_KEY);
        } else {
            tag.putString(DAMAGE_TYPE_KEY, damageTypes.get(nextIndex - 1).toString());
        }
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return getSelectedDamageType(stack);
    }

    public static void clearDamageType(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.remove(DAMAGE_TYPE_KEY);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext context, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        lines.add(Component.translatable("item.abyss_sonata.abyss_sonata.damage_type_switch").withStyle(ChatFormatting.AQUA));
        getSelectedDamageType(stack).ifPresent(id -> lines.add(Component.translatable(
                "item.abyss_sonata.abyss_sonata.selected_damage_type", id.toString()
        ).withStyle(ChatFormatting.AQUA)));
        super.appendHoverText(stack, context, lines, flag);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AbyssSonataRenderer renderer;

            @Override
            public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new AbyssSonataRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 0,
                state -> state.setAndContinue(IDLE_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private List<SpellData> getSpells() {
        if (this.spellData == null) {
            this.spellData = Arrays.stream(PRESET_SPELLS).map(SpellDataRegistryHolder::getSpellData).toList();
        }
        return this.spellData;
    }

    private static final class AbyssSonataTier implements IronsWeaponTier {
        @Override
        public float getSpeed() {
            return (float) AbyssSonataConfig.attackSpeed;
        }

        @Override
        public float getAttackDamageBonus() {
            return (float) AbyssSonataConfig.attackDamageBonus;
        }

        @Override
        public AttributeContainer[] getAdditionalAttributes() {
            return AttributeConfigHelper.parseAttributeContainers(AbyssSonataConfig.additionalAttributes, "Abyss Sonata");
        }
    }
}
