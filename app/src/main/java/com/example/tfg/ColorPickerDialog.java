package com.example.tfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class ColorPickerDialog extends DialogFragment implements ColorPickerDialogListener {

    @Override
    public void onColorSelected(int color) {
        // Call the onColorSelected method of the listener
        ColorPickerDialogListener listener = (ColorPickerDialogListener) getActivity();
        if (listener != null) {
            listener.onColorSelected(color);
        }
    }


}
