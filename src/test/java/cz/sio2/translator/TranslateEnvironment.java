package cz.sio2.translator;

import java.util.List;

import cz.sio2.translator.microsofttranslate.MicrosoftTranslateEngine;
import org.junit.Before;
import org.junit.Test;

public abstract class TranslateEnvironment {
    private final java.util.Locale from = java.util.Locale.ENGLISH;
    private final List<java.util.Locale> to = java.util.Arrays.asList(
            java.util.Locale.FRENCH, java.util.Locale.GERMAN);
    private TranslatorConfiguration cfg;

    @Before
    public void setup()
    {

    /*
    * prefix and postfix of a value - if a value starts with prefix and
    * ends with postfix it is translated. Its translation is wrapped back
    * to the prefix and postfix
    */
        cfg = new TranslatorConfiguration();
        cfg.setPrefix("[");
        cfg.setPostfix("]");
        cfg.setSourceLocale(from);
        cfg.setTargetLocales(to);
        cfg.setEngine(getEngine());
    }

    protected abstract Engine getEngine();

    /*
    * ========================================================================
    * Translation of a term
    * ========================================================================
    */
    @Test
    public void translateTerm() {
        try {
            System.out.println(getEngine().translate("drum", from, to));
        } catch (TranslatorException e) {
            e.printStackTrace();
        }
    }

    /*
    * ========================================================================
    * Translation of property files
    * ========================================================================
    */
    @Test
    public void translatePropertyFile() {
        final String file = "examples/messages_";
        new PropertyFilesTranslator(cfg)
                .translate(file);
    }

    /*
    * ========================================================================
    * Translation of an ontology
    * ========================================================================
    */
    @Test
    public void translateOntology() {
        final String inURI = "http://www.co-ode.org/ontologies/pizza/2007/02/12/pizza.owl";
        final String outURI = "examples/pizza-translated.owl";
        new OntologyTranslator(cfg).translate(
                inURI, outURI);
    }
}
