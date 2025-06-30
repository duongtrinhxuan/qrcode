package com.example.qrcodescanner.ui.ui.adapters

import android.app.AlertDialog
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
            onClicks(qrResult, position)
        }

        private fun onClicks(qrResult: QrResult, position: Int) {
            view.setOnClickListener{
                resultDialog.show(qrResult)
            }
            view.setOnLongClickListener{
                showDeleteDialog(qrResult, position)
                true
            }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this result?")
                .setPositiveButton("Delete") { dialog, which ->
                    deleteThisRecord(qrResult, position)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            qrResult.id?.let { id ->
                dbHelperI.deleteQrResult(id)
                listOfScannedResults.removeAt(position)
                notifyItemRemoved(position)
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