package com.example.pogo_framework.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.pogo_framework.R
import com.example.pogo_framework.helpers.CommonHelpers
import com.example.pogo_framework.helpers.Resource
import com.example.pogo_framework.helpers.Status
import com.example.pogo_framework.models.CapturedModel
import com.example.pogo_framework.models.Friends
import com.example.pogo_framework.models.PokemonData
import com.example.pogo_framework.view_models.CommonViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_pokemon_detail_view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


enum class DetailType {
    CAPTURE, CAPTURED_BY_OTHER, CAPTURED
}

@RequiresApi(Build.VERSION_CODES.O)
class PokemonDetailView : AppCompatActivity() {
    private var detailType = DetailType.CAPTURED
    private var pokemonId = -1
    private var data: PokemonData? = null
    private var capturedByOtherModel: Friends? = null
    private var capturedModel: CapturedModel? = null
    lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(R.layout.activity_pokemon_detail_view)
        captured_map.onCreate(null)
        detailType = intent.getSerializableExtra(DETAIL_TYPE) as DetailType
        pokemonId = intent.getIntExtra(POKEMON_ID, -1)
        capturedByOtherModel =
            intent.getSerializableExtra(CAPTURED_BY_OTHER_MODEL_EXTRA) as Friends?
        capturedModel = intent.getSerializableExtra(CAPTURED_MODEL_EXTRA) as CapturedModel?
        if (pokemonId == -1 && capturedModel == null && capturedByOtherModel == null) {
            finish()
        }
        commonViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
                .create(CommonViewModel::class.java)
        attachObservers()
    }

    private fun attachObservers() {
        if (pokemonId >= 0) {
            commonViewModel.getPokemon(pokemonId).observe(this, Observer {
                setResult(it)
            })
        } else if (capturedByOtherModel != null) {
            capturedByOtherModel?.pokemon?.name?.let {
                commonViewModel.getPokemon(it).observe(this, Observer {
                    setResult(it)
                })
            }
        } else {
            capturedModel?.name?.let {
                commonViewModel.getPokemon(it).observe(this, Observer {
                    setResult(it)
                })
            }
        }

    }

    private fun setResult(it: Resource<PokemonData>?) {
        if (it?.status == Status.LOADING) {
            showLoader()
        } else {
            hideLoader()
        }
        if (it == null && it?.message.isNullOrEmpty().not()) {
            Toast.makeText(
                this,
                it?.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        } else {
            this.data = it?.data
            initUi()
        }
    }

    private fun initUi() {
        Glide.with(this).load(data?.sprites?.frontDefault).into(pokemon_image_front)
        Glide.with(this).load(data?.sprites?.backDefault).into(pokemon_image_back)
        collapsing_toolbar.title = data?.name
        toolbar.title = data?.name
        var moves = ""
        data?.moves?.forEachIndexed { index, it ->
            if (index < 5) {
                moves += it.move.name + ": 10/10 \n"
            }
        }
        tv_moves.text = moves

        var pokemonType = ""
        data?.types?.forEach {
            pokemonType += it.type.name + ","
        }
        if (pokemonType.endsWith(",")) {
            pokemonType = pokemonType.substring(0, pokemonType.length - 1)
        }
        pokemon_type.text = pokemonType
        if (detailType != DetailType.CAPTURE) {
            setCapturedInView()
            capture_on_title.text = "Captured on : "
            capturedByOtherModel?.pokemon?.capturedAt?.let {
                tv_captured_on.text = CommonHelpers.getRelativeTime(it)
            }
            capturedModel?.capturedAt?.let {
                try {
                    val date = it.split("T")[0]
                    val localeDate = LocalDate.parse(date)

                    tv_captured_on.text =
                        CommonHelpers.getFormattedDate(localeDate.toString()).toString()
                } catch (e: Exception) {
                }
            }
        } else {
            capture_on_title.text = "Capture on :"
            val sdf: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy");
            sdf.format(Date(System.currentTimeMillis()))
            tv_captured_on.text = sdf.format(Date(System.currentTimeMillis()))

        }


        if (detailType == DetailType.CAPTURED_BY_OTHER) {
            setCapturedByView()
        }

        capture_btn.setOnClickListener {
            Toast.makeText(this, "doubts", Toast.LENGTH_LONG).show()
        }
    }

    private fun setCapturedInView() {
        ll_captured_in.visibility = View.VISIBLE

        if (detailType == DetailType.CAPTURED) {
            tv_captured_in.text = "Captured in"
        } else {
            tv_captured_in.text = "Found in"
        }

        captured_map?.getMapAsync {
            capturedByOtherModel?.pokemon?.let { pokemon ->
                val pokemonLocation = LatLng(pokemon.lat, pokemon.long)
                it.addMarker(MarkerOptions().position(pokemonLocation).title("Pokemon Location"))
                it.moveCamera(CameraUpdateFactory.newLatLng(pokemonLocation))
            }
            capturedModel?.let { pokemon ->
                val pokemonLocation = LatLng(pokemon.capturedLatAt, pokemon.capturedLongAt)
                it.addMarker(MarkerOptions().position(pokemonLocation).title("Pokemon Location"))
                it.moveCamera(CameraUpdateFactory.newLatLng(pokemonLocation))
            }
            it.animateCamera(CameraUpdateFactory.zoomTo(9f), 2000, null)
        }

    }

    private fun setCapturedByView() {
        ll_captured_by.visibility = View.VISIBLE
        tv_captured_by.text = "Captured by : ${capturedModel?.name}"
    }

    private fun showLoader() {
        progress_layout.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progress_layout.visibility = View.GONE
    }

    companion object {
        const val DETAIL_TYPE = "detail_type"
        const val POKEMON_ID = "pokemon_id"
        const val CAPTURED_MODEL_EXTRA = "captured_model"
        const val CAPTURED_BY_OTHER_MODEL_EXTRA = "captured_by_other_model"

        fun startActivity(
            context: Activity,
            detailType: DetailType,
            pokemonId: Int,
            capturedByOtherModel: Friends? = null,
            capturedModel: CapturedModel? = null,
            transitionView: View? = null
        ) {
            val intent = Intent(context, PokemonDetailView::class.java)
            intent.putExtra(DETAIL_TYPE, detailType)
            intent.putExtra(POKEMON_ID, pokemonId)
            intent.putExtra(CAPTURED_MODEL_EXTRA, capturedModel)
            intent.putExtra(CAPTURED_BY_OTHER_MODEL_EXTRA, capturedByOtherModel)

            transitionView?.let {
                val transitionName: String =
                    context.resources.getString(R.string.str_pokemon_image_transition)

                val options: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context,
                        transitionView,  // Starting view
                        transitionName // The String
                    )
                ActivityCompat.startActivity(context, intent, options.toBundle());
            } ?: context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            captured_map.onResume()
        } catch (e: Exception) {
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            captured_map.onStart()
        } catch (e: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            captured_map.onStop()
        } catch (e: Exception) {
        }
    }

    override fun onPause() {
        try {
            captured_map?.onPause()
        } catch (e: Exception) {
        }
        super.onPause()
    }

    override fun onDestroy() {
        try {
            captured_map.onDestroy()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        try {
            captured_map.onLowMemory()
        } catch (e: Exception) {
        }
    }
}