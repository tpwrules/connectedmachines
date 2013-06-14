package tpw_rules.connectedmachines.util;


import cpw.mods.fml.client.FMLClientHandler;

public class Util {
    public boolean debugging = true;

    public void log(String msg) {
        if (!debugging) return;
        if (FMLClientHandler.instance().getClient().theWorld.isRemote) {
            System.out.print("[SERVER] ");
        } else {
            System.out.print("[CLIENT] ");
        }
        System.out.println(msg);
    }
}
