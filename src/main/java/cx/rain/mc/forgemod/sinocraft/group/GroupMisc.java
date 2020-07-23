package cx.rain.mc.forgemod.sinocraft.group;

import cx.rain.mc.forgemod.sinocraft.SinoCraft;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GroupMisc extends ItemGroup {
    private static final String label = "misc";

    public GroupMisc() {
        super(SinoCraft.MODID + "." + label);
    }

    @Override
    public ItemStack createIcon() {
        return ItemStack.EMPTY;
    }
}