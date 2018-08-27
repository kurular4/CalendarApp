package com.omer.user.calendarapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.omer.user.calendarapp.RestApi.ManagerAll;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentAdapter extends BaseAdapter {

    List<Appointment> appointments;
    Context context;
    Activity activity;

    public AppointmentAdapter(List<Appointment> appointments, Context context, Activity activity) {
        this.appointments = appointments;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int i) {
        return appointments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View layout = LayoutInflater.from(context).inflate(R.layout.event_element, viewGroup, false);
        TextView element_title = layout.findViewById(R.id.event_title);
        element_title.setText(appointments.get(i).getTitle());
        TextView element_desc = layout.findViewById(R.id.event_description);
        element_desc.setText(appointments.get(i).getDescription());
        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openSelectDialog(i);
                return false;
            }
        });

        return layout;
    }

    private void openSelectDialog(final int i) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.select, null);

        Button update = view.findViewById(R.id.update);
        Button delete = view.findViewById(R.id.delete);
        Button cancel = view.findViewById(R.id.cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDeleteAppointmentDialog(i);
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateAppointmentDialog(i);
                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void openDeleteAppointmentDialog(final int i) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.delete_event, null);

        final Button delete = view.findViewById(R.id.delete);
        Button cancel = view.findViewById(R.id.cancel2);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAppointment(appointments.get(i).getId());
                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void openUpdateAppointmentDialog(final int i) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.update_event, null);

        Button update = view.findViewById(R.id.update);
        Button cancel = view.findViewById(R.id.cancel);
        final EditText title = view.findViewById(R.id.title);
        final EditText desc = view.findViewById(R.id.description);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);

        final AlertDialog dialog = builder.create();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAppointment(appointments.get(i).getId(), title.getText().toString() , desc.getText().toString());
                Log.i("date", appointments.get(i).getStart_date()+"!!!");
                dialog.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void deleteAppointment(int id) {
        Call<Appointment> x = ManagerAll.getInstance().delete_appointment(id);
        x.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Appointment deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Log.i("date", t.getMessage());
            }
        });

    }

    private void updateAppointment(int id, String title, String description) {
        Call<Appointment> x = ManagerAll.getInstance().update_appointment(id, title, description);
        x.enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Appointment updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {

            }
        });
    }

    private void listAppointments(String date){
        Call<List<Appointment>> x = ManagerAll.getInstance().all_appointments(date);
        x.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if(response.isSuccessful()) {
                    appointments = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.i("date", t.getMessage().toString());
            }
        });
    }
}
