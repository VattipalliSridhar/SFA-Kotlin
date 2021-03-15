package com.apps.sfaapp.model

data class ScannerModel(
    val ScannerCode: String,
    val bin_id: String,
    val bin_location: String,
    val cat_type: String,
    val message: String,
    val status: Int,
    val supervisorMobile: String,
    val supervisorName: String,
    val circelName: String,
    val wardName: String
)