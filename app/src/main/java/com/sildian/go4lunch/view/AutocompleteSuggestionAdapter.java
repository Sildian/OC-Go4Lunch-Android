package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.sildian.go4lunch.R;

import java.util.List;

/************************************************************************************************
 * AutocompleteSuggestionAdapter
 * Monitors the suggestions data within a recycler view
 ***********************************************************************************************/

public class AutocompleteSuggestionAdapter extends RecyclerView.Adapter<AutocompleteSuggestionViewHolder>{

    /**Information**/

    private List<AutocompletePrediction> predictions;           //The list of predictions given by the places autocomplete API

    /**Constructor**/

    public AutocompleteSuggestionAdapter(List<AutocompletePrediction> predictions){
        this.predictions=predictions;
    }

    /**Adapter methods**/

    @NonNull
    @Override
    public AutocompleteSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_autocomplete_suggestion_item, parent, false);
        return new AutocompleteSuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AutocompleteSuggestionViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull AutocompleteSuggestionViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.update(this.predictions.get(position));
    }

    @Override
    public int getItemCount() {
        return this.predictions.size();
    }
}
