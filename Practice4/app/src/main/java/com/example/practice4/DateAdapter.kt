package com.example.practice4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Адаптер для отображения списка дат в RecyclerView
class DateAdapter(private val dates: List<String>) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    // ViewHolder для хранения ссылки на элементы представления
    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Текстовое поле, которое будет отображать дату
        val textView: TextView = view.findViewById(R.id.textViewDate)
    }

    // Создание нового ViewHolder при необходимости (например, когда RecyclerView нуждается в новом элементе)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        // Инфлейтинг макета list_item для каждого элемента списка
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        // Создание и возврат нового ViewHolder с этим макетом
        return DateViewHolder(view)
    }

    // Привязка данных к элементу списка (ViewHolder)
    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        // Устанавливаем текст для TextView на основе позиции в списке
        holder.textView.text = dates[position]
    }

    // Возвращает общее количество элементов в списке
    override fun getItemCount() = dates.size
}
