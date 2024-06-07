package br.com.alura.literAlura.service;

public interface IConverteDados {
    <T> T getData(String json, Class<T> classe);
}