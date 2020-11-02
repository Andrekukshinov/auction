package by.kukshinov.auction.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Participant implements Runnable {
    private static final Lock lock = new ReentrantLock();

    private long id;
    private String participantName;
    private BigDecimal myLastUpdatedPrice;
    private ItemDTO currentItem;
    private Auction auction;

    @JsonCreator
    public Participant(
		  @JsonProperty("id") long id,
		  @JsonProperty("participantName") String participantName) {
	   this.id = id;
	   this.participantName = participantName;
    }


    @Override
    public void run() {
	   boolean isDesired;
	   do {
		  //make a decision
		  TimeUnit waitTimer = TimeUnit.MICROSECONDS;
		  try {
			 waitTimer.sleep(10);
		  } catch (InterruptedException e) {
			 e.printStackTrace();
		  }
		  lock.lock();
		  currentItem = auction.getCurrentItem();
		  BigDecimal itemPrice = currentItem.getItemPrice();
		  isDesired = isDesired();
		  if (isDesired && !itemPrice.equals(myLastUpdatedPrice)) {
			 bidForItem(currentItem);
		  }
		  lock.unlock();
	   } while (isDesired);
	   String threadName = Thread.currentThread().getName();
	   System.out.println("Thread " + threadName + " is out for the lot...");
    }


    private boolean isDesired() {
	   Random randomDecision = new Random();
	   int chance = randomDecision.nextInt(100);
	   return chance > 5;
    }

    public void updateCurrentItemPrice(ItemDTO currentItem) {
	   this.currentItem = currentItem;
    }

    private void bidForItem(ItemDTO currentItem) {
	   BigDecimal currentPrice = currentItem.getItemPrice();
	   myLastUpdatedPrice = currentPrice.add(BigDecimal.ONE);
	   auction.requestPriseRaise(myLastUpdatedPrice, this);
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
