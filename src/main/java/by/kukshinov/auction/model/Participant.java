package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Participant implements Runnable {
    private static final BigDecimal THRESHOLD_CAPITAL_PERCENT = new BigDecimal("0.2");
    private long id;
    private BigDecimal myLastUpdatedPrice;
    private Item currentItem;
    private BigDecimal capital;
    private Auction auction;
    private Semaphore semaphore;

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
		  try {
			 semaphore.acquire();
		  } catch (InterruptedException e) {
			 e.printStackTrace();
		  }
		  isDesired = makeDecisionOnBidding();
		  semaphore.release();
	   } while (isDesired);
	   String threadName = Thread.currentThread().getName();
	   buyIfOwner();
	   System.out.println("Thread " + threadName + "'s capital is " + capital);
	   System.out.println("Thread " + threadName + " is out for the lot...");
    }

    private void buyIfOwner() {
	   Participant itemOwner = currentItem.getItemOwner();
	   if(this.equals(itemOwner)) {
		  BigDecimal itemPrice = currentItem.getPrice();
		  capital = capital.subtract(itemPrice);
	   }
    }

    private boolean makeDecisionOnBidding() {
	   boolean isDesired;
	   currentItem = auction.getCurrentItem();
	   isDesired = isDesired(currentItem);
	   BigDecimal itemPrice = currentItem.getPrice();
	   boolean isLastUpdatedByMe = itemPrice.equals(myLastUpdatedPrice);
	   if (isDesired && !isLastUpdatedByMe) {
		bidForItem(currentItem);
	  }
	   return isDesired;
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

    private boolean isDesired(Item currentItem) {
	   BigDecimal subtracted = getAvailableBetSize(currentItem);
	   int compared = subtracted.compareTo(BigDecimal.ZERO);
	   return compared > 0;
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

    public void setSemaphore(Semaphore semaphore) {
	   this.semaphore = semaphore;
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
