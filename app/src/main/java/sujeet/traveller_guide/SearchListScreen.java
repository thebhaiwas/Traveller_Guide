package sujeet.traveller_guide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SearchListScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView tvTitle;
    private ListView nearbyPlaces;
    private ArrayAdapter<String> placesAdapter;
    private String places[] = {"Restaurant", "Atm", "Hospital", "Train Station", "Airport",
            "Movie Theater","Police","Park"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_screen);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        nearbyPlaces = (ListView) findViewById(R.id.lvNearbyPlaces);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonta.otf");
        tvTitle.setTypeface(type);

        placesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
        nearbyPlaces.setAdapter(placesAdapter);
        nearbyPlaces.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        StringBuilder sbb = new StringBuilder("");
        String s = places[position];
        for(int i=0; i<s.length(); i++) {
            if(s.charAt(i)>='A' && s.charAt(i)<='Z')
                sbb.append((char)(s.charAt(i)+32));
            else if(s.charAt(i) != ' ')
                sbb.append(s.charAt(i));
            else
                sbb.append('_');
        }
        //Toast.makeText(this, sbb.toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Places.class);
        intent.putExtra("type",sbb.toString());
        startActivity(intent);
    }
}
