package com.sildian.go4lunch.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.sildian.go4lunch.R;
import com.sildian.go4lunch.model.Workmate;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/************************************************************************************************
 * WorkmateViewHolder
 * Displays the items related to a workmate in a RecyclerView
 ***********************************************************************************************/

public class WorkmateViewHolder extends RecyclerView.ViewHolder{

    /**The IDs allows to know which fragment calls the recyclerView**/

    public static final int ID_WORKMATE=0;          //WorkmateFragment calls the recyclerView
    public static final int ID_RESTAURANT=1;        //RestaurantFragment calls the recyclerView

    /**UI components**/

    private View itemView;
    @BindView(R.id.list_workmate_item_image) ImageView workmateImage;
    @BindView(R.id.list_workmate_item_name_and_action) TextView workmateNameAndAction;

    /**Data**/

    private int id;                                 //The id of the view defines its behavior

    /**Constructor
     * @param itemView : the itemView
     * @param id : the id of the view depending on which fragment calls it
     */

    public WorkmateViewHolder(View itemView, int id){
        super(itemView);
        this.itemView=itemView;
        ButterKnife.bind(this, itemView);
        this.id=id;
    }

    /**Updates with a workmate
     * @param workmate : the workmate
     * @param glide : glide manager to display the image
     */

    public void update(Workmate workmate, RequestManager glide){
        updateImage(workmate, glide);
        updateNameAndAction(workmate);
    }

    /**Updates the image
     * @param workmate : the workmate
     * @param glide : glide manager to display the image
     */

    private void updateImage(Workmate workmate, RequestManager glide){
        if(workmate.getImageUrl()!=null){
            glide.load(workmate.getImageUrl()).apply(RequestOptions.circleCropTransform()).into(this.workmateImage);
        }
    }

    /**Updates the name and the action
     * @param workmate : the workmate
     */

    private void updateNameAndAction(Workmate workmate){

        /*Creates the string which populates the text*/

        String nameAndAction;

        /*Populates the string depending on the id*/

        switch(this.id) {

            case ID_WORKMATE:

                /*Gets the date*/

                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date date=calendar.getTime();

                /*Checks if a lunch already exists today, and gets its id in the list*/

                int id=-1;
                for(Workmate.Lunch lunch:workmate.getLunches()){
                    if(lunch.getDate().equals(date)){
                        id=workmate.getLunches().indexOf(lunch);
                    }
                }

                /*Populates the string depending on the id*/

                if (id==-1) {
                    nameAndAction = workmate.getName() + " " +
                            this.itemView.getResources().getString(R.string.text_workmate_name_and_action_workmate_off);
                    this.itemView.setBackground(null);
                } else {
                    this.workmateNameAndAction.setTextColor(this.itemView.getResources().getColor(android.R.color.black));
                    nameAndAction = workmate.getName() + " " +
                            this.itemView.getResources().getString(R.string.text_workmate_name_and_action_workmate_on) + " " +
                            workmate.getLunches().get(id).getRestaurant().getName();
                }
                break;

            case ID_RESTAURANT:

                /*Populates the string with the appropriated item and set the background to null*/

                nameAndAction = workmate.getName() + " " +
                        this.itemView.getResources().getString(R.string.text_workmate_name_and_action_restaurant);
                this.itemView.setBackground(null);
                break;

            default:
                nameAndAction=workmate.getName();
                this.itemView.setBackground(null);
                break;
        }

        /*Then populates the text*/

        this.workmateNameAndAction.setText(nameAndAction);
    }
}
