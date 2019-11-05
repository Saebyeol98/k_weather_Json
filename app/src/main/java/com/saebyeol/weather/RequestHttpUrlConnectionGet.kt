package com.saebyeol.weather

/*
    값에 null 을 반환하고 싶으면 반환자 타입 선언부에 ?를 붙인다
    일반적인 형변환을 원하면 (타입) 이 아닌 as 타입을 적는다

    POST 방식의 클래스

*/

import android.content.ContentValues
import android.util.Log
import java.io.*

import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection

import java.net.MalformedURLException
import java.net.URL


class RequestHttpUrlConnectionGet {
    fun request(_url: String, _params: ContentValues?): String? {

        var sbParams: StringBuffer = StringBuffer()


        /**
         * 1. StringBuffer에 인자 연결
         * */

        // 전송하고자 할 데이터가 없으면 인자를 비운다
        if (_params == null) {
            Log.d("Fail", _params.toString())
            sbParams.append("")
        } else { // 전송 데이터가 있으면 인자를 채워준다.
            // 인자가 2개 이상이면 인자 연결에 &가 필요하므로 스위칭할 변수 생성.
            Log.d("Success", _params.toString())
            var isAnd: Boolean = false
            // 인자 키, 값
            var key: String?
            var value: String?

            for ((key1, value1) in _params.valueSet()) {
                key = key1
                value = value1.toString()

                Log.d("Test", key + "  " + value)
                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd) {
                    sbParams.append("&")
                }

                sbParams.append(key).append("=").append(value)

                Log.d("Send", sbParams.toString())
                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd) {
                    if (_params.size() >= 2) {
                        isAnd = true
                    }
                }
            }
        }

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try {
            val url: URL = URL(_url + sbParams)

            return url.toString()

            Log.d("TEST GET", url.toString())
            var urlConn = url.openConnection() as HttpURLConnection
            urlConn.requestMethod = "GET"
            urlConn.doOutput = true
            urlConn.doInput = true
            urlConn.useCaches = false
            urlConn.defaultUseCaches = false
//            urlConn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ")
//            urlConn.setRequestProperty("Accept","*/*")

//            val strCookie: String = urlConn.getHeaderField("Set-Cookie")
            if (urlConn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("Conn Fail", urlConn.responseCode.toString())
                return null
            }


            val inputStream: InputStream = urlConn.inputStream
            Log.d("TEST", "PASS")

            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

            // 결과물의 라인과 그 합에 대한 변수
            var line: String?
            var page: String = ""

            do {
                line = reader.readLine()
                page += line
            } while (line != null)

            Log.d("Request", page)
            return page

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }
}