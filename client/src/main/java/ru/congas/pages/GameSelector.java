package ru.congas.pages;

import ru.congas.loader.AnthologyLoader;

/**
 * @author Mr_Told
 */
public class GameSelector extends MenuSelector {

    final AnthologyLoader loader;

    public GameSelector(AnthologyLoader loader) {
        super(loader.getName(), "Select game from " + loader.getName(), false, loader.getGames());
        this.loader = loader;
    }

    @Override
    protected void selected(String value) {
        exit(loader.getNewGameInstance(value));
    }

}
