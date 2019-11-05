package com.saebyeol.weather

import android.util.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseJson(result: String): ArrayList<MainData> {

    val list = ArrayList<MainData>()
     val list2 = ArrayList<WeatherData>()

    // JSON 만들기.
    var strJson: String = result.toString()
    strJson = strJson.replace("null", "")

    // Json parser를 만들어 만들어진 문자열 데이터를 객체화
    val parser = JSONParser()
    Log.d("Parse Data", strJson)
    val obj: JSONObject = parser.parse(strJson) as JSONObject

    // response 키를 가지고 json 파싱 시작
    val parse_response: JSONObject = obj.get("response") as JSONObject
    val parse_body: JSONObject = parse_response.get("body") as JSONObject
    val parse_items: JSONObject = parse_body.get("items") as JSONObject

    val parse_item: JSONArray = parse_items.get("item") as JSONArray
    var category: String?
    var weather: JSONObject

    var day = ""
    var time = ""

    for (i in 0 until parse_item.size) {
        weather = parse_item.get(i) as JSONObject
        val fcstValue = weather.get("fcstValue")
        val fcstDate = weather.get("fcstDate")
        val fcstTime = weather.get("fcstTime")
        //double형으로 받고싶으면 아래내용 주석 해제
        //double fcstValue = Double.parseDouble(weather.get("fcstValue").toString());
        category = weather.get("category") as String


        // 출력
        if (day != fcstDate!!.toString()) {
            day = fcstDate.toString()
        }
        if (time != fcstTime!!.toString()) {
            time = fcstTime.toString()
            Log.d("Time", day + "   " + time)
        }
        Log.d("Category", category)
        Log.d("Fcst_value", fcstValue!!.toString())
        Log.d("FcstDate", fcstDate!!.toString())
        Log.d("FcstTime", fcstTime!!.toString())

        if (category == "SKY" || category == "PTY") {
            category = ctySel(category, fcstValue.toString())
        } else {
            category = setCast(category)
        }
        var nextDate: String?
        nextDate = (Integer.parseInt(getBaseDate()) + 1).toString()

        if (category!! != "NoValue") {
            if (day == getBaseDate() || nextDate == day) {
                list.add(MainData(category, fcstValue!!.toString(), day, time))
            }
            if (category == "최저기온" || category == "최고기온"){
                list2.add(WeatherData(category, fcstValue!!.toString()))
            }

//            if (day == getBaseDate()) {
//                Log.d("Test 1", getBaseDate() + " " + fcstDate.toString() + " " + category)
//                list.add(
//                    MainData(category, fcstValue!!.toString(), day, time)
//                )
//            }
//            if () {
//                Log.d("Test 2", getBaseDate() + " " + fcstDate + " " + category)
//                list.add(MainData(category, fcstValue!!.toString(), day, time))
//            }
        }
    }
    for (i in 0 until list2.size){
        Log.d("Value!", list2.get(i).strValue + " "+ list2.get(i).strCat)
    }

    return list
}

fun getBaseDate(): String {
    var nYear: String
    var nMonth: String
    var nDay: String
    val calendar: Calendar = GregorianCalendar(Locale.KOREA)
    val numberFormat: NumberFormat = NumberFormat.getNumberInstance()
    numberFormat.minimumIntegerDigits = 2

    nYear = calendar.get(Calendar.YEAR).toString()

    nMonth = (calendar.get(Calendar.MONTH) + 1).toString()

    if (Integer.parseInt(nMonth) < 10) {
        nMonth = numberFormat.format(Integer.parseInt(nMonth))
    }

    nDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
    if (Integer.parseInt(nDay) < 10) {
        nDay = numberFormat.format(Integer.parseInt(nDay))
    }

    val date: String = nYear + nMonth + nDay
    Log.d("Date! ", date)
    return date
}

private var calBase: Calendar? = null
private var hour: Int? = null
private var lastBaseTime: Int? = null

fun WeatherFetcher() {
    calBase = Calendar.getInstance() // 현재시간 가져옴
    calBase!!.set(Calendar.MINUTE, 0) // 분, 초 필요없음
    calBase!!.set(Calendar.SECOND, 0)
    hour = calBase!!.get(Calendar.HOUR_OF_DAY)
    lastBaseTime = getLastBaseTime(hour!!)
}

fun getLastBaseTime(t: Int): Int {
    if (t >= 0) {
        if (t < 2) {
            calBase!!.add(Calendar.DATE, -1)
            calBase!!.set(Calendar.HOUR_OF_DAY, 23)
            return 23
        } else {
            calBase!!.set(Calendar.HOUR_OF_DAY, t - (t + 1) % 3)
            return t - (t + 1) % 3
        }
    } else
        return -1
}

fun getBaseTime(): String {
    if ((lastBaseTime!! / 10) > 0) // 두자리수이면
        return lastBaseTime!!.toString() + "00"
    else // 한자리수이면
        return "0" + lastBaseTime!!.toString() + "00"
}
