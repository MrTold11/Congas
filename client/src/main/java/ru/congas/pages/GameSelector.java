package ru.congas.pages;

import ru.congas.CongasClient;
import ru.congas.loader.GameLoader;

/**
 * @author Mr_Told
 */
public final class GameSelector extends AbstractValueSelector {

    final GameLoader loader;

    public GameSelector(GameLoader loader) {
        super(loader.getName(), "Select game from " + loader.getName(), false, true, loader.getGames());
        this.loader = loader;
    }

    @Override
    protected void selected(String value) {
        CongasClient.openPage(loader.getNewGameInstance(value));
    }

}
