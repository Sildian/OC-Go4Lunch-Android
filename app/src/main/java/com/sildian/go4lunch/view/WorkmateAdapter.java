package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

/************************************************************************************************
 * WorkmateAdapter
 * Monitors the workmates data within a recycler view
 ***********************************************************************************************/

public class WorkmateAdapter extends FirestoreRecyclerAdapter<Workmate, WorkmateViewHolder> {

    /**Data**/

    private int id;                                         //The id of the view defines its behavior
    private RequestManager glide;                           //Glide manager to display the images

    /**Constructor**/

    public WorkmateAdapter(@NonNull FirestoreRecyclerOptions<Workmate> options, int id, RequestManager glide){
        super(options);
        this.id=id;
        this.glide=glide;
    }

    /**Adapter methods**/

    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_workmate_item, parent, false);
        return new WorkmateViewHolder(view, this.id);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull Workmate workmate) {
        holder.update(workmate, this.glide);
    }
}
