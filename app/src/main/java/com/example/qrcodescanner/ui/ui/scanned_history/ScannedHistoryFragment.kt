package com.example.qrcodescanner.ui.ui.scanned_history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        fun newInstance(screenType : ResultListType): ScannedHistoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENT_RESULT_LIST_TYPE, screenType)
            val fragment = ScannedHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var resultType: ResultListType
    private lateinit var mView: View
    private lateinit var dbHelperI: DBHelperI
    private lateinit var scannedHistoryRecyclerView : RecyclerView
    private lateinit var noResultFound: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
    }

    private fun handleArguments() {
        resultType = arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE) as ResultListType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_scanned_history, container, false)
        scannedHistoryRecyclerView = mView.findViewById(R.id.scannedHistoryRecyclerView)
        noResultFound = mView.findViewById(R.id.noResultFound)
        init()
        showListOfResults()
        return mView
    }

    private fun showListOfResults() {
        when(resultType) {
            ResultListType.ALL_RESULT -> {
                showAllResults()
            }

            ResultListType.FAVORITE_RESULT -> {
                showFavoriteResults()
            }
        }
    }

    private fun showFavoriteResults() {
        var listOfFavouriteResult = dbHelperI.getAllFavouriteQrScannedResult()
        showResults(listOfFavouriteResult)
    }

    private fun showResults(listOfQrResults: List<QrResult>) {
        if(listOfQrResults.isEmpty()){
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

    }

    private fun showEmptyState() {
        scannedHistoryRecyclerView.gone()
        noResultFound.visible()
    }

    private fun showAllResults() {
        var listOfAllResult = dbHelperI.getAllQrScannedResult()
        showResults(listOfAllResult)
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(requireContext().applicationContext))
    }


}