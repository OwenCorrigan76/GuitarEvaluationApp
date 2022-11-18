import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.wit.ValueGuitar.main.MainApp
import org.wit.valueGuitar.R
import org.wit.valueGuitar.databinding.FragmentGuitarListBinding

class GuitarListFragment : Fragment() {
    lateinit var app: MainApp

    private var _fragBinding: FragmentGuitarListBinding? = null
    private val fragBinding get() = _fragBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGuitarListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_addGuitar)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GuitarListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}
