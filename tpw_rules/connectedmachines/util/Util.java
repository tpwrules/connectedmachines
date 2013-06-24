package tpw_rules.connectedmachines.util;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;

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
            System.out.print(" ");
        }
        System.out.println("");
    }

    public static ForgeDirection getPlayerFacing(EntityLiving entity) {
        int facing = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;
        switch (facing) {
            case 0:
                return ForgeDirection.NORTH;
            case 1:
                return ForgeDirection.EAST;
            case 2:
                return ForgeDirection.SOUTH;
            case 3:
                return ForgeDirection.WEST;
        }
        return null;
    }
}
