package Gui;
/*
 * Sorguyu forma aktariyor.Butun instancelari string seklinde aktardigi icin her makelenin 
 * ozelliklerini yazdiramadik.Gelistirilir.
 */
import java.awt.EventQueue;
import java.io.InputStream;

import javax.swing.JFrame;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.ProfileRegistry;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.FileManager;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.Color;

import localOntology.jena.TDBveSorgu.LocalOntologyTripleStore;




public class SorgununFormaAktarilmasi {

	private JFrame frmMakaleler;

	private static JLabel label;
	 private static JTextPane makale;

	/**
	 * Launch the application.
	 */
	  //jena log hatasi icin ve veri formatini gosteriyor.
	   static Logger l=Logger.getLogger(LocalOntologyTripleStore.class);
	    public static void main(String[] args) {
		BasicConfigurator.configure();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SorgununFormaAktarilmasi window = new SorgununFormaAktarilmasi();
					window.frmMakaleler.setVisible(true);
					String NS = "http://www.semanticweb.org/abdullah/ontologies/2015/1/Tez";
			        String xmlbase = NS;
			 
			        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);
			 
			        RDFWriter rdfw = m.getWriter("RDF/XML-ABBREV");
			        rdfw.setProperty("xmlbase", xmlbase);
			        rdfw.setProperty("relativeURIs", "");
			 
			        String dosya = "C:/Users/abdullah/Desktop/makale/tez1.owl";
			         
			        InputStream in = FileManager.get().open(dosya);
			             
			            if (in == null) {
			                throw new IllegalArgumentException(dosya
			                        + " bulunamadi");
			            }
			                   // Ontoloji modeli okuma islemini yapar
			            m.read(in, xmlbase);
			          
			                        //okunan modeli konsolda ekrana yazdirir
			        m.write(System.out,"N3");

			          String sonuc=  Test(m);
			        
			  		 QueryExecution qe = QueryExecutionFactory.create(sonuc, m);
			  	     ResultSet r =  qe.execSelect();
			  	
			  	   
			  	makale.setText(ResultSetFormatter.asText(r).toString());
			  	  
			  	   
			  	  
			     
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SorgununFormaAktarilmasi() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMakaleler = new JFrame();
		frmMakaleler.setTitle("MAKALELER");
		frmMakaleler.setBackground(Color.RED);
		frmMakaleler.getContentPane().setBackground(new Color(34, 139, 34));
		frmMakaleler.getContentPane().setPreferredSize(new Dimension(500, 500));
		frmMakaleler.getContentPane().setSize(new Dimension(500, 500));
		frmMakaleler.getContentPane().setMinimumSize(new Dimension(1000, 1000));
		frmMakaleler.setBounds(100, 100, 449, 336);
		frmMakaleler.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMakaleler.getContentPane().setLayout(null);
		
	   label = new JLabel("");
	   label.setForeground(new Color(0, 100, 0));
	   label.setBackground(new Color(102, 205, 170));
		label.setBounds(31, 90, 414, 160);
		frmMakaleler.getContentPane().add(label);
		 makale = new JTextPane();
		 makale.setForeground(new Color(0, 0, 0));
		 makale.setBackground(new Color(50, 205, 50));
		 makale.setSize(new Dimension(1000, 1000));
		 makale.setPreferredSize(new Dimension(500, 500));
		 makale.setMinimumSize(new Dimension(500, 500));
	  	   makale.setBounds(150, 75, 800, 400);
	  	   frmMakaleler.getContentPane().add(makale);
	}
	public static  String Test(OntModel m)
	{  
		
		String  query="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
	"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
	"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
	"PREFIX : <http://www.semanticweb.org/abdullah/ontologies/2015/1/Tez#>"+
	//Turkçede yazýlmýþ Makalelerin  yazar ismi ve  unvaný
    "SELECT  ?makale ?yazar ?ulkesi ?unvani "  +
		"WHERE { ?makale :dildedir :Turkce."+
		"?makale :yazaridir ?yazar."+
		"?yazar :ulkesi ?ulkesi."+
		"?yazar :akademikSeviyesi ?unvani.}";
		
		/*
	 * //Türkçe dilinde makale yazmýþ yazarlarýn ülkesi
		   "SELECT ?makale?yazar ?ulke "+
			"WHERE { ?makale :dildedir :Turkce."+
			"?makale :yazaridir ?yazar."+
			"?yazar :ulkesi ?ulke }
		*/
	/*
	 * //Makale ,yazar  ve ulkesi
			"SELECT ?makale?yazar ?ulke " +
			"WHERE {?makale :yazaridir ?yazar."+
			"?yazar :ulkesi ?ulke }";
	*/
		
	
	/*
	 * //Türkçe dilinde makale yazan yazarlar
			"SELECT  ?yazar "+
			"WHERE { ?y :dildedir :Turkce."+
			"?y :yazaridir ?yazar}";
	*/
	/*
	 * //Yazar ve Makalesi
			"SELECT  ?Yazar ?Makale "+
			"WHERE { ?Makale :dildedir :Turkce."+
			"?Makale :yazaridir ?Yazar}";
	*/
		
			
		/*	
		 * //türkçe dilinde yazýlmýþ makaleler.
			    "SELECT  ?y "+
				"WHERE { ?y :dildedir :Turkce.}";
	  */
    /*	
     * //Makaleler  ve yazdýlgi dil
		       "SELECT  ?makale ?dil "+
				"WHERE { ?makale :dildedir ?dil.}";
	*/
	/*
	 * //Makalelerin yayýnlandýði yerler
			   "SELECT  ?makale ?yayinlandi "+
				"WHERE { ?makale :yayinlandigiYer ?yayinlandi.}";
	*/
	      return query;      
	     
	         
	}
}
