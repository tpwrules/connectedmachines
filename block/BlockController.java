package tpw_rules.connectedmachines.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.common.ConnectedMachines;
import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.tile.TileLinkable;

public class BlockController extends Block implements ITileEntityProvider {
    public Icon blockUnlinkedIcon;
    public Icon blockLinkedIcon;

    public BlockController(int id) {
        super(id, Material.iron);
        setUnlocalizedName("Machine Controller");
        setCreativeTab(ConnectedMachines.tabMachines);
        setHardness(5.0F);
        setResistance(10.0F);
        setStepSound(Block.soundMetalFootstep);

        GameRegistry.registerBlock(this, "CMController");
        GameRegistry.registerTileEntity(TileController.class, "CMTEController");
        LanguageRegistry.addName(this, "Machine Controller");
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        blockUnlinkedIcon = iconRegister.registerIcon("connectedmachines:blockController");
        blockLinkedIcon = iconRegister.registerIcon("connectedmachines:blockLinked");
        this.blockIcon = blockUnlinkedIcon;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (ForgeDirection.getOrientation(side) == ForgeDirection.UP) {
            if (((TileLinkable)blockAccess.getBlockTileEntity(x, y, z)).linkConnected) {
                return blockLinkedIcon;
            }
        }
        return blockUnlinkedIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileController();
    }
}
