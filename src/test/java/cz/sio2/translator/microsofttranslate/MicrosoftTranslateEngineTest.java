package cz.sio2.translator.microsofttranslate;

import cz.sio2.translator.Engine;
import cz.sio2.translator.TranslateEnvironment;
import org.junit.Before;
import org.junit.Test;

public class MicrosoftTranslateEngineTest extends TranslateEnvironment {

    @Override
    protected Engine getEngine() {
        return new MicrosoftTranslateEngine();
    }
}
