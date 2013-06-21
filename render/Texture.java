package tpw_rules.connectedmachines.render;


import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class Texture {
    public static Icon blockLinked;
    public static Icon blockSide;
    public static Icon blockController;
    public static Icon blockConnectedFurnace;
    public static Icon blockConnectedFurnaceActive;

    public static boolean loaded;

    public static void loadTextures(IconRegister r) {
        if (loaded)
            return;
        blockLinked = r.registerIcon("connectedmachines:blockLinked");
        blockSide = r.registerIcon("connectedmachines:blockSide");
        blockController = r.registerIcon("connectedmachines:blockController");
        blockConnectedFurnace = r.registerIcon("connectedmachines:blockConnectedFurnace");
        blockConnectedFurnaceActive = r.registerIcon("connectedmachines:blockConnectedFurnaceActive");
        loaded = true;
    }
}
