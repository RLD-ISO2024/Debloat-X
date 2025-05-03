package com.shisui.debloater;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private List<AppList> installedApps;
    private AppAdapter installedAppAdapter;
    private TextView countApps;
    public Integer cnt=0;
    public String g;
    public Button but,sbut;
public StringBuilder everything;
public Integer count;
public SharedPreferences sharedpreferences;
public TextView stat;
public String appName;
public Drawable icon;
public String packages;
public ListView userInstalledApps;
public String states = "";
public String alph;
public String sorting[];
private SearchView inputSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("APPS", Context.MODE_PRIVATE);
firstrun();
        una();
        userInstalledApps = (ListView) findViewById(R.id.installed_app_list);
        inputSearch = (SearchView) findViewById(R.id.searchView);
        stat=(TextView)findViewById(R.id.textView3);
        but=(Button)findViewById(R.id.button4);
        sbut=(Button)findViewById(R.id.button2);
inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        installedAppAdapter.filter(text);

        return false;
    }
});
        installedApps = getInstalledApps();
        installedAppAdapter = new AppAdapter(MainActivity.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);

        //Total Number of Installed-Apps(i.e. List Size)
        String  abc = userInstalledApps.getCount()+"";
        TextView countApps = (TextView)findViewById(R.id.countApps);
        countApps.setText("Total Installed Apps: "+abc);
        Toast.makeText(this, "Debloat-X by Shisui\nPH TEAM", Toast.LENGTH_SHORT).show();

    }

    private List<AppList> getInstalledApps() {
        PackageManager pm = getPackageManager();
        List<AppList> apps = new ArrayList<AppList>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        //List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
       String sorting []=new String[packs.size()];
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p))) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                String packages = p.applicationInfo.packageName;
                int z = getPackageManager().getApplicationEnabledSetting(packages);

                if (z == 0) {
                    states = "Enabled";
                } else if (z == 1) {
                    states = "Enabled";
                } else if (z == 2) {
                    states = "Disabled";
                } else if (z == 3) {
                    states = "Disabled";
                } else if (z == 4) {
                    states = "Disabled";
                }
                apps.add(new AppList(appName, icon, packages, false, states));

            }


        }
        
        return apps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
    }

    public class AppAdapter extends BaseAdapter {
        private ArrayList<AppList> arraylist;

        public LayoutInflater layoutInflater;
        public List<AppList> listStorage;
        public ArrayList<AppList> employeeArrayList;
        public ArrayList<AppList> orig;
        public AppAdapter(Context context, List<AppList> customizedListView) {
            this.arraylist = new ArrayList<AppList>();
            this.arraylist.addAll(installedApps);
            layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listStorage = customizedListView;
        }
        public void filter (String charText) {

            charText = charText.toLowerCase(Locale.getDefault());
            installedApps.clear();
            if (charText.length() == 0) {

                installedApps.addAll(arraylist);
            } else {
                for (AppList wp : arraylist) {

if(sharedpreferences.getInt("Search",0)==0){
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        installedApps.add(wp);
                    }}
else if(sharedpreferences.getInt("Search",0)==1){
                        if (wp.getPackages().toLowerCase(Locale.getDefault()).contains(charText)) {
                            installedApps.add(wp);
                        }}
                }
            }
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return listStorage.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder listViewHolder;
            if(convertView == null){
                listViewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);

                listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_app_name);
                listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
                listViewHolder.packageInListView=(TextView)convertView.findViewById(R.id.app_package);
                listViewHolder.cb=(CheckBox)convertView.findViewById(R.id.checkBox);
                listViewHolder.eab=(TextView)convertView.findViewById(R.id.textView3);
                convertView.setTag(listViewHolder);
            }else{
                listViewHolder = (ViewHolder)convertView.getTag();
            }
            listViewHolder.textInListView.setText(listStorage.get(position).getName());
            listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
            listViewHolder.packageInListView.setText(listStorage.get(position).getPackages());
            listViewHolder.eab.setText(listStorage.get(position).getEn());
            String states = "";
            if(listStorage.get(position).getSt()==0){listViewHolder.eab.setTextColor(Color.GREEN);}
            else if(listStorage.get(position).getSt()==1){listViewHolder.eab.setTextColor(Color.GREEN);}
            else if(listStorage.get(position).getSt()==2){listViewHolder.eab.setTextColor(Color.RED);}
            else if(listStorage.get(position).getSt()==3){listViewHolder.eab.setTextColor(Color.RED);}
            else if(listStorage.get(position).getSt()==4){listViewHolder.eab.setTextColor(Color.RED);}


            listViewHolder.cb.setChecked(listStorage.get(position).getSelected());
            listViewHolder.cb.setTag( position);

            count=0;
            listViewHolder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer) listViewHolder.cb.getTag();

                    if (listStorage.get(pos).getSelected()){
                        listStorage.get(pos).setSelected(false);
                        String x = listStorage.get(position).getPackages()+"\n";
                        String hey = sharedpreferences.getString("App","");
                        String ne = hey.replace(x,"");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("App",ne);
                        cnt--;
                        but.setText("Selected"+" ("+String.valueOf(cnt)+")");
                        if(cnt==0){
                            but.setEnabled(false);
                        }
                        else{but.setEnabled(true);}
                        editor.commit();
                    }
                    else {
                        listStorage.get(pos).setSelected(true);
                        cnt++;
                        but.setText("Selected"+" ("+String.valueOf(cnt)+")");
                        if(cnt==0){
                            but.setEnabled(false);
                        }
                        else{but.setEnabled(true);}
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        String x = listStorage.get(position).getPackages()+"\n";
                        String hey = sharedpreferences.getString("App", "");
                        g=hey+x;
                        editor.putString("App",g);
                        editor.commit();
                        String posi=String.valueOf(position)+"\n";
                        String eyy=sharedpreferences.getString("Index","");
                        String c=eyy+posi;
                        editor.putString("Index",c);
                        editor.commit();

                    }

                }
            });

            return convertView;
        }

        class ViewHolder{
            public CheckBox cb;
            TextView textInListView;
            ImageView imageInListView;
            TextView packageInListView;
            TextView eab;

        }
    }

    public class AppList {
        private String name;
        private boolean isSelected,cb;
        Drawable icon;
        private String en;
        private String packages;
        private Integer st;
        public AppList(String name, Drawable icon, String packages, boolean cb, String en) {
            this.name = name;
            this.icon = icon;
            this.packages = packages;
            this.cb=isSelected;
            this.en=en;
        }
        public String getEn() {


            return en;
        }
        public Integer getSt() {
            st =getPackageManager().getApplicationEnabledSetting(packages);
            return st;
        }
        public String getName() {
            return name;
        }
        public boolean getSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        public Drawable getIcon() {
            return icon;
        }
        public String getPackages() {
            return packages;
        }

    }
    public void two(View v){
      SharedPreferences.Editor sby=sharedpreferences.edit();
      if(sharedpreferences.getInt("Search",0)==0){
          sbut.setText("Search by App Name");
          sby.putInt("Search",1);
          sby.commit();
      }
       else if(sharedpreferences.getInt("Search",0)==1){
            sbut.setText("Search by Package");
            sby.putInt("Search",0);
            sby.commit();
    }}
    public void uns (View v){
        String[] colors = {"Uninstall excluding app data", "Uninstall including app data", "Disable Applications", "Enable Applications"};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position of the selected item

                if (which==0){

                        String[] separated = sharedpreferences.getString("App","").split("\n");
                    String[] lines = sharedpreferences.getString("App","").split("\n");
                    Integer k = lines.length;
                    Integer x = 0;
                    while (x != k) {

                        try{
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("su -c 'pm uninstall -k --user 0'"+' '+separated[x]+"\n");
                            outputStream.flush();

                            outputStream.writeBytes("exit\n");
                            outputStream.flush();

                        }catch(IOException e){}

                        x++;}
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("App","");
                    editor.putString("Index","");
                    editor.commit();
                    Toast.makeText(MainActivity.this,"Thanks for using my app\n-Shisui",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,com.shisui.debloater.MainActivity.class);
                    startActivity(intent);}


                else if(which==1){String[] separated = sharedpreferences.getString("App","").split("\n");
                    String[] lines = sharedpreferences.getString("App","").split("\n");
                    Integer k = lines.length;
                    Integer x = 0;
                    while (x != k) {

                        try{
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("su -c 'pm uninstall --user 0'"+' '+separated[x]+"\n");
                            outputStream.flush();

                            outputStream.writeBytes("exit\n");
                            outputStream.flush();

                        }catch(IOException e){}

                        x++;}
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("App","");
                    editor.commit();
                    Toast.makeText(MainActivity.this,"Thanks for using my app\n-Shisui",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,com.shisui.debloater.MainActivity.class);
                    startActivity(intent);}
                else if(which==2){String[] separated = sharedpreferences.getString("App","").split("\n");
                    String[] lines = sharedpreferences.getString("App","").split("\n");
                    Integer k = lines.length;
                    Integer x = 0;
                    while (x != k) {

                        try{
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("su -c 'pm disable-user --user 0'"+' '+separated[x]+"\n");
                            outputStream.flush();

                            outputStream.writeBytes("exit\n");
                            outputStream.flush();

                        }catch(IOException e){}

                        x++;}
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("App","");
                    editor.commit();
                    Toast.makeText(MainActivity.this,"Thanks for using my app\n-Shisui",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,com.shisui.debloater.MainActivity.class);
                    startActivity(intent);}
                else if(which==3){String[] separated = sharedpreferences.getString("App","").split("\n");
                    String[] lines = sharedpreferences.getString("App","").split("\n");
                    Integer k = lines.length;
                    Integer x = 0;
                    while (x != k) {

                        try{
                            Process su = Runtime.getRuntime().exec("su");
                            DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                            outputStream.writeBytes("su -c 'pm enable'"+' '+separated[x]+"\n");
                            outputStream.flush();

                            outputStream.writeBytes("exit\n");
                            outputStream.flush();

                        }catch(IOException e){}

                        x++;}
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("App","");
                    editor.commit();
                    Toast.makeText(MainActivity.this,"Thanks for using my app\n-Shisui",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,com.shisui.debloater.MainActivity.class);
                    startActivity(intent);}
            }
        });

        builder.show();

    }

public void firstrun(){

        if(sharedpreferences.getInt("Una",0)==0){
            SharedPreferences.Editor sp = sharedpreferences.edit();
            sp.putInt("Una",1);
            sp.commit();
            try {
                Process p = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
public void una(){

    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString("App","");
    editor.putString("Index","");
    editor.putInt("Search",0);
    editor.commit();}
}