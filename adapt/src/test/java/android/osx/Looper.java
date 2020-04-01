package android.osx;

import org.mockito.Mockito;

public class Looper {

  private static Looper instance = Mockito.mock(Looper.class);

  public static Looper getMainLooper() {
    return instance;
  }
}
