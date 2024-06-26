package org.dci.starwars;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView swList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //        https://swapi.dev/api/films
        // 1 - Add a recycler view to the MainActivity Layout
        // 2 - Set the LayoutManager (LinearLayoutManager)
        // 3 - Set the Adapter
        // 3.1 - Create a custom adapter for the star wars list
        // 3.2 - The adapter requires a ViewHolder to pass around the item layout information
        // 4 - Create a POJO class to hold the required Info: Year, Director, Title
        // 5 - Use OkHTTP to download the information from the API address: https://swapi.dev/api/films
        // 6 - Parse the downloaded info from JSON to a List<POJO> using Jackson
        // 7 - Pass the List to the adapter and watch the magic happen :D

        progressBar = findViewById(R.id.progressBar);
        swList = findViewById(R.id.swList);
        swList.setLayoutManager(new LinearLayoutManager(this));


        new Thread(() -> {
            List<StarWarsFilm> films = StarWarsService.getStarWars();
            runOnUiThread(() -> {
                updateView(films);
            });
        }).start();

    }
    private void updateView(List<StarWarsFilm> films) {
        SWAdapter swAdapter = new SWAdapter(films);

        swAdapter.setOnItemClickListener((view, position) -> {
            Toast.makeText(
                    this,
                    "Hello Hello Hello" + position,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, FilmActivity.class);
            intent.putExtra("title", films.get(position).getTitle());
            intent.putExtra("Opening Crawl",films.get(position).getOpeningCrawl());
            startActivity(intent);
        });
        progressBar.setVisibility(View.INVISIBLE);
        swList.setAdapter(swAdapter);
    }
}