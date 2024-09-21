package com.example.provaa1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayoutMain()
        }
    }
}

@Composable
fun TelaCadastro(navController: NavController)
{
    var nome by remember{ mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth(),
            isError = nome.isEmpty()
        )
        if (preco.isEmpty()) {
            Text("Campo obrigatório")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth(),
            isError = categoria.isEmpty()
        )
        if (categoria.isEmpty()) {
            Text("Campo obrigatório")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth(),
            isError = preco.isEmpty(),
        )
        if (preco.isEmpty()) {
            Text("Campo obrigatório")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade em Estoque") },
            modifier = Modifier.fillMaxWidth(),
            isError = quantidade.isEmpty(),
        )

        Button(onClick = {
            val precoDouble = preco.toDoubleOrNull() ?: -1.0
            val quantidadeInt = quantidade.toIntOrNull() ?: -1

            if(precoDouble >= 0 && quantidadeInt >0)
            {
                if (nome.isNotEmpty() && categoria.isNotEmpty() && preco.isNotEmpty() && quantidade.isNotEmpty()) {
                    val produto = Produto(nome, categoria, precoDouble, quantidadeInt)
                    Estoque.adicionarProduto(produto)

                    nome = ""
                    categoria = ""
                    preco = ""
                    quantidade = ""

                    navController.navigate("telaProdutosCadastrados")

                    Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Preencha todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(context, "Preencha todos os campos corretamente! Preço deve ser maior ou igual a 0 e quantidade maior que 0.", Toast.LENGTH_SHORT).show()
            }

        }) {
            Text(text = "Cadastrar")
        }

    }
}

@Composable
fun TelaListaProdutos(navController: NavController)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Lista de produtos cadastrados")

        val listaProdutos = Estoque.getProdutos()

        Spacer(modifier = Modifier.height(20.dp))

        if (listaProdutos.isEmpty()) {
            Text(text = "Nenhum produto cadastrado")
        } else {
            LazyColumn {
                items(listaProdutos) { produto ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Produto: ${produto.nome} (${produto.quantidade})")

                        Button(onClick = {
                            val produtoJson = Gson().toJson(produto)
                            navController.navigate("detalhesProduto/$produtoJson")
                        }) {
                            Text(text = "Ver detalhes")
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate("telaEstatisticas")
        }) {
            Text(text = "Estatísticas")
        }
    }
}

@Composable
fun LayoutNav(){
    val navController = rememberNavController()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NavHost(navController = navController, startDestination = "telaInicial")
        {
            composable("telaInicial") { TelaCadastro(navController) }
            composable("telaProdutosCadastrados") { TelaListaProdutos(navController) }
            composable("detalhesProduto/{produtoJson}")
            {
                backStackEntry ->
                val produtoJSON = backStackEntry.arguments?.getString("produtoJson")
                val produto = Gson().fromJson(produtoJSON, Produto::class.java)

                DetalhesProduto(produto,navController)
            }
            composable("telaEstatisticas") { TelaEstatisticas(navController) }
        }
    }
}

@Composable
fun DetalhesProduto(produto: Produto, navController: NavController)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Detalhes do produto", fontSize = 20.sp)
        Text(text = "Nome do produto: ${produto.nome}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Categoria: ${produto.categoria}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Preço: ${produto.preco}")
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Quantidade em estoque:  ${produto.quantidade}")
        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "Voltar")
        }
    }
}

@Composable
fun TelaEstatisticas(navController: NavController) {
    val valorTotalEstoque = Estoque.calcularValorTotalEstoque()
    val quantidadeTotalProdutos = Estoque.calcularQuantidadeTotalProdutos()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Estatísticas do Estoque", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Valor Total do Estoque: R$ $valorTotalEstoque")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Quantidade Total de Produtos: $quantidadeTotalProdutos unidades")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "Voltar")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LayoutMain()
{
    LayoutNav()
}