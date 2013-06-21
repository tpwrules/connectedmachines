package tpw_rules.connectedmachines.api;


import net.minecraft.item.ItemStack;

public interface IConnectedMachine extends ILinkable {
    public int getOperationStackSize(ItemStack stack);

    public String getGUIName();
    public String getGroupName();
    public void setGroupName(String name);
}
