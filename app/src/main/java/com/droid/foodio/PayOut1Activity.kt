package com.droid.foodio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.droid.foodio.databinding.ActivityPayOut1Binding
import com.droid.foodio.utils.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.PaymentResultListener
import kotlin.math.roundToInt

class PayOut1Activity : AppCompatActivity(), PaymentResultListener {
    lateinit var binding: ActivityPayOut1Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phoneNo: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemQuantity: ArrayList<Int>
    private lateinit var foodItemImage: ArrayList<String>
    private lateinit var foodItemDescription: ArrayList<String>
    private lateinit var foodItemIngredients: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOut1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Set user data
        setUserData()

        val intent=intent
        foodItemName = intent.getStringArrayListExtra("foodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") as ArrayList<String>
        foodItemQuantity = intent.getIntegerArrayListExtra("foodItemQuantity") as ArrayList<Int>
        foodItemImage = intent.getStringArrayListExtra("foodItemImage") as ArrayList<String>
        foodItemDescription = intent.getStringArrayListExtra("foodItemDescription") as ArrayList<String>
        foodItemIngredients = intent.getStringArrayListExtra("foodItemIngredient") as ArrayList<String>

        totalAmount=calculateTotalAmount()
        binding.price.isEnabled=false
        binding.price.setText(totalAmount)

        binding.placeOrderButton.setOnClickListener {

             name = binding.Name.text.toString().trim()
             address = binding.Address.text.toString().trim()
             phoneNo = binding.phoneNo.text.toString().trim()

            if (name.isEmpty() || address.isEmpty() || phoneNo.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

            }
            else{
                val amount = totalAmount.replace("$", "").trim().toFloatOrNull() ?: 0f
                savePayments(amount)
            }

        }

        binding.BackButton.setOnClickListener{
            finish()
        }
    }


    private fun placeOrder() {
        userId=auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(userId,name,foodItemName,foodItemImage,foodItemPrice,foodItemQuantity,address,totalAmount,phoneNo, false,false ,itemPushKey,time)
        val orderDetailsReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderDetailsReference.setValue(orderDetails).addOnSuccessListener {
            Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")

            // remove item from cart
            removeItemFromCart()

            // add order to history
            addOrderToHistory(orderDetails)

        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("User").child(userId).child("BuyHistory").child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {
                Toast.makeText(this, "Order added to history", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }


    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("User").child(userId).child("cartItems")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): String {
        var totalAmount = 0
        for (i in 0 until foodItemPrice.size) {

            val quantity = foodItemQuantity[i]
            val price = foodItemPrice[i].toInt()
            totalAmount += quantity * price
        }
        return "$ $totalAmount"

    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userReference = databaseReference.child("User").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names = snapshot.child("name").getValue(String::class.java) ?: ""
                        val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phones = snapshot.child("phoneNo").getValue(String::class.java) ?: ""

                        binding.apply {
                            Name.setText(names)
                            Address.setText(addresses)
                            phoneNo.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@PayOut1Activity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Stub for savePayments function (you should implement Razorpay payment integration here)
    private fun savePayments(amount: Float) {
        val checkout = com.razorpay.Checkout()
        checkout.setKeyID("rzp_live_7rk7sJYf7JnVOk") // Use test key while testing

        try {
            val amountValue = (amount * 100).roundToInt() // Razorpay needs amount in paise
            val options = org.json.JSONObject()
            options.put("name", "Foodio Order")
            options.put("description", "Delicious food at your doorstep")
            options.put("theme.color", "#1F4FE0")
            options.put("currency", "INR")
            options.put("amount", amountValue)

            val prefill = org.json.JSONObject()
            prefill.put("email", auth.currentUser?.email ?: "test@foodio.com")
            prefill.put("contact", phoneNo.ifEmpty { "9999999999" })
            options.put("prefill", prefill)

            com.razorpay.Checkout.preload(applicationContext)
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Payment Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Razorpay Payment Callbacks
    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_SHORT).show()
        placeOrder()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
    }

}

