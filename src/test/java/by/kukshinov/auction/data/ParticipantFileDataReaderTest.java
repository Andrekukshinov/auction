package by.kukshinov.auction.data;


import by.kukshinov.auction.model.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ParticipantFileDataReaderTest {
    public static final String TEST_JSON_PARTICIPANTS = "src/test/resources/participants.json";
    public static final int FIRST_PARTICIPANT_ID = 1;
    public static final int SECOND_PARTICIPANT_ID = 2;
    public static final BigDecimal FIRST_CAPITAL = new BigDecimal(23450);
    public static final BigDecimal SECOND_CAPITAL = new BigDecimal(54455);


    @Test
    public void testReadDataShouldReadDataAndReturnParticipantsList() throws DataException {
	   Participant firstParticipant = new Participant(FIRST_PARTICIPANT_ID,
			 FIRST_CAPITAL);
	   Participant secondParticipant = new Participant(SECOND_PARTICIPANT_ID,
			 SECOND_CAPITAL);
	   List<Participant> expectedParticipants = Arrays
			 .asList(firstParticipant, secondParticipant);
	   ParticipantFileDataReader itemDataReader = new ParticipantFileDataReader();
	   List<Participant> items = itemDataReader.readData(TEST_JSON_PARTICIPANTS);
	   Assert.assertEquals(items, expectedParticipants);
    }
}
