package javaKoduyla.articleOntology;
/*
 * Bu sinif ontology yaratiyor.
 * Makale(subject) ve Yazar(object) DbPedialink(Predicate) .
 * */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.XSD;

public class ArticleOntologyCreator {
	//Ontology NameSpace
	private static final String ONTOLOGY_URI = "http://ege.makale-ontology.com#";

	public static void createMakaleOntology(String location) throws IOException {
		OntModel ontModel = ModelFactory.createOntologyModel();

		OntClass makaleCls = ontModel.createClass(ONTOLOGY_URI + "Makale");
		OntClass authorCls = ontModel.createClass(ONTOLOGY_URI + "Author");
		ObjectProperty authorPrp = ontModel.createObjectProperty(ONTOLOGY_URI
				+ "author");
		authorPrp.addDomain(makaleCls);
		authorPrp.addRange(authorCls);
		DatatypeProperty labelPrp = ontModel
				.createDatatypeProperty(ONTOLOGY_URI + "label");
		labelPrp.addDomain(makaleCls);
		labelPrp.addRange(XSD.xstring);
		//Dosyaya yaziyor.
		ontModel.write(new FileWriter(new File(location)));
	}
}
