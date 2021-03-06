package edu.com.medicalapp.Retrofit;

import edu.com.medicalapp.Models.login.loginResponse;
import edu.com.medicalapp.Models.maincat.CategoryDetailData;
import edu.com.medicalapp.Models.registration.CommonResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {


   /* @POST("api/api.php?req=login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
*/

   @Multipart
    @POST("api/api.php?req=login")
    Call<loginResponse> loginUser(@Part("email_id") RequestBody email, @Part("password") RequestBody password);


    @Multipart
    @POST("api/api.php?req=registration")
    Call<CommonResponse> registerUser(@Part("name") RequestBody name,
                                      @Part("username") RequestBody username,
                                      @Part("email_id") RequestBody email,
                                      @Part("password") RequestBody password);


    @GET("api/api.php?req=category")
    Call<CategoryDetailData> getCourse();



}