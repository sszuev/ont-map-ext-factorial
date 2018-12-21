package ru.avicomp.map.tests;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.XSD;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topbraid.spin.vocabulary.SP;
import ru.avicomp.map.Managers;
import ru.avicomp.map.MapFunction;
import ru.avicomp.map.MapManager;
import ru.avicomp.map.MapModel;
import ru.avicomp.map.spin.vocabulary.MATH;
import ru.avicomp.map.spin.vocabulary.SPINMAPL;
import ru.avicomp.ontapi.jena.OntModelFactory;
import ru.avicomp.ontapi.jena.model.OntClass;
import ru.avicomp.ontapi.jena.model.OntDT;
import ru.avicomp.ontapi.jena.model.OntGraphModel;
import ru.avicomp.ontapi.jena.model.OntNDP;
import ru.avicomp.ontapi.jena.utils.Iter;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Created by @ssz on 21.12.2018.
 */
@SuppressWarnings("WeakerAccess")
public class FactorialMapTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactorialMapTest.class);

    public static String asString(Model m) {
        StringWriter s = new StringWriter();
        m.write(s, "ttl");
        return s.toString();
    }

    @Test
    public void testRunInference() {
        LOGGER.info("Create ontology");
        String uri = "http://ex.com/factorial-test";
        String ns = uri + "#";
        OntGraphModel o = getOntology(uri);
        LOGGER.debug("Source ontology <{}>:\n{}", ns, asString(o));

        MapManager m = Managers.createMapManager();
        MapFunction factorial = m.getFunction(MATH.NS + "factorial");
        MapFunction self = m.getFunction(SPINMAPL.self);
        Assert.assertNotNull(factorial);
        Assert.assertNotNull(self);

        MapModel map = m.createMapModel();
        OntClass c = o.getOntEntity(OntClass.class, ns + "Numbers");
        OntNDP p1 = o.getOntEntity(OntNDP.class, ns + "number");
        OntNDP p2 = o.getOntEntity(OntNDP.class, ns + "factorial");
        map.createContext(c, c, self.create()).addPropertyBridge(factorial.create().addProperty(SP.arg1, p1), p2);
        LOGGER.debug("Mapping:\n{}", asString(map.asGraphModel()));

        map.runInference(o.getBaseGraph(), o.getBaseGraph());
        LOGGER.debug("After inference <{}>:\n{}", ns, asString(o));

        // validate:
        o.listNamedIndividuals().forEach(i -> {
            Assert.assertEquals(2, i.positiveAssertions().count());
            int num = Iter.findFirst(i.listProperties(p1).mapWith(Statement::getInt))
                    .orElseThrow(AssertionError::new);
            double actual = Iter.findFirst(i.listProperties(p2).mapWith(Statement::getDouble))
                    .orElseThrow(AssertionError::new);
            double expected = CombinatoricsUtils.factorialDouble(num);
            Assert.assertEquals(expected, actual, 0.00001);
        });
    }

    public OntGraphModel getSchema(String uri) {
        String ns = uri + "#";
        OntGraphModel res = OntModelFactory.createModel()
                .setNsPrefixes(OntModelFactory.STANDARD)
                .setNsPrefix("test", ns);
        res.setID(uri);
        OntClass clazz = res.createOntEntity(OntClass.class, ns + "Numbers");
        OntNDP p1 = res.createOntEntity(OntNDP.class, ns + "number");
        OntNDP p2 = res.createOntEntity(OntNDP.class, ns + "factorial");
        p1.addDomain(clazz);
        p2.addDomain(clazz);
        p1.addRange(res.getOntEntity(OntDT.class, XSD.integer));
        p2.addRange(res.getOntEntity(OntDT.class, XSD.xdouble));
        return res;
    }

    public OntGraphModel getOntology(String uri) {
        OntGraphModel res = getSchema(uri);
        String ns = uri + "#";
        OntClass c = res.getOntEntity(OntClass.class, ns + "Numbers");
        OntNDP p = res.getOntEntity(OntNDP.class, ns + "number");
        OntDT integer = res.getOntEntity(OntDT.class, XSD.integer);
        Arrays.asList(1, 5, 12, 166, 2235)
                .forEach(i -> c.createIndividual(ns + i).addProperty(p, integer.createLiteral(String.valueOf(i))));
        return res;
    }
}
