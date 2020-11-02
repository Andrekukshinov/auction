package by.kukshinov.auction.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AuctionStarter {
    private static final Logger LOGGER =  LogManager.getLogger();

    public void startAuctionBidding(Auction auction) {
	   List<Participant> participants = auction.getParticipants();
	   int size = participants.size();
	   ExecutorService service = null;
	   List<Item> items = auction.getItems();
	   try {
		  service = Executors.newFixedThreadPool(size);
		  for (Item itemToBidFor : items) {
			 auction.setCurrentItem(itemToBidFor);
			 for (Participant participant : participants) {
				participant.setAuction(auction);
				//replace init
				service.execute(participant);
			 }
			 TimeUnit timeUnit = TimeUnit.SECONDS;
			 timeUnit.sleep(5);
			 Participant currentItemOwner = auction.getCurrentItemOwner();
			 LOGGER.info("Item " + itemToBidFor + " is wun by " + currentItemOwner);

		  }
	   } catch (InterruptedException e) {
	       LOGGER.error(e.getMessage(), e);
	   } finally {
		  if (service != null) {
			 service.shutdown();
		  }
	   }
    }

}
