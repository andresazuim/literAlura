package br.com.alura.literAlura.service;

import br.com.alura.literAlura.model.*;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetLivro {
    ConexaoApi conexaoApi = new ConexaoApi();
    ConversorDados converter = new ConversorDados();

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    public GetLivro(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }


    @Transactional
    public void getDataBook(String livro) {
        String json = conexaoApi.getData(livro);
        Data data = converter.getData(json, Data.class);
        try {
            DadosLivro dadosLivro = data.livros().get(0);

            Livro livros = new Livro(dadosLivro);
            if (livroRepository.findByTitle(livros.getTitle()) != null) {
                System.out.println("Livro já pesquisado, recuperando dados do Banco de dados:"+ "\n");
                System.out.println(livroRepository.findByTitle(livros.getTitle()).toString());
            } else {
                List<Autor> authors = new ArrayList<>();
                for (DadosAutor authordata : dadosLivro.authors()) {
                    Autor author = new Autor(authordata);
                    List<Autor> existingAuthors = autorRepository.findByName(author.getName());
                    if (existingAuthors.isEmpty()) {
                        author = autorRepository.save(author); // Save the new author to the database
                    } else {
                        author = existingAuthors.get(0); // Use the existing author from the database
                    }
                    authors.add(author);
                }
                livros.setAuthors(authors);
                livroRepository.save(livros); // Save the book with the list of authors
                System.out.println(livros);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Livro não encontrado"+ "\n");

        }
    }


    public void getTop(String url) {
        String json = conexaoApi.getData(url);
        Data data = converter.getData(json, Data.class);
        for (int i = 0; i < 20; i++) {
            try {
                DadosLivro dadosLivro = data.livros().get(i);
                Livro livro = new Livro(dadosLivro);
                if (livroRepository.findByTitle(livro.getTitle()) != null) {
                    System.out.println(livroRepository.findByTitle(livro.getTitle()).toString());
                } else {
                    List<Autor> authors = new ArrayList<>();
                    for (DadosAutor authordata : dadosLivro.authors()) {
                        Autor author = new Autor(authordata);
                        List<Autor> existingAuthors = autorRepository.findByName(author.getName());
                        if (existingAuthors.isEmpty()) {
                            author = autorRepository.save(author); // Save the new author to the database
                        } else {
                            author = existingAuthors.get(0); // Use the existing author from the database
                        }
                        authors.add(author);
                    }
                    livro.setAuthors(authors);
                    livroRepository.save(livro); // Save the book with the list of authors
                    System.out.println(livro);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Livro não encontrado" + "\n");

            }
        }
    }

    public void getAuthor(String search) {
        String json = conexaoApi.getData("https://gutendex.com/books/?search=" + search);
        Data data = converter.getData(json, Data.class);
        try {
            DadosLivro dadosLivro = data.livros().get(0);
            Livro livro = new Livro(dadosLivro);
            String normalizedSearch = search.toLowerCase();
            // busca no banco
            List<Autor> matchingAuthors = autorRepository.findByNameContainingIgnoreCase(normalizedSearch);
            if (!matchingAuthors.isEmpty()) {
                System.out.println("Encontrado no banco de dados: " +"\n"+ matchingAuthors.get(0));
            } else {
                // se não encontrou no db o autor, busca pelo livro, isso depende da input do usuário na busca
                System.out.println("Autor não encontrado no banco de dados.");
                Livro existingLivro = livroRepository.findByTitle(livro.getTitle());
                if (existingLivro != null) {
                    List<Autor> existingAuthors = existingLivro.getAuthor().stream().filter(a -> a.getName().contains(normalizedSearch)).collect(Collectors.toList());
                    System.out.println(existingAuthors);
//                    System.out.println("Possível livro do autor encontrado: " + existingAuthors);
                    for (Autor author : existingAuthors) {
                        String normalizedAuthorName = author.getName().toLowerCase().trim();
                        if (normalizedAuthorName.contains(normalizedSearch)) {
                            System.out.println("Found author in existing book: " + author);
                            return;
                        }
                    }
                } else {
                    //se não achar MESMO no banco de dados, aí busca um livro do autor
                    System.out.println("Autor não encontrado no banco de dados.");
                    // busca um livro do autor e insere no banco de dados.
                    List<Autor> authors = new ArrayList<>();
                    for (DadosAutor authordata : dadosLivro.authors()) {
                        Autor author = new Autor(authordata);
                        List<Autor> existingAuthorsList = autorRepository.findByName(author.getName());
                        if (existingAuthorsList.isEmpty()) {
                            author = autorRepository.save(author); // Save the new author to the database
                        } else {
                            author = existingAuthorsList.get(0); // Use the existing author from the database
                        }
                        authors.add(author);
                    }
                    livro.setAuthors(authors);
                    livroRepository.save(livro); // Save the book with the list of authors
                    System.out.println("Novo livro inserido no banco de dados: \n" + livro);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Autor não encontrado.\n");
        }
    }
}