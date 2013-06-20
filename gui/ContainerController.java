package tpw_rules.connectedmachines.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import tpw_rules.connectedmachines.tile.TileController;

public class ContainerController extends Container {
    private TileController controller;

    public ContainerController(TileController controller) {
        this.controller = controller;
    }

    @Override
    public void putStackInSlot(int slot, ItemStack stack) {
        return;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
