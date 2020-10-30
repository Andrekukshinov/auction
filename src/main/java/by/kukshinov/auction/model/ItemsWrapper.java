package by.kukshinov.auction.model;

import java.util.ArrayList;
import java.util.List;

public class ItemsWrapper {
    private List<Item> items;

    public ItemsWrapper() {
    }

    public ItemsWrapper(List<Item> items) {
	   this.items = items;
    }

    public List<Item> getItems() {
	   return new ArrayList<>(items);
    }

    public void setItems(List<Item> items) {
	   this.items = items;
    }
}
