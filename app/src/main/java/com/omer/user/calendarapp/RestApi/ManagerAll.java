package com.omer.user.calendarapp.RestApi;

import com.omer.user.calendarapp.Appointment;

import java.util.List;

import retrofit2.Call;

public class ManagerAll extends BaseManager {

    private static ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance() {
        return ourInstance;
    }

    public Call<Appointment> add_appointment(String title, String description, String date) {
        Call<Appointment> call = getRestApiClient().add_appointment(title, description, date);
        return call;
    }

    public Call<Appointment> delete_appointment(int id) {
        Call<Appointment> call = getRestApiClient().delete_appointment(id);
        return call;
    }

    public Call<Appointment> update_appointment(int id, String title, String description) {
        Call<Appointment> call = getRestApiClient().update_appointment(id, title, description);
        return call;
    }

    public Call<List<Appointment>> all_appointments(String date) {
        Call<List<Appointment>> call = getRestApiClient().all_appointments(date);
        return call;
    }

    public Call<String> send_notification() {
        Call<String> call = getRestApiClient().send_notification();
        return call;
    }

    public Call<List<Appointment>> all_appointments_() {
        Call<List<Appointment>> call = getRestApiClient().all_appointments_();
        return call;
    }
}
