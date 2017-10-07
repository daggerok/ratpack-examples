package daggerok.services;

import daggerok.domain.Order;
import daggerok.domain.PlanetSummary;
import daggerok.domain.Size;
import daggerok.domain.Variant;

import java.util.Collection;
import java.util.List;

public interface PizzaService {

  void placeOrder(final String planet, final Variant variant, Size size);

  List<Order> takeOrdersFromBestPlanet();

  long countStandingOrders();

  Collection<PlanetSummary> getPlanetSummary();
}
