package dbpedia.jena.bookvericekme;
/*
 * Bu Sinif DbPediadan cekilen veriyi TDB Store yazmayi ve TDB Storedan okumayi saglar.
 * 
 */
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TripleStoreManager {

	private Model model;

	public TripleStoreManager() {
		super();
		model = ModelFactory.createDefaultModel();
	}
  //Sorguyu verilen locationa gore TDBye kaydediyor.
	public void save(String location, Model queryModel) {

		Dataset dataset = TDBFactory.createDataset(location);
		dataset.begin(ReadWrite.WRITE);
		Model datasetModel = dataset.getDefaultModel();
		datasetModel.add(queryModel);
		dataset.commit();
		dataset.end();
		dataset.close();
		this.model = queryModel;

	}

	public Model getModel() {
		return model;
	}
 //Locationdeki veriyi okuyor.
	public void readModel(String location) {
		Dataset dataset = TDBFactory.createDataset(location);
		dataset.begin(ReadWrite.READ);
		Model datasetModel = dataset.getDefaultModel();
		this.model = datasetModel;
		dataset.end();
		dataset.close();
	}

}
