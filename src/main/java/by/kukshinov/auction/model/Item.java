package by.kukshinov.auction.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Item {
    private long id;
    private String name;
    private BigDecimal price;

    @JsonCreator()
    public Item(
		  @JsonProperty("id") long id,
		  @JsonProperty("name") String name,
		  @JsonProperty("price") BigDecimal price) {
	   this.id = id;
	   this.name = name;
	   this.price = price;
    }

    public void setName(String name) {
	   this.name = name;
    }

    public long getId() {
	   return id;
    }

    public void setId(long id) {
	   this.id = id;
    }

    public String getName() {
	   return name;
    }

    public BigDecimal getPrice() {
	   return price;
    }

    public void setPrice(BigDecimal price) {
	   this.price = price;
    }


    @Override
    public String toString() {
	   return "Item{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + '}';
    }

    @Override
    public boolean equals(Object o) {
	   if (this == o) {
		  return true;
	   }
	   if (o == null || getClass() != o.getClass()) {
		  return false;
	   }

	   Item item = (Item) o;

	   if (getId() != item.getId()) {
		  return false;
	   }
	   if (getName() != null ? !getName().equals(item.getName()) : item
			 .getName() != null) {
		  return false;
	   }
	   return getPrice() != null ? getPrice().equals(item.getPrice()) : item
			 .getPrice() == null;
    }

    @Override
    public int hashCode() {
	   int result = (int) (getId() ^ (getId() >>> 32));
	   result = 31 * result + (getName() != null ? getName().hashCode() : 0);
	   result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
	   return result;
    }

}
