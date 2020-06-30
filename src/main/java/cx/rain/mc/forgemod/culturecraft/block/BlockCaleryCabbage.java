package cx.rain.mc.forgemod.culturecraft.block;

import cx.rain.mc.forgemod.culturecraft.api.enumerate.CaleryCabbageType;
import cx.rain.mc.forgemod.culturecraft.registry.RegistryItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockCaleryCabbage extends CropsBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D),
    };

    private CaleryCabbageType caleryCabbageType = null;

    protected BlockCaleryCabbage(CaleryCabbageType type) {
        super(Block.Properties.from(Blocks.CARROTS));
        caleryCabbageType = type;
        setDefaultState(getStateContainer().getBaseState().with(getAgeProperty(), 0));
    }
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        int age = state.get(getAgeProperty());
        int stage = age / 4;
        return SHAPE_BY_AGE[stage];

    }

    @Override
    public IItemProvider getSeedsItem() {
        switch (caleryCabbageType) {
            case CALERY_CABBAGE:
                return RegistryItem.ITEMS.get("celery_cabbage");
        }
        return null;
    }
}