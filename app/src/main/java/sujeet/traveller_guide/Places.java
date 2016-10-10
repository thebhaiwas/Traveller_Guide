package sujeet.traveller_guide;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Places extends AppCompatActivity {

    private String type;
    private String url = "https://maps.googleapis.com/maps/api/place/search/json?location=";
    private String key = "AIzaSyAjl_BlNKjkIR-9XM53R2b6XUTkIMZig10";
    private float lat;
    private float lon;
    private String radius = "5000";
    private String finalUrl;
    private String names[];
    private String add [] ;
    private SharedPreferences spLocation;
    private ListView listView;
    private TextView namesOf;
    private TextView addressOf;
    private Typeface ttf;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        listView = (ListView) findViewById(R.id.lvPlace);

        ttf = Typeface.createFromAsset(getAssets(), "fonta.otf");

        type = getIntent().getStringExtra("type");
        spLocation = getSharedPreferences("location", MODE_PRIVATE);
        lat = spLocation.getFloat("lat", 0f);
        lon = spLocation.getFloat("lon", 0f);

        getFinalUrl();

        loadList();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public class Custom extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length ;
        }

        @Override
        public Object getItem(int position) {
            return "hello";
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.activity_custom, parent, false);
            namesOf = (TextView) convertView.findViewById(R.id.namesOf);
            addressOf = (TextView) convertView.findViewById(R.id.addressOf);

            namesOf.setTypeface(ttf);
            addressOf.setTypeface(ttf);

            namesOf.setText(names[position]);
            addressOf.setText(add[position]);
            return convertView;
        }
    }


    public void getFinalUrl() {

        StringBuilder sb = new StringBuilder("");
        sb.append(url);
        sb.append(lat + ",");
        sb.append(lon);
        sb.append("&radius=");
        sb.append(radius);
        sb.append("&type=");
        sb.append(type);
        sb.append("&key=");
        sb.append(key);

        finalUrl = sb.toString();
    }

    private void loadList() {

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, finalUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(Places.this, "Response", Toast.LENGTH_SHORT).show();
                            JSONArray jsArray = response.getJSONArray("results");
                            names = new String[jsArray.length()];
                            add = new String[jsArray.length()];
                            for (int i = 0; i < jsArray.length(); ++i) {
                                JSONObject jsObject = jsArray.getJSONObject(i);
                                String name = jsObject.getString("name");
                                String address = jsObject.getString("vicinity");
                                names[i] = name;
                                add[i]=address;
                            }
                            Custom custom=new Custom();
                            listView.setAdapter(custom);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null)
                            Toast.makeText(Places.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest1);
    }

    @Override
    public void onStart() {

        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Places Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://sujeet.traveller_guide/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {

        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Places Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://sujeet.traveller_guide/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
