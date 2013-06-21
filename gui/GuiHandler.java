package tpw_rules.connectedmachines.gui;


import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tpw_rules.connectedmachines.api.IConnectedMachine;
import tpw_rules.connectedmachines.tile.TileConnectedGenerator;
import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.tile.TileInputOutput;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
                                      int x, int y, int z) {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        switch (GuiType.values[id]) {
            case GUI_CONNECTED_GENERATOR:
                return new ContainerConnectedGenerator(player.inventory, (TileConnectedGenerator)tileEntity);
            case GUI_CONTROLLER:
                return new ContainerController((TileController)tileEntity);
            case GUI_INPUT_OUTPUT:
                return new ContainerInputOutput(player.inventory, (TileInputOutput)tileEntity);
            case GUI_CONNECTED_MACHINE:
                return new ContainerConnectedMachine((IConnectedMachine)tileEntity);
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
            case GUI_CONTROLLER:
                return new GuiController((TileController)tileEntity);
            case GUI_INPUT_OUTPUT:
                return new GuiInputOutput(player.inventory, (TileInputOutput)tileEntity);
            case GUI_CONNECTED_MACHINE:
                return new GuiConnectedMachine((IConnectedMachine)tileEntity);
        }
        return null;
    }
}
