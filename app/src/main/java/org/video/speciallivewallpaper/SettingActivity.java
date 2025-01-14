package org.video.speciallivewallpaper;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
        CheckBoxPreference sound = (CheckBoxPreference) getPreferenceScreen().findPreference("sound");
        sound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                VideoWallpaperService.setMute(getApplicationContext(), (Boolean) o);
                return true;
            }
        });
    }
}
