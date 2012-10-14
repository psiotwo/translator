package cz.sio2.translator.dbpedia;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.RDFNode;
import cz.sio2.translator.Engine;
import cz.sio2.translator.TranslatorException;
import cz.sio2.translator.TranslatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DBPediaEngine implements Engine {

    public Map<Locale,String> translate(String text, Locale from, List<Locale> to) throws TranslatorException {
        LOG.info("Trying with from '" + from + "'");
        Map<Locale,String> map = new HashMap<Locale, String>( translate(text, from.toString(), to) );
        List<Locale> list = new ArrayList<Locale>();
        
        for (Locale l : map.keySet()) {
            String s = map.get(l);

            if (s == null || s.trim().isEmpty()) {
                list.add(l);
            }
        }

        if (!list.isEmpty()) {
            LOG.info("Trying with null from");
            map.putAll(translate(text, (String) null, list));
        }

        return map;
    }

    private Map<Locale,String> translate(String text, String from, List<Locale> to) throws TranslatorException {
        try {
            LOG.info("Translating '" + text + "' from " + from + ", to " + to);

            String varList = "";
            String where = "";

            for (Locale loc : to) {
                if (varList.length() > 0) {
                    varList += " ";
//                    where += " AND ";
                }

                String var = "?v_" + loc;
                varList += var;


                where += "OPTIONAL  {?r rdfs:label " + var + " . FILTER langMatches( lang(" + var + "), \"" + loc + "\")} . ";
            }

            String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                    + " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                    + " SELECT " + varList
                    + " WHERE {"
                    + " ?r rdfs:label \"" + text.replaceFirst(text.substring(0, 1), text.substring(0, 1).toUpperCase()) + (from == null ? "\"" : "\"@" + from) + " . "//?l. "
                    + where
                    //                + "FILTER regex(?l, \""+r.getLabel(null) + "\", \"i\")"
                    + "}";

            final Query query = QueryFactory.create(sparqlQuery);
            LOG.debug("Executing query " + query.toString(Syntax.syntaxSPARQL_11));

            final QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
            final ResultSet rs = qexec.execSelect();

            Map<Locale,String> results = new HashMap<Locale,String>();
            if (rs.hasNext()) {
                QuerySolution b = rs.next();
                for (Locale loc : to) {
                    RDFNode ol = b.get("v_" + loc);
                    if (ol != null) {
                        results.put(loc, ol.asLiteral().getString());
                    }
                }
            }
            LOG.info("     > result: '" + results + "'");
            return results;
        } catch (Exception ex) {
            LOG.error("Error occured during translation", ex);
            throw new TranslatorException(ex);
        }
    }
}
