package ru.congas.pages.testApps;

import ru.congas.core.application.Bundle;
import ru.congas.core.pages.ErrorActivity;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mr_Told
 */
@SuppressWarnings("unused")
public class TestCriticalCrash extends ErrorActivity {

    @Override
    public void onCreate(Bundle args) {
        try {
            Field counter = ErrorActivity.class.getDeclaredField("fatalCounter");
            ((AtomicInteger) counter.get(this)).set(777);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        super.onCreate(new Bundle().addExtra("err", "TestError")
                .addExtra("cause", "TestAppCrash")
                .addExtra("exception", new RuntimeException("This is test exception")));
    }

}
