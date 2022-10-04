package com.ksaucedo.deco_arte_v2.model

data class Usuario(
    var cedula: String,
    var nombre: String,
    var apellido: String,
    var ciudad: String,
    var direccion: String,
    var fechaNac: String,
    var telefono: String
)
