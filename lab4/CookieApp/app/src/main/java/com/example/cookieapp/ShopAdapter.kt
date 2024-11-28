import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookieapp.R
import com.example.cookieapp.ShopItem

class ShopAdapter(
    private val onBuyClick: (ShopItem) -> Unit,
    private val getCurrentCookies: () -> Int
) : ListAdapter<ShopItem, ShopAdapter.ShopViewHolder>(ShopItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameText: TextView = view.findViewById(R.id.nameText)
        private val costText: TextView = view.findViewById(R.id.costText)
        private val itemCard: CardView = itemView.findViewById(R.id.itemCard)
        private val levelCountText: TextView = view.findViewById(R.id.levelCountText)
        private val imageView: ImageView = view.findViewById(R.id.itemImage)

        @SuppressLint("SetTextI18n")
        fun bind(item: ShopItem) {
            nameText.text = "${item.name} (Lv. ${item.currentLevel}/${item.maxLevel})"
            costText.text = "${item.cost}"
            levelCountText.text = "${item.currentLevel}"

            val currentCookies = getCurrentCookies()
            if (item.currentLevel > 0) {
                itemCard.setCardBackgroundColor(itemView.context.getColor(R.color.white))
                itemCard.isEnabled = false
            } else if (currentCookies >= item.cost) {
                itemCard.setCardBackgroundColor(itemView.context.getColor(R.color.white_availiable))
                itemCard.isEnabled = true
            } else {
                itemCard.setCardBackgroundColor(itemView.context.getColor(R.color.white_transparent))
                itemCard.isEnabled = false
            }

            imageView.setImageResource(item.imageResId)

            itemCard.isEnabled = item.currentLevel < item.maxLevel

            itemCard.setOnClickListener {
                if (item.currentLevel < item.maxLevel) {
                    val currentCookies = getCurrentCookies()
                    if (currentCookies >= item.cost) {
                        onBuyClick(item)
                    } else {
                        val missingCookies = item.cost - currentCookies
                        Toast.makeText(
                            itemView.context,
                            "Not enough cookies for upgrade: $missingCookies cookies for ${item.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        itemView.context,
                        "This upgrade ${item.name} has reached its maximum level",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}

class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}
