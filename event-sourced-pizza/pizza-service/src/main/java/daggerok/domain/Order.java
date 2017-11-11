package daggerok.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Order implements Serializable {

  private static final long serialVersionUID = 6688028268846783517L;

  String planet;
  Variant variant;
  Size size;
}
