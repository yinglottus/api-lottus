package api.loja.lotus.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import api.loja.lotus.dtos.cupom.CupomRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
@Entity 
@Table(name = "cupons")
public class Cupom {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal desconto;

    @Column(nullable = false)
    private Integer quantidade;

    @Builder.Default
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "cupom"
    )
    private List<Carrinho> carrinho = new ArrayList<>();
        
    public static Cupom criarCupom(CupomRequestDTO dto) {
        Cupom cupom = new Cupom();
        cupom.setCodigo(dto.codigo());
        cupom.setDesconto(dto.desconto());
        cupom.setQuantidade(dto.quantidade());

        return cupom;
    }

    public Cupom atualizarCupom(CupomRequestDTO dto) {

        this.setCodigo(dto.codigo());
        this.setDesconto(dto.desconto());
        this.setQuantidade(dto.quantidade());

        return this;
    }

}
