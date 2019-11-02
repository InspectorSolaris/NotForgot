package com.example.mobilelab.model.server

import com.example.mobilelab.model.server.form.CategoryForm
import com.example.mobilelab.model.server.form.TaskForm
import com.example.mobilelab.model.taskData.Category
import com.example.mobilelab.model.taskData.Task
import com.example.mobilelab.model.server.user.Token
import com.example.mobilelab.model.server.user.User
import com.example.mobilelab.model.server.user.UserLoginForm
import com.example.mobilelab.model.server.user.UserRegistrationForm
import com.example.mobilelab.model.taskData.Priority
import retrofit2.Call
import retrofit2.http.*

interface NotForgotAPI {

    @POST("register")
    @Headers("Accept:application/json")
    fun postRegister(
        @Body userRegistrationForm: UserRegistrationForm
    ): Call<User>

    @POST("login")
    @Headers("Accept:application/json")
    fun postLogin(
        @Body userLoginForm: UserLoginForm
    ): Call<Token>


    @GET("categories")
    @Headers("Accept:application/json")
    fun getCategories(
        @Header("Authorization") token: String
    ): Call<List<Category>>

    @GET("priorities")
    @Headers("Accept:application/json")
    fun getPriorities(
        @Header("Authorization") token: String
    ): Call<List<Priority>>

    @GET("tasks")
    @Headers("Accept:application/json")
    fun getTasks(
        @Header("Authorization") token: String
    ): Call<List<Task>>


    @POST("categories")
    @Headers("Accept:application/json")
    fun postCategory(
        @Header("Authorization") token: String,
        @Body categoryForm: CategoryForm
    ): Call<Category>

    @POST("tasks")
    @Headers("Accept:application/json")
    fun postTask(
        @Header("Authorization") token: String,
        @Body taskForm: TaskForm
    ): Call<Task>


    @PATCH("tasks/{id}")
    @Headers("Accept:application/json")
    fun patchTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body taskForm: TaskForm
    ): Call<Task>

    @DELETE("tasks/{id}")
    @Headers("Accept:application/json")
    fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

}