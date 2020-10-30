package by.kukshinov.auction.data;

import by.kukshinov.auction.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ItemFileDataReader implements FileDataReader<Item> {
    private final ObjectMapper objectMapper;

    public ItemFileDataReader(ObjectMapper objectMapper) {
	   this.objectMapper = objectMapper;
    }

    @Override
    public List<Item> readData(String filePath) throws DataException {
	   try {
		  File src = new File(filePath);
		  return objectMapper.readValue(src, objectMapper.getTypeFactory()
				.constructCollectionType(List.class, Item.class));
	   } catch (IOException e) {
		  throw new DataException(e.getMessage(), e);
	   }
    }
}
