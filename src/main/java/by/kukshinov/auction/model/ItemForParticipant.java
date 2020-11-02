package by.kukshinov.auction.model;

import java.math.BigDecimal;

public class ItemForParticipant {
    private long id;
    private BigDecimal price;

    public ItemForParticipant(long id, BigDecimal price) {
	   this.id = id;
	   this.price = price;
    }

    public long getId() {
	   return id;
    }

    public void setId(long id) {
	   this.id = id;
    }

    public BigDecimal getPrice() {
	   return price;
    }

    public void setPrice(BigDecimal price) {
	   this.price = price;
    }
}
