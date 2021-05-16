package com.example.birdline.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.MessageAdapter
import com.example.birdline.models.Encriptacion
import com.example.birdline.models.EncryptionKeys
import com.example.birdline.models.Mensajes
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase = FirebaseFirestore.getInstance();

    private lateinit var _Mensaje:EditText
    private lateinit var _id: String

    //Encryption
    private var ENCRYPT = false

    // Location
    private var PERMISSION_ID = 52

    // Variables required
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        // Set Encryption
        val toggle:Switch = findViewById(R.id.switch1)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            ENCRYPT = isChecked
        }

        // Location
        // Initiate the fused...providerClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Add Event to button
        val getPos = findViewById<Button>(R.id.locationbtn)

        getPos.setOnClickListener {
            getLastLocation()
            // Coordinates to get city & country
        }

        // Choose File
        val getFile = findViewById<Button>(R.id.filebtn)

        getFile.setOnClickListener {
            startFileChooser()
        }

        auth = FirebaseAuth.getInstance()

        var ema = intent.getStringExtra("EMAIL")
        var uid = intent.getStringExtra("ID")
        var chatname = intent.getStringExtra("CHAT_NAME")

        var username: TextView = findViewById(R.id.textView9)
        username.text = chatname

        _Mensaje = findViewById(R.id.messageTextField)
        if (uid != null) {
            _id = uid
        }

        // Members
        val SpinnerMembers: Spinner = findViewById(R.id.spinner2)

        val postRef = firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id)
        postRef.get().addOnSuccessListener {
            var users = it.get("users") as List<String>

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
            SpinnerMembers.adapter = adapter
        }

        val btn_send: Button = findViewById(R.id.sendMessageButton)

        btn_send.setOnClickListener() {

            if(_Mensaje.text.toString() != "") {

                if (!ENCRYPT) {
                    sendMessage()
                } else {
                    sendEncryptMessage()
                }

            }else{
                Toast.makeText(this,"Write something", Toast.LENGTH_SHORT).show()
            }
        }

        val userRef = firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id)

        userRef.collection(ReferenciasFirebase.MENSAJES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { document ->
                    var listChats = document.toObjects(Mensajes::class.java)
                    var rv = findViewById<RecyclerView>(R.id.messagesRecyclerView)

                    rv.layoutManager   = LinearLayoutManager(this)
                    val adapter = MessageAdapter(this, listChats)
                    rv.adapter = adapter

                }
        userRef.collection(ReferenciasFirebase.MENSAJES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .addSnapshotListener(){
                    messages,error ->
                    if (error == null){
                        messages?.let { var listChats = it.toObjects(Mensajes::class.java)
                            var rv = findViewById<RecyclerView>(R.id.messagesRecyclerView)

                            rv.layoutManager   = LinearLayoutManager(this)
                            val adapter = MessageAdapter(this, listChats)
                            rv.adapter = adapter }
                    }
                }

    }

    private fun sendMessage() {
        val mensaje = _Mensaje.text.toString()
        val txt_mensaje_main:EditText = findViewById(R.id.messageTextField)

        val chat = Mensajes(
            message = mensaje,
            from = auth.currentUser.email

        )

        firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id).collection(ReferenciasFirebase.MENSAJES.toString()).document().set(chat)
        txt_mensaje_main.setText("")
    }

    private fun sendEncryptMessage() {
        val mensaje = _Mensaje.text.toString()
        val txt_mensaje_main:EditText = findViewById(R.id.messageTextField)

        var encryted = Encriptacion.cifrar(mensaje,EncryptionKeys.MENSAJES.toString())

        val chat = Mensajes(
                message = encryted,
                from = auth.currentUser.email,
                encrypted = true

        )

        firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id).collection(ReferenciasFirebase.MENSAJES.toString()).document().set(chat)
        txt_mensaje_main.setText("")
    }

    // Location
    // Function that will allow us to get the last location
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        // Check permission
        if(CheckPermission()){
            // Check the location service is enabled
            if(IsLocationEnabled()){
                // Get Location
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{task ->
                    var location: Location? = task.result
                    if(location == null){

                        // if the location is null we will get the new user location
                        getNewLocation()

                    }else{

                        // location.latitude will return the latitude coordinates
                        // location.longitude will return the longitude coordinates
                        _Mensaje.setText("Current Coordinates are :\nLat:" + location.latitude + " ;Long:"+ location.longitude+
                                "\nCity: " + getCityName(location.latitude,location.longitude) + ", Country: " + getCountryName(location.latitude,location.longitude) )
                    }

                }
            }else{
                Toast.makeText(this,"Please Enable your location service", Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }

    // Location null function
    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
            // locationCallback variable
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation : Location = p0.lastLocation
            // Set the new Location
            _Mensaje.setText("Your Current Coordinates are :\nLat:" + lastLocation.latitude + " ;Long:"+ lastLocation.longitude+
                    "\nCity: " + getCityName(lastLocation.latitude,lastLocation.longitude) + ", Country: " + getCountryName(lastLocation.latitude,lastLocation.longitude))

        }
    }

    // Function to get city name
    private fun getCityName(lat:Double,long:Double):String{
        var CityName = ""
        var geocoder = Geocoder(this, Locale.getDefault())
        var Adress: MutableList<Address>? = geocoder.getFromLocation(lat,long,1)

        CityName = Adress?.get(0)?.locality.toString()
        return CityName
    }

    // Function to get country name
    private fun getCountryName(lat:Double,long:Double):String{
        var CountryName = ""
        var geocoder = Geocoder(this, Locale.getDefault())
        var Adress: MutableList<Address>? = geocoder.getFromLocation(lat,long,1)

        CountryName = Adress?.get(0)?.countryName.toString()
        return CountryName
    }

    // Function that will check the uses permission
    private fun CheckPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false
    }

    // Function that will allow us to get user permission
    private fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_ID
        )
    }

    // Function that check if the location service of the device is enabled
    private fun IsLocationEnabled():Boolean{

        var locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // this is a buil in function that check the permission result
        // only for debugging our code
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You Have the Permission")
            }
        }
    }


    // Files
    private fun startFileChooser(){

    }
}