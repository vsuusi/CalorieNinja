package com.example.calorieninja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailedView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        Intent intent = getIntent();

        setTextView(R.id.textViewFood, R.string.foodname, intent.getStringExtra("FOOD"));
        setTextView(R.id.textViewSaturatedFat, R.string.saturated_fat, intent.getDoubleExtra("SATURATED_FAT", 0));
        setTextView(R.id.textViewFiber, R.string.fiber, intent.getDoubleExtra("FIBER", 0));
        setTextView(R.id.textViewSugar, R.string.sugar, intent.getDoubleExtra("SUGAR", 0));
        setTextView(R.id.textViewSodium, R.string.sodium, intent.getDoubleExtra("SODIUM_MG", 0));
        setTextView(R.id.textViewPotassium, R.string.potassium, intent.getDoubleExtra("POTASSIUM_MG", 0));
        setTextView(R.id.textViewCholesterol, R.string.cholesterol, intent.getDoubleExtra("CHOLESTEROL_MG", 0));
    }

    private void setTextView(int textViewId, int stringResourceId, double value) {
        TextView textView = findViewById(textViewId);
        textView.setText(getString(stringResourceId) + value);
    }

    private void setTextView(int textViewId, int stringResourceId, String value) {
        TextView textView = findViewById(textViewId);
        textView.setText(getString(stringResourceId) + value);
    }

    public void returnToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}