package com.test.palmapi

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.test.palmapi.database.DatabaseRepo
import com.test.palmapi.database.accounts.Accounts
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.ImageFromText
import com.test.palmapi.dto.PalmApi
import com.test.palmapi.dto.Prompt
import com.test.palmapi.login.SignInResult
import com.test.palmapi.login.SignInState
import com.test.palmapi.repository.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.Client
import io.appwrite.models.Collection
import io.appwrite.services.Databases
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: ApiService,
    private val dbRepository: DatabaseRepo
) : AndroidViewModel(application) {

    var apiData: MutableState<PalmApi?> = mutableStateOf(null)
    val listOfMessages: MutableState<MutableList<ChatMessage>> = mutableStateOf(mutableListOf())
    val message: MutableState<String> = mutableStateOf("")
    val imagePrompt: MutableState<String> = mutableStateOf("")
    fun allMessages(uid: String): Flow<List<ChatMessage>> = dbRepository.allMessages(uid)
    fun getType(uid: String) = dbRepository.getType(uid)
    fun savedMessages(savedName: String): Flow<List<ChatMessage>> =
        dbRepository.getSavedMessage(savedName)

    fun getUniqueSaved() = dbRepository.getUniqueSaved()
    var isBlurred: MutableState<Boolean> = mutableStateOf(false)
    var savedName: MutableState<String> = mutableStateOf("")
    var isSaved: MutableState<Boolean> = mutableStateOf(false)
    var uid: MutableState<String> = mutableStateOf("")
    val allAccounts: Flow<List<Accounts>>
    val datastore = UserDatastore(application.applicationContext)

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    // StateFlow to manage image loading state
    private val _imageState = MutableStateFlow<ImageState>(ImageState.NotStarted)
    val imageState: StateFlow<ImageState> = _imageState.asStateFlow()

    // StateFlow to manage image loading state
    private val _devices = MutableStateFlow<Collection?>(null)
    val devices: StateFlow<Collection?> = _devices.asStateFlow()

    private val _isValidQR = MutableStateFlow<Boolean>(false)
    val validQR: StateFlow<Boolean> = _isValidQR.asStateFlow()

    private val _isRegistered = MutableStateFlow<Boolean>(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered.asStateFlow()

    private val _isConnected = MutableStateFlow<Boolean>(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    init {
        viewModelScope.launch {
            datastore.getUID.collectLatest {
                uid.value = it
            }
        }
        allAccounts = dbRepository.allAccounts
    }


    fun fetchImage(prompt: String) {
        _imageState.value = ImageState.Loading
        viewModelScope.launch {
            try {
                val imageData = repository.textToImage(
                    ImageFromText(
                        inputs = prompt
                    )
                )
                if (imageData != null) {
                    _imageState.value = ImageState.Loaded(imageData)
                    Log.i("Imagesssssssss", _imageState.value.toString())
                } else {
                    _imageState.value = ImageState.Error(Exception("Something went wrong."))
                }
            } catch (e: Exception) {
                _imageState.value = ImageState.Error(e)
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun addAccount(
        firstName: String,
        lastName: String,
        email: String,
        photoUrl: String,
        uniqueId: String,
        isCurrent: Boolean = false,
        type: String
    ) {
        viewModelScope.launch {
            Log.i("Type", type)
            dbRepository.insertAccount(
                Accounts(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    photoUrl = photoUrl,
                    uniqueId = uniqueId,
                    isCurrent = isCurrent,
                    type = type
                )
            )

            createCollection(email, firstName)
        }
    }


    fun getApiData() {
        viewModelScope.launch {
            Log.i("Messages API Called Before", listOfMessages.value.toString())
            delay(1000)
            apiData = mutableStateOf(
                repository.getApiData(
                    ApiPrompt(
                        prompt = Prompt(
                            text = message.value
                        )
                    )
                )
            )
            insertChat(
                ChatMessage(
                    time = System.currentTimeMillis(),
                    message = apiData.value?.candidates?.get(0)?.output ?: "Something went wrong.",
                    isUser = false,
                    uID = uid.value,
                    name = if (isSaved.value) savedName.value else "New Chat",
                )
            )
            Log.i("Messages API Called", listOfMessages.value.toString())
        }
    }


    fun insertChat(chatMessage: ChatMessage) {
        viewModelScope.launch {
            dbRepository.insertMessage(chatMessage)
        }
    }

    fun updateCurrentAccount(isCurrent: Boolean, uniqueId: String) {
        viewModelScope.launch {
            dbRepository.updateCurrentAccount(isCurrent, uniqueId)
        }
    }

    fun removeCurrentAccount() {
        viewModelScope.launch {
            dbRepository.removeCurrentAccount()
        }
    }

    fun saveChatMessage(name: String, previousName: String = "New Chat", isPined: Boolean) {
        viewModelScope.launch {
            dbRepository.saveChatMessage(name, previousName, isPined)
        }
    }

    fun deleteChat(time: Long) {
        viewModelScope.launch {
            dbRepository.deleteMessage(time)
        }
    }

    fun getDevices(email: String) {
        viewModelScope.launch {
            try {
                val client = setUpClient()

                val databases = Databases(client)

                val response = databases.getCollection(
                    databaseId = "logged-in",
                    collectionId = email.substringBefore("@")
                )
                _devices.value = response


            } catch (e: Exception) {
                Log.i("Appwrite Exception", e.toString())
            }
        }
    }

    fun validateQr(data: String) {
        viewModelScope.launch {
            try {
                val client = setUpClient()
                val databases = Databases(client)
                val response = databases.listCollections(
                    databaseId = "generated-qr",
                )

                response.collections.forEach {
                    Log.i("Appwrite Validation: data", data)
                    Log.i("Appwrite Validation: name", it.id)
                    if (it.id == data) {
                        Log.i("Appwrite Validation", it.toString())
                        _isValidQR.value = true
                    }
                }
            } catch (e: Exception) {
                Log.i("Appwrite Exception", e.toString())
                _isValidQR.value = false
            }
        }
    }

    fun successfullyRegister(
        data: String,
        deleteCollectionID: String,
        userCollectionID: String,
        email: String,
        name: String,
        pfp: String
    ) {
        viewModelScope.launch {
            _isRegistered.value = true
            try {
                deleteCollection(deleteCollectionID)
                val client = setUpClient()
                val databases = Databases(client)
                delay(1000)
                val response = databases.createCollection(
                    databaseId = "mapping",
                    collectionId = data,
                    name = email.substringBefore("@"),
                )
                val emailResponse = databases.createEmailAttribute(
                    databaseId = "logged-in",
                    collectionId = userCollectionID,
                    key = "email",
                    required = false,
                )
                val pfpResponse = databases.createStringAttribute(
                    databaseId = "logged-in",
                    collectionId = userCollectionID,
                    key = "pfp",
                    required = false,
                    default = pfp,
                    size = 70
                )
                delay(1000)
                val nameResponse = databases.createStringAttribute(
                    databaseId = "logged-in",
                    collectionId = userCollectionID,
                    key = "name",
                    size = 40,
                    required = false,
                    default = name
                )
                val qrResponse = databases.createStringAttribute(
                    databaseId = "logged-in",
                    collectionId = userCollectionID,
                    key = "qrcode",
                    required = false,
                    default = data,
                    size = 30
                )
                val boolResponse = databases.createBooleanAttribute(
                    databaseId = "logged-in",
                    collectionId = userCollectionID,
                    key = "isSuccessful",
                    required = false,
                    default = true
                )

                _isRegistered.value = false
                _isConnected.value = true
            } catch (e: Exception) {
                Log.i("Appwrite Exception", e.toString())
            }
        }
    }

    private fun deleteCollection(collectionID: String, databaseID: String = "generated-qr"){
        viewModelScope.launch {
            try {
                val client = setUpClient()
                val databases = Databases(client)
                val response = databases.deleteCollection(
                    databaseId = databaseID,
                    collectionId = collectionID
                )
            } catch (e: Exception) {
                Log.i("Appwrite Exception", e.toString())
            }
        }

    }

    private fun createCollection(email: String, name: String) {
        viewModelScope.launch {
            try {
                val client = setUpClient()
                val databases = Databases(client)
                val response = databases.createCollection(
                    databaseId = "logged-in",
                    collectionId = email.substringBefore("@"),
                    name = name,
                )
            } catch (e: Exception) {
                Log.i("Appwrite Exception", e.toString())
            }
        }
    }

    private fun setUpClient(): Client {
        return Client()
            .setEndpoint("https://cloud.appwrite.io/v1") // Your API Endpoint
            .setProject("65469b6cbb0b40e8a44f") // Your project ID
            .setKey(
                "3b14bdc3f720db0253fe51192d31dc31df2f7051976dcaa760a94f81eb68525b25bfca177936b1cde4" +
                        "d8ad6fd6f87911aebcbaeba9ec6d7ec837a304ebcb0c8471beedd950ca2b0cac452e17c70594f455538" +
                        "b770a967ee73d2db954592fd632e81eff2be3b06c2e01f19ea0aaa05adc78a60c228e1fdf9453ec240" +
                        "b956f8b36"
            )
    }

}

sealed class ImageState {
    object Loading : ImageState()
    data class Loaded(val imageData: ByteArray) : ImageState() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Loaded

            if (!imageData.contentEquals(other.imageData)) return false

            return true
        }

        override fun hashCode(): Int {
            return imageData.contentHashCode()
        }
    }

    data class Error(val exception: Exception) : ImageState()

    object NotStarted : ImageState()
}


