package br.com.alura.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(@JsonAlias("title") String title,
                         @JsonAlias("languages") List<Linguagen> languages,
                         @JsonAlias("download_count") Integer downloadCount,
                         @JsonAlias("authors") List<DadosAutor> authors) {}