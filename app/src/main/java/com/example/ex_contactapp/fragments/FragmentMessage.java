package com.example.ex_contactapp.fragments;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.ex_contactapp.R;
import com.example.ex_contactapp.data.Entities.ContactGroup;
import com.example.ex_contactapp.data.Entities.Grouplist;
import com.example.ex_contactapp.data.Relations.ContactGroupAndGroupList;
import com.example.ex_contactapp.models.ModelMessageParameters;
import com.example.ex_contactapp.models.ModelMessages;
import com.example.ex_contactapp.viewmodels.ContactGroupViewModel;
import com.example.ex_contactapp.viewmodels.MessageViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragmentMessage extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static Integer groupId;
    String DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
    String SENT_ACTION = "SMS_SENT_ACTION";
    ArrayAdapter<ContactGroup> arrayAdapter;
    ChipGroup chipGroup;
    LiveData<ContactGroupAndGroupList> contactGroupAndGroupList;
    Spinner contactGroupSpinner;
    ContactGroupViewModel contactGroupViewModel;
    private EditText editTextmessage;
    private Button firstNameButton;
    List<Grouplist> grouplistToUse;
    private Button lastNameButton;
    private String message;
    ConstraintLayout messageConstraintLayout;
    private String phonenumber;
    ProgressBar progressBarMessage;
    RelativeLayout relativeLayout;
    private ImageButton smsButton;

    List<ModelMessageParameters> messageParametersList;

    MessageViewModel messageViewModel;

    /* renamed from: v */
    private View v;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.frag_message, container, false);
        this.v = inflate;
        this.firstNameButton = v.findViewById(R.id.includeFirstname);
        this.lastNameButton = v.findViewById(R.id.includelastname);
        this.smsButton = v.findViewById(R.id.message_icon);
        this.editTextmessage = v.findViewById(R.id.messageText);
        this.chipGroup = v.findViewById(R.id.chip_group);
        this.contactGroupSpinner = v.findViewById(R.id.contact_group_spinner);
        ContactGroupViewModel contactGroupViewModel2 = ViewModelProviders.of((Fragment) this, (ViewModelProvider.Factory) new ContactGroupViewModel.Factory(getActivity().getApplicationContext())).get(ContactGroupViewModel.class);
        this.contactGroupViewModel = contactGroupViewModel2;
        messageParametersList = new ArrayList<>();

        messageViewModel = ViewModelProviders.of(this,new MessageViewModel.Factory(getActivity().getApplicationContext())).get(MessageViewModel.class);
        this.contactGroupViewModel.readGroup().observe(this, new Observer<List<ContactGroup>>() {
            @Override
            public void onChanged(List<ContactGroup> contactGroups) {
                FragmentMessage.this.setSpinnerAdapter(contactGroups);
            }
        });

        this.messageConstraintLayout = v.findViewById(R.id.message_layout);
        this.progressBarMessage = v.findViewById(R.id.progressBar);
        checkForSmsPermission();
        this.firstNameButton.setOnClickListener(view -> appendFirstName());
        this.lastNameButton.setOnClickListener(view -> appendLastName());
        this.smsButton.setOnClickListener(view -> {
            message = editTextmessage.getText().toString();
            new sendSms(new ModelMessages(grouplistToUse, message,groupId)).execute();
            editTextmessage.setText("");
        });
        return this.v;
    }

    public void appendFirstName(){
        String message = editTextmessage.getText().toString();

        ModelMessageParameters modelMessageParameters = new ModelMessageParameters(message.length(),"fn");

        messageParametersList.add(modelMessageParameters);

        editTextmessage.append("'includeFirstName'");

    }

    public void appendLastName(){
        String message = editTextmessage.getText().toString();

        ModelMessageParameters modelMessageParameters = new ModelMessageParameters(message.length(),"ln");

        messageParametersList.add(modelMessageParameters);
        editTextmessage.append("'includeLastName'");


    }
    private void sendMessage(List<Grouplist> grouplistToUse2, String message2) {
        checkForSmsPermission();
        PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), 100, new Intent(this.SENT_ACTION), 0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(this.DELIVERED_ACTION), 0);
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode = getResultCode();
                if (resultCode == -1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                } else if (resultCode == 1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Generic failure",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 2) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Radio off",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 3) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Null PDU",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 4) {
                    Toast.makeText(FragmentMessage.this.getContext(), "No service",  Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(this.SENT_ACTION));
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode = getResultCode();
                if (resultCode == -1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
                } else if (resultCode == 0) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(this.DELIVERED_ACTION));
        SmsManager smsManager = SmsManager.getDefault();
        for (Grouplist grouplistnumber : grouplistToUse2) {
            smsManager.sendTextMessage(grouplistnumber.getPhoneNumber(), null, message2, sentIntent, deliveryIntent);
        }
    }

    public void setContactDetailsNeeded(ContactGroup contactGroup, List<Grouplist> grouplist) {
        this.grouplistToUse = new ArrayList();
        this.chipGroup.removeAllViews();
        for (Grouplist grouplistname : grouplist) {
            Chip chip = new Chip(getContext());
            chip.setText(grouplistname.getFirstName());
            chip.setChipIconResource(R.drawable.ic_person);
            chip.setTextAppearanceResource(R.style.ChipTextStyle);
            this.chipGroup.addView(chip);
            this.grouplistToUse.add(grouplistname);
        }
    }

    /* access modifiers changed from: private */
    public void setSpinnerAdapter(List<ContactGroup> contactGroups) {

        ArrayAdapter<ContactGroup> arrayAdapter2 = new ArrayAdapter<>( Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, contactGroups);
        this.arrayAdapter = arrayAdapter2;
        arrayAdapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.contactGroupSpinner.setAdapter(this.arrayAdapter);
        this.contactGroupSpinner.setOnItemSelectedListener(this);
    }

    /* access modifiers changed from: private */
    public void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.SEND_SMS") != 0) {
            Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.SEND_SMS"}, 1);
            return;
        }
        enableSmsButton();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (!permissions[0].equalsIgnoreCase("android.permission.SEND_SMS") || grantResults[0] != 0) {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                disableSmsButton();
                return;
            }
            enableSmsButton();
        }
    }

    public void smsSendMessage(String destinationAddress, String message2) {
        checkForSmsPermission();
        PendingIntent sentIntent = PendingIntent.getBroadcast(getContext(), 100, new Intent(this.SENT_ACTION), 0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(getContext(), 0, new Intent(this.DELIVERED_ACTION), 0);
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode = getResultCode();
                if (resultCode == -1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                } else if (resultCode == 1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Generic failure",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 2) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Radio off",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 3) {
                    Toast.makeText(FragmentMessage.this.getContext(), "Null PDU",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 4) {
                    Toast.makeText(FragmentMessage.this.getContext(), "No service",  Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(this.SENT_ACTION));
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int resultCode = getResultCode();
                if (resultCode == -1) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS delivered",  Toast.LENGTH_SHORT).show();
                } else if (resultCode == 0) {
                    Toast.makeText(FragmentMessage.this.getContext(), "SMS not delivered",  Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter(this.DELIVERED_ACTION));
        SmsManager.getDefault().sendTextMessage(destinationAddress, (String) null, message2, sentIntent, deliveryIntent);
    }

    private void enableSmsButton() {
        this.smsButton.setVisibility(View.VISIBLE);
    }

    private void disableSmsButton() {
        Toast.makeText(getContext(), "sms button disabled",  Toast.LENGTH_SHORT).show();
        this.smsButton.setVisibility(View.INVISIBLE);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Integer id2 = ((ContactGroup) parent.getItemAtPosition(position)).getId();
        groupId = id2;
        LiveData<ContactGroupAndGroupList> readGroupAndContacts = this.contactGroupViewModel.readGroupAndContacts(id2);
        this.contactGroupAndGroupList = readGroupAndContacts;

        this.contactGroupAndGroupList.observe(this, contactGroupAndGroupLists -> {

            try{
                setContactDetailsNeeded(contactGroupAndGroupLists.getContactGroup(),contactGroupAndGroupLists.getGrouplist());
            }catch(Exception e){
                e.printStackTrace();
            }

        });

    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public class sendSms extends AsyncTask<Void, Void, Void> {
        AlertDialog dialog;
        List<String> messageReport = new ArrayList();
        private ModelMessages modelMessages;
        ProgressBar progressBar = new ProgressBar(FragmentMessage.this.getContext());
        String statusReport;

        sendSms(ModelMessages modelMessages2) {
            this.modelMessages = modelMessages2;
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            AlertDialog.Builder builder = new AlertDialog.Builder(FragmentMessage.this.getContext());
            builder.setView(((LayoutInflater) ((Context) Objects.requireNonNull(FragmentMessage.this.getContext())).getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress_dialog_layout, (ViewGroup) null));
            builder.setCancelable(true);
            AlertDialog create = builder.create();
            this.dialog = create;
            create.show();
            if (this.dialog.getWindow() != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(this.dialog.getWindow().getAttributes());
                layoutParams.width = -2;
                layoutParams.height = -2;
                this.dialog.getWindow().setAttributes(layoutParams);
            }
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... params) {
            FragmentMessage.this.checkForSmsPermission();
            PendingIntent sentIntent = PendingIntent.getBroadcast(FragmentMessage.this.getContext(), 100, new Intent(FragmentMessage.this.SENT_ACTION), 0);
            PendingIntent deliveryIntent = PendingIntent.getBroadcast(FragmentMessage.this.getContext(), 0, new Intent(FragmentMessage.this.DELIVERED_ACTION), 0);
            FragmentMessage.this.getActivity().registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int resultCode = getResultCode();
                    if (resultCode == -1) {
                        sendSms.this.statusReport = "SMS sent";
                    } else if (resultCode == 1) {
                        sendSms.this.statusReport = "Generic failure";
                    } else if (resultCode == 2) {
                        sendSms.this.statusReport = "Radio off";
                    } else if (resultCode == 3) {
                        sendSms.this.statusReport = "Null PDU";
                    } else if (resultCode == 4) {
                        sendSms.this.statusReport = "No service";
                    }
                }
            }, new IntentFilter(FragmentMessage.this.SENT_ACTION));
            FragmentMessage.this.getActivity().registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int resultCode = getResultCode();
                    if (resultCode == -1) {
                        sendSms.this.statusReport = "SMS delivered";
                    } else if (resultCode == 0) {
                        sendSms.this.statusReport = "SMS not delivered";
                    }
                }
            }, new IntentFilter(FragmentMessage.this.DELIVERED_ACTION));
            SmsManager smsManager = SmsManager.getDefault();
            String theMessageToSend="";

            for (Grouplist grouplist : this.modelMessages.getGrouplistToUse()) {
                if(modelMessages.getMessage().contains("'includeFirstName'")){
                    theMessageToSend=modelMessages.getMessage().replace("'includeFirstName'",grouplist.getFirstName());
                }else if(modelMessages.getMessage().contains("'includeLastName'")){
                    theMessageToSend=modelMessages.getMessage().replace("'includeLastName'",grouplist.getLastName());
                }else{
                    theMessageToSend = modelMessages.getMessage();
                }
                    smsManager.sendTextMessage(grouplist.getPhoneNumber(), (String) null, theMessageToSend, sentIntent, deliveryIntent);
                    List<String> list = this.messageReport;
                    list.add(grouplist.getFirstName() + " " + grouplist.getLastName() + " -> " + this.statusReport);
            }
            //Log.i("Message : ",modelMessages.getMessage());
            //Log.i("Message_ID : ",modelMessages.getGroupId() + " ");
            messageViewModel.createMessage(modelMessages.getMessage(),modelMessages.getGroupId(),0);
            return null;
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            this.dialog.show();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (String report : this.messageReport) {
                Toast.makeText(FragmentMessage.this.getContext(), report, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

