package com.sildian.go4lunch.view;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.sildian.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/************************************************************************************************
 * AutocompleteSuggestionViewHolder
 * Displays the items related to an autocomplete suggestion in a RecyclerView
 ***********************************************************************************************/

public class AutocompleteSuggestionViewHolder extends RecyclerView.ViewHolder {

    /**UI components**/

    private View itemView;
    @BindView(R.id.list_autocomplete_suggestion_item_name) TextView suggestionNameText;
    @BindView(R.id.list_autocomplete_suggestion_item_address) TextView suggestionAddressText;

    /**Constructor**/

    public AutocompleteSuggestionViewHolder(View itemView){
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
    }

    /**Updates**/

    public void update(AutocompletePrediction prediction){
        this.suggestionNameText.setText(prediction.getPrimaryText(null));
        this.suggestionAddressText.setText(prediction.getSecondaryText(null));
    }
}
