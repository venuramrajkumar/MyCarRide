package com.raj.mycarride.rest

class Resource<T> private constructor(var mStatus: Status?, var mData: T, val message: String?) {
    enum class Status {
        SUCCESS, ERROR, LOADING,DONE
    }

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String = "Sevice Failed", errordata: T): Resource<T> {
            return Resource(Status.ERROR, errordata, msg)
        }

        fun  <T> loading(data: T): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }


        fun <T> done(data: T): Resource<T> {
            return Resource(Status.DONE, data, null)
        }
    }
}