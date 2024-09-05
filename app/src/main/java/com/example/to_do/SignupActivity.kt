package com.example.to_do

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {

            val email = binding.etSignupEmail.text.toString().trim()
            val password = binding.etSignupPassword.text.toString().trim()
            val confirmPassword = binding.etSignupConfirmPassword.text.toString().trim()
            val name = binding.etSignupName.text.toString().trim()
            val mobileNumber = binding.etSignupMobileNumber.text.toString().trim()

            if (validateInput(email, password, confirmPassword, name, mobileNumber)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Signup Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


        private fun validateInput(
        email: String,
        password: String,
        confirmPassword: String,
        name: String,
        mobileNumber: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidPassword(password)) {
            Toast.makeText(
                this,
                "Password must be at least 6 characters long, contain an uppercase letter, a number, and a special character",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidName(name)) {
            Toast.makeText(this, "Name must start with an uppercase letter", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isValidMobileNumber(mobileNumber)) {
            Toast.makeText(this, "Mobile number must be exactly 10 digits", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>]).{6,}\$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun isValidName(name: String): Boolean {
        val namePattern = "[A-Z].*"
        return name.matches(namePattern.toRegex())
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        val mobilePattern = "[0-9]{10}"
        return mobileNumber.matches(mobilePattern.toRegex())
    }
}
