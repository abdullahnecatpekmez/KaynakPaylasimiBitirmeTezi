package javaKoduyla.articleOntology;
/*
 * Java koduyla basit bir bir ontology þemasi yaratma.
 */
import java.io.IOException;

import localOntology.jena.TDBveSorgu.LocalOntologyTripleStore;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class OntologyCreation {
	  //jena log hatasi icin ve veri formatini gosteriyor.
	   static Logger l=Logger.getLogger(LocalOntologyTripleStore.class);
	    public static void main(String[] args) {
		BasicConfigurator.configure();
		//local path/dosyaisi.owl
		String location = "C:/Users/abdullah/Desktop/makale/webtez.owl";
		try {
			ArticleOntologyCreator.createMakaleOntology(location);
		} catch (IOException e) {
			e.printStackTrace();
		}

		RDFDataMgr.loadModel(location)
				.write(System.out);
	}

}
