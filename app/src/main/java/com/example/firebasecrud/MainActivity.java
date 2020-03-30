package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
   private FirebaseFirestore firebaseFirestore;
   private static final String COLLECTION_NAME="BSONFO";
    private Dialog objectDialog;
    private EditText documentIDET,StuNameET,StuCityET;
    private DocumentReference documentReference;
    private TextView downloadedDataTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseFirestore=FirebaseFirestore.getInstance();
        objectDialog=new Dialog(this);
        objectDialog.setContentView(R.layout.please_wait);
        objectDialog.setCancelable(false);
        documentIDET=findViewById(R.id.documentIDET);
        StuNameET=findViewById(R.id.StuNameET);
        StuCityET=findViewById(R.id.StuCityET);
        downloadedDataTV=findViewById(R.id.downloadedDataTV);
    }
    public  void addValueToFirebaseFirestore(View v){
        try {
            if (!documentIDET.getText().toString().isEmpty() && !StuNameET.getText().toString().isEmpty()
                    && !StuCityET.getText().toString().isEmpty())
            {
                objectDialog.show();
            Map<String, Object> objectMap = new HashMap();
            objectMap.put("Stuname", StuNameET.getText().toString());
            objectMap.put("City", StuCityET.getText().toString());
            firebaseFirestore.collection(COLLECTION_NAME)
                    .document(documentIDET.getText().toString())
                    .set(objectMap)
                    .addOnSuccessListener(

                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    documentIDET.setText("");
                                    StuNameET.setText("");
                                    StuCityET.setText("");
                                    documentIDET.requestFocus();
                                    objectDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Data added to collection", Toast.LENGTH_SHORT).show();
                                }
                            }
                    )
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    objectDialog.dismiss();

                                    Toast.makeText(MainActivity.this, "fails data add to collection" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
        }
        else if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "please enter document id", Toast.LENGTH_SHORT).show();
            }
            else if(StuNameET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "please enter the Name", Toast.LENGTH_SHORT).show();
            }
            else if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "please enter The city", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            objectDialog.dismiss();
            Toast.makeText(this, "addValueToFirebaseFirestore"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDatafromFirebaseFirestore(View view){
        try {
            objectDialog.show();
           documentReference=firebaseFirestore.collection(COLLECTION_NAME)
                   .document("StuAli");
           documentReference.get()
                   .addOnSuccessListener(
                           new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 if(documentSnapshot.exists())
                                 {
                                     objectDialog.dismiss();
                                        String documentID=documentSnapshot.getId();
                                        String stuName=documentSnapshot.getString("Stuname");
                                        String stuCity=documentSnapshot.getString("City");
                                     Toast.makeText(MainActivity.this, ""+documentID
                                             +"\n"
                                             +stuName+"\n"
                                             +stuCity+"\n", Toast.LENGTH_SHORT).show();
                                 }
                                 else
                                 {
                                     objectDialog.dismiss();
                                     Toast.makeText(MainActivity.this, "No Data Exists", Toast.LENGTH_SHORT).show();
                                 }
                               }
                           }
                   )
                   .addOnFailureListener(
                           new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   objectDialog.dismiss();

                                   Toast.makeText(MainActivity.this, "fails get data from collection" + e.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           });
        }
        catch (Exception e){
            objectDialog.dismiss();
            Toast.makeText(this, "getDatafromFirebaseFirestore"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateValuesOfDocumentField(View view)
    {
        try
        {

            if(documentIDET.getText().toString().isEmpty() && StuCityET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the values in required fields", Toast.LENGTH_SHORT).show();
            }
            else {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("City",StuCityET.getText().toString());
                objectMap.put("StuInformation", "Some information about the student");

                documentReference = firebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                documentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Values Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to update values :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValuesOfDocumentField:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteFieldValue(View  view)
    {
        try
        {
            if(documentIDET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "Please enter the value for document id", Toast.LENGTH_SHORT).show();
            }
            else {

                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("City", FieldValue.delete());

                documentReference = firebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                documentReference.update(objectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Value Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Fails to delete values :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        catch (Exception e)
        {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValuesOfDocumentField:"+
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public  void onComplete(View v){
        try {

            FirebaseFirestore.getInstance()
                .collection("BSONFO")
                        .get()
                        .addOnCompleteListener(
                                new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                        }
                                    }
                                });


        }catch (Exception e){
            objectDialog.dismiss();
            Toast.makeText(this, "addValueToFirebaseFirestore"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteDocument(View v){
        if(documentIDET.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Please enter the document id", Toast.LENGTH_SHORT).show();
        }else {
            objectDialog.show();
            documentReference = firebaseFirestore.collection(COLLECTION_NAME)
                    .document(documentIDET.getText().toString());
            documentReference.delete() .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Data Doucment deleted Successfully", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            objectDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Fails to delete filed data:"
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void addUniqueDocument(View v) {
        try {
            if (documentIDET.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter valid document id", Toast.LENGTH_SHORT).show();
            } else {
                objectDialog.show();
                documentReference = firebaseFirestore.collection(COLLECTION_NAME)
                        .document(documentIDET.getText().toString());

                documentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                objectDialog.dismiss();
                                if (documentSnapshot.exists()) {
                                    Toast.makeText(MainActivity.this, "No Document Retrieved", Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String, Object> objmap = new HashMap<>();

                                    objmap.put("Student_name", StuNameET.getText().toString());
                                    objmap.put("City_name", StuCityET.getText().toString());
                                    firebaseFirestore.collection("BSONFO")
                                            .document(documentIDET.getText().toString()).set(objmap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Data not Added" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        objectDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Fails to retrieve data:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "getValuesFromFirebaseFirstore:" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}
