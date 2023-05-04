package com.couple.sns.common.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    public static Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static Response<Void> success() {
        return new Response<>("SUCCESS", null);
    }

    public static Response<Boolean> success(Boolean liked) {
        return new Response<>(liked.toString(), null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }


}
