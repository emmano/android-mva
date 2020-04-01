package android.viewx;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import org.mockito.Mockito;

public class LayoutInflater {

  private static LayoutInflater inflater = Mockito.mock(LayoutInflater.class);

  public static LayoutInflater from(Context context) {
    return inflater;
  }

  public View inflate(int id, ViewGroup parent, boolean attachToParent){
    return Mockito.mock(View.class);
  }
}
