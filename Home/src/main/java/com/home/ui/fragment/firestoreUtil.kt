package com.home.ui.fragment

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class firestoreUtil {

    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("suaColecao")

    fun obterMidiasPaginadas(limite: Long, ultimaMidia: DocumentSnapshot?) {
        var query = collectionRef.orderBy("dataCriacao") // Substitua "dataCriacao" pelo nome do campo que você usa para ordenar as mídias

        // Se tiver uma referência para a última mídia carregada, use como ponto de partida para a próxima página
        if (ultimaMidia != null) {
            query = query.startAfter(ultimaMidia)
        }

        query.limit(limite).get()
            .addOnSuccessListener { querySnapshot ->
                // Verifique se há algum documento retornado
                if (!querySnapshot.isEmpty) {
                    val ultima = querySnapshot.documents[querySnapshot.size() - 1]
                    // Processar os documentos retornados
                    for (document in querySnapshot) {
                        // Aqui você pode extrair os dados da mídia e fazer o que desejar
                        val url = document.getString("url")
                        // Faça o que você precisa com a URL da mídia

                        // Por exemplo, você pode adicioná-la a uma lista para uso posterior
                        // minhaListaDeMidias.add(url)
                    }

                    // Chame a função novamente para carregar a próxima página
                    obterMidiasPaginadas(limite, ultima)
                }
            }
            .addOnFailureListener { exception ->
                // Trate as falhas na recuperação dos dados
                // ...
            }
    }

}