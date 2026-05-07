package com.example.tragomaestro.ui.packs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tragomaestro.R
import com.example.tragomaestro.database.QuestionPackEntity
import com.example.tragomaestro.databinding.ItemQuestionPackBinding

class QuestionPacksAdapter(
    private val onPackClicked: (QuestionPackEntity) -> Unit,
    private val onEditClicked: (QuestionPackEntity) -> Unit
) : RecyclerView.Adapter<QuestionPacksAdapter.QuestionPackViewHolder>() {

    private var packs: List<QuestionPackEntity> = emptyList()

    fun submitList(newPacks: List<QuestionPackEntity>) {
        packs = newPacks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionPackViewHolder {
        val binding = ItemQuestionPackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionPackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionPackViewHolder, position: Int) {
        holder.bind(packs[position])
    }

    override fun getItemCount(): Int = packs.size

    inner class QuestionPackViewHolder(
        private val binding: ItemQuestionPackBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pack: QuestionPackEntity) {
            binding.tvPackName.text = pack.name
            binding.tvPackDescription.text = pack.description
            binding.tvPackType.text = if (pack.isCustom) {
                binding.root.context.getString(R.string.pack_type_custom)
            } else {
                binding.root.context.getString(R.string.pack_type_default)
            }

            binding.ivEditPack.visibility = if (pack.isCustom) View.VISIBLE else View.GONE

            if (pack.isSelected) {
                binding.itemPackRoot.setBackgroundResource(R.drawable.bg_pack_item_selected)
                binding.ivPackSelected.alpha = 1f
                binding.itemPackRoot.alpha = 1f
            } else {
                binding.itemPackRoot.setBackgroundResource(R.drawable.bg_pack_item_unselected)
                binding.ivPackSelected.alpha = 0f
                binding.itemPackRoot.alpha = 0.65f
            }

            binding.itemPackRoot.setOnClickListener {
                onPackClicked(pack)
            }

            binding.ivEditPack.setOnClickListener {
                onEditClicked(pack)
            }
        }
    }
}