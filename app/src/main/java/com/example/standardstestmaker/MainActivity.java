package com.example.standardstestmaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private EditText nameInput;
    private EditText emailInput;

    private String TAG = "Main Activity LOG";

    private TextView showData;

    private Button submitButton;
    private Button showDataButton;

    // Keys
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    // Values

    // Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference journalRef = db.collection("First Storage").document("First Input");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);

        showData = findViewById(R.id.show_data_text);

        submitButton = findViewById(R.id.submit_button);
        showDataButton = findViewById(R.id.show_data_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();
                data.put(KEY_NAME, name);
                data.put(KEY_EMAIL, email);

                Log.d("MAP OBJECT", "onClick: " + data );

                journalRef.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                                Log.d("SUCCESS", "onSuccess: IT WORKED");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SEE THE ERROR HERE", "onFailure: " + e.toString());
                            }
                        });
            }
        });

//        showDataButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("SHOW DATA", "onClick: SHOWING DATA BUTTON SUCCESS");
//                journalRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Toast.makeText(MainActivity.this, "retrieved data", Toast.LENGTH_LONG).show();
//                        if (documentSnapshot.exists()) {
//                            String name = documentSnapshot.getString(KEY_NAME);
//                            showData.setText("this has been changed: " + name);
//
//                        } else {
//                            Toast.makeText(MainActivity.this, "Data does not exist", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("RETRIEVE DATA ERROR", "onFailure: " + e.toString());
//                    }
//                });
//            }
//        });
    }

    //
    @Override
    protected void onStart() {
        super.onStart();

        journalRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }

                if (documentSnapshot.exists() && documentSnapshot != null) {
                    String name = documentSnapshot.getString(KEY_NAME);
                    String email = documentSnapshot.getString(KEY_EMAIL);
                    showData.setText("this has been changed: " + name + " " + email);
                }
            }
        });
    }
}
