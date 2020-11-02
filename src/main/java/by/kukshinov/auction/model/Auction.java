package by.kukshinov.auction.model;

import by.kukshinov.auction.data.DataException;
import by.kukshinov.auction.data.ItemFileDataReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Auction {
    private static final Lock LOCKER = new ReentrantLock();
    private static Auction instance;

    private List<Participant> participants;
    private List<Item> items;

    private Participant currentItemOwner;

    private Item currentItem;
    private static final String ITEMS_JSON = "src/json/items.json";


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
				instance.items = new ItemFileDataReader().readData(ITEMS_JSON);
			 }
		  } catch (DataException e) {
			 e.printStackTrace();
			 //log here
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

    public ItemDTO getCurrentItem() {
	   BigDecimal itemPrice = currentItem.getPrice();
	   long id = currentItem.getId();
	   return new ItemDTO(id, itemPrice);
    }

    public void requestPriseRaise(ItemDTO participantItem, Participant currentItemOwner) {
	   BigDecimal suggestedPrice = participantItem.getItemPrice();
	   currentItem.setPrice(suggestedPrice);
	   this.currentItemOwner = currentItemOwner;
	   System.out.printf(" Participant %s raises price for %s to %s \n", currentItemOwner,
			 currentItem, currentItem.getPrice());
	   priceUpdateNotify(participantItem);
    }

    private void priceUpdateNotify(ItemDTO updatedCurrentItem) {
	   for (Participant participant : participants) {
		  participant.updateCurrentItemPrice(updatedCurrentItem);
	   }
    }

    public Participant getItemOwner() {
	   return currentItemOwner;
    }

    public Participant getCurrentItemOwner() {
	   return currentItemOwner;
    }

    public List<Item> getItems() {
	   return new ArrayList<>(items);
    }

    public void setCurrentItem(Item currentItem) {
	   this.currentItem = currentItem;
    }
}
