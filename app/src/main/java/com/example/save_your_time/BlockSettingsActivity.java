package com.example.save_your_time;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlockSettingsActivity extends ListActivity {

    private PackageManager packageManager = null;
    private List applist = null;
    private AppAdapter listadapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_settings);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Настройки блокировки");

        packageManager = getPackageManager();

        new LoadApplications().execute();
        //Получить все возможные приложения и вывести их с помощью кода (настройки блока в layout)
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = (ApplicationInfo) applist.get(position);

        try{
            Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);

            if(intent != null) {
                startActivity(intent);
            }
        } catch(ActivityNotFoundException e) {
            Toast.makeText(BlockSettingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(BlockSettingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private List checkForLaunchIntent(List list) {

        ArrayList appList = new ArrayList();

        for(Object inf : list) {
            try{
                ApplicationInfo info = (ApplicationInfo)inf;
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));

            listadapter = new AppAdapter(BlockSettingsActivity.this, R.layout.list_item, applist);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlockSettingsActivity.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }
}

class AppAdapter extends ArrayAdapter{

    private List appList = null;
    private Context context;
    private PackageManager packageManager;

    public AppAdapter(Context context, int resource,
                      List objects) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appList) ? (ApplicationInfo) appList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        ApplicationInfo data = (ApplicationInfo) appList.get(position);

        if(null != data) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            //TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(data.loadLabel(packageManager));
            //packageName.setText(data.packageName);
            iconView.setImageDrawable(data.loadIcon(packageManager));
        }
        return view;
    }
}

