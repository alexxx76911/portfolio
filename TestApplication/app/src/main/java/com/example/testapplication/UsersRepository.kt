package com.example.testapplication


import com.example.testapplication.db.DataBase
import com.example.testapplication.network.Network
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepository {
    private val api = Network.api
    private val usersDao = DataBase.usersDb.userDao()

    fun getUsers(): Single<List<Person>> {
        return Single.create {
            api.getUsers().enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    it.onSuccess(response.body()?.data.orEmpty())
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    it.onError(t)
                }
            })


        }
    }

    fun saveUsersToDb(list: List<Person>): Single<Unit> {
        return Single.create {
            try {
                usersDao.insertUsers(list)
                it.onSuccess(Unit)
            } catch (e: Throwable) {
                it.onError(e)
            }
        }

    }

    fun getUsersFromDb(): Single<List<Person>> {
        return Single.create {
            try {
                it.onSuccess(usersDao.getUsers())
            } catch (e: Throwable) {
                it.onError(e)
            }
        }
    }
}