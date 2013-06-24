package tpw_rules.connectedmachines.api;


import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import tpw_rules.connectedmachines.tile.TileController;
import tpw_rules.connectedmachines.util.WCoord;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class LinkFinder {
    public static void updateNetwork(ILinkable tile) {
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
                current = posStack.removeFirst();
                currentTile = tileStack.removeFirst();
                currentRot = rotStack.removeFirst();
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

        TileController controller = currentTile.getLink();

        if (controller != null)
            controller.findMachines();
    }

    public static ArrayList<ILinkable> findMachines(TileController controller, boolean reset) {
        // note: method above is basically the same

        // record what we have seen
        HashSet<WCoord> visited = new HashSet<WCoord>();

        // stacks for backtracking
        ArrayDeque<WCoord> posStack = new ArrayDeque<WCoord>();
        ArrayDeque<ILinkable> tileStack = new ArrayDeque<ILinkable>();
        ArrayDeque<Integer> rotStack = new ArrayDeque<Integer>();

        WCoord current = new WCoord(controller); // current information
        ILinkable currentTile = controller;
        TileEntity nextTile;
        WCoord nextPos;
        int currentRot = 0;

        while (true) {
            if (visited.contains(current)) { // have we seen this tile before
                // yes, let's backtrack
                if (posStack.size() == 0)
                    break; // we're done if the stack is empty
                // otherwise, pop a tile and continue examining it
                current = posStack.removeFirst();
                currentTile = tileStack.removeFirst();
                currentRot = rotStack.removeFirst();
            } else {
                visited.add(current); // no, well we have now!
            }
            while (currentRot < 6) { // loop through the 6 faces of this tile
                // get the adjacent tile
                nextPos = current.adjacent(ForgeDirection.getOrientation(currentRot));
                nextTile = nextPos.getTileEntity();
                currentRot++;
                if (!(nextTile instanceof ILinkable))
                    continue; // ignore if it's not something we can connect to
                // if it is, we want to examine it further
                // so save current block on the stack
                posStack.addFirst(current);
                tileStack.addFirst(currentTile);
                rotStack.addFirst(currentRot);
                // update next values
                current = nextPos;
                currentTile = (ILinkable)nextTile;
                currentRot = 0;
                break; // and stop examining this tile for now
            }
        }

        ArrayList<ILinkable> ret = new ArrayList<ILinkable>();

        for (WCoord linkee : visited) {
            currentTile = (ILinkable)linkee.getTileEntity();
            if (reset)
                currentTile.setLink(null, null);
            else if (currentTile.getLink() != controller)
                currentTile.setLink(controller, new WCoord(controller));
            ret.add(currentTile);
        }

        return ret;
    }
}
