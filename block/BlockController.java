package tpw_rules.connectedmachines.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import tpw_rules.connectedmachines.common.ConnectedMachines;

public class BlockController extends Block {
    public BlockController(int id) {
        super(id, Material.iron);
        setUnlocalizedName("Machine Controller");
        setCreativeTab(ConnectedMachines.tabMachines);

        GameRegistry.registerBlock(this, "CMController");
        LanguageRegistry.addName(this, "Machine Controller");
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("connectedmachines:blockController");
    }
}
