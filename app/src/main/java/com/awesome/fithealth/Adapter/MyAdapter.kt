package com.awesome.fithealth.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.awesome.fithealth.Fragments.MealDetailFragment
import com.awesome.fithealth.R
import com.awesome.fithealth.ViewModel.ViewModel


class MyAdapter(private val list: ArrayList<ViewModel>, private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mealtext, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name: TextView = itemView.findViewById(R.id.mealName)
        val image: ImageView = itemView.findViewById(R.id.mealImage)
        val location:TextView=itemView.findViewById(R.id.mealLoction)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position, list[position])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.image.load(item.immgu)
        holder.location.text=item.locationFood
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: ViewModel)
    }
}
