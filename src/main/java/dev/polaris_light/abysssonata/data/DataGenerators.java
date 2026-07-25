package dev.polaris_light.abysssonata.data;

import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class DataGenerators {
    private DataGenerators() {
    }

    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.createProvider(ModRecipeProvider::new);
        }
    }
}
