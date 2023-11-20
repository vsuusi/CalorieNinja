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

    private static String API_KEY = "RFb077W84SPo72J0hswEFg==0prKEcbGbT6oYeLC";
    String Name;
    double ServingSize, Calories, Protein, FatTotal, CarbohydratesTotal, SaturatedFat, Sodium_mg,
            Potassium_mg, Cholesterol_mg, Fiber, Sugar;

    /* ToDo:
    *   HIDE APIKEY
    *   KEKSI LAITERAJAPINTA/COMMON INTENT PALVELU
    *   MERKKIJONOT JA RESURSSIT RESURSSITIEDOSTOIHIN
    *   LOKALISAATIO
    *   AKTIVITEETIN ELINKAARI(NÄYTÖN KÄÄNTÖ YMS)
    *   VÄLITÄ DATA SPECIFICS AKTIVITEETTIIN
    *   STYLING/FONTIT/LAYOUTIT,BUTTON TYYLIT
    *   MAKROJEN SEGMENTÖINTI
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
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

                TextView CaloriesTextView = findViewById(R.id.textViewFood);
                TextView ProteinTextView = findViewById(R.id.textViewSaturatedFat);
                TextView CarbTextView = findViewById(R.id.textViewFiber);
                TextView FatTextView = findViewById(R.id.textViewSugar);
                CaloriesTextView.setText(getString(R.string.calories) + Calories);
                ProteinTextView.setText(getString(R.string.protein) + Protein);
                FatTextView.setText(getString(R.string.fat) + FatTotal);
                CarbTextView.setText(getString(R.string.carbohydrates) + CarbohydratesTotal);
            } else {
                Toast.makeText(this, "No data found for the query", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e){
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_SHORT).show();
        }
    }

    public void ViewSpecifics(View view) {
        Intent intent = new Intent(this, DetailedView.class);
        intent.putExtra("FOOD", Name);
        intent.putExtra("SATURATED_FAT", SaturatedFat);
        intent.putExtra("SODIUM_MG", Sodium_mg);
        intent.putExtra("POTASSIUM_MG", Potassium_mg);
        intent.putExtra("CHOLESTEROL_MG", Cholesterol_mg);
        intent.putExtra("FIBER", Fiber);
        intent.putExtra("SUGAR", Sugar);
        startActivity(intent);
    }
}