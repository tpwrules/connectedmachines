package tpw_rules.connectedmachines.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockController extends Block {
    public BlockController(int id) {
        super(id, Material.iron);
        setUnlocalizedName("Machine Controller");
        GameRegistry.registerBlock(this, "CMController");
        LanguageRegistry.addName(this, "Machine Controller");
    }
}
