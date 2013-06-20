package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.tile.TileController;

public class GuiController extends GuiContainer {
    private TileController controller;

    public GuiController(TileController controller) {
        super(new ContainerController(controller));
        this.controller = controller;
        ySize = 89;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Controller", 8, 6, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/controller.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
