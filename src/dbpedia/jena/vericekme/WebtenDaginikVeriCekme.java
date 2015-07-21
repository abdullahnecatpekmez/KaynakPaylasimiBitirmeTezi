package dbpedia.jena.vericekme;


/*DbPedia üzerinden verilen PREFÝXten sorguya gore webten yazar ismi baslik ve konu cekiyor.
 *Webten cekilen veri kitap ,makale ve normal bilgi bazinda veri olabilecek sekilde rasgele veri cekiyor.
 *  Cekilen verileri Subject predicate ve Object olarak ekrana yazdiriliyor. Wenten 10 tane olacak sekilde ayarlanmastir.
 *  Ýstendigi kadar veri cekilir.*/
import java.io.FileOutputStream;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.vocabulary.XSD;

public class WebtenDaginikVeriCekme {
	//jenanin kendi log hatasinda kurtarip cekilen verinin formatini gosteriyor.
static Logger l=Logger.getLogger(WebtenDaginikVeriCekme.class);
	public static void main(String[] args) {
		BasicConfigurator.configure();
		FileOutputStream outputFile;
		int sayici=0;
		//Modelin NameSpacesini veriyoruz.
		String NS = "http://localhost/owl/ontologies/WebVeriTez/#";
		 try {
			 //Cekilen verinin owl uzantili dosyaya gosterilen local yere yazdiriyoruz.
	            outputFile = new FileOutputStream("C:/Users/abdullah/Desktop/makale/WebVeriTez.owl");
	             
	       
		
	
        String xmlbase = NS;
        
        
 
        //Model olusturulup yazdirma icin ayarlama yapiliyor.        
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);
        RDFWriter rdfw = m.getWriter("RDF/XML-ABBREV");
        rdfw.setProperty("xmlbase", xmlbase);
        rdfw.setProperty("relativeURIs", "");
	    QueryExecution qexec = null;
		  //Verinin hangi formatta owl dosyasina yazacagimizi modelliyoruz.
            OntClass Makale= m.createClass(NS + "Makale");
            DatatypeProperty konu= m.createDatatypeProperty(NS + "konu");
        	konu.addDomain(m.getOntClass(NS + "Makale"));
            konu.addRange(XSD.xstring);
            DatatypeProperty yazar= m.createDatatypeProperty(NS + "yazar");
        	yazar.addDomain(m.getOntClass(NS + "Makale"));
            yazar.addRange(XSD.xstring);
            DatatypeProperty dblink= m.createDatatypeProperty(NS + "dblink");
        	dblink.addDomain(m.getOntClass(NS + "Makale"));
            dblink.addRange(XSD.xstring);

		    try {	    	
		        while(true){
		        	//Sorgunun nerde calistigini gosteriyoruz
		            String service = "http://dbpedia.org/sparql";

		            StringBuffer stbSparqlQueryString = new StringBuffer("");
                   //Cekilen veriye gore PREFIXleri ekliyoruz.http://prefix.cc/ linkinden istedigimiz prefixsi bulabiliriz.		            
		            stbSparqlQueryString.append(" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
		            stbSparqlQueryString.append(" PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n");
		            stbSparqlQueryString.append("PREFIX dbo:     <http://dbpedia.org/property/>"); 
	        		
	        		
		       
		            
		         
       //Sorgumuzu yaziyoruz.Baþlik ve yazar Ýngizlizce olanlari cekiyoruz.
		        stbSparqlQueryString.append("	select DISTINCT ?baslik ?yazar ?link WHERE {"); 
				stbSparqlQueryString.append("  ?resource rdfs:label ?baslik."); 
				stbSparqlQueryString.append(" ?resource dbo:author ?yazar .");
				stbSparqlQueryString.append(" ?resource foaf:isPrimaryTopicOf ?link .");
				stbSparqlQueryString.append("FILTER(LANGMATCHES( LANG(?baslik), 'en'))"); 
				stbSparqlQueryString.append("FILTER(LANGMATCHES( LANG(?yazar), 'en'))}");

				
				
				
		           //Sorgu yaratiyoruz.
		        Query query = QueryFactory.create(stbSparqlQueryString.toString());
		        //System.out.println("Going to call service");
		        qexec = QueryExecutionFactory.sparqlService(service, query);
		         System.out.println("going to execute select");
		         ResultSet results = qexec.execSelect();
		      
		            System.out.println("End Query Execution");

			     System.out.printf("\n%-40s%50s %50s\n", "baslik", "yazar","link");
			     //Modelleri(Triplelarý) tek tek gezerek yazdiriyor.
		           while (results.hasNext()) {
		        	   //Model icin veri toplaniyor.
		                  QuerySolution qs = results.nextSolution();
		                  RDFNode basliklabel = qs.get("baslik");
		                  RDFNode yazarlabel = qs.get("yazar");
		                  RDFNode dbpedialink = qs.get("link");	          
		                  //10 tane veriden sonra veri cekme bitsin.
		                  sayici++;
				            if(sayici==20)
				            	return;
		                  
		                  //Ekrana yazdirmak icin format ayarlaniyor.
		                  String strbaslikLabel = basliklabel.toString();
		                  String strDBPediaLink = dbpedialink.toString();
		                  String stryazarLabel = yazarlabel.toString();
		                  //Internetten cektigi ekrana verileri yazdiriyoruz.
                         System.out.printf("\n%-40s%70s%80s", strbaslikLabel, stryazarLabel, strDBPediaLink);
		                 
		                //Modele Ýnstance olarak ekleniyor
		                	Makale.createIndividual( NS + Makale );        
		                	 Makale.createIndividual( konu + strbaslikLabel);
		            		 Makale.createIndividual( yazar + stryazarLabel);
		            		 Makale.createIndividual( dblink+ strDBPediaLink);
		            		
		 		        	
		     
		 		        		 
		 		        		    
		 		        		 
		                  
		            		
		         	   
		         	        try {
		         	          
		         	             m.write(outputFile, "RDF/XML-ABBREV", xmlbase);
		         	        } catch (Exception e) {
		         	            e.printStackTrace();
		         	        }
		         	      
		            }
		           
		        }
		   
		        
		       
		      }catch (Exception e) {
		          System.err.println(e);
		      } 
		     		
		
		
		
		
	       
		 } catch (Exception e) {
	            e.printStackTrace();
	        }	
	}
		 

}
