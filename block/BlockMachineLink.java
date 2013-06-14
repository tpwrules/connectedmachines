package tpw_rules.connectedmachines.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import tpw_rules.connectedmachines.common.ConnectedMachines;

public class BlockMachineLink extends Block {
    public BlockMachineLink(int id) {
        super(id, Material.rock);
        setCreativeTab(ConnectedMachines.tabMachines);
    }
}
