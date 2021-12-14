package ru.congas.pages;

/**
 * @author Mr_Told
 */
public class MainMenu extends AbstractValueSelector {

    public MainMenu() {
        super("MainMenu", "Congas Client", true, false, "Library", "Store", "Settings");
    }

    @Override
    protected void selected(String value) {
        switch (value) {
            case "Library":
                exit(new LibrarySelector());
                break;
            case "Store":
                break;
            case "Settings":
                break;
        }
    }

}
