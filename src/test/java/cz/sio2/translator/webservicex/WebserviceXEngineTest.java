package cz.sio2.translator.webservicex;

import cz.sio2.translator.*;

public class WebserviceXEngineTest extends TranslateEnvironment {

    @Override
    protected Engine getEngine() {
        return new WebserviceXEngine();
    }
}
