package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.tile.TileController;

public class GuiController extends GuiContainer {
    private TileController controller;

    private GuiTextField groupName;
    private GuiTextField inputName;
    private GuiTextField outputName;

    private int groupPos;
    private int inputPos;
    private int outputPos;

    private String[] groupList;

    public GuiController(TileController controller) {
        super(new ContainerController(controller));
        this.controller = controller;
        ySize = 98;
    }

    @Override
    public void updateScreen() {
        groupName.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(new GuiButton(0, 20+guiLeft, 26+guiTop, 10, 20, "<"));
        buttonList.add(new GuiButton(1, 154+guiLeft, 26+guiTop, 10, 20, ">"));
        buttonList.add(new GuiButton(2, 20+guiLeft, 48+guiTop, 10, 20, "<"));
        buttonList.add(new GuiButton(3, 154+guiLeft, 48+guiTop, 10, 20, ">"));
        buttonList.add(new GuiButton(4, 20+guiLeft, 70+guiTop, 10, 20, "<"));
        buttonList.add(new GuiButton(5, 154+guiLeft, 70+guiTop, 10, 20, ">"));
        buttonList.add(new GuiButton(6, 114+guiLeft, 4+guiTop, 28, 20, "Set"));
        buttonList.add(new GuiButton(7, 144+guiLeft, 4+guiTop, 28, 20, "New"));
        groupName = new GuiTextField(this.fontRenderer, 32, 26, 120, 20);
        inputName = new GuiTextField(this.fontRenderer, 32, 48, 120, 20);
        outputName = new GuiTextField(this.fontRenderer, 32, 70, 120, 20);
        groupList = controller.groups.keySet().toArray(new String[0]);
        groupPos = 0;
        inputPos = controller.ioPortList.indexOf(controller.groups.get(groupList[groupPos])[0]);
        outputPos = controller.ioPortList.indexOf(controller.groups.get(groupList[groupPos])[1]);
        updateText();
    }

    public int wrap(int val, int max) {
        if (val >= max)
            return 0;
        else if (val < 0)
            return max;
        return val;
    }

    public void updateText() {
        groupName.setText(groupList[groupPos]);
        if (inputPos != -1 && outputPos != -1) {
            inputName.setText(controller.ioPortList.get(inputPos));
            outputName.setText(controller.ioPortList.get(outputPos));
        } else {
            inputName.setText("");
            outputName.setText("");
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 2:
                inputPos = wrap(--inputPos, controller.ioPortList.size());
                break;
            case 3:
                inputPos = wrap(++inputPos, controller.ioPortList.size());
                break;
            case 4:
                outputPos = wrap(--outputPos, controller.ioPortList.size());
                break;
            case 5:
                outputPos = wrap(++outputPos, controller.ioPortList.size());
                break;
        }
        updateText();
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        groupName.textboxKeyTyped(par1, par2);
        if (par2 == 1) // escape
            super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        groupName.mouseClicked(x-guiLeft, y-guiTop, button);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString("Controller", 8, 6, 0x404040);
        fontRenderer.drawString("Power: "+controller.powerBuffer+"/"+controller.powerBufferMax,
                8, 16, 0x404040);
        fontRenderer.drawString("G:", 8, 32, 0x404040);
        fontRenderer.drawString("I:", 8, 54, 0x404040);
        fontRenderer.drawString("O:", 8, 76, 0x404040);
        super.drawGuiContainerForegroundLayer(par1, par2);
        groupName.drawTextBox();
        inputName.drawTextBox();
        outputName.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/controller.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
