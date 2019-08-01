package com.sildian.go4lunch.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Workmate;

import java.util.List;

/************************************************************************************************
 * WorkmateAdapter
 * Monitors the workmates data within a recycler view
 ***********************************************************************************************/

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {

    /**Data**/

    private int id;                                         //The id of the view defines its behavior
    private List<Workmate> workmates;                       //The list of workmates
    private RequestManager glide;                           //Glide manager to display the images

    /**Constructor**/

    public WorkmateAdapter(int id, List<Workmate> workmates, RequestManager glide){
        this.id=id;
        this.workmates=workmates;
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
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.update(this.workmates.get(position), this.glide);
    }

    @Override
    public int getItemCount() {
        return this.workmates.size();
    }
}
