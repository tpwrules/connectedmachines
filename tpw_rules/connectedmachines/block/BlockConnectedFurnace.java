package tpw_rules.connectedmachines.block;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.common.ConnectedMachines;
import tpw_rules.connectedmachines.gui.GuiType;
import tpw_rules.connectedmachines.render.Texture;
import tpw_rules.connectedmachines.tile.TileConnectedFurnace;
import tpw_rules.connectedmachines.util.Util;

public class BlockConnectedFurnace extends BlockContainer implements ITileEntityProvider {
    public BlockConnectedFurnace(int id) {
        super(id, Material.iron);
        setUnlocalizedName("Connected Furnace");

        setCreativeTab(ConnectedMachines.tabMachines);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(Block.soundMetalFootstep);

        GameRegistry.registerBlock(this, "CMFurnace");
        GameRegistry.registerTileEntity(TileConnectedFurnace.class, "CMTEFurnace");
        LanguageRegistry.addName(this, "Connected Furnace");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack item) {
        TileConnectedFurnace tile = (TileConnectedFurnace)world.getBlockTileEntity(x, y, z);
        tile.facing = Util.getPlayerFacing(entity);
        if (!world.isRemote)
            tile.placed();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    int meta, float hitX, float hitY, float hitZ) {
        if (player.isSneaking())
            return false;
        if (((TileConnectedFurnace)world.getBlockTileEntity(x, y, z)).getLink() == null)
            return true;
        player.openGui(ConnectedMachines.instance, GuiType.GUI_CONNECTED_MACHINE.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int side, int meta) {
        if (!world.isRemote)
            ((TileConnectedFurnace)world.getBlockTileEntity(x, y, z)).broken();
        super.breakBlock(world, x, y, z, side, meta);
    }

    @Override
    public void registerIcons(IconRegister r) {
        Texture.loadTextures(r);
        this.blockIcon = Texture.blockConnectedFurnace;
    }


    @Override
    public Icon getIcon(int side, int meta) {
        if (ForgeDirection.getOrientation(side) == ForgeDirection.SOUTH)
            return Texture.blockConnectedFurnace;
        return Texture.blockSide;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileConnectedFurnace tile = (TileConnectedFurnace)world.getBlockTileEntity(x, y, z);
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP) {
            return tile.getLink() != null ? Texture.blockLinked : Texture.blockUnlinked;
        }
        else if (ForgeDirection.getOrientation(side) == tile.facing) {
            return tile.cooking ? Texture.blockConnectedFurnaceActive : Texture.blockConnectedFurnace;
        }
        return Texture.blockSide;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileConnectedFurnace();
    }
}
