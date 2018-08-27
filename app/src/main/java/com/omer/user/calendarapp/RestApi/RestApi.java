package com.omer.user.calendarapp.RestApi;

import com.omer.user.calendarapp.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestApi {

    @FormUrlEncoded
    @POST("add_appointment.php")
    Call<Appointment> add_appointment(@Field("title") String title, @Field("description") String description,
                               @Field("date") String date);

    @FormUrlEncoded
    @POST("all_appointments.php")
    Call<List<Appointment>> all_appointments(@Field("date") String date);

    @GET("all_appointments_.php")
    Call<List<Appointment>> all_appointments_();

    @FormUrlEncoded
    @POST("update_appointment.php")
    Call<Appointment> update_appointment(@Field("id") int id, @Field("title") String title, @Field("description") String description);

    @FormUrlEncoded
    @POST("delete_appointment.php")
    Call<Appointment> delete_appointment(@Field("id") int id);

    @GET("send_notification.php")
    Call<String> send_notification();

}
