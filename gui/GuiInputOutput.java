package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.network.OutputPacket;
import tpw_rules.connectedmachines.network.PacketType;
import tpw_rules.connectedmachines.tile.TileInputOutput;

public class GuiInputOutput extends GuiContainer {
    private GuiTextField ioName;
    private TileInputOutput tile;

    public GuiInputOutput(InventoryPlayer inventoryPlayer, TileInputOutput inputOutput) {
        super(new ContainerInputOutput(inventoryPlayer, inputOutput));
        tile = inputOutput;
        ySize = 196;
    }

    @Override
    public void updateScreen() {
        ioName.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(false);
        buttonList.clear();
        buttonList.add(new GuiButton(0, 130 + (width - xSize) / 2, 80 + (height - ySize) / 2, 40, 20, "Set"));
        ioName = new GuiTextField(this.fontRenderer, 8, 80, 115, 20);
        ioName.setFocused(true);
        ioName.setCanLoseFocus(true);
        ioName.setText(tile.name);
        ioName.setMaxStringLength(128);
        ((GuiButton)buttonList.get(0)).enabled = (tile.name != ioName.getText());
        guiLeft = (width-xSize)/2;
        guiTop = (height-ySize)/2;
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) return;
        OutputPacket packet = new OutputPacket(PacketType.GUI_CHANGE, 64, tile);
        try {
            packet.data.writeUTF(ioName.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        packet.sendServer();
        tile.name = ioName.getText();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        ioName.textboxKeyTyped(par1, par2);
        ((GuiButton)buttonList.get(0)).enabled = (tile.name != ioName.getText());
        if (par2 == 1) // escape
            super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        ioName.mouseClicked(x-guiLeft, y-guiTop, button);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Input/Output", 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"),
                8, ySize-94, 0x404040);
        fontRenderer.drawString("Name", 8, 69, 0x404040);
        this.ioName.drawTextBox();
        super.drawGuiContainerForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/inputoutput.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
