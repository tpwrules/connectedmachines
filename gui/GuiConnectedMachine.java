package tpw_rules.connectedmachines.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.opengl.GL11;
import tpw_rules.connectedmachines.api.IConnectedMachine;

public class GuiConnectedMachine extends GuiContainer {
    private IConnectedMachine machine;

    private GuiTextField groupName;
    private int groupPos;
    private String[] groupList;

    public GuiConnectedMachine(IConnectedMachine machine) {
        super(new ContainerConnectedMachine(machine));
        this.machine = machine;
        ySize = 98;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new GuiButton(0, 20+guiLeft, 26+guiTop, 10, 20, "<"));
        buttonList.add(new GuiButton(1, 154+guiLeft, 26+guiTop, 10, 20, ">"));
        buttonList.add(new GuiButton(2, 144+guiLeft, 4+guiTop, 28, 20, "Set"));
        groupName = new GuiTextField(this.fontRenderer, 32, 26, 120, 20);
        groupList = machine.getLink().groups.keySet().toArray(new String[0]);
        groupPos = 0;
        for (int i = 0; i < groupList.length; i++) {
            if (groupList[i].equals(machine.getGroupName())) {
                groupPos = i;
                break;
            }
        }
        updateText();
    }

    public int wrap(int val, int max) {
        if (val >= max)
            return 0;
        else if (val < 0)
            return max-1;
        return val;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                groupPos = wrap(--groupPos, groupList.length);
                break;
            case 1:
                groupPos = wrap(++groupPos, groupList.length);
                break;
            case 2:
                machine.setGroupName(groupName.getText());
                break;
        }
        updateText();
    }

    public void updateText() {
        groupName.setText(groupList[groupPos]);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(machine.getGUIName(), 8, 6, 0x404040);
        fontRenderer.drawString("G:", 8, 32, 0x404040);
        super.drawGuiContainerForegroundLayer(par1, par2);
        groupName.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        this.mc.renderEngine.bindTexture("/mods/connectedmachines/textures/gui/connectedmachine.png");
        this.drawTexturedModalRect((width-xSize)/2, (height-ySize)/2,
                0, 0, xSize, ySize);
    }
}
