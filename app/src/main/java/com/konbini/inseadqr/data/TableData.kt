package com.konbini.inseadqr.data

data class TableData(
    var id: String,
    var label: String,
    var isSelected: Boolean = false,
    var status: String? = "inactive"
)
