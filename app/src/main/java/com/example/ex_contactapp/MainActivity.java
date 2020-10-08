package com.example.ex_contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.ex_contactapp.adapters.ViewPagerAdapter;
import com.example.ex_contactapp.fragments.FragmentCalls;
import com.example.ex_contactapp.fragments.FragmentContactGroups;
import com.example.ex_contactapp.fragments.FragmentContacts;
import com.example.ex_contactapp.fragments.FragmentMessage;
import com.example.ex_contactapp.fragments.FragmentMessageReport;
import com.example.ex_contactapp.fragments.FragmentProfile;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final int[] ICONS = {R.drawable.ic_contacts,R.drawable.ic_message,R.drawable.ic_group,R.drawable.ic_message_report,R.drawable.ic_person};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        //askPermissions();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //adapter.addFragment(new FragmentCalls(),"Calls");
        adapter.addFragment(new FragmentContacts(),"Contacts");
        adapter.addFragment(new FragmentMessage(),"Message");
        adapter.addFragment(new FragmentContactGroups(),"Groups");
        adapter.addFragment(new FragmentMessageReport(),"Message Report");
        adapter.addFragment(new FragmentProfile(),"Profile");


        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        tabLayout.setupWithViewPager(viewPager);


        for (int count = 0; count < tabLayout.getTabCount(); count++){
            TabLayout.Tab tab = tabLayout.getTabAt(count);
            tab.setIcon(ICONS[count]);
        }

//        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
//        startActivity(loginIntent);
//        finish();
    }
    private void askPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},1);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);}
    }
}
