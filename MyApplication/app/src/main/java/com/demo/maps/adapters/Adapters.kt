package com.demo.maps.adapters

import android.R
import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.ForegroundColorSpan
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import androidx.core.content.ContextCompat
import com.demo.maps.data.SearchResult
import com.demo.maps.databinding.ItemsResultBinding

class Adapters(
    private val ctx: Context,
    private val onClick: (SearchResult) -> Unit
) : RecyclerView.Adapter<Adapters.VH>() {

    private val list = mutableListOf<SearchResult>()
    var highlightKeyword: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun submit(items: List<SearchResult>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemsResultBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos >= 0 && pos < list.size) onClick(list[pos])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemsResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = list[position]
        holder.binding.tvSub.text = item.subtitle

        if (highlightKeyword.isNotBlank()) {
            val titleLower = item.title.lowercase()
            val keyLower = highlightKeyword.lowercase()
            val idx = titleLower.indexOf(keyLower)
            if (idx >= 0) {
                val spannable = SpannableString(item.title)
                spannable.setSpan(StyleSpan(Typeface.BOLD), idx, idx + keyLower.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val color = ContextCompat.getColor(ctx, R.color.holo_blue_dark)
                spannable.setSpan(ForegroundColorSpan(color), idx, idx + keyLower.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.binding.tvTitle.text = spannable
            } else {
                holder.binding.tvTitle.text = item.title
            }
        } else {
            holder.binding.tvTitle.text = item.title
        }
    }

    override fun getItemCount(): Int = list.size
}
