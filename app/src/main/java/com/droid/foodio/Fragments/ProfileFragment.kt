package com.droid.foodio.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.droid.foodio.databinding.FragmentProfileBinding
import com.droid.foodio.utils.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)

        //set user data
        setUserData()

        binding.name.isEnabled=false
        binding.email.isEnabled=false
        binding.address.isEnabled=false
        binding.phoneNo.isEnabled=false

        binding.edtProfileBtn.setOnClickListener{
            binding.name.isEnabled=true
            binding.email.isEnabled=true
            binding.address.isEnabled=true
            binding.phoneNo.isEnabled=true
        }

        //update user data
        binding.button4.setOnClickListener{
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val phoneNo = binding.phoneNo.text.toString()

            updateUserData(name,email,address,phoneNo)

        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phoneNo: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userRef = database.getReference("User").child(userId)
            val userData = hashMapOf("name" to name , "email" to email , "address" to address , "phoneNo" to phoneNo)

            userRef.setValue(userData).addOnSuccessListener {
                binding.name.isEnabled=false
                binding.email.isEnabled=false
                binding.address.isEnabled=false
                binding.phoneNo.isEnabled=false
                Toast.makeText(requireContext(),"Profile Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Failed to Update Profile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        val userId=auth.currentUser?.uid

        if (userId != null)
        {
            val userRef = database.getReference("User").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                       val userProfile=snapshot.getValue(userModel::class.java)
                        if(userProfile!=null)
                        {
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.phoneNo.setText(userProfile.phoneNo)
                            binding.address.setText(userProfile.address)


                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }


}