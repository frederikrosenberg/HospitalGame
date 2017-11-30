/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import common.Directions;
import common.IBusiness;
import common.IRoom;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Tobias
 */
public class MapController implements Initializable {

    private IBusiness business;

    private ResizableCanvas roomCanvas;

    private GraphicsContext graphicsContext;
    
    @FXML
    private StackPane stack;

    private int size = 30;
    
    
    
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        business = UI.getInstance().getBusiness();
        stack.autosize();
        ChangeListener<Number> anchorSizeListener = new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                calculateSizeAndDraw();
            }
        };

        stack.widthProperty().addListener(anchorSizeListener);
        stack.heightProperty().addListener(anchorSizeListener);
        
        createCanvas();
    }

    public void updateMap() {
        graphicsContext.clearRect(0, 0, roomCanvas.getWidth(), roomCanvas.getHeight());
        IRoom currentRoom = business.getPlayer().getCurrentRoom();
        Set<IRoom> rooms = getRoomsInRoom(new HashSet<>(), currentRoom);
        
        int border = size / 10;
        border = border < 1 ? 1 : border;

        for (IRoom room : rooms) {
            if (!room.isInspected()) {
                
                int xOffset = room.getCoordinate().getX() + Math.abs(minX);
                int yOffset = room.getCoordinate().getY() + Math.abs(minY);
                int sizeOffsetX = (int)((roomCanvas.getWidth() - (size * (maxX - minX + 1))) / 2);
                int sizeOffsetY = (int)((roomCanvas.getHeight()- (size * (maxY - minY + 1))) / 2);
                int xStart = xOffset * size + sizeOffsetX;
                int yStart = yOffset * size + sizeOffsetY; 
                
                if (room.getCoordinate().getX() == currentRoom.getCoordinate().getX() && room.getCoordinate().getY() == currentRoom.getCoordinate().getY()) {
                    graphicsContext.setFill(Color.RED);
                } else {
                    graphicsContext.setFill(Color.BLACK);
                }

                graphicsContext.fillRect(xStart, yStart, size, size);
                graphicsContext.clearRect(xStart + border, yStart + border, size - (border * 2), size - (border * 2));
                for (Directions d : room.getExitDirections()) {
                    switch (d) {
                        case SOUTH:
                            graphicsContext.clearRect(xStart + size / 2 - size / 8, yStart + size - border, size / 4, border);
                            break;
                        case NORTH:
                            graphicsContext.clearRect(xStart + size / 2 - size / 8, yStart, size / 4, border);
                            break;
                        case EAST:
                            graphicsContext.clearRect(xStart + size - border, yStart + size / 2 - size / 8, border, size / 4);
                            break;
                        case WEST:
                            graphicsContext.clearRect(xStart, yStart + size / 2 - size / 8, border, size / 4);
                            break;
                        default:
                            throw new AssertionError(d.name());
                    }
                }
            }
        }
    }
    
    private void calculateSizeAndDraw(){
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        
        IRoom currentRoom = business.getPlayer().getCurrentRoom();
        Set<IRoom> rooms = getRoomsInRoom(new HashSet<>(), currentRoom);
        
        for(IRoom room : rooms){
            if(room.getCoordinate().getX() > maxX)
                maxX = room.getCoordinate().getX();
            else if(room.getCoordinate().getX() < minX)
                minX = room.getCoordinate().getX();
            else if(room.getCoordinate().getY() > maxY)
                maxY = room.getCoordinate().getY();
            else if(room.getCoordinate().getY() < minY)
                minY = room.getCoordinate().getY();
        }
        
        int differenceX = maxX - minX + 1;
        int differenceY = maxY - minY + 1;
        
        int sizeX = (int)stack.getWidth() / differenceX;
        int sizeY = (int)stack.getHeight() / differenceY;
        
        if(sizeX < sizeY)
            size = sizeX;
        else
            size = sizeY;
        
        System.out.println(size);
        
        updateMap();
    }
    
    private void createCanvas(){
        roomCanvas = new ResizableCanvas();
        stack.getChildren().add(roomCanvas);
        roomCanvas.widthProperty().bind(stack.widthProperty());
        roomCanvas.heightProperty().bind(stack.heightProperty());
        graphicsContext = roomCanvas.getGraphicsContext2D();
        
        calculateSizeAndDraw();
    }

    private Set<IRoom> getRoomsInRoom(Set<IRoom> roomSet, IRoom nextRoom) {
        if (!roomSet.contains(nextRoom)) {
            roomSet.add(nextRoom);
            
            for (Directions d : nextRoom.getExitDirections()) {
                roomSet.addAll(getRoomsInRoom(roomSet, nextRoom.getExit(d)));
            }
        }

        return roomSet;
    }
}