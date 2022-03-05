package ru.congas.pages.testApps;

import ru.congas.core.application.Bundle;
import ru.congas.core.pages.ErrorActivity;

/**
 * @author Mr_Told
 */
@SuppressWarnings("unused")
public class TestAppCrash extends ErrorActivity {

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(new Bundle().addExtra("err", "TestError")
                .addExtra("cause", "TestAppCrash")
                .addExtra("exception", new RuntimeException("This is test exception")));
    }

}
