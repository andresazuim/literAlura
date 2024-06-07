package br.com.alura.literAlura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Autor> authors;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private List<Linguagen> languages;
    private Integer downloadCount;

    public Livro(DadosLivro dadosLivro) {
        this.title = dadosLivro.title();
        this.languages = dadosLivro.languages();
        this.downloadCount = dadosLivro.downloadCount();
    }

    public Livro() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Autor> getAuthor() {
        return authors;
    }


    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }


    public void addAuthor(Autor autor) {
        this.authors.add(autor);
        if (!autor.getBooks().contains(this)) {
            autor.getBooks().add(this);
        }
    }

    public void setAuthors(List<Autor> authors) {
        this.authors = authors;
        for (Autor author : authors) {
            if (!author.getBooks().contains(this)) {
                author.getBooks().add(this);
            }
        }
    }

    public String getLanguages() {
        if (languages == null || languages.isEmpty()) {
            return "";
        } else {
            return String.valueOf(languages.get(0));
        }
    }

    @Override
    public String toString() {
        return "Título: " + title + '\n' +
                "Autor(es): " + "\n"  + authors.stream().map(author -> author.toString()).collect(Collectors.joining("\n")) + '\n' +
                "Idioma(s): " + languages.stream().map(Linguagen::getIdioma).collect(Collectors.joining(", ")) + '\n' +
                "Downloads: " + downloadCount+ "\n";
    }
}