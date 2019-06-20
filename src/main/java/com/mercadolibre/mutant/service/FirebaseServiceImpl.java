package com.mercadolibre.mutant.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.mercadolibre.mutant.exception.MutantException;
import com.mercadolibre.mutant.pojo.Sequence;
import com.mercadolibre.mutant.pojo.Stat;

@Service
public class FirebaseServiceImpl implements IFirebaseService {
	private Firestore db;
	private static final String CERTIFICATE = "{\n" + 
			"  \"type\": \"service_account\",\n" + 
			"  \"project_id\": \"mlmt-43518\",\n" + 
			"  \"private_key_id\": \"40830b115c43d5f332c589ccb81c890b58ebbc99\",\n" + 
			"  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdDfeCuFoV+02u\\nCnGF/Wf9rPu3iG+dNaZEB/FKHEDxGBFDxoHaDmMRkthjzBwbJCFodfLZIMveqI0k\\nh/2wTumOg+3WerdHmjBQODAGg1/icQcBCdpXE3KMAxiidwE/oL1t/1CvItKG6MXu\\ny1s8BApi+P4xYaDzrc8+Jj9xJz76OgAEmrfL4OJ5MXfPGyM2ylUzNLZIPQZd/QX/\\nCK3TU7DIu0TOn3LV9VheHBwQlzoBsYblzFFYLpV6kU2To8RRiYKjY7wBrpzhsCPu\\n1HfvZMQX2Oi8Thylj8pU2WxWwHgQzr+9w8ZI1SCuvStKYKbnSLl+nDZMNzuSf5YZ\\nn0zbYBEDAgMBAAECggEAEA6oAiTwDPRLHmKx9aWRdsWzXLHl23hrPeRj/pRB7YRs\\n63WFvDIhXeAaIKXzL7Ezezcn85wfS+4GtDf1L4v55BjH/dhBGeCBVQpRmrDe4kt1\\nEF2eBa4W1YrRf3bUWVmJVJA4gxd319Q/vGjtNIcjOrhDYPAe4Rshp7SIYvzEuxd+\\n/AEmNZnLtIBAPtA0FYlqLutU9Im0RqwreAUtelsrNdLszwMrprTO98vrevYQUjLO\\nUH2zzGeHxBAOKqp37E5lcWgdazVirPcfm+KTIf2k6cjwfVq1yCJXg5tJTFayjD7I\\nyDRIDiTyQFiBleYitTNTihw+VKsR+CSMZCwgJzHZ6QKBgQDL9F3gbp2fTUsuw8By\\nz1wcA+pIN5usHexXr498pWBHFgkTafG8mjX7fJBFYQI43vo0vBl2IIVOaErLhSYc\\nHVx9rpBavirj4D/S4AdrYXG5IkQDvjSE27Folnfq+Hx5YsXrWyyBqlRTi3lCWqzK\\nGRGtBL+uXarnmek689qsB3xRiQKBgQDFIcnLZXBLsv998DERRDRe9TKh4J5D0/aK\\n4tkNIXiPAa6YtHkJ2ibMl3gJivDScA87kl0XduE5VS5fQvFSl7DIXnafR2tGc0gQ\\nTFMrv00UQ+QCRC5ywPFdVTUEmfZ6n2OMifqc9Foo6rD4y6WM3ZKU1gXaZzAOFX1U\\najHPEF6nKwKBgHKGBTDA7EFHZbAcuzO8vDv+9s8WapN7OfsC2c9kDWvdM7tzAgql\\njSpqB5jtoBHPIy/5b1KlcDVW2qjtWzjuQ8FPBD/wCKVshmoi4gep7/HFC+wOIe94\\nbWYNF8kZPsvqarh9ucAVpMREzRI1WFEjQomk4P1IG29xPUWdKGzwxx4BAoGAAUQv\\nTKwxJXtX4tI5Lng2sel3Uszjl2H0Mn40kL4HTTvgiECYinSpTSUn8z++PkvT/Tj6\\neh0cY0blzlzaugIZc6APuQQ3bzNsYhZrbluDPj8VZuJTPg95PXU/qvDmQXFmrU9x\\nmbKEJcEkM+TzAk2mFGivjvCcwWpn7LPl8AOV6BMCgYEAjvuet33HpGqa++lce3aQ\\nT1NSYj/+qX8d68ESYfgYU4UVLCAK8x8i5oHmtoNJTDhtuYSvOV2eZvvSJspWRlMr\\nEWWpvvaOmTfmlxWpGDG3H/1kWJbUtJUoLB/k8/G2nh1YPnAePCyef4WanRQ2gWAk\\n1frc+P3bn8hE9q6/RWFmXA4=\\n-----END PRIVATE KEY-----\\n\",\n" + 
			"  \"client_email\": \"firebase-adminsdk-lqnkz@mlmt-43518.iam.gserviceaccount.com\",\n" + 
			"  \"client_id\": \"108545304158355877792\",\n" + 
			"  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" + 
			"  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" + 
			"  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" + 
			"  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-lqnkz%40mlmt-43518.iam.gserviceaccount.com\"\n" + 
			"}";
	
	@PostConstruct
	public void init() throws MutantException{
		InputStream serviceAccount;
		try {
			//serviceAccount = new FileInputStream(ResourceUtils.getFile("classpath:mlmt.json"));
			serviceAccount = new ByteArrayInputStream(CERTIFICATE.getBytes());

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
