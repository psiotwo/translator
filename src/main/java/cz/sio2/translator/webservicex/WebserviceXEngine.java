package cz.sio2.translator.webservicex;

import cz.sio2.translator.TranslatorException;
import cz.sio2.translator.webservicex.generated.TranslateService;
import cz.sio2.translator.Engine;
import cz.sio2.translator.TranslatorException;

import java.util.*;

public class WebserviceXEngine implements Engine {

    TranslateService service;

    public WebserviceXEngine() {
        service = new TranslateService();
    }

    public Map<Locale,String> translate(String text, Locale from, List<Locale> to) throws TranslatorException {
        try {
            LOG.info("Translating '" + text + "' from " + from.toString() + ", to " + to);
            Map<Locale,String> map = new HashMap<Locale,String>();

            map.put(Locale.FRENCH,
                    service.getTranslateServiceSoap12().translate("EnglishTOFrench", text));
//            Language fromL = Language.fromString(from.toString());
//            for (final Locale lang : to) {
//                map.put(lang,Translate.execute(text, fromL, Language.fromString(lang.toString())));
//            }
            LOG.debug("     > result: '" + map + "'");
            return map;
        } catch (Exception ex) {
            LOG.error("Error occured during translation", ex);
            throw new RuntimeException(ex);
        }
    }
}
