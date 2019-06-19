package com.mercadolibre.mutant.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.Query;
import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;

@Service
public class FirebaseServiceImpl implements IFirebaseService {
	private Firestore db;

	public FirebaseServiceImpl() throws MutantException{
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream(ResourceUtils.getFile("classpath:mlmt.json"));

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://mlmt-43518.firebaseio.com").build();

			FirebaseApp.initializeApp(options);

			db = FirestoreClient.getFirestore();

		} catch (IOException e) {
			throw new MutantException("Error al conectarse al servicio cloud");
		}
	}

	@Override
	public void save(Sequence sequence, boolean mutant) throws MutantException{
		Map<String, Object> data = new HashMap<>();
		data.put("dna", sequence.getAdn());
		data.put("mutant", mutant);
		ApiFuture<WriteResult> future = db.collection("sequences").document(UUID.randomUUID().toString()).set(data);
	}

	@Override
	public Stat getAll() throws MutantException{
		ApiFuture<QuerySnapshot> future = db.collection("sequences").get();
		List<QueryDocumentSnapshot> documents;
		Stat stat = new Stat();
		try {
			documents = future.get().getDocuments();
			for (DocumentSnapshot document : documents) {
				if ((boolean) document.get("mutant"))
					stat.setCountMutantDna(stat.getCountMutantDna() + 1);
				else
					stat.setCountHumanDna(stat.getCountHumanDna() + 1);
			}
			stat.setRatio(Math.round(((stat.getCountMutantDna()*1.0)/(stat.getCountHumanDna()==0?1.0:stat.getCountHumanDna())) * 100.0)/100.0);
		} catch (InterruptedException | ExecutionException e) {
			throw new MutantException("Error al recuperar las muestras");
		}

		return stat;
	}
}
