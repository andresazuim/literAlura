package br.com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Livro> livros = new ArrayList<>();

    public Autor(DadosAutor dadosAutor) {
        this.name = dadosAutor.nome();
        this.setBirthYear(dadosAutor.anoNascimento());
        this.setDeathYear(dadosAutor.anoFalecimento());
        this.livros = new ArrayList<>();
    }

    public Autor() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        if (birthYear == null) {
            this.birthYear = 0;
        } else {
            this.birthYear = birthYear;
        }
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        if (deathYear == null) {
            this.deathYear = 0;
        } else {
            this.deathYear = deathYear;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Livro> getBooks() {
        return livros;
    }

    public void setBooks(List<Livro> livros) {
        this.livros = livros;
    }
    @Override
    public String toString() {
        return "Nome Autor: " + name + "\n" +
                "Ano Nascimento: " + birthYear + "\n" +
                "Ano Falecimento: " + deathYear + "\n";
    }


}
