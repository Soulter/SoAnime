package com.soulter.soanime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);



            Preference apiProvider = findPreference("api_provider");
            Preference apiAuthor = findPreference("api_provider_author");
            Preference license = findPreference("license");
            apiProvider.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                    intent.setData(Uri.parse("https://trace.moe"));//为Intent设置数据
                    startActivity(intent);//将Intent传递给Activity
                    return false;
                }
            });
            apiAuthor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
                    intent.setData(Uri.parse("https://trace.moe"));//为Intent设置数据
                    startActivity(intent);//将Intent传递给Activity
                    return false;
                }
            });
            license.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(),LicenseActivity.class);
                    startActivity(intent);//将Intent传递给Activity
                    return false;
                }
            });

              /*
                <PreferenceCategory app:title="鸣谢">

        <Preference app:key="api_provider"
            app:title="API提供方"
            app:summary="https://trace.moe/">
        </Preference>


        <Preference app:key="api_provider_author"
            app:title="API作者"
            app:summary="soruly">
        </Preference>


        <Preference app:key="license"
            app:title="LICENSE"
            app:summary="用到的开源项目">
        </Preference>

    </PreferenceCategory>

    <PreferenceCategory app:title="关于">

        <Preference app:key="app_repository"
            app:title="本软件开源地址"
            app:summary="-">

        </Preference>

        <Preference app:key="author"
            app:title="作者"
            app:summary="Soulter">
        </Preference>

        <Preference app:key="author_contact"
            app:title="QQ"
            app:summary="905617992">
        </Preference>

    </PreferenceCategory>

</PreferenceScreen>
             */
        }
    }
}