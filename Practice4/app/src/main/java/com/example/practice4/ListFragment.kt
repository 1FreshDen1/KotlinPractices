package com.example.practice4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

// Фрагмент для отображения списка дат в RecyclerView
class ListFragment : Fragment() {

    // RecyclerView для отображения списка дат
    private lateinit var recyclerView: RecyclerView

    // Адаптер для работы с RecyclerView
    private lateinit var adapter: DateAdapter

    // Метод для создания и настройки интерфейса фрагмента
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейтинг макета фрагмента и получение ссылки на корневой View
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        // Инициализация RecyclerView, который будет отображать список дат
        recyclerView = view.findViewById(R.id.recyclerView)

        // Загрузка и отображение списка дат
        loadDates()

        return view
    }

    // Метод для загрузки дат из файла и обновления RecyclerView
    private fun loadDates() {
        // Получение директории, где хранятся фотографии и файл с датами
        val photoDir = File(requireContext().filesDir, "photos")
        val file = File(photoDir, "date.txt")

        // Чтение дат из файла, если файл существует
        val dates = if (file.exists()) {
            // Чтение строк из файла и сортировка их в обратном порядке (от новых к старым)
            file.readLines().sortedDescending()
        } else {
            // Если файл не существует, возвращаем пустой список
            emptyList()
        }

        // Создание адаптера для RecyclerView с загруженными датами
        adapter = DateAdapter(dates)

        // Установка адаптера и менеджера компоновки для RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
