package ru.avicomp.map.spin.system;

import org.apache.jena.graph.Factory;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.avicomp.map.spin.functions.avc.factorial;
import ru.avicomp.map.spin.vocabulary.MATH;
import ru.avicomp.ontapi.jena.utils.Graphs;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;

/**
 * See {@code /etc/avc.factorial.ttl} spin-description and {@link factorial} ARQ function.
 * Created by @ssz on 21.12.2018.
 */
public class FactorialOntMAPExtension implements Extension {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactorialOntMAPExtension.class);

    @Override
    public Map<String, Graph> graphs() {
        return Collections.singletonMap("http://avc.ru/factorial", getGraph());
    }

    @Override
    public Map<String, Class<? extends Function>> functions() {
        return Collections.singletonMap(MATH.NS + "factorial", factorial.class);
    }

    public Graph getGraph() {
        Graph res = Factory.createGraphMem();
        try (InputStream in = FactorialOntMAPExtension.class.getResourceAsStream("/etc/avc.factorial.ttl")) {
            RDFDataMgr.read(res, in, null, Lang.TURTLE);
        } catch (IOException e) {
            throw new UncheckedIOException("Can't load graph", e);
        }
        LOGGER.debug("Graph {} is loaded, size: {}", Graphs.getName(res), res.size());
        return res;
    }
}
