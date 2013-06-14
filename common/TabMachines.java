package tpw_rules.connectedmachines.common;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import tpw_rules.connectedmachines.common.ConnectedMachines;

public class TabMachines extends CreativeTabs {
    public TabMachines() {
        super(CreativeTabs.getNextID(), "CMTabMachines");
    }

    @SideOnly(Side.CLIENT)
    public int getTabIconItemIndex() {
        return ConnectedMachines.blockController.blockID;
    }

    public String getTranslatedTabLabel() {
        return "Connected Machines";
    }
}
