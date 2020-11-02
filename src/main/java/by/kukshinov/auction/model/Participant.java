package by.kukshinov.auction.model;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Participant implements Runnable {
    private static final Lock lock = new ReentrantLock();
    private static final BigDecimal THRESHOLD_CAPITAL_PERCENT = new BigDecimal("0.2");
    private long id;
    private ItemDTO itemData;
    private ItemDTO lastMyUpdatedItemData;
    private BigDecimal capital;
    private Auction auction;

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
		  TimeUnit waitTimer = TimeUnit.MILLISECONDS;
		  try {
			 waitTimer.sleep(100);
		  } catch (InterruptedException e) {
			 e.printStackTrace();
		  }
		  lock.lock();
		  isDesired = isDesired();
		  if (isDesired && !isLastUpdatedByMe()) {
			 bidForItem(itemData);
		  }
		  lock.unlock();
	   } while (isDesired);
	   String threadName = Thread.currentThread().getName();
	   buyIfOwner();
	   System.out.println("Thread " + threadName + "'s capital is " + capital);
	   System.out.println("Thread " + threadName + " is out for the lot...");
    }

    private boolean isLastUpdatedByMe() {
	   itemData = auction.getCurrentItem();
	   if (lastMyUpdatedItemData == null) {
		  return false;
	   }
	   BigDecimal itemPrice = itemData.getItemPrice();
	   BigDecimal myLastUpdatedPrice = lastMyUpdatedItemData.getItemPrice();
	   return itemPrice.equals(myLastUpdatedPrice);
    }

    private void buyIfOwner() {
	   Participant itemOwner = auction.getItemOwner();
	   if (this.equals(itemOwner)) {
		  BigDecimal itemPrice = itemData.getItemPrice();
		  capital = capital.subtract(itemPrice);
	   }
    }

    private boolean isDesired() {
	   Random randomDecision = new Random();
	   return randomDecision.nextBoolean();
    }


    public void updateCurrentItemPrice(ItemDTO currentItem) {
	   this.itemData = currentItem;
    }

    private void bidForItem(ItemDTO currentItem) {
	   BigDecimal currentPrice = currentItem.getItemPrice();
	   BigDecimal availableBetSize = getAvailableBetSize(itemData);
	   BigDecimal newPrice = currentPrice.add(availableBetSize);
	   lastMyUpdatedItemData = new ItemDTO(currentItem.getItemId(), newPrice);
	   auction.requestPriseRaise(lastMyUpdatedItemData, this);
    }


    private BigDecimal getAvailableBetSize(ItemDTO item) {
	   BigDecimal itemPrice = item.getItemPrice();
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
//8509

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
