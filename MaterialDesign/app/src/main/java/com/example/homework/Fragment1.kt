package com.example.homework

import android.os.Bundle

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.homework.databinding.Fragment1Binding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis


class Fragment1 : Fragment(R.layout.fragment_1) {
    private val viewBinding: Fragment1Binding by viewBinding()
    private val viewModel: Fragment1ViewModel by viewModels()
    private var cardAdapter: CardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300
        }
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300
        }

        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
        cardAdapter = CardAdapter { cardView, id ->
            val cardDetailsTransitionName = "cardDetails"
            val extras = FragmentNavigatorExtras(cardView to cardDetailsTransitionName)
            val directions = Fragment1Directions.actionFragment1ToFragmentCardDetails(id)
            findNavController().navigate(directions, extras)
        }

        initList()

        viewModel.cards.observe(viewLifecycleOwner, Observer { cards ->
            cardAdapter?.updateList(cards)
        })

        viewModel.getCards()

        showSnackBar()

    }


    private fun initList() {
        with(viewBinding.cardsList) {
            adapter = cardAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
                viewBinding.cardsList,
                "Соединение с сервером отсутствует, показаны сохранённые объекты",
                Snackbar.LENGTH_LONG
        )
                .setAction("повторить") {
                    Snackbar.make(
                            viewBinding.cardsList,
                            "Список обновлен",
                            Snackbar.LENGTH_SHORT
                    )
                            .show()
                }
                .setMaxInlineActionWidth(10)
                .show()
    }


    override fun onDestroyView() {
        cardAdapter = null
        super.onDestroyView()
    }


}