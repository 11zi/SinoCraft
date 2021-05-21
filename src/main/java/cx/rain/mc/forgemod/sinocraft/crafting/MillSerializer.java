package cx.rain.mc.forgemod.sinocraft.crafting;

import com.google.gson.JsonObject;
import cx.rain.mc.forgemod.sinocraft.api.crafting.IModRecipeSerializer;
import cx.rain.mc.forgemod.sinocraft.utility.CraftingHelper;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class MillSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IModRecipeSerializer<MillRecipe> {

    static final MillSerializer SERIALIZER = new MillSerializer();

    @Override
    public MillRecipe read(ResourceLocation recipeId, JsonObject json) {
        return (MillRecipe) MillRecipe.builder(recipeId)
                .setInput(CraftingHelper.deserializeIngredient(json.get("input")))
                .setOutput(CraftingHelper.deserializeItem(json.get("output")))
                .setTime(json.get("time").getAsInt())
                .build();
    }

    @Nullable
    @Override
    public MillRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        return (MillRecipe) MillRecipe.builder(recipeId)
                .setInput(CountIngredient.read(buffer))
                .setOutput(buffer.readItemStack())
                .setTime(buffer.readVarInt())
                .build();
    }

    @Override
    public void write(PacketBuffer buffer, MillRecipe recipe) {
        recipe.getInput().write(buffer);
        buffer.writeItemStack(recipe.getRecipeOutput());
        buffer.writeVarInt(recipe.getTime());
    }

    @Override
    public void write(JsonObject json, MillRecipe recipe) {
        json.add("input", CraftingHelper.serializeIngredient(recipe.getInput()));
        json.add("output", CraftingHelper.serializeItem(recipe.getRecipeOutput()));
        json.addProperty("time", recipe.getTime());
    }
}
