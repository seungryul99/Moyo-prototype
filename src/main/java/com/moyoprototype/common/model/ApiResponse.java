package com.moyoprototype.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder ({"code", "data"})
public class ApiResponse <T>{

    private final int code;

    private final T data;

    public static <E> ApiResponse<E> success(E data){

        return new ApiResponse<>(200, data);
    }

    public static <E> ApiResponse<E> noContent(){

        return new ApiResponse<>(204,null);
    }

}
