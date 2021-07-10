package com.raj.mycarride.rest.repositories


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.raj.mycarride.rest.IGetBeerInfoAPI
import com.raj.mycarride.storage.BeerDao
import com.raj.mycarride.storage.BeerInfo
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DefaultObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import javax.inject.Inject

class OrderBeerRepo @Inject constructor() {

    @Inject
    lateinit var iGetBeerInfoAPI : IGetBeerInfoAPI

    private  var beerLiveData : MutableLiveData<List<BeerInfo>> = MutableLiveData()
    private var dataError: MutableLiveData<String> = MutableLiveData()

    private var disposable :CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var beerDao : BeerDao

    private  var observableToStorage = CompositeDisposable()


    fun getBeerInfoApi() {

        disposable.add(iGetBeerInfoAPI.getBeerInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver()))

    }


    fun demoFlowableBackPressure() {
        iGetBeerInfoAPI.getBeerInfoFlowable().onBackpressureLatest()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread(),false,3)
            .subscribe(getSubscriber())



    }


    fun demoMapOperator() {

        iGetBeerInfoAPI.getBeerInfo().map { listOfBeerInfo -> convertToStringList(listOfBeerInfo)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getStringObserver())

//        iGetBeerInfoAPI.getBeerInfo().map (object : Function<List<BeerInfo>, List<String>> {
//            override fun apply(t: List<BeerInfo>): List<String> {
//                return convertToStringList(t)
//            }
//
//        }).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(getStringObserver())

    }

    fun demoFlatMapOperator() {
        iGetBeerInfoAPI.getBeerInfo()
            .flatMap { listOfBeefInfo -> Observable.fromIterable(listOfBeefInfo) }
//            .flatMap { info -> Observable.just(info) }
            .map { info -> convertToString(info) }
            .filter { info -> info.startsWith("wild",true) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getNameObserver())
    }

    fun demoSearch() {

    }


    fun demoZipOperator() {
        Observable.zip(
            Observable.just("Super","Extra"),
            Observable.just("Wild Trail Pale Ale","Wild Night", "Wild country"),
            BiFunction<String,String,String>{
                t1, t2 -> t1.toUpperCase() +" " +t2.capitalize()
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getNameObserver())


//        Observable.zip(
//            Observable.just("Super","Extra"),
//            Observable.just("Wild Trail Pale Ale","Wild Night"),
//            object :BiFunction<String,String,String>{
//                override fun apply(t1: String, t2: String): String {
//                    return t1 + t2
//                }
//
//            }).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(getNameObserver())
    }


    private fun convertToString(listOfBeerInfo: BeerInfo): String {
        System.out.println("Thread name is " + Thread.currentThread().id)

        return listOfBeerInfo.name
    }

    private fun getNameObserver() : Observer<String> {
        return object : Observer<String> {

            override fun onNext(t: String) {
                Log.d("OrderBeerRepo Items are", t)
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("OrderBeerRepo", "onSubscribe")
            }

            override fun onComplete() {
                Log.d("OrderBeerRepo", "onComplete")
            }

            override fun onError(e: Throwable) {
                Log.d("OrderBeerRepo", "onError")
            }

        }
    }


    private fun convertToStringList(listOfBeerInfo: List<BeerInfo>): List<String> {
        val stringList  = arrayListOf<String>()
        System.out.println("Thread name is " + Thread.currentThread().id)

        for (item in listOfBeerInfo) {
            stringList.add(item.name)
        }
        return stringList
    }


    private fun getStringObserver() : DefaultObserver<List<String>> {
        return object : DefaultObserver<List<String>>() {
            override fun onComplete() {
                Log.d("OrderBeerRepo", "onComplete")
            }

            override fun onNext(t: List<String>) {
                Log.d("OrderBeerRepo", t.get(0))
            }

            override fun onError(e: Throwable) {
                Log.d("OrderBeerRepo", "ERRROR")
            }

        }
    }



    fun getBeerInfoFromDb() {
        disposable.add(beerDao.getAllBeers().toObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d("OrderBeerRepo",it.size.toString())
            }
            .subscribeWith(getOfflineObserver()))
    }

    fun getBeerLiveData() : LiveData<List<BeerInfo>> {
        return beerLiveData
    }

    private fun getObserver() : DisposableObserver<List<BeerInfo>> {
        return object :DisposableObserver<List<BeerInfo>>() {
            override fun onComplete() {
                disposable.dispose()
                Log.d("OrderBeerRepo" , "onComplete")
            }

            override fun onNext(beerInfo: List<BeerInfo>) {//Resource<List<BeerInfo>>
                Log.d("OrderBeerRepo",beerInfo.toString())

                beerLiveData.postValue(beerInfo)

                disposable.add(Observable.just(beerInfo).subscribeOn(Schedulers.io()).subscribe {
                    for (item in it) {
                        beerDao.insertBeer(item)
                        Log.d("OrderBeerRepo Thread",Thread.currentThread().toString())
                    }
                })

                //beerLiveData.value = beerInfo
                //beerLiveData.value = (Resource.success(beerInfo.mData))
            }

            override fun onError(e: Throwable) {
                e.message?.let { Log.d("OrderBeerRepo" , it) }
                //commented below two lines as service is down.
                //dataError.postValue(e.message)
                //getBeerInfoFromDb()
                var beerInfo: List<BeerInfo> = arrayListOf(BeerInfo("Abv","IBU",111,"RAJKUMAR","Style",1.0f),
                    BeerInfo("Abv","IBU",111,"NEELAM","Style",2.0f))
                beerLiveData.postValue(beerInfo)

            }

        }
    }

    fun getSubscriber() : Subscriber<List<BeerInfo>> {
        return object : Subscriber<List<BeerInfo>> {
            override fun onComplete() {
                Log.d("OrderBeerRepo","Complete")
            }

            override fun onSubscribe(s: Subscription?) {
                Log.d("OrderBeerRepo","")
            }

            override fun onNext(t: List<BeerInfo>) {
                Log.d("OrderBeerRepo",t.size.toString())
            }

            override fun onError(t: Throwable?) {
                Log.d("OrderBeerRepo",t.toString())
            }

        }
    }


    private fun getOfflineObserver() : DisposableObserver<List<BeerInfo>> {
        return object :DisposableObserver<List<BeerInfo>>() {
            override fun onComplete() {
                disposable.dispose()
                Log.d("OrderBeerRepo" , "onComplete")
            }

            override fun onNext(beerInfo: List<BeerInfo>) {//Resource<List<BeerInfo>>
                Log.d("OrderBeerRepo",beerInfo.toString())

                beerLiveData.postValue(beerInfo)

                //beerLiveData.value = beerInfo
                //beerLiveData.value = (Resource.success(beerInfo.mData))
            }

            override fun onError(e: Throwable) {
                e.message?.let { Log.d("OrderBeerRepo" , it) }
                dataError.postValue(e.message)

            }

        }
    }


    fun disposeElements(){
        if(null != disposable && !disposable.isDisposed) disposable.dispose()
    }



}