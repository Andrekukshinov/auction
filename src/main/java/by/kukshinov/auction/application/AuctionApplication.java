package by.kukshinov.auction.application;

import by.kukshinov.auction.model.Auction;
import by.kukshinov.auction.model.Item;
import by.kukshinov.auction.model.Participant;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AuctionApplication {

    public static final int FIRST_ITEM_ID = 1;
    public static final int SECOND_ITEM_ID = 2;
    public static final String VASE = "Vase";
    public static final String CANDLE = "Candle";
    public static final BigDecimal FIRST_PRICE = new BigDecimal(450);
    public static final BigDecimal SECOND_PRICE = new BigDecimal(455);

    public static final int FIRST_PARTICIPANT_ID = 1;
    public static final int SECOND_PARTICIPANT_ID = 2;
    public static final BigDecimal FIRST_CAPITAL = new BigDecimal(23450);
    public static final BigDecimal SECOND_CAPITAL = new BigDecimal(54455);

    public static void main(String[] args) {
	   Auction auction = Auction.getInstance();
	   Item vase = new Item(FIRST_ITEM_ID, VASE, FIRST_PRICE);
	   Item candle = new Item(SECOND_ITEM_ID, CANDLE, SECOND_PRICE);
	   Item lamp = new Item(3, "lamp", new BigDecimal("5633"));
	   List<Item> expectedItems = Arrays.asList(vase, candle, lamp);
	   Participant firstParticipant = new Participant(FIRST_PARTICIPANT_ID,
			 FIRST_CAPITAL);
	   Participant secondParticipant = new Participant(SECOND_PARTICIPANT_ID,
			 SECOND_CAPITAL);
	   Participant thirdParticipant = new Participant(3,
			 new BigDecimal(74455));
	   List<Participant> expectedParticipants = Arrays
			 .asList(firstParticipant, secondParticipant, thirdParticipant);
	   auction.setParticipants(expectedParticipants);
	   auction.setItems(expectedItems);
	   auction.startAuctionBidding();
    }
}
