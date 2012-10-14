package cz.sio2.translator;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.sio2.translator.TranslatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Engine {

    static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    public Map<Locale,String> translate(final String text, final Locale from, final List<Locale> to) throws TranslatorException;
}
