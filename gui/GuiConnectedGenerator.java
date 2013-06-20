package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.tile.TileConnectedGenerator;

public class GuiConnectedGenerator extends GuiContainer {
    public GuiConnectedGenerator(InventoryPlayer inventoryPlayer, TileConnectedGenerator connectedGenerator) {
        super(new ContainerConnectedGenerator(inventoryPlayer, connectedGenerator));
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Connected Generator", 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
                8, ySize-94, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/generator.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
