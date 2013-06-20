package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.tile.TileInputOutput;

public class GuiInputOutput extends GuiContainer {
    public GuiInputOutput(InventoryPlayer inventoryPlayer, TileInputOutput inputOutput) {
        super(new ContainerInputOutput(inventoryPlayer, inputOutput));
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Input/Output", 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
                8, ySize-94, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/inputoutput.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
