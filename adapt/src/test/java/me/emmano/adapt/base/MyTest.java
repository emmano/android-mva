package me.emmano.adapt.base;

import android.content.Context;
import android.view.LayoutInflater;

import org.mockito.Mockito;

public class MyTest {
    private static LayoutInflater inflater = Mockito.mock(LayoutInflater.class);

    public LayoutInflater from(Context context) {
        return inflater;
    }

    @org.junit.Test
    public void test() {

    }
}
