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
    @GET("user?candidate=cjm123")
    retrofit2.Call<List<RootObjectUser>> getUserList();

    @POST("user")
    Call<RootObject> createUser(@Body RootObject task);

    @GET("user/{id}/transfers?candidate=cjm123")
    retrofit2.Call<List<RootObjectTransfers>> getTransfers(@Path("id")String id);
    //http://fake-button.herokuapp.com/user/2/transfers?candidate=cjm123

    @POST("transfer")
    Call<RootObjectSendAmounts> sendAmount(@Body RootObjectSendAmounts task);
    //curl --data '{ "amount": "100", "user_id": 1, "candidate": "cjm123" }' http://fake-button.herokuapp.com/transfer

    @DELETE("user/{id}")
    Call<ResponseBody>deleteUser(@Path("id")String id, @Query("candidate") String candidate);
    //curl -X DELETE http://fake-button.herokuapp.com/user/2candidate=cjm123
}
