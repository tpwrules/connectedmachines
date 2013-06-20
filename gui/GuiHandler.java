package tpw_rules.connectedmachines.gui;


import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tpw_rules.connectedmachines.tile.TileConnectedGenerator;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        switch (GuiType.values[id]) {
            case GUI_CONNECTED_GENERATOR:
                return new ContainerConnectedGenerator(player.inventory, (TileConnectedGenerator)tileEntity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        switch (GuiType.values[id]) {
            case GUI_CONNECTED_GENERATOR:
                return new GuiConnectedGenerator(player.inventory, (TileConnectedGenerator)tileEntity);
        }
        return null;
    }
}
