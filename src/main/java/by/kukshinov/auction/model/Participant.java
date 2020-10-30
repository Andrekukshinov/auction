package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Participant implements Runnable {
    private static final Lock lock = new ReentrantLock();
    private static final BigDecimal THRESHOLD_CAPITAL_PERCENT = new BigDecimal("0.2");
    private long id;
    private BigDecimal myLastUpdatedPrice;
    private Item currentItem;
    private BigDecimal capital;
    private Auction auction;
    //    private Semaphore semaphore;
    public Participant(long id, BigDecimal capital) {
	   this.id = id;

	   this.capital = capital;
    }

    public Participant() {
    }


    @Override
    public void run() {
	   boolean isDesired;
	   do {
		  //make a decision
		  lock.lock();
		  isDesired = makeDecisionAndBetIfOk();
		  lock.unlock();
	   } while (isDesired);
	   String threadName = Thread.currentThread().getName();
	   buyIfOwner();
	   System.out.println("Thread " + threadName + "'s capital is " + capital);
	   System.out.println("Thread " + threadName + " is out for the lot...");
    }

    private void buyIfOwner() {
	   Participant itemOwner = auction.getItemOwner();
	   if(this.equals(itemOwner)) {
		  BigDecimal itemPrice = currentItem.getPrice();
		  capital = capital.subtract(itemPrice);
	   }
    }

    private boolean makeDecisionAndBetIfOk() {
	   boolean isDesired;
	   currentItem = auction.getCurrentItem();
	   isDesired = isDesired();
	   BigDecimal itemPrice = currentItem.getPrice();
	   boolean isLastUpdatedByMe = itemPrice.equals(myLastUpdatedPrice);
	   if (isDesired && !isLastUpdatedByMe) {
		bidForItem(currentItem);
	  }
	   return isDesired;
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
	   BigDecimal availableBetSize = getAvailableBetSize(currentItem);
	   BigDecimal newPrice = currentPrice.add(availableBetSize);
	   myLastUpdatedPrice = newPrice;
	   String threadName = Thread.currentThread().getName();
	   System.out.printf(" Thread %s raises price for %s to %s \n", threadName,
			 currentItem, newPrice);
	   auction.setNewItemPrice(newPrice, this);
    }


    private BigDecimal getAvailableBetSize(Item item) {
	   BigDecimal itemPrice = item.getPrice();
	   BigDecimal affordableBet = capital.multiply(THRESHOLD_CAPITAL_PERCENT);
	   return affordableBet.subtract(itemPrice);
    }

    public long getId() {
	   return id;
    }

    public void setId(long id) {
	   this.id = id;
    }

    public BigDecimal getCapital() {
	   return capital;
    }

    public void setCapital(BigDecimal capital) {
	   this.capital = capital;
    }

    public Auction getAuction() {
	   return auction;
    }

    public void setAuction(Auction auction) {
	   this.auction = auction;
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
	   return getCapital() != null ? getCapital().equals(that.getCapital()) : that
			 .getCapital() == null;
    }

    @Override
    public int hashCode() {
	   int result = (int) (getId() ^ (getId() >>> 32));
	   result = 31 * result + (getCapital() != null ? getCapital().hashCode() : 0);
	   return result;
    }

    @Override
    public String toString() {
	   return "Participant{" + "id=" + id + ", capital=" + capital + '}';
    }

}
