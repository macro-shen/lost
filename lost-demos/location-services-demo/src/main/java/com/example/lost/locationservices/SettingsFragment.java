package com.example.lost.locationservices;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Settings Fragment
 */
public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences prefs;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new SettingsListener();

        updateValue(getString(R.string.mock_lat_key));
        updateValue(getString(R.string.mock_lng_key));
        updateValue(getString(R.string.mock_accuracy_key));
        updateValue(getString(R.string.mock_gpx_file_key));
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void updateValue(String key) {
        final String value = getValue(key);
        final Preference pref = findPreference(key);
        pref.setSummary(value);

        if (!getString(R.string.mock_gpx_file_key).equals(key)) {
            try {
                Float.parseFloat(value);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Invalid value: " + pref.getTitle(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private String getValue(String key) {
        if (getString(R.string.mock_gpx_file_key).equals(key)) {
            return prefs.getString(key, "lost.gpx");
        }

        return prefs.getString(key, "0.0");
    }

    /**
     * Settings Listener
     */
    private class SettingsListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (!getString(R.string.mock_mode_key).equals(key) &&
                    !getString(R.string.mock_mode_gpx_key).equals(key)) {
                updateValue(key);
            }
        }
    }
}