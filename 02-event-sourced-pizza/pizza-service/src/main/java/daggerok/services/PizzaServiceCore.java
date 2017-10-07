package daggerok.services;

import daggerok.domain.*;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE)
public class PizzaServiceCore implements PizzaService, Serializable {

  private static final long serialVersionUID = -1742316063379285621L;

  final Map<String, PlanetOrder> planetOrders = new ConcurrentHashMap<>();

  AtomicLong total = new AtomicLong(0);

  @Override
  public void placeOrder(final String planet, final Variant variant, final Size size) {

    val order = new Order(planet, variant, size);

    planetOrders.computeIfAbsent(order.getPlanet(), key -> new PlanetOrder())
                .assignOrder(order);

    total.incrementAndGet();
  }

  @Override
  public List<Order> takeOrdersFromBestPlanet() {

    val result = planetOrders.entrySet()
                             .stream()
                             .sorted(Comparator.comparing(Map.Entry::getValue))
                             .findFirst()
                             .map(Map.Entry::getValue)
                             .map(PlanetOrder::takeOrders)
                             .orElse(Collections.emptyList());

    total.addAndGet(-planetOrders.size());
    return result;
  }

  @Override
  public long countStandingOrders() {
    return total.get();
  }

  @Override
  public Collection<PlanetSummary> getPlanetSummary() {

    return planetOrders.entrySet()
                       .stream()
                       .map(entry -> new PlanetSummary(entry.getKey(), entry.getValue().size()))
                       .collect(Collectors.toList());
  }
}
