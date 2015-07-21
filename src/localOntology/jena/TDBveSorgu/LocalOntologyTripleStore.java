package localOntology.jena.TDBveSorgu;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.util.FileManager;
/*Protege ile olusturulan Makale ontolojisini eclipcse gosterip Jena TDB yazdiriliyor.
 * Sparql sorgularýyla TDB ve model uzerinde sorgu yazilip sonucu ekrana  yazdiriliyor.
 * Ýstendigi kadar sorgu yazilabilir.TDB amac cok büyük veriler olunca (big data) kullanilmasi sart.
 *  */

import dbpedia.jena.bookvericekme.TripleStoreManager;
import dbpedia.jena.bookvericekme.*;

public class LocalOntologyTripleStore {
          //jena log hatasi icin ve veri formatini gosteriyor.
	   static Logger l=Logger.getLogger(LocalOntologyTripleStore.class);
	    public static void main(String[] args) {
		BasicConfigurator.configure();
		//Ontolojini NS  gosteriyoruz.
		String NS = "http://www.semanticweb.org/abdullah/ontologies/2015/1/Tez";
        String xmlbase = NS;
        
        
        //Model olusturulup gerekli ayarlamalar yapiliyor.
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);    
        RDFWriter rdfw = m.getWriter("RDF/XML-ABBREV");
        rdfw.setProperty("xmlbase", xmlbase);
        rdfw.setProperty("relativeURIs", "");
        
       
       //Ontolojini Localdaki yerini gosteriyor.
        String dosya = "C:/Users/abdullah/Desktop/makale/tez1.owl";
        
              
        InputStream in = FileManager.get().open(dosya);  
            if (in == null) {
                throw new IllegalArgumentException(dosya
                        + " bulunamadi");
            }
            //Bu yorumu acarsaniz ontolojideki verileri 
                   // Ontoloji modeli okuma islemini yapar
                m.read(in, xmlbase);
                        //okunan modeli konsolda ekrana yazdirir
               m.write(System.out,"N3");
         

		
		TripleStoreManager readManager = new TripleStoreManager();
		readManager.readModel("E:\\Java\\JSE\\TDB-0.8.10\\tdb");
		Model readedModel = readManager.getModel();
		StmtIterator stmtIterator = readedModel.listStatements();
		//write statements
		while (stmtIterator.hasNext()) {
			Statement statement = (Statement) stmtIterator.next();
			System.out.println(statement);
		}
		//Ontooji üzerinden okuma
        Test(m);
        //TDB üzerinde sorgu
        Test(readedModel);		
		
    }
	
	//Ontology uzerinde sorgulama yapiliyor.
	public static void Test(OntModel m)
	{  
		//Sorgu icin gerekli olan PREFIXsler
	String query="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
	"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
	"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
	"PREFIX : <http://www.semanticweb.org/abdullah/ontologies/2015/1/Tez#>"+
	   
		//Bir Makale ve o makalenin yazari,yazarin unvani,dili  ontologyden cekiliyor.
		"SELECT  ?makale ?yazar ?ulkesi ?unvani "  +
		"WHERE { ?makale :dildedir :Turkce."+
				"?makale :yazaridir ?yazar."+
				"?yazar :ulkesi ?ulkesi."+
				"?yazar :akademikSeviyesi ?unvani.}";
		
		 //Sorgu calistiriliyor.	
		 QueryExecution qe = QueryExecutionFactory.create(query, m);
	     ResultSet r =  qe.execSelect();
	     System.out.println("Ontoloji uzerinde sorgulama");
	     ResultSetFormatter.out(System.out, r);  	         
	}
	
	//TDBden Turkce dilinde olan yazarlar cekiliyor.
	public static void Test(Model tdb)
	{  //Gerekli PREFIXsler.
		String q="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
				"PREFIX : <http://www.semanticweb.org/abdullah/ontologies/2015/1/Tez#>"+
				   
			//Sorgu		
			"SELECT  ?makale ?yazar ?ulkesi ?unvani "  +
			"WHERE { ?makale :dildedir :Turkce."+
					"?makale :yazaridir ?yazar."+
					"?yazar :ulkesi ?ulkesi."+
					"?yazar :akademikSeviyesi ?unvani.}";
					/*"SELECT  ?yazar "+
					"WHERE { ?y :dildedir :Turkce."+
							"?y :yazaridir ?yazar}";*/
		QueryExecution qe = QueryExecutionFactory.create(q, tdb);
	     ResultSet r =  qe.execSelect();
	     System.out.println("Ontoloji uzerinde sorgulama");
	     ResultSetFormatter.out(System.out, r);  
	
	}
	}

	
