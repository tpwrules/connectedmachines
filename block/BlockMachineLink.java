package tpw_rules.connectedmachines.block;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tpw_rules.connectedmachines.common.ConnectedMachines;
import tpw_rules.connectedmachines.tile.TileMachineLink;

public class BlockMachineLink extends Block implements ITileEntityProvider {
    public int renderID;

    public BlockMachineLink(int id) {
        super(id, Material.rock);
        setCreativeTab(ConnectedMachines.tabMachines);
        setHardness(1.0F);
        setResistance(10.0F);
        setStepSound(Block.soundClothFootstep);

        GameRegistry.registerBlock(this, "CMMachineLink");
        GameRegistry.registerTileEntity(TileMachineLink.class, "CMTEMachineLink");
        LanguageRegistry.addName(this, "Machine Link");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileMachineLink();
    }

    @Override
    public int getRenderType() {
        return renderID;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
