package com.example.provaa1

class Estoque {
    companion object {
        private val listaDeProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            listaDeProdutos.add(produto)
        }

        fun calcularValorTotalEstoque(): Double {
            return listaDeProdutos.sumOf { it.preco * it.quantidade }
        }

        fun calcularQuantidadeTotalProdutos(): Int {
            return listaDeProdutos.sumOf { it.quantidade }
        }

        fun getProdutos(): List<Produto> {
            return listaDeProdutos
        }
    }
}