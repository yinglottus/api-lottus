package api.loja.lotus.specs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import api.loja.lotus.dtos.produto.ProdutoFilterDTO;
import api.loja.lotus.models.Produto;
import jakarta.persistence.criteria.Predicate;

public class ProdutoSpecification {

    public static Specification<Produto> filterAtivos(ProdutoFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                cb.isTrue(root.get("ativo"))
            );

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("nome")),
                        "%" + filter.nome().toLowerCase() + "%"
                    )
                );
            };

            if (filter.categoria() != null) {
                predicates.add(
                    cb.equal(root.get("categoria"),
                        filter.categoria()
                    )
                );
            };

            if (filter.precoMinimo() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(root.get("preco"),
                        filter.precoMinimo()
                    )
                );
            };

            if (filter.precoMaximo() != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(root.get("preco"),
                        filter.precoMaximo()
                    )
                );
            };

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Produto> filterAllAdmin(ProdutoFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("nome")),
                        "%" + filter.nome().toLowerCase() + "%"
                    )
                );
            };

            if (filter.categoria() != null) {
                predicates.add(
                    cb.equal(root.get("categoria"),
                        filter.categoria()
                    )
                );
            };

            if (filter.precoMinimo() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(root.get("preco"),
                        filter.precoMinimo()
                    )
                );
            };

            if (filter.precoMaximo() != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(root.get("preco"),
                        filter.precoMaximo()
                    )
                );
            };

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
}
