package tpw_rules.connectedmachines.util;


import cpw.mods.fml.client.FMLClientHandler;

public class Util {
    public static boolean debugging = true;

    public static void log(Object... args) {
        if (!debugging) return;
        if (FMLClientHandler.instance().getClient().theWorld.isRemote) {
            System.out.print("[SERVER] ");
        } else {
            System.out.print("[CLIENT] ");
        }
        for (Object thing : args) {
            System.out.print(thing);
        }
        System.out.println("");
    }
}
