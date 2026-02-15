package com.egon.spring_ai_ollama.dtos;

public record CountryInfo(
    String capital,
    String continent,
    double size,
    int population,
    String language,
    String currency,
    double summerAverageTemperature,
    double winterAverageTemperature
) {}
