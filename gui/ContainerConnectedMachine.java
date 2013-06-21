package tpw_rules.connectedmachines.gui;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import tpw_rules.connectedmachines.api.IConnectedMachine;

public class ContainerConnectedMachine extends Container {
    private IConnectedMachine machine;

    public ContainerConnectedMachine(IConnectedMachine machine) {
        this.machine = machine;
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
