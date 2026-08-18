package dev.polaris_light.abysssonata.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class AbyssSonataConfig {
    private static final double DEFAULT_ATTACK_SPEED = -2.4D;
    private static final double DEFAULT_ATTACK_DAMAGE_BONUS = 16.0D;
    private static final List<String> DEFAULT_ADDITIONAL_ATTRIBUTES = List.of(
            "irons_spellbooks:cast_time_reduction|0.20|1",
            "irons_spellbooks:cooldown_reduction|0.30|1",
            "irons_spellbooks:spell_power|0.30|1",
            "irons_spellbooks:eldritch_spell_power|0.10|1",
            "irons_spellbooks:mana_regen|0.50|1",
            "minecraft:player.entity_interaction_range|1.0|0"
    );
    private static final List<String> DEFAULT_SAKURA_SPELL_BOOK_ATTRIBUTES = List.of(
            "irons_spellbooks:max_mana|500|0",
            "irons_spellbooks:spell_power|0.30|1",
            "irons_spellbooks:holy_spell_power|0.10|1",
            "irons_spellbooks:cooldown_reduction|0.20|1"
    );

    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue ATTACK_SPEED;
    public static final ModConfigSpec.DoubleValue ATTACK_DAMAGE_BONUS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ADDITIONAL_ATTRIBUTES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SAKURA_SPELL_BOOK_ATTRIBUTES;
    public static final ModConfigSpec.BooleanValue DAMAGE_TYPE_SWITCHING_ENABLED;
    public static double attackSpeed = DEFAULT_ATTACK_SPEED;
    public static double attackDamageBonus = DEFAULT_ATTACK_DAMAGE_BONUS;
    public static List<String> additionalAttributes = DEFAULT_ADDITIONAL_ATTRIBUTES;
    public static List<String> sakuraSpellBookAttributes = DEFAULT_SAKURA_SPELL_BOOK_ATTRIBUTES;
    public static boolean damageTypeSwitchingEnabled = true;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("abyssSonataTier");

        DAMAGE_TYPE_SWITCHING_ENABLED = builder
                .comment("Allow Abyss Sonata to select and apply a replacement damage type.")
                .define("enabled", true);

        ATTACK_SPEED = builder
                .comment("Attack speed modifier used by Abyss Sonata.")
                .defineInRange("attackSpeed", DEFAULT_ATTACK_SPEED, -4.0D, 1024.0D);
        ATTACK_DAMAGE_BONUS = builder
                .comment("Attack damage bonus used by Abyss Sonata.")
                .defineInRange("attackDamageBonus", DEFAULT_ATTACK_DAMAGE_BONUS, 0.0D, Double.MAX_VALUE);
        ADDITIONAL_ATTRIBUTES = builder
                .comment(
                        "Additional attribute list used by Abyss Sonata.",
                        "Format: attribute_id|value|operation",
                        "Operation uses AttributeModifier.Operation ordinal values:",
                        "0 = ADD_VALUE, 1 = ADD_MULTIPLIED_BASE, 2 = ADD_MULTIPLIED_TOTAL"
                )
                .defineListAllowEmpty(
                        "additionalAttributes",
                        DEFAULT_ADDITIONAL_ATTRIBUTES,
                        () -> "irons_spellbooks:cast_time_reduction|0.20|1",
                        value -> value instanceof String
                );

        builder.pop();

        builder.push("sakuraSpellBook");

        SAKURA_SPELL_BOOK_ATTRIBUTES = builder
                .comment(
                        "Additional attribute list used by Izumo no Kuni Fudoki.",
                        "Format: attribute_id|value|operation",
                        "Operation uses AttributeModifier.Operation ordinal values:",
                        "0 = ADD_VALUE, 1 = ADD_MULTIPLIED_BASE, 2 = ADD_MULTIPLIED_TOTAL"
                )
                .defineListAllowEmpty(
                        "additionalAttributes",
                        DEFAULT_SAKURA_SPELL_BOOK_ATTRIBUTES,
                        () -> "irons_spellbooks:max_mana|500|0",
                        value -> value instanceof String
                );

        builder.pop();

        SPEC = builder.build();
    }

    private AbyssSonataConfig() {
    }

    public static void bake() {
        damageTypeSwitchingEnabled = DAMAGE_TYPE_SWITCHING_ENABLED.get();
        attackSpeed = ATTACK_SPEED.get();
        attackDamageBonus = ATTACK_DAMAGE_BONUS.get();
        additionalAttributes = List.copyOf(ADDITIONAL_ATTRIBUTES.get().stream()
                .map(String::valueOf)
                .toList());
        sakuraSpellBookAttributes = List.copyOf(SAKURA_SPELL_BOOK_ATTRIBUTES.get().stream()
                .map(String::valueOf)
                .toList());
    }
}
