package com.omer.user.calendarapp.RestApi;


public class BaseManager {

    protected RestApi getRestApiClient(){
        RestApiClient restApiClient = new RestApiClient(BaseURL.Data_url);
        return  restApiClient.getRestApi();
    }

}
