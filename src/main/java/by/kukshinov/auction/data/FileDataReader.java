package by.kukshinov.auction.data;

import java.util.List;

public interface FileDataReader<T> {
    List<T> readData(String filePath) throws DataException;
}
