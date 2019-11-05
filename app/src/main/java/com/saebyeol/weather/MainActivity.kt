package com.saebyeol.weather

/*
    val = final 과 같은 변수 변경불가
    var = 일반적인 변수 변경가능
    클래스 생성자 초기화는 init 이라는 메소드가 담당한다.
*/
import android.content.ContentValues
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import org.json.simple.parser.JSONParser
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    //    private var mRecyclerView: RecyclerView? = null
//    private var mAdapter: RecyclerView.Adapter<*>? = null
//    private var mLayoutManager: RecyclerView.LayoutManager? = null
    protected lateinit var myDataset: ArrayList<MainData>


    private val values: ContentValues = ContentValues()
    private lateinit var loadData: LoadData
    private val svUrl: String = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Pass", "pass Complete")
        startServer()

    }

    fun startServer() {
        WeatherFetcher()

        values.put(
            "ServiceKey",
            "ZE4CaBs5RvTvJltKmPqYs86f1g1fIE5iJZ0dWVXBVpaDN%2FNsKO6QS9XqiNzL%2FHtKchT85jkmHW2H5GRq1jIodw%3D%3D"
        )
        values.put("base_date", getBaseDate())
        values.put("base_time", getBaseTime())
        values.put("nx", "60")
        values.put("ny", "127")
        values.put("numOfRows", "300")
        values.put("pageNo", "1")
        values.put("_type", "json")

//        loadData =
        loadData = LoadData(svUrl, values, this)
        loadData.execute()

    }


    class LoadData(svUrl: String, values: ContentValues, activity: MainActivity) :
        AsyncTask<Unit, Unit, String?>() {
        private lateinit var svUrl: String
        private lateinit var values: ContentValues
        private lateinit var activity: MainActivity
        private lateinit var getJSON: GetJSON

        init {
            this.svUrl = svUrl
            this.values = values
            this.activity = activity
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("준비물", svUrl)


        }

        override fun doInBackground(vararg p0: Unit?): String? {
            val result: String? // 요청 결과를 저장할 변수.
            var requestHttpURLConnection: RequestHttpUrlConnection = RequestHttpUrlConnection()
            val requestHttpUrlConnectionGet: RequestHttpUrlConnectionGet = RequestHttpUrlConnectionGet()
//            result = requestHttpURLConnection.request(svUrl, values) // 해당 URL로 부터 결과물을 얻어온다.
            result = requestHttpUrlConnectionGet.request(svUrl, values)
            Log.d("result", result)

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            getJSON = GetJSON(activity)

            getJSON.LoadNameHttpConnection(result.toString())
        }
    }


    class GetJSON(activity: MainActivity) : AsyncTask<Void, Void, String>() {
        private var httpURLConnection: HttpURLConnection? = null
        private var url: URL? = null
        private var activity: MainActivity


        init {
            this.activity = activity

        }

        fun LoadNameHttpConnection(url: String) {
            try {
                this.url = URL(url)
                httpURLConnection = this.url!!.openConnection() as HttpURLConnection
                httpURLConnection!!.requestMethod = "GET"
                httpURLConnection!!.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8"
                )
                execute()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (ie: IOException) {
                ie.printStackTrace()
            }

        }

        override fun doInBackground(vararg voids: Void): String {
            var line: String?
            val data: StringBuilder = StringBuilder()
            var bufferedReader: BufferedReader? = null

            try {
                httpURLConnection!!.connect()

                val inputStream = httpURLConnection!!.inputStream
                bufferedReader = BufferedReader(InputStreamReader(inputStream))

                do {
                    line = bufferedReader.readLine()
                    data.append(line)
                } while (line != null)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            Log.d("SUCCESS", data.toString())

            return data.toString()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val list: ArrayList<MainData> = parseJson(result!!)

            val adapter: MainAdapter = MainAdapter(list)

            activity.main_recycler_view.adapter = adapter



            /**
             * 항목값	항목명	단위
             * POP	강수확률	 %
             * PTY	강수형태	코드값
             * R06	6시간 강수량	범주 (1 mm)
             * REH	습도	 %
             * S06	6시간 신적설	범주(1 cm)
             * SKY	하늘상태	코드값
             * T3H	3시간 기온	 ℃
             * TMN	아침 최저기온	 ℃
             * TMX	낮 최고기온	 ℃
             * UUU	풍속(동서성분)	 m/s
             * VVV	풍속(남북성분)	 m/s
             * WAV	파고	 M
             * VEC	풍향	 m/s
             * WSD	풍속	1

             */

//            activity.tv_json.text =
        }
    }
}





