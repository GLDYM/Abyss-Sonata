package dev.polaris_light.abysssonata.item;

import dev.polaris_light.abysssonata.client.renderer.item.AbyssSonataRenderer;
import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.IronsWeaponTier;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import java.util.List;
import java.util.function.Consumer;

public final class AbyssSonataItem extends StaffItem implements IPresetSpellContainer, GeoItem {
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

    public static ItemAttributeModifiers createAttributes() {
        return ExtendedSwordItem.createAttributes(TIER);
    }

    @Override
    public boolean hasCustomRendering() {
        return true;
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
        private static final AttributeContainer[] ATTRIBUTES = new AttributeContainer[]{
                new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, 0.20D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.COOLDOWN_REDUCTION, 0.30D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.SPELL_POWER, 0.30D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, 0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.MANA_REGEN, 0.50D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(Attributes.ENTITY_INTERACTION_RANGE, 1.0D, AttributeModifier.Operation.ADD_VALUE)
        };

        @Override
        public float getSpeed() {
            return -2.4F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 12.0F;
        }

        @Override
        public AttributeContainer[] getAdditionalAttributes() {
            return ATTRIBUTES;
        }
    }
}
