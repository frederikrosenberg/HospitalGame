/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import common.IBusiness;
import common.IInventory;
import common.IItem;
import common.IPlayer;
import common.ItemName;
import static common.ItemName.BLOODBAG;
import static common.ItemName.IDCARD;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author larsjorgensen
 */
public class InventoryController implements Initializable {

    /**
     * Contains the reference to the page label.
     */
    @FXML
    private Label pageLabel;
    
    /**
     * Contains the reference to the next page button.
     */
    @FXML
    private Button nextBtn;
    
    /**
     * Contains the reference to the previous page button.
     */
    @FXML
    private Button previousBtn;
    
    /**
     * Contains the business facade reference.
     */
    private IBusiness business;
    
    /**
     * Contains reference to the Image Ressource class.
     */
    private ImageResource imgRes;
    
    /**
     * Contains which page the inventory is showing.
     */
    private int page = 1;
    
    /**
     * Contains how many items there is per page in the inventory.
     */
    private int itemsPerPage = 6;
    
    /**
     * Contains if the inventory is focussed.
     */
    private boolean isFocussed = false;
    
    /**
     * Contains the player reference.
     */
    private IPlayer player;
    
    /**
     * Contains the reference to the gridpane.
     */
    @FXML
    private GridPane inventoryGrid;
    
    /**
     * Contains the selected index in the inventory.
     */
    private int selectedIndex = 0;
    
    /**
     * Contains all the items in the inventory.
     */
    private ArrayList<? extends IItem> items;
    
    /**
     * Contains the inventory.
     */
    private IInventory inventory;
    
    /**
     * Contains all the VBox´ that gets generated.
     */
    private ArrayList<VBox> itemContainers;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        business = UI.getInstance().getBusiness();
        imgRes = UI.getInstance().getImageResource();
        player = business.getPlayer();
        items = new ArrayList<>();
        itemContainers = new ArrayList<>();
        nextBtn.setBackground(new Background(new BackgroundImage(imgRes.getSprite(Sprites.ARROW_RIGHT), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        previousBtn.setBackground(new Background(new BackgroundImage(imgRes.getSprite(Sprites.ARROW_LEFT), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }    
    
    /**
     * Update the items of the selected page to the GUI.
     */
    public void updateItems(IInventory inventory) {
        clearGrid();
        itemContainers.clear();
        this.inventory = inventory;
        items = this.inventory.getItems();
        refreshPageButtons();
        if(getPageCount() == 0) {
            pageLabel.setText("0/" + getPageCount());
        } else {
            pageLabel.setText(page + "/" + getPageCount());
        }
        int calc = 0;
        for (int i = (page - 1) * itemsPerPage; i < (page - 1) * itemsPerPage + itemsPerPage && i < items.size(); i++) {
            int column = calc % 3;
            int row = calc / 3;
            if(i != selectedIndex) {
                inventoryGrid.add(getGUIItem(items.get(i), false), column, row + 1);
            } else {
                inventoryGrid.add(getGUIItem(items.get(i), true), column, row + 1);
            }
            calc++;
        }
    }
    
    /**
     * Checking if some of the buttons should be disabled or not.
     */
    private void refreshPageButtons() {
        System.out.println("page count" + getPageCount());
        if(getPageCount() == 1 || getPageCount() == 0) {
            nextBtn.setDisable(true);
            previousBtn.setDisable(true);
        } else if(getPageCount() > page && page == 1){
            nextBtn.setDisable(false);
            previousBtn.setDisable(true);
        } else if(getPageCount() == page) {
            nextBtn.setDisable(true);
            previousBtn.setDisable(false);
        } else {
            nextBtn.setDisable(false);
            previousBtn.setDisable(false);
        }
    }
    
    /**
     * Calculates page count.
     * @return Count of pages.
     */
    private int getPageCount() {
        return (int) Math.ceil(items.size() / (double) itemsPerPage);
       
    }

    /**
     * Get the inventory.
     * @return The inventory.
     */
    public IInventory getInventory() {
        return inventory;
    }
    
    public void setFocus(boolean focus) {
        this.isFocussed = focus;
    }
    
    /**
     * Clears the inventory gridpane for items.
     */
    private void clearGrid() {
        if(!itemContainers.isEmpty()) {
            inventoryGrid.getChildren().removeAll(itemContainers);
        }
    }
    
    /**
     * Returning an VBox with a item icon and a name of the item.
     * @param item Which item it should return an image of.
     * @param selected If the item is selected by the user.
     * @return Return an VBox containing an image of the item and a label with the name.
     */
    public VBox getGUIItem(IItem item, boolean selected) {
        VBox vBox = new VBox();
        if(selected && isFocussed) {
            vBox.setStyle("-fx-border-color: blue;");
        }
        ImageView img = new ImageView();
        Label name = new Label(item.getName().toString());
        switch (item.getName()) {
            case BANDAGE:
                img.setImage(imgRes.getSprite(Sprites.BANDAGE));
                break;
            case BLOODBAG:
                img.setImage(imgRes.getSprite(Sprites.BLOODBAG_A));
                break;
            case IDCARD:
                img.setImage(imgRes.getSprite(Sprites.IDCARD));
                break;
            case MORPHINE:
                img.setImage(imgRes.getSprite(Sprites.MORPHINE));
                break;
            default:
                throw new AssertionError();
        }
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(img, name);
        itemContainers.add(vBox);
        return vBox;
    }
    
    /**
     * Setting the selected index by a number 0-5.
     * @param index 
     */
    public void setSelectedIndex(int index) {
        if(index + (page - 1) * itemsPerPage < items.size()) {
            selectedIndex = index + (page - 1) * itemsPerPage;
        }
    }
    
    /**
     * Changes inventory page to the next page.
     */
    public void nextPage() {
        if(page < getPageCount()) {
            page++;
            setSelectedIndex(0);
            updateItems(inventory);
        }
    }
    
    /**
     * Changes inventory page to the previous page.
     */
    public void previousPage() {
        if(page != 1 && page > 0) {
            page--;
            setSelectedIndex(0);
            updateItems(inventory);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}