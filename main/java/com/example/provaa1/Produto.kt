package com.example.provaa1

class Produto(var nome: String, var categoria:String, var preco: Double, var quantidade: Int)
{
    companion object {
        private val listaDeProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            listaDeProdutos.add(produto)
        }

        fun getProdutos(): List<Produto> {
            return listaDeProdutos
        }
    }
}