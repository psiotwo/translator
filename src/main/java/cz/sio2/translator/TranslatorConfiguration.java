package cz.sio2.translator;

import cz.sio2.translator.Engine;
import cz.sio2.translator.dbpedia.DBPediaEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TranslatorConfiguration {

    public final static TranslatorConfiguration DEFAULT;

    static {
        DEFAULT = new TranslatorConfiguration();
        DEFAULT.setPrefix("[");
        DEFAULT.setPostfix("]");
        DEFAULT.setSourceLocale(Locale.ENGLISH);
        DEFAULT.setTargetLocales(Locale.GERMAN);
        DEFAULT.setEngine(new DBPediaEngine());

    }
    protected Locale sourceLocale;

    /**
     * Get the value of sourceLocale
     *
     * @return the value of sourceLocale
     */
    public Locale getSourceLocale() {
        return sourceLocale;
    }

    /**
     * Set the value of sourceLocale
     *
     * @param sourceLocale new value of sourceLocale
     */
    public void setSourceLocale(Locale sourceLocale) {
        this.sourceLocale = sourceLocale;
    }
    protected List<Locale> targetLocales;

    /**
     * Get the value of targetLocales
     *
     * @return the value of targetLocales
     */
    public List<Locale> getTargetLocales() {
        return targetLocales;
    }

    /**
     * Set the value of targetLocales
     *
     * @param targetLocales new value of targetLocales
     */
    public void setTargetLocales(List<Locale> targetLocales) {
        this.targetLocales = targetLocales;
    }

    public void setTargetLocales(Locale... targetLocales) {
        this.targetLocales = Arrays.asList(targetLocales);
    }
    protected String prefix;

    /**
     * Get the value of prefix
     *
     * @return the value of prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set the value of prefix
     *
     * @param prefix new value of prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    protected String postfix;

    /**
     * Get the value of postfix
     *
     * @return the value of postfix
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * Set the value of postfix
     *
     * @param postfix new value of postfix
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
    protected Engine engine;

    /**
     * Get the value of engine
     *
     * @return the value of engine
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Set the value of engine
     *
     * @param engine new value of engine
     */
    public void setEngine(Engine translator) {
        this.engine = translator;
    }
}
