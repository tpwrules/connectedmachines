package tpw_rules.connectedmachines.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import tpw_rules.connectedmachines.render.Texture;
import tpw_rules.connectedmachines.tile.TileController;

public class BlockController extends BlockConnected {
    public BlockController(int id) {
        super(id);
        setUnlocalizedName("Machine Controller");

        GameRegistry.registerBlock(this, "CMController");
        GameRegistry.registerTileEntity(TileController.class, "CMTEController");
        LanguageRegistry.addName(this, "Machine Controller");
    }

    @Override
    public Icon getFrontIcon() {
        return Texture.blockController;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileController();
    }
}
