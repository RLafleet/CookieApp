package com.example.cookieapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopAdapter(private val onBuyClick: (ShopItem) -> Unit) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    private var items: List<ShopItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ShopItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.nameText)
        private val costText: TextView = view.findViewById(R.id.costText)
        private val buyButton: Button = view.findViewById(R.id.buyButton)

        @SuppressLint("SetTextI18n")
        fun bind(item: ShopItem) {
            nameText.text = "${item.name} (Lv. ${item.currentLevel}/${item.maxLevel})"
            costText.text = "Cost: ${item.cost}"

            if (item.currentLevel < item.maxLevel) {
                buyButton.isEnabled = true
                buyButton.text = "Buy"
                buyButton.setBackgroundResource(R.drawable.button_active)
            } else {
                buyButton.isEnabled = false
                buyButton.text = "Max Level"
                buyButton.setBackgroundResource(R.drawable.button_disabled)
            }

            buyButton.setOnClickListener { onBuyClick(item) }
        }
    }
}
