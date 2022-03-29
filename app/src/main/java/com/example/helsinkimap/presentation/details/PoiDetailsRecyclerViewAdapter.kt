package com.example.helsinkimap.presentation.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.helsinkimap.R
import com.example.helsinkimap.core.ext.loadImage
import com.example.helsinkimap.databinding.PoiImageBinding
import com.example.helsinkimap.specs.entity.ActivityImageLinkDto

class PoiDetailsRecyclerViewAdapter(
    context: Context
) : RecyclerView.Adapter<PoiDetailsRecyclerViewAdapter.PoiDetailsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var imageUrlList: List<ActivityImageLinkDto> = listOf()

    inner class PoiDetailsViewHolder(
        private val binding: PoiImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            with(binding) {
                imagePoi.loadImage(
                    imageUrl,
                    RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_not_found)
                )
            }
        }
    }

    fun setData(data: List<ActivityImageLinkDto>) {
        imageUrlList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoiDetailsViewHolder =
        PoiDetailsViewHolder(
            PoiImageBinding.inflate(
                inflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PoiDetailsViewHolder, position: Int) {
        holder.bind(imageUrlList[position].url)
    }

    override fun getItemCount(): Int = imageUrlList.size
}
