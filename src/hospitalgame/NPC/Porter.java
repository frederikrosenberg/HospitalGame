package hospitalgame.NPC;

import hospitalgame.*;
import java.util.HashMap;
import java.util.Map;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.util.Queue;

/**
 * @author Frederik Schultz Rosenberg
 * @author Andreas Bøgh Mølgaard-Andersen
 * @author Tobias Ahrenschneider Sztuk
 * @author Lars Bjerregaard Jørgensen
 * @author Robert Francisti
 */
public class Porter extends Move {

    private Room endRoom;

    /**
     * Calls the NPC constructor through the super
     *
     * @param name name of the NPC moving
     * @param description decription of the NPC that moves
     */
    public Porter(String name, String description, Room endRoom) {
        super(name, description);
        this.endRoom = endRoom;
    }

    /**
     * Overrides the abstract method in NPC
     *
     * @param player is the player object
     */
    @Override
    public void interact(Player player) {

        System.out.print("These directions will lead you two rooms ahead ");
        List<String> path = pathfinder(player.getCurrentRoom(), endRoom);
        for (int i = 0; i < 2; i++) {
           System.out.print(path.get(i) + " "); 
        }
        System.out.println();
    }

    public static List<String> pathfinder(Room startRoom, Room endRoom) {

        Queue<Room> queue = new LinkedList<Room>(); //
        Map<Room, String> pathMap = new HashMap<>();
        for (String key : startRoom.getKeySet()) {
            Room r = startRoom.getExit(key);
            queue.add(r);
        }
        pathMap.put(startRoom, "start");
        while (!queue.isEmpty()) {
            Room room = queue.poll();

            for (String key : room.getKeySet()) {
                Room r = room.getExit(key);
                if (pathMap.containsKey(r)) {
                    pathMap.put(r, key);
                } else {
                    queue.add(r);
                }

            }

        }
        List<String> path = new ArrayList<>();
        Room currentRoom = endRoom;
        while (currentRoom != startRoom) {
            String s = pathMap.get(currentRoom);
            path.add(s);
            currentRoom = currentRoom.getExit(s);

        }
        Collections.reverse(path);
        for (String dir : path) {
            int index = GameConstants.DIRECTIONS.indexOf(dir);
            int newIndex = (index + 2) % 4;
            dir = GameConstants.DIRECTIONS.get(newIndex);
        }
        return path;
    }

}
