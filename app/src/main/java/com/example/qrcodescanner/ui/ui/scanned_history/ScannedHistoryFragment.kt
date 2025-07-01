package com.example.qrcodescanner.ui.ui.scanned_history

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.db.DBHelper
import com.example.qrcodescanner.ui.db.DBHelperI
import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.db.entities.QrResult
import com.example.qrcodescanner.ui.ui.adapters.ScannedResultListAdapter
import com.example.qrcodescanner.ui.ui.utils.gone
import com.example.qrcodescanner.ui.ui.utils.visible

class ScannedHistoryFragment : Fragment() {
    enum class ResultListType {
        ALL_RESULT,
        FAVORITE_RESULT,
    }

    companion object {
        private const val ARGUMENT_RESULT_LIST_TYPE = "ArgumentResultListType"
        fun newInstance(screenType: ResultListType): ScannedHistoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENT_RESULT_LIST_TYPE, screenType)
            val fragment = ScannedHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var resultType: ResultListType
    private lateinit var dbHelperI: DBHelperI
    private lateinit var scannedHistoryRecyclerView: RecyclerView
    private lateinit var noResultFound: ImageView
    private lateinit var tvHeaderTextView: TextView
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var removeAll: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        resultType = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE, ResultListType::class.java)
                ?: ResultListType.ALL_RESULT
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE) as? ResultListType
                ?: ResultListType.ALL_RESULT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanned_history, container, false)
        scannedHistoryRecyclerView = view.findViewById(R.id.scannedHistoryRecyclerView)
        noResultFound = view.findViewById(R.id.noResultFound)
        tvHeaderTextView = view.findViewById(R.id.tvHeaderText)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        removeAll = view.findViewById(R.id.removeAll)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showListOfResults()
        setSwipeRefreshLayout()
        onClicks()
    }

    private fun onClicks() {
        removeAll.setOnClickListener {
            showRemoveAllScannedResultsDialog()
        }
    }

    private fun showRemoveAllScannedResultsDialog() {
        AlertDialog.Builder(context, R.style.CustomAlertDialog)
            .setTitle("Delete All")
            .setMessage("Are you sure you want to delete all result?")
            .setPositiveButton("Delete") { dialog, which ->
                clearRecords()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun clearRecords() {
        Thread {
            when(resultType) {
                ResultListType.ALL_RESULT -> dbHelperI.deleteAllScannedResults()
                ResultListType.FAVORITE_RESULT -> dbHelperI.deleteAllFavouriteQrScannerResults()
            }
            requireActivity().runOnUiThread {
                scannedHistoryRecyclerView?.adapter?.notifyDataSetChanged()
                showAllResults()
            }
        }.start()
    }

    private fun setSwipeRefreshLayout(){
        swipeRefresh.isRefreshing = false
        showListOfResults()
    }
    private fun init() {
        val ctx = context?.applicationContext
        if (ctx != null) {
            dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(ctx))
        }
    }

    private fun showListOfResults() {
        when (resultType) {
            ResultListType.ALL_RESULT -> showAllResults()
            ResultListType.FAVORITE_RESULT -> showFavoriteResults()
        }
    }

    private fun showAllResults() {
        Thread {
            val listOfAllResult = dbHelperI.getAllQrScannedResult()
            requireActivity().runOnUiThread {
                showResults(listOfAllResult)
                tvHeaderTextView.text = "Recent Scanned"
            }
        }.start()
    }

    private fun showFavoriteResults() {
        Thread {
            val listOfFavouriteResult = dbHelperI.getAllFavouriteQrScannedResult()
            requireActivity().runOnUiThread {
                showResults(listOfFavouriteResult)
                tvHeaderTextView.text = "Favourite Scanned Results"
            }
        }.start()
    }

    private fun showResults(listOfQrResults: List<QrResult>) {
        if (listOfQrResults.isEmpty()) {
            showEmptyState()
        } else {
            initRecyclerView(listOfQrResults)
        }
    }

    private fun initRecyclerView(listOfQrResults: List<QrResult>) {
        scannedHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        scannedHistoryRecyclerView.adapter = ScannedResultListAdapter(
            dbHelperI,
            requireContext(),
            listOfQrResults.toMutableList()
        )
        scannedHistoryRecyclerView.visible()
        noResultFound.gone()
        removeAll.visible()
    }

    private fun showEmptyState() {
        scannedHistoryRecyclerView.gone()
        noResultFound.visible()
        removeAll.gone()
    }
    private fun showRecyclerView() {
        scannedHistoryRecyclerView.visible()
        noResultFound.gone()
        removeAll.visible()
    }
}