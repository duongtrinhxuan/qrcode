package com.example.qrcodescanner.ui.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.db.DBHelperI
import com.example.qrcodescanner.ui.db.entities.QrResult
import com.example.qrcodescanner.ui.ui.dialogs.QrCodeResultDialog
import com.example.qrcodescanner.ui.ui.utils.gone
import com.example.qrcodescanner.ui.ui.utils.toFormattedDisplay
import com.example.qrcodescanner.ui.ui.utils.visible

class ScannedResultListAdapter(
    var dbHelperI: DBHelperI,
    var context: Context,
    var listOfScannedResults: MutableList<QrResult>
): RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>(){

    private var resultDialog = QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_single_item_qr_result, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listOfScannedResults.size
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResults[position], position)
    }
    inner class ScannedResultListViewHolder(var view : View): RecyclerView.ViewHolder(view) {
        private val result: TextView = view.findViewById(R.id.result)
        private val tvTime: TextView = view.findViewById(R.id.tvTime)
        private val favouriteIcon : ImageView = view.findViewById(R.id.favouriteIcon)
        fun bind(qrResult: QrResult, position: Int) {
            result.text = qrResult.result
            tvTime.text = qrResult.calendar.toFormattedDisplay()
            setFavourite(qrResult.favourite)
            onClicks(qrResult)
        }

        private fun onClicks(qrResult: QrResult) {
            view.setOnClickListener{
                resultDialog.show(qrResult)
            }
        }

        private fun setFavourite(favourite: Boolean) {
            if (favourite) {
                favouriteIcon.visible()
            } else {
                favouriteIcon.gone()
            }
        }
    }
}