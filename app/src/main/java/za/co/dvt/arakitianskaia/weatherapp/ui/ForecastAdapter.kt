package za.co.dvt.arakitianskaia.weatherapp.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.forecast_list_item.view.*
import za.co.dvt.arakitianskaia.weatherapp.R
import za.co.dvt.arakitianskaia.weatherapp.data.ForecastItemViewModel

class ForecastAdapter(val context: Context) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private var forecastList = mutableListOf<ForecastItemViewModel>()

    fun addForecast(list: List<ForecastItemViewModel>) {
        forecastList.clear()
        forecastList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_list_item, parent, false)
        return ForecastViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        forecastList[position].let {
            holder.bind(forecastElement = it)
        }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    class ForecastViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bind(forecastElement: ForecastItemViewModel) {
            itemView.txtDay.text = forecastElement.degreeDay
            itemView.txtDegrees.text = context.getString(R.string.degrees, forecastElement.maxTemp)

            itemView.icWeather.setImageResource(when (forecastElement.weather) {
                in 200..531 -> R.drawable.rain
                in 701..781 -> R.drawable.partlysunny
                800 -> R.drawable.clear
                in 801..804 -> R.drawable.partlysunny
                else -> R.drawable.partlysunny
            })
        }

    }

}