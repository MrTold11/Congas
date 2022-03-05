package ru.congas.pages;

import ru.congas.core.application.Bundle;
import ru.congas.core.application.PageActivity;
import ru.congas.core.input.keys.Key;
import ru.congas.core.input.keys.KeyPressed;
import ru.congas.core.output.modifier.Color;
import ru.congas.core.output.modifier.Style;
import ru.congas.core.output.widgets.properties.Gravity;
import ru.congas.core.output.widgets.TextView;

/**
 * Page appears if an application that should be in the package somehow isn't loaded
 * @author Mr_Told
 */
public final class AppNotFound extends PageActivity {

    @Override
    public void onCreate(Bundle args) {
        String appName, packageName;
        try {
            appName = (String) args.getUnsafeObject("name");
            packageName = (String) args.getUnsafeObject("package");
        } catch (Exception e) {
            throw new IllegalArgumentException("Dialog cannot be created without arguments", e);
        }
        super.onCreate(args);
        addWidget(new TextView("Application " + appName + " not found in package " + packageName, new Style(Color.RED)))
                .pos().setGravity(Gravity.center);
        addWidget(new TextView("Press [Space] or [Enter] to continue", null))
                .pos().setGravity(Gravity.centerBottom);
        render();
    }

    @Override
    public boolean handle(KeyPressed event) {
        if (event.getDefinedKey() == Key.SPACE || event.getDefinedKey() == Key.ENTER) {
            closeActivity();
            return true;
        }
        return false;
    }

}
