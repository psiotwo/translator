package cz.sio2.translator.dbpedia;

import cz.sio2.translator.Engine;
import cz.sio2.translator.TranslateEnvironment;
import cz.sio2.translator.webservicex.WebserviceXEngine;

public class DBPediaEngineTest extends TranslateEnvironment {

    @Override
    protected Engine getEngine() {
        return new DBPediaEngine();
    }
}
