/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

import common.Direction;
import common.ICoordinate;
import common.IInventory;
import common.IRoom;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author andreasmolgaard-andersen
 */
public class DataRoom implements IRoom, Serializable {

    private String name;
    private HashMap<Direction, Integer> exits;
    private boolean inspected;
    private DataCoordinate coordinate;
    private int inventoryID;
    private boolean isLocked;
    private int roomID;

    public DataRoom(IRoom room) {
        name = room.getName();
        exits = new HashMap<>();
        for (Direction dir : room.getExitDirections()) {
            exits.put(dir, room.getExit(dir).getRoomID());
        }
        inspected = room.isInspected();
        inventoryID = room.getInventory().getInventoryID();
        coordinate = new DataCoordinate(room.getCoordinate());
        isLocked = room.isLocked();
        roomID = room.getRoomID();
    }

    @Override
    public IRoom getExit(Direction dir) {
        throw new UnsupportedOperationException("Invalid operation for data object.");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public boolean isInspected() {
        return inspected;
    }

    @Override
    public Set<Direction> getExitDirections() {
        return exits.keySet();
    }

    @Override
    public ICoordinate getCoordinate() {
        return coordinate;

    }

    @Override
    public IInventory getInventory() {
        throw new UnsupportedOperationException("Invalid operation for data object.");
    }

    @Override
    public int getRoomID() {
        return roomID;
    }

    @Override
    public int getInventoryID() {
        return inventoryID;
    }

    @Override
    public int getExitID(Direction dir) {
        return exits.get(dir);
    }

}
