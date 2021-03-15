package com.apps.sfaapp.model

data class ScannerModel(
        val ScannerCode: String,
        val bin_id: String,
        val bin_location: String,
        val cat_type: String,
        val circelName: String,
        val circle_id: String,
        val message: String,
        val roadName: String,
        val road_id: String,
        val status: Int,
        val supervisorMobile: String,
        val supervisorName: String,
        val wardName: String,
        val ward_id: String,
        val zone_id: String
)