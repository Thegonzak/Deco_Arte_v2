package com.ksaucedo.deco_arte_v2.model

import com.google.firebase.Timestamp

data class Novedad(
    val id: String = "",
    val texto: String,
    val activo: Boolean,
    val fechaInicio: Timestamp,
    val fechaFin: Timestamp,
)
