package com.jwt.util;

public class ExceptionHelper {
    public static RuntimeException throwResourceNotFoundException(){
        return new RuntimeException("User not found !!");
    }
}