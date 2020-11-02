package by.kukshinov.auction.model;

import java.math.BigDecimal;

public class ItemDTO {
    private final long itemId;
    private BigDecimal itemPrice;

    public ItemDTO(long itemId, BigDecimal itemPrice) {
	   this.itemId = itemId;
	   this.itemPrice = itemPrice;
    }

    public long getItemId() {
	   return itemId;
    }


    public BigDecimal getItemPrice() {
	   return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
	   this.itemPrice = itemPrice;
    }
}
