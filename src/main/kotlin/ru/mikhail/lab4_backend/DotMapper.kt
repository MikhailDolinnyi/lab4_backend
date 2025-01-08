package ru.mikhail.lab4_backend


import org.mapstruct.Mapper
import ru.mikhail.lab4_backend.dbobjects.Dot
import ru.mikhail.lab4_backend.responses.GetDotsResponse

@Mapper(componentModel = "spring")
interface DotMapper {



    fun toDto(dot: Dot): GetDotsResponse

    fun toEntity(getDotsResponse: GetDotsResponse): Dot
}