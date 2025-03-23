package vn.feature.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.feature.util.LocalizationUtils;

@Component
public abstract class TranslateMessages {

    @Autowired
    private LocalizationUtils localizationUtils;

    // translate message
    protected String translate(String message) {
        return localizationUtils.getLocalizedMessage(message);
    }

    // translate many message
    protected String translate(String message, Object... listMessage) {
        return localizationUtils.getLocalizedMessage(message, listMessage);
    }
}
