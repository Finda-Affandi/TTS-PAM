package com.example.project3activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project3activity.Firebase.AddUserDataToFirebase
import com.example.project3activity.models.UserModel
import com.example.project3activity.models.UserViewModel
import com.example.project3activity.ui.screens.Signup
import com.example.project3activity.ui.theme.Project3activityTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignupActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            Project3activityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

//                    Greeting2("Android")
                    Signup(btnOnClickAction = ::sendUsernameBackToLogin)
                }
            }
        }
    }


    fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
//            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show()
            throw InvalidEmailFormat("Invalid Email Format")
        }
    }


    private fun sendUsernameBackToLogin(username: String, password: String, firstname: String, lastname:String) {
        val errorToast = Toast.makeText(applicationContext, applicationContext.getResources().getString(R.string.user_created_failed), Toast.LENGTH_SHORT)

        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user : FirebaseUser = it.result.user!!
                    val addToFirebase = AddUserDataToFirebase()
                    addToFirebase.addDataToFirebase(
                        UserModel(user.uid, firstname, lastname),
                        { userModel ->
                            if (userModel!=null){
                                setResult(
                                    Activity.RESULT_OK,
                                    Intent().putExtra("username", username)
                                )
                                Toast.makeText(
                                    applicationContext, applicationContext.getResources().getString(R.string.user_created_success), Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                errorToast.show()
                            }
                        },
                        {
                            errorToast.show()
                        }
                    )
                }
                else {
                    errorToast.show();
                }
            }
    }
}