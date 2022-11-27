package org.wit.valueGuitar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.guitar.activities.GuitarMapsActivity
import org.wit.valueGuitar.activities.Home
import org.wit.valueGuitar.adapters.GuitarAdapter
import org.wit.valueGuitar.adapters.GuitarListener
import org.wit.valueGuitar.databinding.ActivityValueGuitarBinding
import org.wit.valueGuitar.databinding.FragmentGuitarListBinding
import org.wit.valueGuitar.main.MainApp
import org.wit.valueGuitar.models.GuitarModel


class GuitarListFragment : Fragment(), GuitarListener {


    private var _fragBinding: FragmentGuitarListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = activity?.application as MainApp

       setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentGuitarListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_guitar)

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = GuitarAdapter(app.guitars.findAll(), this)

        loadGuitars()
        registerRefreshCallback()
        registerMapCallback()
        return root;
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GuitarListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(requireContext(), GuitarFragment::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(requireContext(), GuitarMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onGuitarClick(guitar: GuitarModel) {
        val launcherIntent = Intent(requireContext(), Home::class.java)
        launcherIntent.putExtra("guitar_edit", guitar)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    private fun loadGuitars() {
        showGuitars(app.guitars.findAll())
    }

    fun showGuitars (guitars: List<GuitarModel>) {
        fragBinding.recyclerView.adapter = GuitarAdapter(guitars, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }



}