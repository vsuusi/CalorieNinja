package com.example.calorieninja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public String API_KEY = BuildConfig.apiKey;
    String Name;
    double ServingSize, Calories, Protein, FatTotal, CarbohydratesTotal, SaturatedFat, Sodium_mg,
            Potassium_mg, Cholesterol_mg, Fiber, Sugar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null) {
            Name = savedInstanceState.getString("name", "");
            Calories = savedInstanceState.getDouble("calories", 0 );
            CarbohydratesTotal = savedInstanceState.getDouble("carbohydrates_total_g", 0 );
            FatTotal = savedInstanceState.getDouble("fat_total_g", 0 );
            Protein = savedInstanceState.getDouble("protein_g", 0 );
            drawMacrosToUi(Name, ServingSize, Calories, Protein, CarbohydratesTotal, FatTotal);
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("name", Name);
        bundle.putDouble("calories", Calories);
        bundle.putDouble("carbohydrates_total_g", CarbohydratesTotal);
        bundle.putDouble("fat_total_g", FatTotal);
        bundle.putDouble("protein_g", Protein);
    }

    protected void onRestoreInstanceState( Bundle savedInstanceState ) {
        super.onRestoreInstanceState(savedInstanceState);
        Name = savedInstanceState.getString("name", "");
        Calories = savedInstanceState.getDouble("calories", 0 );
        CarbohydratesTotal = savedInstanceState.getDouble("carbohydrates_total_g", 0 );
        FatTotal = savedInstanceState.getDouble("fat_total_g", 0 );
        Protein = savedInstanceState.getDouble("protein_g", 0 );
        drawMacrosToUi(Name, ServingSize, Calories, Protein, CarbohydratesTotal, FatTotal);
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void fetchCalories(View view) {
        hideKeyboard(view);
        EditText foodInput = findViewById(R.id.editTextFood);
        String foodToFind = foodInput.getText().toString();
        String url = "https://api.api-ninjas.com/v1/nutrition?query=" + foodToFind;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                        parseJSONData(response);
                },
                error -> {
                    Toast.makeText(this, R.string.ErrorFetchingDataToast, Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", API_KEY);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
        foodInput.setText("");
    }

    private void drawMacrosToUi(String Name, double servingSize, double cals, double protein, double carbs, double fat) {
        TextView NameTextView = findViewById(R.id.textViewName);
        TextView ServingSizeTextView = findViewById(R.id.textViewServing);
        TextView CaloriesTextView = findViewById(R.id.textViewCals);
        TextView ProteinTextView = findViewById(R.id.textViewProtein);
        TextView CarbTextView = findViewById(R.id.textViewCarbs);
        TextView FatTextView = findViewById(R.id.textViewFats);
        NameTextView.setText(Name);
        ServingSizeTextView.setText(Math.round(servingSize)+" g");
        CaloriesTextView.setText(Math.round(cals)+" kcal");
        ProteinTextView.setText(Math.round(protein)+" g");
        FatTextView.setText(Math.round(fat)+" g");
        CarbTextView.setText(Math.round(carbs)+" g");
    }
    private void parseJSONData(String response) {
        try {
            JSONArray jArray = new JSONArray(response);
            if(jArray.length() > 0) {
                JSONObject foodObject = jArray.getJSONObject(0);

                Name  = foodObject.getString("name");
                ServingSize = foodObject.getDouble("serving_size_g");
                // main macros
                Calories = foodObject.getDouble("calories");
                Protein = foodObject.getDouble("protein_g");
                FatTotal = foodObject.getDouble("fat_total_g");
                CarbohydratesTotal = foodObject.getDouble("carbohydrates_total_g");
                // specific macros
                SaturatedFat = foodObject.getDouble("fat_saturated_g");
                Sodium_mg = foodObject.getDouble("sodium_mg");
                Potassium_mg = foodObject.getDouble("potassium_mg");
                Cholesterol_mg = foodObject.getDouble("cholesterol_mg");
                Fiber = foodObject.getDouble("fiber_g");
                Sugar = foodObject.getDouble("sugar_g");
                drawMacrosToUi(Name, ServingSize, Calories, Protein, CarbohydratesTotal, FatTotal);
            } else {
                Toast.makeText(this, R.string.NoDataFoundForTheQueryToast, Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e){
            Toast.makeText(this, R.string.ErrorParsingJSONDataToast, Toast.LENGTH_SHORT).show();
        }
    }

    public void ViewSpecifics(View view) {
        EditText foodInput = findViewById(R.id.editTextFood);
        String foodToFind = foodInput.getText().toString();
        if (!Name.isEmpty()) {
            Intent intent = new Intent(this, DetailedView.class);
            intent.putExtra("FOOD", Name);
            intent.putExtra("SERVING_SIZE", ServingSize);
            intent.putExtra("SATURATED_FAT", SaturatedFat);
            intent.putExtra("SODIUM_MG", Sodium_mg);
            intent.putExtra("POTASSIUM_MG", Potassium_mg);
            intent.putExtra("CHOLESTEROL_MG", Cholesterol_mg);
            intent.putExtra("FIBER", Fiber);
            intent.putExtra("SUGAR", Sugar);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,R.string.emptyFoodToast, Toast.LENGTH_SHORT).show();
        }
    }
}