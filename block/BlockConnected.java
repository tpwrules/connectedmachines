package tpw_rules.connectedmachines.block;


import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.common.ConnectedMachines;
import tpw_rules.connectedmachines.render.Texture;
import tpw_rules.connectedmachines.tile.TileConnected;
import tpw_rules.connectedmachines.util.Util;


public class BlockConnected extends BlockContainer {
    public Icon frontIcon;

    public BlockConnected(int id) {
        super(id, Material.iron);
        setCreativeTab(ConnectedMachines.tabMachines);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(Block.soundMetalFootstep);

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack item) {
        TileConnected tile = (TileConnected)world.getBlockTileEntity(x, y, z);
        tile.facing = Util.getPlayerFacing(entity);
    }

    @Override
    public void registerIcons(IconRegister r) {
        Texture.loadTextures(r);
        frontIcon = getFrontIcon();
        this.blockIcon = frontIcon;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileConnected tile = (TileConnected)world.getBlockTileEntity(x, y, z);
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP) {
            return Texture.blockSide;
        }
        if (ForgeDirection.getOrientation(side) == tile.facing) {
            return frontIcon;
        }
        return Texture.blockSide;
    }

    public Icon getFrontIcon() {
        return null;
    }

    public TileEntity createNewTileEntity(World world) {
        return null;
    }
}
