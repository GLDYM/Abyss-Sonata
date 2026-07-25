package dev.polaris_light.abysssonata.data;

import dev.polaris_light.abysssonata.registry.ModItems;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ABYSS_SONATA.get())
                .define('A', Items.ECHO_SHARD)
                .define('B', ItemRegistry.AMETHYST_RAPIER.get())
                .define('C', ItemRegistry.ICE_STAFF.get())
                .define('D', ItemRegistry.ELDRITCH_PAGE.get())
                .define('E', ItemRegistry.BLOOD_STAFF.get())
                .define('F', ItemRegistry.PYRIUM_STAFF.get())
                .pattern("ABA")
                .pattern("CDE")
                .pattern("AFA")
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                .save(recipeOutput);
    }
}
