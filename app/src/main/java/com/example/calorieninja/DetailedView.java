package com.example.calorieninja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent intent = getIntent();

        setTextView(R.id.detailViewFood, intent.getStringExtra("FOOD"));
        double servingsize = intent.getDoubleExtra("SERVING_SIZE" , 0);
        if (servingsize != 100) {
            setTextView(R.id.textViewServSize, servingsize, " g");
        }
        else {
            setTextView(R.id.textViewServSize, "100 g");
        }
        setTextView(R.id.textViewSatFat, intent.getDoubleExtra("SATURATED_FAT", 0), " g");
        setTextView(R.id.textViewFib, intent.getDoubleExtra("FIBER", 0), " g");
        setTextView(R.id.textViewSug, intent.getDoubleExtra("SUGAR", 0), " g");
        setTextView(R.id.textViewSod, intent.getDoubleExtra("SODIUM_MG", 0), " mg");
        setTextView(R.id.textViewPot,  intent.getDoubleExtra("POTASSIUM_MG", 0), " mg");
        setTextView(R.id.textViewChol, intent.getDoubleExtra("CHOLESTEROL_MG", 0), " mg");
    }

    private void setTextView(int textViewId, String value) {
        TextView textView = findViewById(textViewId);
        textView.setText(value);
    }
    private void setTextView(int textViewId, double value, String gram) {
        TextView textView = findViewById(textViewId);
        textView.setText(Math.round(value) + gram);
    }

    public void findRecipeHandler(View view) {
        String food = getIntent().getStringExtra("FOOD");
        String url ="https://www.allrecipes.com/search?q="+food;
        Uri searchUri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, searchUri);
        try {
            startActivity(intent);
        }
        catch(Exception e) {
            Toast.makeText(this,"Cannot open webpage", Toast.LENGTH_SHORT).show();
        }

    }

    public void returnToMain(View view) {
        finish();
    }

}