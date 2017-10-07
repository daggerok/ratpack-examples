package daggerok.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.val;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = PRIVATE)
public class PlanetOrder implements Serializable, Comparable<PlanetOrder> {

  private static final long serialVersionUID = -8366694856522168042L;

  List<Order> orders = new ArrayList<>();

  public long size() {
    return orders.size();
  }

  public void assignOrder(final Order order) {
    orders.add(order);
  }

  public List<Order> takeOrders() {
    val result = new ArrayList<>(orders);
    orders = new ArrayList<>();
    return result;
  }

  @Override
  public int compareTo(final PlanetOrder o) {
    return o.orders.size() - this.orders.size();
  }
}
