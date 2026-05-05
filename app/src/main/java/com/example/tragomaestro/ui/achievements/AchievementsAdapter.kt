package com.example.tragomaestro.ui.achievements

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tragomaestro.R
import com.example.tragomaestro.database.AchievementEntity
import com.example.tragomaestro.databinding.ItemAchievementBinding

class AchievementsAdapter(
    private var achievements: List<AchievementEntity> = emptyList()
) : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    fun submitList(newAchievements: List<AchievementEntity>) {
        achievements = newAchievements
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(achievements[position])
    }

    override fun getItemCount(): Int = achievements.size

    inner class AchievementViewHolder(
        private val binding: ItemAchievementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: AchievementEntity) {
            val context = binding.root.context
            val progressPercent = if (achievement.target == 0) {
                0
            } else {
                ((achievement.progress.toFloat() / achievement.target.toFloat()) * 100).toInt()
            }

            binding.tvAchievementTitle.text = achievement.title
            binding.tvAchievementDescription.text = achievement.description
            binding.progressAchievement.progress = progressPercent.coerceIn(0, 100)

            binding.ivAchievementIcon.setImageResource(getIcon(achievement.iconName))

            if (achievement.unlocked) {
                binding.itemAchievementRoot.setBackgroundResource(R.drawable.bg_achievement_item_unlocked)
                binding.itemAchievementRoot.alpha = 1f
                binding.ivUnlockedTrophy.alpha = 1f
                binding.progressAchievement.alpha = 0f
                binding.ivAchievementIcon.imageTintList =
                    ColorStateList.valueOf(context.getColor(R.color.tm_pink))
            } else {
                binding.itemAchievementRoot.setBackgroundResource(R.drawable.bg_achievement_item_locked)
                binding.itemAchievementRoot.alpha = 0.45f
                binding.ivUnlockedTrophy.alpha = 0f
                binding.progressAchievement.alpha = 1f
                binding.ivAchievementIcon.imageTintList =
                    ColorStateList.valueOf(context.getColor(android.R.color.white))
            }
        }

        private fun getIcon(iconName: String): Int {
            return when (iconName) {
                "beer" -> R.drawable.ic_beer
                "users" -> R.drawable.ic_users
                "trophy" -> R.drawable.ic_trophy
                "skull" -> R.drawable.ic_skull
                "zap" -> R.drawable.ic_zap
                "fire" -> R.drawable.ic_fire
                else -> R.drawable.ic_trophy
            }
        }
    }
}