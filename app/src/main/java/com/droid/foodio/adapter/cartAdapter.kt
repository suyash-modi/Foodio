package com.droid.foodio.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.foodio.databinding.CardRevBinding


class cartAdapter(private val cartItems:MutableList<String>, private val cartItemPrices: MutableList<String>, private val itemImages:MutableList<Int>):RecyclerView.Adapter<cartAdapter.cartViewHolder>() {

    private val itemQuantities=IntArray(cartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val binding=CardRevBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return cartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class cartViewHolder(private val binding: CardRevBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int)
        {
            binding.apply {
                val quantitys=itemQuantities[position]
                cartName.text=cartItems[position]
                cartItemPrice.text=cartItemPrices[position]
                cartImage.setImageResource(itemImages[position])
                quantity.text=quantitys.toString()

                minusButton.setOnClickListener{
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener{

                    val itemPos=adapterPosition
                    if(itemPos!=RecyclerView.NO_POSITION)
                    deleteItem(itemPos)
                }

            }
        }

        private fun decreaseQuantity(position: Int)
        {
            if(itemQuantities[position]>1)
            {
                itemQuantities[position]--;
                binding.quantity.text= itemQuantities[position].toString()
            }
        }
        private fun increaseQuantity(position: Int)
        {
            if(itemQuantities[position]<10)
            {
                itemQuantities[position]++;
                binding.quantity.text= itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int)
        {
            cartItems.removeAt(position)
            itemImages.removeAt(position)
            cartItemPrices.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,cartItems.size)
        }

    }
}