package android.osx;

import org.mockito.Mockito;

public class Looper {

  private static Looper looper = new Looper();

  public static Looper getMainLooper() {
    return looper;
  }
}
