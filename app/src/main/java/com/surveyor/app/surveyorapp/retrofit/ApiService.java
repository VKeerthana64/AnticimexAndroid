package com.surveyor.app.surveyorapp.retrofit;

import com.google.gson.JsonElement;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

//    String BASEURL = "http://104.238.94.235/anticimex/";

    String BASEURL = "http://surveypro.anticimex.com.sg/anticimexUAT/"; //test
    String BASEURL_PROFILE = "http://surveypro.anticimex.com.sg/data/Profile/anticimexUAT/"; //test

//    String BASEURL = "http://43.229.85.134/anticimex/"; // Dev
//    String BASEURL_PROFILE = "http://43.229.85.134/anticimex/data/Profile/"; //Dev

//    String BASEURL = "https://surveypro.anticimex.com.sg/"; //Production
//    String BASEURL_PROFILE = "https://surveypro.anticimex.com.sg/data/Profile/"; //Production

    @Multipart
    @POST("api/auth/token")
    Call<JsonElement> login(@Part("imei") RequestBody imei);

    @GET("api/auth/getRegisteredIMEI/{IMEI}")
    Call<JsonElement> loginimeiCheck(@Path("IMEI") String imei);

    @Multipart
    @POST("api/auth/GetUserDetails")
    Call<JsonElement> insertUserDetails(@Part("UserName") RequestBody UserName,
                                              @Part("Password") RequestBody Password);

    @Multipart
    @POST("api/auth/InsertDeviceDetails")
    Call<JsonElement> insertUnRegisteredIMEI (@Part("imei") RequestBody imei,
                                     @Header("DeviceName") String DeviceName,
                                     @Header("DeviceModel") String DeviceModel,
                                     @Header("OSVersion") String OSVersion,
                                     @Header("RegisteredBy") String RegisteredBy,
                                     @Header("ActivatedBy") String ActivatedBy,
                                     @Header("Notes") String Notes);
    @Multipart
    @POST("api/auth/InsertLocation")
    Call<JsonElement> insertLocation(@Part("Latitude") RequestBody is_timeout,
                                     @Part("Longitude") RequestBody latitude);

    @Multipart
    @POST("api/attendance")
    Call<JsonElement> takeAttendance(@Header("Authorization") String authorization,
                                     @Part("is_timeout") RequestBody is_timeout,
                                     @Part("latitude") RequestBody latitude,
                                     @Part("longitude") RequestBody longitude,
                                     @Part("geoaddress") RequestBody geoaddress,
                                     @Part MultipartBody.Part file);

    @GET("api/attendancereport")
    Call<JsonElement> getAttendanceReport(@Header("Authorization") String authorization);

    @GET("api/getalljobtype")
    Call<JsonElement> getJobType(@Header("Authorization") String authorization);

    @GET("api/getalltechnician")
    Call<JsonElement> getAllTechnician(@Header("Authorization") String authorization);

    @Multipart
    @POST("api/searchjoblist")
    Call<JsonElement> searchJobList(@Header("Authorization") String authorization,
                                    @Part("customername") RequestBody customername,
                                    @Part("locationname") RequestBody locationname,
                                    @Part("scheduledate") RequestBody scheduledate,
                                    @Part("serviceaddress") RequestBody serviceaddress);

    @Multipart
    @POST("api/submissionreport")
    Call<JsonElement> getReportStatistics(@Header("Authorization") String authorization,
                                          @Part("datefrom") RequestBody datefrom,
                                          @Part("dateto") RequestBody dateto);

    @Multipart
    @POST("api/addteammember")
    Call<JsonElement> addTeamMembers(@Header("Authorization") String authorization,
                                     @Part("jo_scheduleid") RequestBody jo_scheduleid,
                                     @Part("userid") RequestBody userid);

    @Multipart
    @POST("api/removeteammember")
    Call<JsonElement> removeTeamMembers(@Header("Authorization") String authorization,
                                        @Part("jo_scheduleid") RequestBody jo_scheduleid,
                                        @Part("userid") RequestBody userid);

    @Multipart
    @POST("api/savesurvey")
    Call<JsonElement> saveForm(@Header("Authorization") String authorization,
                               @Part("id") RequestBody id, //optional used only to update
                               @Part("jo_scheduleid") RequestBody jo_scheduleid,
                               @Part("landowner") RequestBody landowner,
                               @Part("postalcode") RequestBody postalcode,
                               @Part("constituency") RequestBody constituency,
                               @Part("town_council") RequestBody town_council,
                               @Part("date_of_finding") RequestBody date_of_finding,
                               @Part("time_of_finding") RequestBody time_of_finding,
                               @Part("bincenter") RequestBody bincenter,
                               @Part("findings") RequestBody findings,
                               @Part("noofburrows") RequestBody noofburrows,
                               @Part("activeburrows") RequestBody noofactiveburrows,
                               @Part("nonactiveburrows") RequestBody noofnonactiveburrows,
                               @Part("noofdefects") RequestBody no_of_defect,
                               @Part("habitate") RequestBody habitate,
                               @Part("probablecauseofburrows") RequestBody probablecauseofburrows,
                               @Part("neajobid") RequestBody neajobid,
                               @Part("feedbacksubstantiated") RequestBody feedbacksubstantiated,
                               @Part("remarks") RequestBody remarks,
                               @Part("locationremarks") RequestBody locationremarks,
                               @Part("actiontaken") RequestBody actiontaken,
                               @Part("contractordateresolved") RequestBody contractordateresolved,
                               @Part("latitude") RequestBody latitude,
                               @Part("longitude") RequestBody longitude,
                               @Part("geoaddress") RequestBody geoaddress,
                               @Part("isdraft") RequestBody isDraft,
                               @Part("noofbinchute") RequestBody noofbinchute,
                               @Part("noofcrc") RequestBody noofcrc,
                               @Part ArrayList<MultipartBody.Part> file);

    @DELETE("api/deletesurveyform/{id}")
    Call<JsonElement> deleteSurveyForm(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("api/getalloption")
    Call<JsonElement> getAllOptions(@Header("Authorization") String authorization);

    @Multipart
    @POST("api/updateprofileimage")
    Call<JsonElement> saveProfileImage(@Header("Authorization") String authorization,
                                      @Part MultipartBody.Part file);

    @GET("api/getjoblistallsync")
    Call<JsonElement> getJoblistAllSync(@Header("Authorization") String authorization);

    @GET("api/attendancestatus")
    Call<JsonElement> checkAttendance(@Header("Authorization") String authorization);

    @GET("api/getjoblistall")
    Call<JsonElement> getJobListAll(@Header("Authorization") String authorization);

    @GET("api/getjoblistscheduled")
    Call<JsonElement> getJobListScheduled(@Header("Authorization") String authorization);

    @GET("api/JobDetail/{id}")
    Call<JsonElement> getJobDetail(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("api/GetSurveyForm/{id}")
    Call<JsonElement> getSurveyForm(@Header("Authorization") String authorization, @Path("id") String id);

    @GET("api/getteammembers/{id}")
    Call<JsonElement> getTeamMembers(@Header("Authorization") String authorization, @Path("id") String id);

    @Multipart
    @POST("api/SaveImages")
    Call<JsonElement> saveSurveyImage(@Header("Authorization") String authorization,
                                      @Part("uniqueid") RequestBody uniqueid,
                                      @Part("latitude") RequestBody latitude,
                                      @Part("longitude") RequestBody longitude,
                                      @Part("geoaddress") RequestBody geoaddress,
                                      @Part MultipartBody.Part file);
//{"success":false,"msg":"failed","data":"An error occurred while updating the entries. See the inner exception for details."}

    @Multipart
    @POST("api/setoption")
    Call<JsonElement> setOption(@Header("Authorization") String authorization,
                                @Part("fieldtype") RequestBody fieldtype,
                                @Part("optionname") RequestBody optionname,
                                @Part("sortorder") RequestBody sortorder);

    @Multipart
    @PUT("api/updateoption/{id}")
    Call<JsonElement> updateOption(@Header("Authorization") String authorization,
                                   @Path("id") String optionId,
                                   @Part("fieldtype") RequestBody fieldtype,
                                   @Part("optionname") RequestBody optionname,
                                   @Part("sortorder") RequestBody sortorder);

    @DELETE("api/deletesurveyimage/{id}")
    Call<JsonElement> deleteSurveyImage(@Header("Authorization") String authorization, @Path("id") String id);

    /*@Multipart
    @POST("api/setoption")
    Call<JsonElement> setOption(@Header("Authorization") String authorization,
                                @Part("fieldtype") RequestBody fieldtype,
                                @Part("optionname") RequestBody optionname,
                                @Part("sortorder") RequestBody sortorder);*/
}
