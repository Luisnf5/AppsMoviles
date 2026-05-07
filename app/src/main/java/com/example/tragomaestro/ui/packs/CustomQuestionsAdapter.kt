package com.example.tragomaestro.ui.packs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tragomaestro.database.QuestionEntity
import com.example.tragomaestro.databinding.ItemCustomQuestionBinding

class CustomQuestionsAdapter(
    private val onDeleteClicked: (QuestionEntity) -> Unit
) : RecyclerView.Adapter<CustomQuestionsAdapter.CustomQuestionViewHolder>() {

    private var questions: List<QuestionEntity> = emptyList()

    fun submitList(newQuestions: List<QuestionEntity>) {
        questions = newQuestions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomQuestionViewHolder {
        val binding = ItemCustomQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CustomQuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomQuestionViewHolder, position: Int) {
        holder.bind(questions[position])
    }

    override fun getItemCount(): Int = questions.size

    inner class CustomQuestionViewHolder(
        private val binding: ItemCustomQuestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(question: QuestionEntity) {
            binding.tvCustomQuestionText.text = question.text
            binding.tvCustomQuestionAnswers.text =
                "A) ${question.optionA}\nB) ${question.optionB}\nC) ${question.optionC}\nD) ${question.optionD}"

            binding.btnDeleteQuestion.setOnClickListener {
                onDeleteClicked(question)
            }
        }
    }
}