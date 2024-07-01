package com.example.assignment;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingActivity extends AppCompatActivity {

    ListView ranking;
    String[] items;
    ArrayAdapter<String> adapter;
    DownloadTask task;
    JSONObject JsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ranking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ranking = findViewById(R.id.rankingList);
        // get json data from https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run
        // set to ranking list
        getData();
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... values) {

            InputStream inputStream = null;
            String result = "";
            URL url = null;

            try {
                url = new URL(values[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

// Make GET request
                con.setRequestMethod("GET");  // May omit this line since "GET" is the default.
                con.connect();

// Get response string from inputstream of the connection

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = bufferedReader.readLine()) != null)
                    result += line;


                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            proessData(result);
        }
    }

    public void getData() {

        if (task == null ||
                task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new DownloadTask();
            task.execute("https://ranking-mobileasignment-wlicpnigvf.cn-hongkong.fcapp.run");
        }
    }

    public void proessData(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            List<RankingItem> rankingItems = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("Name");
                int correct = jsonObject.getInt("Correct");
                int time = jsonObject.getInt("Time");
                rankingItems.add(new RankingItem(name, correct, time));
            }
            Collections.sort(rankingItems, new SortForRankingItem());
            List rankingsString = new ArrayList<String>(rankingItems.size());
            for (int i = 1; i <= rankingItems.size(); i++) {
                rankingsString.add(R.string.rank+ i + ", " + rankingItems.get(i - 1).toString());
            }

            ArrayAdapter<RankingItem> adapter = new ArrayAdapter<>(this, R.layout.list_item,R.id.text_view, rankingsString);
            ranking.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d("D", e.getMessage());
        }
    }

    public class RankingItem {
        private String name;
        private int correct;
        private int time;

        public RankingItem(String name, int correct, int time) {
            this.name = name;
            this.correct = correct;
            this.time = time;
        }

        // getters and setters
        public String getName() {
            return name;
        }

        public int getCorrect() {
            return correct;
        }

        public int getTime() {
            return time;
        }

        @Override
        public String toString() {
            return name + ", " + correct + R.string.correct+ time + R.string.sec;
        }
    }

    class SortForRankingItem implements Comparator<RankingItem> {
        // Used for sorting in descending order of correct number
        public int compare(RankingItem a, RankingItem b) {
            int ac = a.getCorrect(), bc = b.getCorrect();
            if (ac == bc) {
                return (b.getTime() - a.getTime());
            }
            return bc - ac;
        }
    }


}