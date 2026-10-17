package br.com.desafio.compradireta.domain.usecase.compradireta.atualizar.converter;

import java.util.List;
import java.util.stream.Collectors;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.ItemCompraDireta;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.ItemCompraDiretaInput;

public final class ItemCompraDiretaConverter {

    private ItemCompraDiretaConverter() {
    }

    public static List<ItemCompraDireta> converter(List<ItemCompraDiretaInput> itensInput, CompraDireta compraDireta) {
        return itensInput.stream()
                .map(item -> ItemCompraDireta.builder()
                        .compraDireta(compraDireta)
                        .descricao(item.getDescricao())
                        .unidadeMedida(item.getUnidadeMedida())
                        .quantidade(item.getQuantidade())
                        .valorUnitarioEstimado(item.getValorUnitarioEstimado())
                        .build())
                .collect(Collectors.toList());
    }
}
