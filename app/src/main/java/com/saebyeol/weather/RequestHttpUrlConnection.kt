package com.saebyeol.weather

/*
    값에 null 을 반환하고 싶으면 반환자 타입 선언부에 ?를 붙인다
    일반적인 형변환을 원하면 (타입) 이 아닌 as 타입을 적는다

    POST 방식의 클래스

*/

import android.content.ContentValues
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.Exception
import java.net.HttpURLConnection

import java.net.MalformedURLException
import java.net.URL


class RequestHttpUrlConnection {
    fun request(_url: String, _params: ContentValues): String? {
        val url: URL = URL(_url)
        var urlConn = url.openConnection() as HttpURLConnection
        var sbParams: StringBuffer = StringBuffer()


        /**
         * 1. StringBuffer에 인자 연결
         * */

        // 전송하고자 할 데이터가 없으면 인자를 비운다
        if (_params == null) {
            Log.d("Fail", _params)
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

                Log.d("Test", key+"  "+value)
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
            // 2-1. urlConn 설정
            urlConn.requestMethod = "POST"
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            // 2-2. 인자 전달 및 데이터 읽어오기.
            val strParams: String = sbParams.toString() //sbParams에 정리한 인자들을 스트링으로 저장.

            Log.d("sendMessage", strParams)

            val out: OutputStream = urlConn.outputStream
            out.write(strParams.toByteArray(charset("UTF-8"))) // 출력 스트림에 출력.
            out.flush() // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            out.close() // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // 2-3. 연결 요청 확인.
            // 실패시 null 값을 반환하고 종료함
            if (urlConn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("Conn Fail", urlConn.responseCode.toString())
                return null
            }

            // 2-4. 읽어온 결과물 리턴.
            // 요청한 url 의 결과물을 BufferedReader 로 받는다.
            val reader: BufferedReader = BufferedReader(InputStreamReader(urlConn.inputStream, "UTF-8"))

            Log.d("Pass1", reader.toString())

            // 결과물의 라인과 그 합에 대한 변수
            var line: String
            var page: String = ""

            do {
                line = reader.readLine()
                page += line
            } while (line != null)

            Log.d("Request", page)
            return page

        } catch (e: MalformedURLException) { // for URL.
            e.printStackTrace()
        } catch (e: IOException) { // for openConnection().
            e.printStackTrace()
        } finally {
            urlConn.disconnect()
        }
        return null
    }
}