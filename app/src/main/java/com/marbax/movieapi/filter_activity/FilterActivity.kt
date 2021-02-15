package com.marbax.movieapi.filter_activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.marbax.movieapi.Const.KEY_DATE_FROM
import com.marbax.movieapi.Const.KEY_DATE_TO
import com.marbax.movieapi.Const.RESET_FILTER_CODE
import com.marbax.movieapi.R
import kotlinx.android.synthetic.main.activity_filter.*
import java.text.SimpleDateFormat
import java.util.*

class FilterActivity : AppCompatActivity() {

    private var dateFrom = ""
    private var dateTo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        supportActionBar?.apply {
            this.setDisplayShowHomeEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
        }

        dateFrom = intent.getStringExtra(KEY_DATE_FROM)!!
        dateTo = intent.getStringExtra(KEY_DATE_TO)!!

        if (isFieldsNotEmpty(dateFrom, dateTo)) {
            tvDateFrom.text = dateFrom
            tvDateTo.text = dateTo
        }

        setListeners()
        btnResetFilters.setOnClickListener {
            val result = Intent()
                .putExtra(KEY_DATE_FROM, "")
                .putExtra(KEY_DATE_TO, "")
            setResult(RESET_FILTER_CODE, result)
            finish()
        }
    }

    private fun isFieldsNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.isNotEmpty() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuFilterOk) {
            if (isFieldsNotEmpty(dateFrom, dateTo)) {
                val result = Intent()
                    .putExtra(KEY_DATE_FROM, dateFrom)
                    .putExtra(KEY_DATE_TO, dateTo)
                setResult(Activity.RESULT_OK, result)
                finish()
            } else {
                Snackbar.make(filterActivityLayout, "Fill all fields", Snackbar.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun getDate(date: String): Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        return if (date.isNotBlank()) {
            val cal = Calendar.getInstance()
            cal.time = format.parse(date)
            cal
        } else {
            Calendar.getInstance()
        }
    }

    private fun formatDateToString(year: Int, month: Int, dayOfMonth: Int): String {
        val monthStr = String.format("%02d", month + 1)
        val dayStr = String.format("%02d", dayOfMonth)
        return "${year}-${monthStr}-${dayStr}"
    }

    private fun setListeners() {
        tvDateFrom.setOnClickListener {
            val dateBegin = getDate(dateFrom)
            DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val date = formatDateToString(year, month, dayOfMonth)
                    dateFrom = date
                    tvDateFrom.text = date
                },
                dateBegin[Calendar.YEAR],
                dateBegin[Calendar.MONTH],
                dateBegin[Calendar.DAY_OF_MONTH]
            ).show()
        }

        tvDateTo.setOnClickListener {
            val dateBegin = getDate(dateTo)
            DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val date = formatDateToString(year, month, dayOfMonth)
                    dateTo = date
                    tvDateTo.text = date
                },
                dateBegin[Calendar.YEAR],
                dateBegin[Calendar.MONTH],
                dateBegin[Calendar.DAY_OF_MONTH]
            ).show()
        }
    }

}