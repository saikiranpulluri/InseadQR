package com.konbini.inseadqr.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konbini.inseadqr.data.CustomerOrdersByStatusModelItem
import com.konbini.inseadqr.data.Resource
import com.konbini.inseadqr.repositories.MainRepository
import com.konbini.inseadqr.utils.PrefUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.Credentials
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val pollingState = MutableLiveData("INACTIVE")
    var isPendingOrdersExist = MutableLiveData<Boolean>()
    var isOrderStatusUpdated = MutableLiveData<Boolean>()
    var isLogoutCompleted = MutableLiveData<Boolean>()
    var temporaryLoginURL = MutableLiveData<String>()
    val orderItems: ArrayList<CustomerOrdersByStatusModelItem> = arrayListOf()

    fun checkCustomerOrderByStatus(status: String, customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = PrefUtil.getString("AppSettings.Cloud.Host") + "/wp-json/wc/v2/orders"
            Log.i("MainViewModel", url)
            val basic = Credentials.basic(
                "ck_ab364e76dffa761a95ad2c073c95f0ce602b608a",
                "cs_5536154aed2df0e1514f1bbe09a787b228a4883f"
            )
            val checkCustomerOrderByStatusResponse =
                mainRepository.checkCustomerOrderByStatus(url, status, customerId, basic)

            if (checkCustomerOrderByStatusResponse.status == Resource.Status.SUCCESS) {
                if ((checkCustomerOrderByStatusResponse.data?.size ?: 0) > 0) {
                    isPendingOrdersExist.postValue(true)
                    Log.i("MainViewModel", "Pending orders exist")
                    orderItems.clear()
                    checkCustomerOrderByStatusResponse.data?.forEach {
                        orderItems.add(it)
                    }
                } else {
                    isPendingOrdersExist.postValue(false)
                    Log.i("MainViewModel", "Pending orders does not exist")
                }
            }
        }
    }

    fun enableTemporaryLoginForUser(customerId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = PrefUtil.getString("AppSettings.Cloud.Host") + "?oauth=token"
            val map = HashMap<String, String>()
            map["grant_type"] = "client_credentials"
            map["client_id"] = PrefUtil.getString("AppSettings.Cloud.ClientId")
            map["client_secret"] = PrefUtil.getString("AppSettings.Cloud.ClientSecret")
            val authTokenResponse = mainRepository.getAuthToken(url, map)
            if (authTokenResponse.status == Resource.Status.SUCCESS) {
                authTokenResponse.data?.let {
                    val temporaryLoginMap = HashMap<String, String>()
                    temporaryLoginMap["access_token"] = it.accessToken ?: ""
                    temporaryLoginMap["user_id"] = customerId ?: ""
                    temporaryLoginMap["user_id_type"] = "user_id_number"

                    val enableTemporaryLoginURL =
                        PrefUtil.getString("AppSettings.Cloud.Host") + "/wp-json/wp/v2/ktl/enable-temporary-login"
                    val enableTemporaryLoginResponse = mainRepository.enableTemporaryLogin(
                        enableTemporaryLoginURL,
                        temporaryLoginMap
                    )
                    if (enableTemporaryLoginResponse.status == Resource.Status.SUCCESS) {
                        enableTemporaryLoginResponse.data?.let { response ->
                            response.tempResponse.temporaryLoginUrl?.let { loginURL ->
                                temporaryLoginURL.postValue(loginURL)
                            }
                        }
                    } else {
//                        val messageResponse =
//                            Gson().fromJson(
//                                enableTemporaryLoginResponse.message,
//                                MessageResponse::class.java
//                            )
//                        message = messageResponse.message
//                        walletTopUp.postValue(false)
                    }
                }
            } else {
//                message = "The client credentials are invalid"
//                walletTopUp.postValue(false)
            }
        }
    }

    fun disableTemporaryLoginForUser(customerId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = PrefUtil.getString("AppSettings.Cloud.Host") + "?oauth=token"
            val map = HashMap<String, String>()
            map["grant_type"] = "client_credentials"
            map["client_id"] = PrefUtil.getString("AppSettings.Cloud.ClientId")
            map["client_secret"] = PrefUtil.getString("AppSettings.Cloud.ClientSecret")
            val authTokenResponse = mainRepository.getAuthToken(url, map)
            if (authTokenResponse.status == Resource.Status.SUCCESS) {
                authTokenResponse.data?.let {
                    val temporaryLoginMap = HashMap<String, String>()
                    temporaryLoginMap["access_token"] = it.accessToken ?: ""
                    temporaryLoginMap["user_id"] = customerId ?: ""
                    temporaryLoginMap["user_id_type"] = "user_id_number"

                    val enableTemporaryLoginURL =
                        PrefUtil.getString("AppSettings.Cloud.Host") + "/wp-json/wp/v2/ktl/disable-temporary-login"
                    val enableTemporaryLoginResponse = mainRepository.enableTemporaryLogin(
                        enableTemporaryLoginURL,
                        temporaryLoginMap
                    )
                    if (enableTemporaryLoginResponse.status == Resource.Status.SUCCESS) {
                        isLogoutCompleted.postValue(true)
                    } else {
//                        val messageResponse =
//                            Gson().fromJson(
//                                enableTemporaryLoginResponse.message,
//                                MessageResponse::class.java
//                            )
//                        message = messageResponse.message
//                        walletTopUp.postValue(false)
                    }
                }
            } else {
//                message = "The client credentials are invalid"
//                walletTopUp.postValue(false)
            }
        }
    }

    fun updateOrderStatus(selectedOrders: ArrayList<CustomerOrdersByStatusModelItem>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val runningTasks = selectedOrders.map { order ->
                    async { // this will allow us to run multiple tasks in parallel
                        delay(200)
                        val url =
                            PrefUtil.getString("AppSettings.Cloud.Host") + "/wp-json/wc/v3/orders/" + order.id
                        val map = HashMap<String, String>()
                        map["status"] = "completed"
                        val basic = Credentials.basic(
                            "ck_ab364e76dffa761a95ad2c073c95f0ce602b608a",
                            "cs_5536154aed2df0e1514f1bbe09a787b228a4883f"
                        )
                        val updateOrderStatus = mainRepository.updateOrderStatus(url, map, basic)
                        order to updateOrderStatus
                    }
                }

                val responses = runningTasks.awaitAll()
                responses.forEach { (_, response) ->
                    if (response.status == Resource.Status.SUCCESS) {
                        isOrderStatusUpdated.postValue(true)
                    }
                }
            }
        }

        /*  selectedOrders.forEach {
              viewModelScope.async {
                  delay(200)
                  val url = AppSettings.Cloud.Host + "/wp-json/wc/v3/orders/" + it.id
                  val map = HashMap<String, String>()
                  map["status"] = "completed"
                  val basic = Credentials.basic(
                      "ck_ab364e76dffa761a95ad2c073c95f0ce602b608a",
                      "cs_5536154aed2df0e1514f1bbe09a787b228a4883f"
                  )
                  mainRepository.updateOrderStatus(url, map, basic)
              }
          }*/
    }
}