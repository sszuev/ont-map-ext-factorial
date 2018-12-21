# This is an example of [ONT-MAP](https://github.com/avicomp/ont-map) extension with a factorial function.

## Structure:
* main:
    - ru.avicomp.map.spin.functions.avc.factorial - ARQ function to calculate `n!` in RDF
    - ru.avicomp.map.spin.system.FactorialOntMAPExtension - a module initializer
    - resources/etc/avc.factorial.ttl - SPIN library with function description
    - resources/META-INF/services/org.apache.jena.sys.JenaSubsystemLifecycle - a resource file with a reference to module initializer, see [Apache Jena - The Standard Subsystem Registry](http://jena.apache.org/documentation/notes/system-initialization.html#the-standard-subsystem-registry)
* tests:
    - ru.avicomp.map.tests.FactorialMapTest - a test, that builds a mapping and runs inference
    - resources/log4j.properties - logging settings

## Dependencies 
 - **[ONT-MAP](https://github.com/avicomp/ont-map)** the [OWL2](https://www.w3.org/TR/owl2-overview/) ontology to ontology data mapper (builder and inference engine) 
 - **[ONT-API](https://github.com/avicomp/ont-api)** the OWL-API implementation on top of Jena, transitively from ONT-MAP
 - [SPIN-RDF API](https://github.com/spinrdf/spinrdf) (but right now it is [Topbraid SHACL, ver 1.0.1](https://github.com/TopQuadrant/shacl)) - a [SPIN-RDF](http://spinrdf.org/) API, transitively from ONT-MAP
 - [Jena-ARQ](https://github.com/apache/jena) transitively from ONT-API
 - [OWL-API](https://github.com/owlcs/owlapi) transitively from ONT-API
 
## License
* Apache License Version 2.0