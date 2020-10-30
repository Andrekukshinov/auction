package by.kukshinov.auction.data;

import by.kukshinov.auction.model.Item;
import by.kukshinov.auction.model.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParticipantFileDataReader implements FileDataReader<Participant> {

    private final ObjectMapper mapper;

    public ParticipantFileDataReader(ObjectMapper mapper) {
	   this.mapper = mapper;
    }

    @Override
    public List<Participant> readData(String filePath) throws DataException {
	   try {
		  File src = new File(filePath);
		  return mapper.readValue(src, mapper.getTypeFactory()
				.constructCollectionType(List.class, Participant.class));
	   } catch (IOException e) {
		  throw new DataException(e.getMessage(), e);
	   }
    }
}
