package cz.sio2.translator.microsofttranslate;

import java.io.IOException;
import java.util.*;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import cz.sio2.translator.Engine;
import cz.sio2.translator.TranslatorException;
import cz.sio2.translator.TranslatorException;

public class MicrosoftTranslateEngine implements Engine {

    public MicrosoftTranslateEngine() {
        Properties p = new Properties();
        try {
            p.load(getClass().getResource("/microsoft-translate.properties").openStream());
            set(p.getProperty("clientId"), p.getProperty("clientSecret"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public MicrosoftTranslateEngine(String clientId, String clientSecret) {
        set(clientId,clientSecret);
    }

    private static void set(String clientId, String clientSecret) {
        try {
            Translate.setClientId(clientId);
            Translate.setClientSecret(clientSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Locale,String> translate(String text, Locale from, List<Locale> to) throws TranslatorException {
        try {
            LOG.info("Translating '" + text + "' from " + from.toString() + ", to " + to);
            Map<Locale,String> map = new HashMap<Locale,String>();
            Language fromL = Language.fromString(from.toString());
            for (final Locale lang : to) {
                map.put(lang,Translate.execute(text, fromL, Language.fromString(lang.toString())));
            }
            LOG.debug("     > result: '" + map + "'");
            return map;
        } catch (Exception ex) {
            LOG.error("Error occured during translation", ex);
            throw new RuntimeException(ex);
        }
    }
}
