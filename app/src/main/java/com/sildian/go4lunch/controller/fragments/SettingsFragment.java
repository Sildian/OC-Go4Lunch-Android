package com.sildian.go4lunch.controller.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.sildian.go4lunch.R;
import com.sildian.go4lunch.controller.activities.SettingsActivity;
import com.sildian.go4lunch.model.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;

/**************************************************************************************************
 * SettingsFragment
 * Allows the user to manage settings
 *************************************************************************************************/

public class SettingsFragment extends Fragment {

    /**UI components**/

    @BindView(R.id.fragment_settings_seek_bar_radius) SeekBar radiusBar;
    @BindView(R.id.fragment_settings_text_radius_progress) TextView radiusText;
    @BindView(R.id.fragment_settings_switch_notifications) Switch notificationsSwitch;
    @BindView(R.id.fragment_settings_button_reset) Button resetButton;
    @BindView(R.id.fragment_settings_button_account_delete) Button accountDeleteButton;

    /**Data**/

    private Settings settings;                              //The user settings

    /**Constructors**/

    public SettingsFragment() {

    }

    public SettingsFragment(Settings settings){
        this.settings=settings;
    }

    /**Callbacks**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        initializeValues();
        initializeRadiusItems();
        initializeResetButton();
        initializeAccountDeleteButton();
        return view;
    }

    /**Updates the settings**/

    public void updateSettings(){
        this.settings.setSearchRadius(this.radiusBar.getProgress());
        this.settings.setNotificationsOn(this.notificationsSwitch.isChecked());
        SettingsActivity activity=(SettingsActivity) getActivity();
        activity.updateSettings(this.settings);
    }

    /**Initializes the item's values**/

    private void initializeValues(){
        int radiusValue=this.settings.getSearchRadius();
        this.radiusBar.setProgress(radiusValue);
        String radius=radiusValue+" m";
        this.radiusText.setText(radius);
        this.notificationsSwitch.setChecked(this.settings.getNotificationsOn());
    }

    /**Initializes the radius items behavior**/

    private void initializeRadiusItems(){
        this.radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String radius=progress+" m";
                radiusText.setText(radius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**Initializes the resetButton**/

    private void initializeResetButton(){
        this.resetButton.setOnClickListener(v -> {
            int defaultRadius=getResources().getInteger(R.integer.figure_default_radius);
            this.radiusBar.setProgress(defaultRadius);
            this.radiusText.setText(R.string.text_settings_default_radius);
            this.notificationsSwitch.setChecked(true);
        });
    }

    /**Initializes the accountDeleteButton**/

    private void initializeAccountDeleteButton(){

    }
}
