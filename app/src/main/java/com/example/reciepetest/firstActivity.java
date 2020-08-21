package com.example.reciepetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class firstActivity extends AppCompatActivity implements View.OnClickListener {

    private final String url = "https://raw.githubusercontent.com/coolboyhs10/test/master/db-recipes.json";
    JsonArrayRequest request;
    private FirebaseFirestore firestoreDatabase;
    private Button load;
    private Button save;
    private ImageView imageView;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        firestoreDatabase = FirebaseFirestore.getInstance();

        load = (Button) findViewById(R.id.load);
        save = (Button) findViewById(R.id.save);
        imageView = (ImageView) findViewById(R.id.imageView);
        String imgURL = "https://user-images.githubusercontent.com/44211713/79859334-77961100-83ee-11ea-860d-229ab49e146b.png";
        Picasso.get().load(imgURL).into(imageView);
        load.setOnClickListener(this);
        save.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void jsonParse(){
        request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    JSONObject jsonObject = null;
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Recipe r = new Recipe();
                                jsonObject = response.getJSONObject(i);

                                String name = jsonObject.getString("name");
                                r.setName(name);
                                String image = jsonObject.getString("image");
                                r.setImage(image);
                                String mainIngredient = jsonObject.getString("mainIngredient");
                                r.setMainIngredient(mainIngredient);
                                String instructions = jsonObject.getString("instructions");
                                r.setInstructions(instructions);
                                int calories = jsonObject.getInt("calories");
                                r.setCalories(calories);
                                int servings = jsonObject.getInt("servings");
                                r.setServings(servings);

                                JSONArray ingredients = jsonObject.getJSONArray("ingredients");
                                List<String> ingredientsR = new ArrayList<>();
                                for(int j = 0; j < ingredients.length(); j++)
                                    ingredientsR.add(ingredients.getString(j));
                                r.setIngredients(ingredientsR);

                                JSONArray tags = jsonObject.getJSONArray("tags");
                                List<String> tagsR = new ArrayList<>();
                                for(int j = 0; j < tags.length(); j++)
                                    tagsR.add(tags.getString(j));
                                r.setTags(tagsR);

                                //Recipe r = new Recipe(name, instructions, calories, servings, ingredientsR, tagsR);
                                firestoreDatabase.collection("recipe")
                                        .add(r)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"COULD NOT ADD",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                //Toast.makeText(getApplicationContext(),jsonObject.getString("name"),Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

  /*  private void storeIntoDatabase(){
        String reciepeIdStr = reciepeId.getText().toString().trim();
        String reciepeNamestr = reciepeName.getText().toString().trim();
        String mainIngridientStr = mainIngridient.getText().toString().trim();
        String caloriesStr = calories.getText().toString().trim();
        String cookingTimeStr = cookingTime.getText().toString().trim();
        List<String> list = new ArrayList<>();
        list.add("Potato");
        list.add("Tomato");
        list.add("Karela");

        //
        reciepe reciepeObj = new reciepe(reciepeNamestr,mainIngridientStr,list,list,0,0,0,list);

        // Add a new document with a generated ID
        firestoreDatabase.collection("reciepe")
                .add(reciepeObj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"COULD NOT ADD",Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    private void getfromDataBase(){
        List<String> list = new ArrayList<>();
        //list.add("breakfast");
        list.add("sides");
        firestoreDatabase = FirebaseFirestore.getInstance();
        firestoreDatabase.collection("recipe")
          //      .whereArrayContains("tags","breakfast")
                .whereArrayContainsAny("tags",list)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            Recipe obj = d.toObject(Recipe.class);
                            obj.setDocumentId(d.getId());
                            Toast.makeText(getApplicationContext(),obj.name+"\n"+obj.getMainIngredient(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        /*if(v == add){
           // storeIntoDatabase();
        }*/
        if(v == save){
            jsonParse();
        }

        if(v == load){
            getfromDataBase();
        }
    }
}
