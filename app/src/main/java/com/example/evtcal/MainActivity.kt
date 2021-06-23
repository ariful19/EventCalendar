package com.example.evtcal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : ComponentActivity() {
    @ExperimentalUnitApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val orderDateList = listOf(OrdDate(2021, 6, 1), OrdDate(2021, 6, 7), OrdDate(2021, 6, 10))
            EventCalendar(orderDateList) {}
        }
    }

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    @Preview
    @Composable
    fun MainPreview(){
        val orderDateList = listOf(OrdDate(2021, 6, 1), OrdDate(2021, 6, 7), OrdDate(2021, 6, 10))
        EventCalendar(orderDateList) {}
    }

    @ExperimentalUnitApi
    @ExperimentalAnimationApi
    @Composable
    fun EventCalendar(dtList: List<OrdDate>, onDateChange: (OrdDate) -> Unit) {
        val list by remember { mutableStateOf(dtList) }
        val cal = remember {
            mutableStateOf(Calendar.getInstance())
        }
        var currentDateText by remember {
            mutableStateOf("")
        }
        val h = 30.dp
        currentDateText = getCurrentDate(cal.value)
        var isCal by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(5.dp)
                .border(
                    1.dp, Color(239, 239, 239),
                    RoundedCornerShape(5.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(currentDateText, color = Color(136, 200, 236))
                Row(modifier = Modifier.height(h)) {
                    IconButton(onClick = {
                        cal.value.add(Calendar.MONTH, -1)
                        cal.value = cal.value.clone() as Calendar
                        currentDateText = getCurrentDate(cal.value)
                        onDateChange(
                            OrdDate(
                                cal.value.get(Calendar.YEAR),
                                cal.value.get(Calendar.MONTH) + 1,
                                cal.value.get(Calendar.DATE)
                            )
                        )
                        isCal = false
                        Timer("tmr").schedule(300) {
                            isCal = true
                        }
                    }, modifier = Modifier.height(h)) {
                        Icon(Icons.Default.ArrowBack, "", tint = Color(149, 149, 149))
                    }
                    IconButton(onClick = {
                        cal.value.add(Calendar.MONTH, 1)
                        cal.value = cal.value.clone() as Calendar
                        currentDateText = getCurrentDate(cal.value)
                        onDateChange(
                            OrdDate(
                                cal.value.get(Calendar.YEAR),
                                cal.value.get(Calendar.MONTH) + 1,
                                cal.value.get(Calendar.DATE)
                            )
                        )
                        isCal = false
                        Timer("tmr").schedule(300) {
                            isCal = true
                        }
                    }, modifier = Modifier.height(h)) {
                        Icon(Icons.Default.ArrowForward, "", tint = Color(149, 149, 149))
                    }
                }
            }
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .background(Color(239, 239, 239))
                    .fillMaxWidth()
            )
            if (isCal) {
                CalenderBody(cal, list) {
                    currentDateText = getCurrentDate(cal.value)
                    onDateChange(
                        OrdDate(
                            cal.value.get(Calendar.YEAR),
                            cal.value.get(Calendar.MONTH) + 1,
                            cal.value.get(Calendar.DATE)
                        )
                    )
                }
            }
        }
    }

    private fun getCurrentDate(it: Calendar): String {
        val arr = listOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )
        val arrM = listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "Jun",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
        val d = it.get(Calendar.DAY_OF_WEEK)
        val td = arr[d - 1]

        val m = it.get(Calendar.MONTH)
        val tm = arrM[m]
        val y = it.get(Calendar.YEAR)
        val dt = it.get(Calendar.DATE)
        return "$td, $dt $tm, $y"
    }

    //@Preview
    @ExperimentalUnitApi
    @Composable
    fun CalenderBody(cal: MutableState<Calendar>, list: List<OrdDate>, onChange: () -> Unit) {

        val startDate = Calendar.getInstance()
        startDate.set(cal.value.get(Calendar.YEAR), cal.value.get(Calendar.MONTH), 1)

        val lastDate = startDate.getMaximum(Calendar.DAY_OF_MONTH)

        val dayOw = startDate.get(Calendar.DAY_OF_WEEK)
        var i = 2 - dayOw
        var selectedDate by remember { mutableStateOf(cal.value.get(Calendar.DATE)) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)

        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Su", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Mo", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Tu", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "We", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Th", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Fr", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Sa", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
            (1..6).forEach { _ ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    (1..7).forEach { _ ->
                        val selected = i > 0 && i == selectedDate
                        val month = cal.value.get(Calendar.MONTH) + 1
                        val year = cal.value.get(Calendar.YEAR)
                        val dDate = i
                        val ho =
                            list.find { o -> o.m == month && o.y == year && o.d == dDate } != null
                        DateBox(
                            i++,
                            lastDate,
                            selected = selected,
                            hasOrders = ho,
                            modifier = Modifier.weight(1f)
                        ) { selDate ->
                            selectedDate = selDate
                            cal.value.set(Calendar.DATE, selDate)
                            onChange()
                        }
                    }
                }
            }
        }
    }


    @ExperimentalUnitApi
    @Composable
    fun DateBox(
        date: Int,
        lastDate: Int = 31,
        selected: Boolean = false,
        hasOrders: Boolean = false,
        modifier: Modifier,
        onClick: (Int) -> Unit
    ) {
        val backColor = if (selected) Color(239, 248, 252) else if (!selected && hasOrders) Color(
            246,
            246,
            246
        ) else Color.Transparent
        val txtColor = if (selected) Color(74, 179, 239) else Color(184, 184, 184, 255)
        val dtt = if (date > 0 && date < lastDate + 1) date.toString().padStart(2, '0') else ""
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(color = backColor)
                .clickable { onClick(date) },

            ) {
            Row(modifier = Modifier.padding(vertical = 5.dp)) {
                Text(
                    dtt,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = txtColor
                )
            }
            if (hasOrders)
                Text("Orders", color = txtColor, fontSize = TextUnit(2f, TextUnitType.Em))
        }
    }

    @ExperimentalUnitApi
    @Preview
    @Composable
    fun DateBoxPrev() {
        DateBox(1, 31, selected = true, hasOrders = false, modifier = Modifier.fillMaxWidth()) {}
    }
}

class OrdDate(var y: Int, var m: Int, var d: Int)