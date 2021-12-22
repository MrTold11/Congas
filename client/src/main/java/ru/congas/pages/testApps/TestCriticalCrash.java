package ru.congas.pages.testApps;

import ru.congas.CongasClient;
import ru.congas.SimpleGame;

/**
 * @author Mr_Told
 */
public class TestCriticalCrash extends SimpleGame {

    int[] k;

    public TestCriticalCrash() {
        super("TestCriticalCrash", false, true, 100, 10, 10);

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(finalI * 100);
                } catch (InterruptedException ignored) {}
                CongasClient.openPage(new TestCriticalCrash(finalI));
            });
            t.start();
        }
    }

    public TestCriticalCrash(int i) {
        super("TestCriticalCrash" + i, false, true, 100, 10, 10);
    }

    @Override
    public boolean handle(int c) {
        return false;
    }

    @Override
    public void updateCanvas() {
        k[0] = 1;
    }
}
