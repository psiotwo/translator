package cz.sio2.translator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class OntologyTranslator {

	private static final Logger LOG = LoggerFactory.getLogger(Engine.class);
	private final TranslatorConfiguration cfg;

	public OntologyTranslator(final TranslatorConfiguration cfg) {
		this.cfg = cfg;
	}

	private static String convert(final String s, final boolean firstLowerCase) {
		if (s == null) {
			return "";
		} else {
			String x = "";		
			char[] charArray=s.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				final char c = charArray[i];
				if (!x.isEmpty() && Character.isUpperCase(c) && !(i < charArray.length-1 && (Character.isUpperCase(charArray[i+1]) || Character.isWhitespace(charArray[i+1])))) {
					x += " " + Character.toLowerCase(c);
				} else if ( c == '_') {
					x += " ";
				} else {
					x += c;
				}
			}

			x = x.trim();

			if (firstLowerCase) {
				x = Character.toLowerCase(x.charAt(0)) + x.substring(1);
			}

			return x;
		}
	}

	private void updateResource(final OntResource r) throws TranslatorException {
		Locale loc = cfg.getSourceLocale();
		String label = r.getLabel(loc.getLanguage());

		if (label == null) {
			label = r.getLabel(null);
			if (label == null) {
				label = convert(r.getLocalName(), r.isProperty());
			}
			r.addLabel(label, loc.getLanguage());
		}

		final Map<Locale,String> map = cfg.getEngine().translate(label,
				cfg.getSourceLocale(), cfg.getTargetLocales());

		for (int i = 0; i < cfg.getTargetLocales().size(); i++) {
			final Locale tl = cfg.getTargetLocales().get(i);
			final String val = r.getLabel(tl.toString());
			if (val != null
					&& !val.isEmpty()
					&& !(val.startsWith(cfg.getPrefix()) && val.endsWith(cfg
							.getPostfix()))) {
				continue;
			}
			
			if( map.get(tl) != null) {			
				r.addLabel(cfg.getPrefix() + map.get(tl) + cfg.getPostfix(),tl.toString());
			}
		}
	}

	public void translate(final String inputURI, final String outputFN) {
		try {
			final OntModel m = ModelFactory.createOntologyModel();
			m.read(inputURI);

			for (OntClass clz : m.listClasses().toSet()) {

				if (!clz.isURIResource()) {
					continue;
				}

				updateResource(clz);
			}

			for (OntProperty prop : m
					.listAllOntProperties().toSet()) {

				if (!prop.isURIResource()) {
					continue;
				}
				updateResource(prop);
			}

			for (Individual prop : m.listIndividuals()
					.toSet()) {

				if (!prop.isURIResource()) {
					continue;
				}
				updateResource(prop);
			}

			m.write(new FileOutputStream(outputFN));
		} catch (TranslatorException ex) {
			LOG.error("Error during translation: ", ex);
		} catch (FileNotFoundException ex) {
			LOG.error("File not found: ", ex);
		}
	}
}
