package by.kukshinov.auction.application;

import by.kukshinov.auction.data.DataException;
import by.kukshinov.auction.data.FileDataReader;
import by.kukshinov.auction.data.ItemFileDataReader;
import by.kukshinov.auction.data.ParticipantFileDataReader;
import by.kukshinov.auction.model.Auction;
import by.kukshinov.auction.model.AuctionStarter;
import by.kukshinov.auction.model.Item;
import by.kukshinov.auction.model.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AuctionApplication {
    public static final String PARTICIPANTS_JSON = "src/json/participants.json";

    public static void main(String[] args) throws DataException {
	   Auction auction = Auction.getInstance();
	   AuctionStarter starter = new AuctionStarter();
	   FileDataReader<Participant> participantFileDataReader = new ParticipantFileDataReader();
	   List<Participant> expectedParticipants = participantFileDataReader.readData(PARTICIPANTS_JSON);
	   auction.setParticipants(expectedParticipants);
	   starter.startAuctionBidding(auction);
    }
}
