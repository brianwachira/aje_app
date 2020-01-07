package com.example.ex_contactapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.adapters.CallsRvAdapter;
import com.example.ex_contactapp.models.ModelCalls;


import android.text.format.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentCalls extends Fragment {
    private RecyclerView recyclerView;
    private View v;


    public FragmentCalls() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_calls,container,false);

        recyclerView = v.findViewById(R.id.rv_calls);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(layoutManager);

        CallsRvAdapter adapter = new CallsRvAdapter(getContext(),getCallLogs());

        recyclerView.setAdapter(adapter);
        return v;
    }

    private List<ModelCalls> getCallLogs(){

        List<ModelCalls> list = new ArrayList<>();
        try{
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CALL_LOG},1);
            }

            Cursor cursor = getContext().getContentResolver().query(CallLog.Calls.CONTENT_URI,null,
                    null,null,CallLog.Calls.DATE + " ASC");

            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            //Log.d("name",cursor.getColumnIndex(CallLog.Calls.NUMBER) + "");
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int date_idx = cursor.getColumnIndex(CallLog.Calls.DATE);
            cursor.moveToFirst();
            while(cursor.moveToNext()){

                Date date = new Date(Long.valueOf(cursor.getString(date_idx)));

                String mnth_date,week_day,month;


                mnth_date = (String) DateFormat.format("dd",date);
                week_day = (String) DateFormat.format("EEEE",date);
                month = (String) DateFormat.format("MMMM",date);


                list.add(new ModelCalls(cursor.getString(number),cursor.getString(duration), week_day + "  " + mnth_date + " " + month));
            }

        }catch (Exception e){
            Log.d("Exception",e.getMessage());
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return list;
    }
}

