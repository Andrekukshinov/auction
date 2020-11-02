package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Participant implements Runnable {
    private static final Lock lock = new ReentrantLock();

    private long id;
    private String participantName;
    private BigDecimal myLastUpdatedPrice;
    private Item currentItem;
    private Auction auction;

    public Participant(long id, String participantName) {
	   this.id = id;

	   this.participantName = participantName;
    }

    public Participant() {
    }


    @Override
    public void run() {
	   boolean isDesired;
	   do {
		  //make a decision
		  lock.lock();
		  currentItem = auction.getCurrentItem();
		  isDesired = isDesired();
		  BigDecimal itemPrice = currentItem.getPrice();
		  boolean isLastUpdatedByMe = itemPrice.equals(myLastUpdatedPrice);
		  if (isDesired && !isLastUpdatedByMe) {
			 bidForItem(currentItem);
		  }
		  lock.unlock();
	   } while (isDesired);
	   String threadName = Thread.currentThread().getName();
	   buyIfOwner();
	   System.out.println("Thread " + threadName + " is out for the lot...");
    }

    private void buyIfOwner() {
	   Participant itemOwner = auction.getItemOwner();
	   if (this.equals(itemOwner)) {
		  BigDecimal itemPrice = currentItem.getPrice();
	   }
    }


    private boolean isDesired() {
	   Random randomDecision = new Random();
	   return randomDecision.nextBoolean();
    }

    public void updateCurrentItemPrice(Item currentItem) {
	   this.currentItem = currentItem;
    }

    private void bidForItem(Item currentItem) {
	   BigDecimal currentPrice = currentItem.getPrice();
	   BigDecimal availableBetSize = BigDecimal.ONE;
	   BigDecimal newPrice = currentPrice.add(availableBetSize);
	   myLastUpdatedPrice = newPrice;
	   String threadName = Thread.currentThread().getName();
	   System.out.printf(" Thread %s raises price for %s to %s \n", threadName,
			 currentItem, newPrice);
	   auction.setNewItemPrice(newPrice, this);
    }

    public long getId() {
	   return id;
    }

    public void setId(long id) {
	   this.id = id;
    }


    public Auction getAuction() {
	   return auction;
    }

    public void setAuction(Auction auction) {
	   this.auction = auction;
    }

    public String getParticipantName() {
	   return participantName;
    }

    public void setParticipantName(String participantName) {
	   this.participantName = participantName;
    }

    @Override
    public boolean equals(Object o) {
	   if (this == o) {
		  return true;
	   }
	   if (o == null || getClass() != o.getClass()) {
		  return false;
	   }

	   Participant that = (Participant) o;

	   if (getId() != that.getId()) {
		  return false;
	   }
	   return participantName.equals(that.participantName);
    }

    @Override
    public int hashCode() {
	   int result = (int) (getId() ^ (getId() >>> 32));
	   result = 31 * result + participantName.hashCode();
	   return result;
    }

    @Override
    public String toString() {
	   return "Participant{" + "id=" + id + ", participantName='" + participantName + '\'' + '}';
    }
}
