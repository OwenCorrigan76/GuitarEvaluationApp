package org.wit.ValueGuitar.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.databinding.CardGuitarBinding


class GuitarAdapter constructor(private var guitars: List<GuitarModel>) :
    RecyclerView.Adapter<GuitarAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGuitarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val guitar = guitars[holder.adapterPosition]
        holder.bind(guitar)
    }

    override fun getItemCount(): Int = guitars.size

    class MainHolder(private val binding : CardGuitarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guitar: GuitarModel) {
            binding.value.text  = ("€" +  guitar.value.toDouble().toString())
            binding.guitarMake.text = guitar.guitarMake
            binding.guitarModel.text = guitar.guitarModel
        }
    }
}
