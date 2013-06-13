package tpw_rules.connectedmachines.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockController extends Block {
    public BlockController(int id) {
        super(id, Material.iron);
        setUnlocalizedName("Machine Controller");
    }
}
