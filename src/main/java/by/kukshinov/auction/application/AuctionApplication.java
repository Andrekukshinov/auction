package by.kukshinov.auction.application;

import by.kukshinov.auction.data.DataException;
import by.kukshinov.auction.data.FileDataReader;
import by.kukshinov.auction.data.ItemFileDataReader;
import by.kukshinov.auction.data.ParticipantFileDataReader;
import by.kukshinov.auction.model.Auction;
import by.kukshinov.auction.model.Item;
import by.kukshinov.auction.model.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AuctionApplication {


    public static final String ITEMS_JSON = "src/json/items.json";
    public static final String PARTICIPANTS_JSON = "src/json/participants.json";

    public static void main(String[] args) throws DataException {
	   Auction auction = Auction.getInstance();
	   ObjectMapper objectMapper = new ObjectMapper();
	   FileDataReader<Item> itemDataReader = new ItemFileDataReader(objectMapper);
	   FileDataReader<Participant> participantFileDataReader = new ParticipantFileDataReader(objectMapper);
	   List<Item> expectedItems = itemDataReader.readData(ITEMS_JSON);
	   List<Participant> expectedParticipants = participantFileDataReader.readData(PARTICIPANTS_JSON);
	   auction.setParticipants(expectedParticipants);
	   auction.setItems(expectedItems);
	   auction.startAuctionBidding();
    }
}
