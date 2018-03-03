package com.example.franciscoandrade.button_challenge.restApi;

import com.example.franciscoandrade.button_challenge.restApi.model.RootObject;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectSendAmounts;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectTransfers;
import com.example.franciscoandrade.button_challenge.restApi.model.RootObjectUser;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by franciscoandrade on 3/1/18.
 */

public interface EndPointApi {
    /**
     * Get list ob Users request
     */
    @GET("user?candidate=cjm123")
    retrofit2.Call<List<RootObjectUser>> getUserList();

    /**
     * Post to create User
     */
    @POST("user")
    Call<RootObject> createUser(@Body RootObject task);

    /**
     * Get list of Transfers
     */
    @GET("user/{id}/transfers?candidate=cjm123")
    retrofit2.Call<List<RootObjectTransfers>> getTransfers(@Path("id")String id);
    //http://fake-button.herokuapp.com/user/2/transfers?candidate=cjm123


    /**
     * Post to transfer money to a user
     */
    @POST("transfer")
    Call<RootObjectSendAmounts> sendAmount(@Body RootObjectSendAmounts task);


    /**
     * Delete user request
     */
    @DELETE("user/{id}")
    Call<ResponseBody>deleteUser(@Path("id")String id, @Query("candidate") String candidate);
}
