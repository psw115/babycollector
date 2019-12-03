package com.psw.collector.firestore;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FStoreManager {

	private Firestore db;
	
	public FStoreManager() throws IOException{
		this("babyproejct");
	}
	
	public FStoreManager(String projectId) throws IOException{
		GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
		FirebaseOptions options = new FirebaseOptions.Builder()
		    .setCredentials(credentials)
		    .setProjectId(projectId)
		    .build();
		FirebaseApp.initializeApp(options);

		Firestore db = FirestoreClient.getFirestore();
	}
	
	public Firestore getFStore() {
		return db;
	}
}
