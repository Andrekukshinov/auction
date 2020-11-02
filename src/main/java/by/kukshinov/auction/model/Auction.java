package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Auction {
    private static final Lock LOCKER = new ReentrantLock();
    private static Auction instance;

    private List<Participant> participants;
    private List<Item> items;
    private Participant currentItemOwner;
    private Item currentItem;


    private Auction() {
    }

    public static Auction getInstance() {
	   if (instance == null) {
		  try {
			 LOCKER.lock();

			 Auction localInstance;
			 if (instance == null) {
				localInstance = new Auction();
				instance = localInstance;
			 }
		  } finally {
			 LOCKER.unlock();
		  }
	   }
	   return instance;
    }

    public List<Participant> getParticipants() {
	   return new ArrayList<>(participants);
    }

    public void setParticipants(
		  List<Participant> participants) {
	   this.participants = participants;
    }

    public List<Item> getItems() {
	   return new ArrayList<>(items);
    }

    public void setItems(List<Item> items) {
	   this.items = items;
    }

    public ItemDTO getCurrentItem() {
	   BigDecimal itemPrice = currentItem.getPrice();
	   long id = currentItem.getId();
	   return new ItemDTO(id, itemPrice);
    }

    public void requestPriseRaise(BigDecimal newItemPrice, Participant currentItemOwner) {
	   currentItem.setPrice(newItemPrice);
	   this.currentItemOwner = currentItemOwner;
	   priceUpdateNotify(currentItem);
    }

    private void priceUpdateNotify(Item updatedCurrentItem) {
	   for (Participant participant : participants) {
		  BigDecimal newItemPrice = updatedCurrentItem.getPrice();
		  participant.updateCurrentItemPrice(newItemPrice);
	   }
    }

    public Participant getItemOwner() {
	   return currentItemOwner;
    }

    public Participant getCurrentItemOwner() {
	   return currentItemOwner;
    }

    public void setCurrentItem(Item currentItem) {
	   this.currentItem = currentItem;
    }
}
