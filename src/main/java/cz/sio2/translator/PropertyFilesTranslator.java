package cz.sio2.translator;

import cz.sio2.util.OrderedProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFilesTranslator {

	private static final Logger LOG = LoggerFactory.getLogger(Engine.class);
	private final TranslatorConfiguration cfg;

	public PropertyFilesTranslator(TranslatorConfiguration cfg) {
		this.cfg = cfg;
	}

	public void translate(String file) {
		try {
			LOG.info("Translating '" + file + "' from "
					+ cfg.getSourceLocale().toString());

			final File f = new File(file);
			final OrderedProperties p = new OrderedProperties();
			p.load(new FileInputStream(f + cfg.getSourceLocale().toString()
					+ ".properties"));

			LOG.info("  > Loading property files for locales "
					+ cfg.getTargetLocales());
			final Map<Locale, OrderedProperties> tps = new HashMap<Locale, OrderedProperties>();
			for (final Locale l : cfg.getTargetLocales()) {
				final OrderedProperties tp = new OrderedProperties();
				tps.put(l, tp);

				final File fx = new File(f + l.toString() + ".properties");

				if (fx.exists()) {
					tp.load(new FileInputStream(fx));
				}
			}

			LOG.info("  > Translating ... ");
			for (final Object key : p.keySet()) {
				final String keyObject = (String) key;

				Map<Locale, String> translatedText = cfg.getEngine().translate(
						p.getProperty(keyObject), cfg.getSourceLocale(),
						cfg.getTargetLocales());
				LOG.debug("           key: " + keyObject + ", orig='"
						+ p.getProperty(keyObject) + "', trans='"
						+ translatedText + "'");

				for (final Locale t : cfg.getTargetLocales()) {
					final OrderedProperties tp = tps.get(t);

					String val;
					if (tp.containsKey(key)
							&& ((val = (String) tp.get(key)) != null)
							&& !val.isEmpty()
							&& !(val.startsWith(cfg.getPrefix()) && val
									.endsWith(cfg.getPostfix()))) {
						continue;
					}

					String value = translatedText.get(t); 
					if (value != null) {
						tp.put(key, cfg.getPrefix() + value
								+ cfg.getPostfix());
					}
				}
			}

			LOG.info("  > Storing property files for locales "
					+ cfg.getTargetLocales());
			for (final Locale l : tps.keySet()) {
				final File fx = new File(f + l.toString() + ".properties");

				if (!fx.exists()) {
					fx.createNewFile();
				}
				tps.get(l).store(new FileOutputStream(fx), "");
			}
		} catch (Exception ex) {
			LOG.error("error during translation", ex);
		}
	}
}
