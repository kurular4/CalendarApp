package com.omer.user.calendarapp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.omer.user.calendarapp.RestApi.ManagerAll;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    CalendarView calendar;
    ListView appointment;
    List<Appointment> appointments;
    AppointmentAdapter appointmentAdapter;
    Button addEvent, help, searchb, do_search;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        initialize();
        help();
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, final int i, final int i1, final int i2) {
                final int i_m = i1 + 1;
                Log.i("date", i2 + "/" + i_m + "/" + i);
                listAppointments(i2 + "/" + i_m + "/" + i);
                addEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater layoutInflater = getLayoutInflater();
                        view = layoutInflater.inflate(R.layout.add_event, null);

                        Button add = view.findViewById(R.id.add);
                        Button cancel = view.findViewById(R.id.cancel);

                        final EditText title = view.findViewById(R.id.title);
                        final EditText description = view.findViewById(R.id.description);

                        final TextView date = view.findViewById(R.id.date);

                        date.setText(i2 + "/" + i_m + "/" + i);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(view);
                        builder.setCancelable(true);

                        final AlertDialog dialog = builder.create();

                        dialog.show();

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addAppointment(title.getText().toString(), description.getText().toString(), i2 + "/" + i_m + "/" + i);
                                listAppointments(i2 + "/" + i_m + "/" + i);
                                dialog.cancel();
                            }
                        });

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                    }
                });
            }
        });

        searchb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search.getVisibility() != View.VISIBLE) {
                    search.setVisibility(View.VISIBLE);
                    do_search.setVisibility(View.VISIBLE);
                } else {
                    search.setVisibility(View.GONE);
                    do_search.setVisibility(View.GONE);
                }
            }
        });

        do_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(search.getText().toString());
            }
        });

        checkService();
    }

    private void initialize() {
        calendar = findViewById(R.id.calendar);
        appointment = findViewById(R.id.appointments);
        appointments = new ArrayList<>();
        addEvent = findViewById(R.id.addevent);
        help = findViewById(R.id.help);
        searchb = findViewById(R.id.searchb);
        search = findViewById(R.id.search);
        do_search = findViewById(R.id.do_search);
    }

    private void checkService(){
        if (!isServiceOn()) {
            Intent intent = new Intent(getApplicationContext(), NotificationService.class);
            startService(intent);
        }
    }

    public boolean isServiceOn() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void help() {
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = getLayoutInflater();
                view = layoutInflater.inflate(R.layout.help, null);

                Button exit = view.findViewById(R.id.exit_help);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view);
                builder.setCancelable(true);

                final AlertDialog dialog = builder.create();

                dialog.show();

                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    private void fillList() {
        appointmentAdapter = new AppointmentAdapter(appointments, getApplicationContext(), MainActivity.this);
        appointment.setAdapter(appointmentAdapter);
    }

    private void listAppointments(String date) {
        Call<List<Appointment>> x = ManagerAll.getInstance().all_appointments(date);
        x.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    appointments = response.body();
                    fillList();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.i("date", t.getMessage().toString());
            }
        });
    }

    private void addAppointment(String title, String description, String date) {
        Call<Appointment> x = ManagerAll.getInstance().add_appointment(title, description, date);
        x.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Appointment created", Toast.LENGTH_SHORT).show();
                    send_notification();
                } else
                    Log.i("date", "Error creating..");
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Log.i("date", t.getMessage().toString());
            }
        });
    }



    private void search(String keyword) {
        listAll(keyword);
    }

    private void listAll(final String keyword){
        Call<List<Appointment>> x = ManagerAll.getInstance().all_appointments_();
        x.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    appointments.clear();
                    if (response.body() != null) {
                        for (int i = 0; i < response.body().size(); i++) {
                            if (response.body().get(i).getTitle().contains(keyword))
                                appointments.add(response.body().get(i));
                        }
                        fillList();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {

            }
        });
    }

    private void send_notification() {
        Call<String> x = ManagerAll.getInstance().send_notification();
        x.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}


