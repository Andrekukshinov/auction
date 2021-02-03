package by.kukshinov.auction.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AuctionStarter {
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
			 System.out.println("Item " + itemToBidFor + " is wun by " + currentItemOwner);

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
