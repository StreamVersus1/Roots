package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBonfire extends BlockTEBase {

  public static PropertyBool BURNING = PropertyBool.create("burning");

  public BlockBonfire(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);

    setDefaultState(blockState.getBaseState().withProperty(BURNING, false));
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
  {
      if (state.getValue(BURNING))
        return 12;
      else
        return 0;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 0.25, 1.125);
  }

  //Concerning the blockstate --------------------
  @Nonnull
  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, BURNING);
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand)
  {
    return this.getDefaultState().withProperty(BURNING, false);
  }

  public static void setState(boolean burning, World world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);

    if(burning) world.setBlockState(pos, ModBlocks.bonfire.getDefaultState().withProperty(BURNING, true), 3);
    else world.setBlockState(pos, ModBlocks.bonfire.getDefaultState().withProperty(BURNING, false), 3);

    if (te != null)
    {
      te.validate();
      world.setTileEntity(pos, te);
    }

  }

  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta)
  {
      return this.getDefaultState().withProperty(BURNING, meta == 1);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return state.getValue(BURNING) ? 1 : 0;
  }
}
