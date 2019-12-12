package com.clover.celebrities.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clover.celebrities.MyCelebrityModel
import com.clover.celebrities.R
import com.clover.celebrities.fragments.home.HomeFragmentDirections


class CelebrityAdapter : RecyclerView.Adapter<CelebrityAdapter.ViewHolder>() {

    private var celebrityList = listOf<MyCelebrityModel>()

    fun updateCelebrityList(celebrityList: List<MyCelebrityModel>) {
        this.celebrityList = celebrityList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = celebrityList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val celebrity = celebrityList.get(position)

        holder.name.text = celebrity.name
        holder.birthDate.text = celebrity.birthDate ?: "Unknown"
        if(celebrity.imageUrl == null) {
            Glide.with(holder.context).load(R.drawable.ic_person_foreground).into(holder.picture)
        }else{
            Glide.with(holder.context).load(celebrity.imageUrl).into(holder.picture)
        }
        holder.cardView.setOnClickListener {
            val nextPage = HomeFragmentDirections.actionDestinationHomeToDestinationInfo()
            nextPage.celebrityUrl = celebrity.detailsUrl
            Navigation.findNavController(it).navigate(nextPage)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.tv_name)
        val picture: ImageView = itemView.findViewById(R.id.iv_picture)
        val birthDate: TextView = itemView.findViewById(R.id.tv_birthday)
        val context: Context = itemView.context
        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }


}