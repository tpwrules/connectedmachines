package tpw_rules.connectedmachines.api;


import net.minecraft.item.ItemStack;

public interface IConnectedMachine {
    public int getOperationStackSize(ItemStack stack);

    public String getGUIName();
}
