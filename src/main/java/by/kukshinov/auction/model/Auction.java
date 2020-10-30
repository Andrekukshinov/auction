package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Auction {
    //todo method for betting + executor service + while for every lot with exe service
    private static Auction instance;
    private static final Lock LOCKER = new ReentrantLock();
    Semaphore semaphore = new Semaphore(1);

    private List<Participant> participants;
    private List<Item> items;
    private Item currentItem;


    private Auction() {
    }

    public static Auction getInstance() {
	   if (instance == null) {
		  LOCKER.lock();
		  Auction localInstance;
		  if (instance == null) {
			 localInstance = new Auction();
			 instance = localInstance;
		  }
		  LOCKER.unlock();
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

    public Item getCurrentItem() {
	   return currentItem;
    }

    public void setNewItemPrice(BigDecimal newItemPrice, Participant currentItemOwner) {
	   currentItem.setPrice(newItemPrice);
	   currentItem.setItemOwner(currentItemOwner);
	   priceUpdateNotify(currentItem);
    }

    private void priceUpdateNotify(Item updatedCurrentItem) {
	   for (Participant participant : participants) {
		  participant.updateCurrentItemPrice(updatedCurrentItem);
	   }

    }

    public void startAuctionBidding() {
	   int size = participants.size();
	   ExecutorService service = null;
	   try {
		  service = Executors.newFixedThreadPool(size);
		  for (Item itemToBidFor : items) {
			 currentItem = itemToBidFor;
			 for (Participant participant : participants) {
				participant.setSemaphore(semaphore);
				participant.setAuction(this);
				service.execute(participant);
			 }
			 TimeUnit timeUnit = TimeUnit.SECONDS;
			 timeUnit.sleep(5);
			 System.out.println("Item " + currentItem + " is wun by " + currentItem
				    .getItemOwner());
		  }
	   } catch (InterruptedException e) {
		  e.printStackTrace();
	   } finally {
		  if (service != null) {
			 service.shutdown();
		  }
	   }
    }
}
