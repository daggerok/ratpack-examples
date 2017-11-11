package daggerok;

import java.util.Objects;

import static java.lang.String.format;

public class JavaClasses {

  public interface MyService {
    String myMethod(final String string);
  }

  public static class MyServiceImpl implements MyService {

    @Override
    public String myMethod(final String string) {
      Objects.requireNonNull(string, "string must not be null");
      return format("this is a '%s'", string.length() == 0 ? "nothing" : string);
    }
  }

  public static void main(String[] args) {
    // stub
  }
}
