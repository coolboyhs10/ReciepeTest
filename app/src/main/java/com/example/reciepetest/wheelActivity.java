package com.example.reciepetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class wheelActivity extends AppCompatActivity implements View.OnClickListener {

    private Button spin;
    private ImageView wheel;
    private int degreeNew = 0, degreeOld = 0;
    private int chooseDish;
    private FirebaseFirestore firestoreDatabase;
    private EditText tag;
    private String tagStr = "";
    private Button enterTag;
    private List<Recipe> recipeList, recipeList6;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        recipeList = new ArrayList<>();

        spin = (Button) findViewById(R.id.spin);
        wheel = (ImageView) findViewById(R.id.wheel);
        tag = (EditText) findViewById(R.id.tag);
        enterTag = (Button) findViewById(R.id.enterTag);
        enterTag.setOnClickListener(this);
        spin.setOnClickListener(this);
    }

    private void getTextFirst(){
        tagStr = tag.getText().toString().trim();
        if(tagStr.length() == 0)
            Toast.makeText(getApplicationContext(),"Enter Tag First", Toast.LENGTH_SHORT).show();
        else
            getfromDataBase();
    }

    private void spinWheel() {
        degreeOld = degreeNew % 360;
        /**int count = 0;
        for(Recipe r : recipeList){
            if(count < 6)
                recipeList6.add(r);
        }*/

        degreeNew = new Random().nextInt(360) + 720;
        RotateAnimation rotateAnimation = new RotateAnimation(degreeOld, degreeNew,RotateAnimation.RELATIVE_TO_SELF,
                0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                chooseDish = (degreeNew % 360) / 60;
                if(recipeList.size() > 0) {
                    final Dialog dialogBox = new Dialog(wheelActivity.this);
                    dialogBox.setContentView(R.layout.dialog);
                    dialogBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogBox.show();

                    Button dismiss = (Button) dialogBox.findViewById(R.id.dismiss);
                    dismiss.setText("" + recipeList.get(0).getName());

                    ImageView image = (ImageView) dialogBox.findViewById(R.id.image);
                    Picasso.get().load(recipeList.get(0).getImage()).into(image);

                    dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBox.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        wheel.startAnimation(rotateAnimation);
    }

    private void  getfromDataBase(){
        recipeList = new ArrayList<>();
        progressDialog = new ProgressDialog(wheelActivity.this);
        progressDialog.show();
        firestoreDatabase = FirebaseFirestore.getInstance();
        firestoreDatabase.collection("recipe")
                .whereArrayContains("tags",tagStr)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list) {
                            Recipe obj = d.toObject(Recipe.class);
                            recipeList.add(obj);
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == enterTag)
            getTextFirst();

        if(v == spin) {
            if(tagStr.length() == 0)
                Toast.makeText(getApplicationContext(),"Enter Tag First", Toast.LENGTH_SHORT).show();
            else if(recipeList.size() == 0)
                Toast.makeText(getApplicationContext(),"Tag not found", Toast.LENGTH_SHORT).show();

            if(tagStr.length() > 0 && recipeList.size() > 0)
                spinWheel();
        }
    }

}
