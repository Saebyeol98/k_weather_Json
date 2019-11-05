package com.saebyeol.weather

fun ctySel(_cty: String, _value: String): String {
    var strCat: String = _cty
    var strValue: String = _value

    when (strCat) {
        "SKY" -> when (strValue) { // 하늘상태
            "1" -> return "맑음"
            "2" -> return "정보를 찾을 수 없습니다."
            "3" -> return "구름많음"
            "4" -> return "흐림"
        }
        "PTY" -> when (strValue) { // 강수형태
            "0" -> return "없음"
            "1" -> return "비"
            "2" -> return "진눈개비"
            "3" -> return "눈"
            "4" -> return "소나기"
        }
    }
    return null!!
}
fun setCast(_category: String): String {
    var strCat: String = _category
    when(strCat){
        "POP" -> return "강수확률"
        "REH" -> return "습도"
        "TMN" -> return "최저기온"
        "TMX" -> return "최고기온"
    }

    return "NoValue"
}
