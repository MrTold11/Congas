package ru.congas.loader;

import ru.congas.SimpleGame;
import ru.congas.pages.testGames.TestInputOutput;
import ru.congas.pages.testGames.TestPictureOutput;

/**
 * Anthology loader for test applications
 * @author Mr_Told
 */
public class TestGames implements GameLoader {

    @Override
    public SimpleGame getNewGameInstance(String name) {
        switch (name) {
            case "TestInputOutput":
                return new TestInputOutput();
            case "TestPictureOutput":
                return new TestPictureOutput();
        }
        return null;
    }

    @Override
    public String getName() {
        return "TestGames";
    }

    @Override
    public String[] getGames() {
        return new String[] {"TestInputOutput", "TestPictureOutput"};
    }
}
