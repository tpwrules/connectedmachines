package tpw_rules.connectedmachines.api;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.util.WCoord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class LinkFinder {
    public static TileController findController(ILinkable tile) {
        HashSet<WCoord> visited = new HashSet<WCoord>();
        ArrayDeque<WCoord> posStack = new ArrayDeque<WCoord>();
        ArrayDeque<ILinkable> tileStack = new ArrayDeque<ILinkable>();
        ArrayDeque<Integer> rotStack = new ArrayDeque<Integer>();

        WCoord current = new WCoord((TileEntity)tile);
        ILinkable currentTile = tile;
        TileEntity nextTile;
        WCoord nextPos;
        int currentRot = 0;

        while (true) {
            if (currentTile.getLink() != null)
                break;
            if (visited.contains(current)) {
                if (posStack.size() == 0)
                    break;
                current = posStack.getFirst();
                currentTile = tileStack.getFirst();
                currentRot = rotStack.getFirst();
            } else {
                visited.add(current);
            }
            while (currentRot < 6) {
                nextPos = current.copy().move(ForgeDirection.getOrientation(currentRot));
                nextTile = nextPos.getTileEntity();
                currentRot++;
                if (!(nextTile instanceof ILinkable))
                    continue;
                posStack.addFirst(current);
                tileStack.addFirst(currentTile);
                rotStack.addFirst(currentRot);
                current = nextPos;
                currentTile = (ILinkable)nextTile;
                currentRot = 0;
                break;
            }
        }

        TileController controller = currentTile.getLink();
        if (controller == null)
            return null;

        for (WCoord linkee : visited) {
            ((ILinkable)linkee.getTileEntity()).setLink(controller);
        }

        return controller;
    }

    public static ArrayList<ILinkable> findMachines(TileController controller) {
        HashSet<WCoord> visited = new HashSet<WCoord>();
        ArrayDeque<WCoord> posStack = new ArrayDeque<WCoord>();
        ArrayDeque<ILinkable> tileStack = new ArrayDeque<ILinkable>();
        ArrayDeque<Integer> rotStack = new ArrayDeque<Integer>();

        WCoord current = new WCoord(controller);
        ILinkable currentTile = controller;
        TileEntity nextTile;
        WCoord nextPos;
        int currentRot = 0;

        while (true) {
            if (visited.contains(current)) {
                if (posStack.size() == 0)
                    break;
                current = posStack.getFirst();
                currentTile = tileStack.getFirst();
                currentRot = rotStack.getFirst();
            } else {
                visited.add(current);
            }
            while (currentRot < 6) {
                nextPos = current.adjacent(ForgeDirection.getOrientation(currentRot));
                nextTile = nextPos.getTileEntity();
                currentRot++;
                if (!(nextTile instanceof ILinkable))
                    continue;
                posStack.addFirst(current);
                tileStack.addFirst(currentTile);
                rotStack.addFirst(currentRot);
                current = nextPos;
                currentTile = (ILinkable)nextTile;
                currentRot = 0;
                break;
            }
        }

        ArrayList<ILinkable> ret = new ArrayList<ILinkable>();

        for (WCoord linkee : visited) {
            currentTile = (ILinkable)linkee.getTileEntity();
            currentTile.setLink(controller);
            ret.add(currentTile);
        }

        return ret;
    }
}
