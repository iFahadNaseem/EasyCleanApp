package com.tigan_lab.customer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tigan_lab.easy_clean.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;





public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder> implements Filterable {

    public interface PlaceAutoCompleteInterface{
        public void onPlaceClick(ArrayList<PlaceAutocomplete> mResultList, int position);
    }

    Context mContext;
    PlaceAutoCompleteInterface mListener;
    private static final String TAG = "PlaceAutocompleteAdapter";
    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    ArrayList<PlaceAutocomplete> mResultList;


    private LatLngBounds mBounds;

    private int layout;

    private AutocompleteFilter mPlaceFilter;



    public PlaceAutocompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                    LatLngBounds bounds, AutocompleteFilter filter){
        this.mContext = context;
        layout = resource;
        mBounds = bounds;
        mPlaceFilter = filter;
        this.mListener = (PlaceAutoCompleteInterface)mContext;
    }


    public void clearList(){
        if(mResultList!=null && mResultList.size()>0){
            mResultList.clear();
        }
    }


    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    mResultList = autocomplete(constraint.toString());

                    filterResults.values = mResultList;
                    filterResults.count = mResultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                }
            }};
        return filter;
    }

    @SuppressLint("LongLogTag")
    private ArrayList<PlaceAutocomplete> autocomplete(String input) {
        ArrayList<PlaceAutocomplete> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=");
            sb.append(mContext.getResources().getString(R.string.place_map_key));
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("resp", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("resp", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.d("resp",jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<PlaceAutocomplete>(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(new PlaceAutocomplete(predsJsonArray.getJSONObject(i).getString("place_id"),predsJsonArray.getJSONObject(i).getString("description")));
            }
        } catch (JSONException e) {
            Log.e("resp", "Cannot process JSON results", e);
        }

        return resultList;
    }



    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PlaceViewHolder mPredictionHolder = new PlaceViewHolder(convertView);
        return mPredictionHolder;

    }


    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, final int i) {

        mPredictionHolder.mAddress.setText(mResultList.get(i).description);

        mPredictionHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlaceClick(mResultList,i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mParentLayout;
        public TextView mAddress;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (RelativeLayout)itemView.findViewById(R.id.predictedRow);
            mAddress = (TextView)itemView.findViewById(R.id.address);
        }

    }


    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }




}
