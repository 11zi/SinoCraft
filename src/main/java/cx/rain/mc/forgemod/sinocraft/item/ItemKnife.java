package cx.rain.mc.forgemod.sinocraft.item;

import cx.rain.mc.forgemod.sinocraft.SinoCraft;
import cx.rain.mc.forgemod.sinocraft.api.interfaces.IFactory;
import cx.rain.mc.forgemod.sinocraft.api.interfaces.IShave;
import cx.rain.mc.forgemod.sinocraft.api.interfaces.defaultImpl.ShaveBase;
import cx.rain.mc.forgemod.sinocraft.block.base.BlockLog;
import cx.rain.mc.forgemod.sinocraft.group.Groups;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemKnife extends Item {
	public static List<IFactory<IShave,ItemUseContext>> shaveManagers = new ArrayList();

	public static void dropItem(World worldIn, BlockPos pos, ItemStack stack){
        worldIn.addEntity(new ItemEntity(worldIn.getWorld(),
                pos.getX(),pos.getY(),pos.getZ(),stack));
    }

    public static void dropItem(World worldIn, BlockPos pos, ItemStack stack, Direction face) {
	    int x=pos.getX();
	    int y=pos.getY();
	    int z=pos.getZ();
	    switch (face){
            case EAST: x+=1;break;
            case WEST: x-=1;break;
            case UP: y+=1;break;
            case DOWN: y-=1;break;
            case NORTH: z-=1;break;
            case SOUTH: z+=1;break;
        }
        worldIn.addEntity(new ItemEntity(worldIn.getWorld(), x,y,z,stack));
    }

	public static class DefaultManager implements IFactory<IShave, ItemUseContext> {
        private static Map<Block, IShave> recipes = new HashMap();

        public DefaultManager(){
            initIShave();
        }

        private void initIShave(){

        }

        public static void addShaveRecipe(Block block,IShave shave){
            recipes.put(block,shave);
        }

        public static void addShaveRecipe(Block block, BlockState replace, ItemStack... stacks){
            addShaveRecipe(block,new ShaveBase(replace,stacks));
        }

        @Override
        public IShave get(ItemUseContext type, @Nullable Object[] args){
            if(recipes.containsKey(type.getWorld().getBlockState(type.getPos()).getBlock())){
                return recipes.get(type.getWorld().getBlockState(type.getPos()).getBlock());
            }
            return null;
        }
    }

    private static class ShaveBarkManager implements IFactory<IShave, ItemUseContext> {
        public ShaveBarkManager(){

        }

        @Override
        public IShave get(ItemUseContext type, @Nullable Object[] args){
            if(type.getWorld().getBlockState(type.getPos()).getBlock() instanceof BlockLog){
                BlockLog b = (BlockLog) type.getWorld().getBlockState(type.getPos()).getBlock();
                if(b.kind!= BlockLog.KIND.LOG){
                    return null;
                }
                return (context) -> {
                    BlockLog block = (BlockLog)context.getWorld().getBlockState(context.getPos()).getBlock();
                    context.getWorld().setBlockState(context.getPos(),
                            block.type.getTag().LogStripped.get().getDefaultState());
                    dropItem(context.getWorld(), context.getPos(),
                            new ItemStack(block.type.getTag().Bark.get(), context.getWorld().getRandom().nextInt(2)),
                            context.getFace()
                    );
                };
            }
            return null;
        }
    }

    public ItemKnife() {
        super(new Item.Properties().group(Groups.MISC));
        initManager();
    }

    void initManager(){
	    shaveManagers.add(new DefaultManager());
	    shaveManagers.add(new ShaveBarkManager());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
	    for(IFactory<IShave,ItemUseContext> shaveManager : shaveManagers){
            try {
                IShave shave=shaveManager.get(context,null);
                if(shave!=null){
                    shave.Shave(context);
                    break;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return ActionResultType.FAIL;
    }
}