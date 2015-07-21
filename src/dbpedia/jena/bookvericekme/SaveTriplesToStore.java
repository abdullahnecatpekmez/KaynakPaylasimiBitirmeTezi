package dbpedia.jena.bookvericekme;
/*
 *DbPedia dan yazara gore veri cekmeyi  TDB yazilmasini saglar.
 */
import localOntology.jena.TDBveSorgu.LocalOntologyTripleStore;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;

public class SaveTriplesToStore {
	//jena log hatasi icin ve veri formatini gosteriyor.
	   static Logger l=Logger.getLogger(LocalOntologyTripleStore.class);
	    public static void main(String[] args) {
		BasicConfigurator.configure();
		//Ontology yaratiliyor.
		TripleStoreManager tripleStoreManager = new TripleStoreManager();
      //DbPediadan veri cekmek icin gerekli PREFÝXler ekleniyor.
		String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX foaf: <" + FOAF.getURI() + ">"
				+ "PREFIX dbo: <http://dbpedia.org/property/>"
				+ "PREFIX dbp-ont: <http://dbpedia.org/ontology/> "
				+ "PREFIX rdf: <" + RDF.getURI() + "> " + "CONSTRUCT {"
				+ "?book rdfs:label ?baslik.  " + "?book dbo:author ?yazar . "
				+ "?book foaf:isPrimaryTopicOf ?link .} " + "WHERE {"
				+ "?book rdf:type dbp-ont:Book."
				+ "?book rdfs:label ?baslik.  " + "?book dbo:author ?yazar . "
				+ "?book foaf:isPrimaryTopicOf ?link ."
				+ " FILTER(LANGMATCHES( LANG(?baslik), 'en')) "
				+ " FILTER(LANGMATCHES( LANG(?yazar), 'en'))} LIMIT 10";
		//Sorgunun calismasini saglayacak adres gosteriliyor.
		Model model = QueryExecutionFactory.sparqlService(
				"http://dbpedia.org/sparql", query).execConstruct();
		//TDB nerde yaratilacagini gosteriyor.
		tripleStoreManager.save("C:/Users/abdullah/Desktop/makale/PaperStore",
				model);
		//Ekrana yazdiriliyor.
		tripleStoreManager.getModel().write(System.out);

		// read from model
		TripleStoreManager readManager = new TripleStoreManager();
		readManager.readModel("C:/Users/abdullah/Desktop/makale/PaperStore");
		Model readedModel = readManager.getModel();
		StmtIterator stmtIterator = readedModel.listStatements();
		//write statements
		while (stmtIterator.hasNext()) {
			Statement statement = (Statement) stmtIterator.next();
			System.out.println(statement);
		}
	}

}
