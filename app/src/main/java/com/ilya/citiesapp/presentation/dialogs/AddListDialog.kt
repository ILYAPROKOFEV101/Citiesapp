package com.ilya.citiesapp.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ilya.citiesapp.R
import com.ilya.citiesapp.data.model.City
import com.ilya.citiesapp.data.model.CityList
import com.ilya.citiesapp.presentation.adapters.SelectCityAdapter

class AddCityListDialog(
    private val availableCities: List<City>,
    private val onSubmit: (CityList) -> Unit
) : DialogFragment() {

    private val selectedCities = mutableListOf<City>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_city_list_dialog, null)

        val shortInput = view.findViewById<EditText>(R.id.shortNameInput)
        val fullInput = view.findViewById<EditText>(R.id.fullNameInput)
        val colorSpinner = view.findViewById<Spinner>(R.id.colorSpinner)
        val cityRecycler = view.findViewById<RecyclerView>(R.id.selectableCitiesRecycler)


        val adapter = SelectCityAdapter(availableCities) { city, selected ->
            if (selected) {
                if (selectedCities.size < 5) selectedCities.add(city)
                else Toast.makeText(context, "Максимум 5 городов", Toast.LENGTH_SHORT).show()
            } else {
                selectedCities.remove(city)
            }
        }
        cityRecycler.adapter = adapter
        cityRecycler.layoutManager = LinearLayoutManager(requireContext())

        val colors = mapOf(
            "Зеленый" to "#4CAF50",
            "Синий" to "#2196F3",
            "Красный" to "#F44336",
            "Фиолетовый" to "#9C27B0",
            "Оранжевый" to "#FF9800",
            "Жёлтый" to "#FFEB3B",
            "Бирюзовый" to "#00BCD4",
            "Серый" to "#9E9E9E",
            "Чёрный" to "#000000"
        )
        colorSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            colors.keys.toList()
        )

        builder.setView(view)

        val dialog = builder.create()

        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            val short = shortInput.text.toString().trim()
            val full = fullInput.text.toString().trim()
            val color = colors[colorSpinner.selectedItem.toString()] ?: "#000000"

            if (short.isEmpty() || full.isEmpty() || selectedCities.isEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val list = CityList(short, full, color, selectedCities.toList())
            onSubmit(list)
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }
}
