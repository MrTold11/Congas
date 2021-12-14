package ru.congas.loader;

import ru.congas.SimpleGame;

/**
 * @author Mr_Told
 */
public interface GameLoader {

    SimpleGame getNewGameInstance(String name);

    String getName();

    String[] getGames();

}
