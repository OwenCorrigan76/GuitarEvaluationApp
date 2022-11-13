package org.wit.ValueGuitar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.ValueGuitar.models.GuitarModel
import org.wit.valueGuitar.databinding.CardGuitarBinding

/** This is the Listener Interface listening for onGuitarClick and binding to
the recycler view in card_guitar. This will enable us to click anywhere
in the card and in this case, get to another activity. */

interface GuitarListener {
    fun onGuitarClick(guitar: GuitarModel)
}

/** This is the Adapter Class. */
class GuitarAdapter constructor(
    private var guitars: List<GuitarModel>,
    private val listener: GuitarListener
) :
    RecyclerView.Adapter<GuitarAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGuitarBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val guitar = guitars[holder.adapterPosition]
        holder.bind(guitar, listener)
    }

    override fun getItemCount(): Int = guitars.size

    class MainHolder(private val binding: CardGuitarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /** We bind the object to the view to display in the card */
        fun bind(guitar: GuitarModel, listener: GuitarListener) {
         //   binding.value.text = ("€" + guitar.value.toDouble().toString())
            binding.valuation.text = ("€" + guitar.valuation.toDouble().toString())
            binding.guitarMake.text = guitar.guitarMake
            binding.guitarModel.text = guitar.guitarModel
            binding.root.setOnClickListener { listener.onGuitarClick(guitar) }
        }
    }
}