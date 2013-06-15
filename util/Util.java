package tpw_rules.connectedmachines.util;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Util {
    public static boolean debugging = true;

    public static void log(Object... args) {
        if (!debugging) return;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            System.out.print("[SERVER] ");
        } else {
            System.out.print("[CLIENT] ");
        }
        for (Object thing : args) {
            System.out.print(thing);
            System.out.print("");
        }
        System.out.println("");
    }
}
