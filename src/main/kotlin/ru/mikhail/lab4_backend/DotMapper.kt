package ru.mikhail.lab4_backend


import org.mapstruct.Mapper
import ru.mikhail.lab4_backend.dbobjects.Dot
import ru.mikhail.lab4_backend.dto.ResponseData

@Mapper(componentModel = "spring")
interface DotMapper {



    fun toDto(dot: Dot): ResponseData

    fun toEntity(responseData: ResponseData): Dot
}