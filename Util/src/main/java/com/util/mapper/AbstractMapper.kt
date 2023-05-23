package com.util.mapper

interface AbstractMapper<in PARAMETER, out RESULT> {
    fun map(parameter: PARAMETER): RESULT
}