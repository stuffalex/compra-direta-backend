package br.com.desafio.compradireta.domain.entity.entities;

import br.com.desafio.compradireta.domain.constant.TipoFornecedor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fornecedor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_fornecedor", nullable = false)
    private TipoFornecedor tipoFornecedor;

    @Column(nullable = false)
    private String email;

    private String telefone;

    @Column(nullable = false)
    private boolean ativo;
}
