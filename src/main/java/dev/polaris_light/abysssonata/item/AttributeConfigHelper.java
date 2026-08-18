package dev.polaris_light.abysssonata.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

final class AttributeConfigHelper {
    private static final Logger LOGGER = LogUtils.getLogger();

    private AttributeConfigHelper() {
    }

    static AttributeContainer[] parseAttributeContainers(List<String> entries, String sourceName) {
        return entries.stream()
                .map(entry -> parseAttribute(entry, sourceName))
                .flatMap(Optional::stream)
                .toArray(AttributeContainer[]::new);
    }

    static Multimap<Holder<Attribute>, AttributeModifier> buildCurioAttributeMap(
            AttributeContainer[] attributes,
            String modifierKey
    ) {
        ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();
        for (AttributeContainer attribute : attributes) {
            builder.put(attribute.attribute(), attribute.createModifier(modifierKey));
        }
        return builder.build();
    }

    private static Optional<AttributeContainer> parseAttribute(String entry, String sourceName) {
        String[] parts = entry.split("\\|", 3);
        if (parts.length != 3) {
            LOGGER.warn("Invalid {} attribute entry '{}', expected format attribute_id|value|operation", sourceName, entry);
            return Optional.empty();
        }

        ResourceLocation attributeId = ResourceLocation.tryParse(parts[0].trim());
        if (attributeId == null) {
            LOGGER.warn("Invalid {} attribute id '{}'", sourceName, parts[0]);
            return Optional.empty();
        }

        Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.getHolder(attributeId);
        if (attributeHolder.isEmpty()) {
            LOGGER.warn("Unknown {} attribute '{}'", sourceName, attributeId);
            return Optional.empty();
        }

        try {
            double value = Double.parseDouble(parts[1].trim());
            int operationIndex = Integer.parseInt(parts[2].trim());
            AttributeModifier.Operation[] operations = AttributeModifier.Operation.values();
            if (operationIndex < 0 || operationIndex >= operations.length) {
                LOGGER.warn("Invalid {} attribute operation '{}' in entry '{}'", sourceName, operationIndex, entry);
                return Optional.empty();
            }

            return Optional.of(new AttributeContainer(attributeHolder.get(), value, operations[operationIndex]));
        } catch (NumberFormatException exception) {
            LOGGER.warn("Invalid {} attribute numeric value in entry '{}'", sourceName, entry);
            return Optional.empty();
        }
    }
}
