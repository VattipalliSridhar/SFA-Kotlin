package com.apps.sfaapp.model

data class DashBoardModel(
    val BinsAssigned: String,
    val BinsCleaned: String,
    val Binspercent: String,
    val ToiletsAssigned: String,
    val ToiletsCleaned: String,
    val Toiletspercent: String,
    val jawan_mobile: String,
    val jawan_name: String,
    val message: String,
    val status: Int
)