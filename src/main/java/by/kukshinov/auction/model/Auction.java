package by.kukshinov.auction.model;

import by.kukshinov.auction.data.DataException;
import by.kukshinov.auction.data.ItemFileDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final String ITEMS_JSON = "src/json/items.json";
    private static final Logger LOGGER =  LogManager.getLogger();

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
				instance.items = new ItemFileDataReader().readData(ITEMS_JSON);
			 }
		  } catch (DataException e) {
		      //logger
			 LOGGER.error(e.getMessage(), e);
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
	   String s = String.format(" Participant %s raises price for %s to %s \n", currentItemOwner,
	   currentItem, currentItem.getPrice());
	   LOGGER.info(s);
	   priceUpdateNotify();
    }

    private void priceUpdateNotify() {
	   for (Participant participant : participants) {
		  ItemDTO forUser = new ItemDTO(currentItem.getId(), currentItem.getPrice());
		  participant.updateCurrentItemPrice(forUser);
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
