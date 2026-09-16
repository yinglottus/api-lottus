package api.loja.lotus.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import api.loja.lotus.models.enums.Categoria;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder 
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria = Categoria.Geral;

    @Column(nullable = false)
    private BigDecimal preco;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @Builder.Default
    @BatchSize(size = 20)
    @OneToMany(
            mappedBy = "produto",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProdutoImagem> imagens = new ArrayList<>();

}
