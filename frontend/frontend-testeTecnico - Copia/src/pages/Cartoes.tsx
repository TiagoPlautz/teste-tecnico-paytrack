import { useEffect, useState } from "react";

import ListaCartoes from "../components/ListaCartoes";
import ModalAdicionarCartao from "../components/ModalAdicionarCartao";
import ModalConsultarEndereco from "../components/ModalConsultarEndereco";
// import ModalDetalhesCartao from "../components/ModalDetalhesCartao";

import { listarCartoes /*buscarCartaoPorId*/ } from "../services/cartaoService";
import consultaEndereco from "../services/enderecoService"

import type { Cartao /*DetalhesCartao*/ } from "../types/Cartao";
import type { ObjetoCep } from "../types/Cep";

function Cartoes() {
  const [listaCartoes, setListaCartoes] = useState<Cartao[]>([]);
  const [modalCadastroAberto, setModalCadastroAberto] = useState(false);
  const [modalConsultaEnderecoAberto, setModalConsultaEnderecoAberto] = useState(false);
  const [enderecoConsultado, setEnderecoConsultado] = useState<ObjetoCep | null>(null);
  // const [cartaoSelecionado, setCartaoSelecionado] = useState<DetalhesCartao | null>(null);
  // const [modalDetalhesAberto, setModalDetalhesAberto] = useState(false);

  useEffect(() => {
    buscarCartoes();
  }, []);

  async function buscarCartoes() {
    try {
      const response = await listarCartoes();

      setListaCartoes(response);
    } catch (error) {
      console.error("Erro ao listar cartões", error);
    }
  }

  async function consultaCep(cep: string) {
    try {
      const response = await consultaEndereco(cep);

      setEnderecoConsultado(response);
    } catch (error) {
      console.error("Erro ao consultar CEP", error);
    }
  }

  // async function abrirDetalhes(id: number) {
  //   try {
  //     const detalhes = await buscarCartaoPorId(id);

  //     setCartaoSelecionado(detalhes);
  //     setModalDetalhesAberto(true);

  //   } catch (error) {
  //     console.error("Erro ao buscar detalhes", error);
  //   }
  // }

  // function fecharDetalhes() {
  //   setModalDetalhesAberto(false);
  //   setCartaoSelecionado(null);
  // }

  return (
    <div className="p-6">

      <div className="flex justify-between mb-6">

        <h1 className="text-3xl font-bold">
          Cartões
        </h1>

        <button
          onClick={() => setModalConsultaEnderecoAberto(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer"
        >
          Consultar Endereço
        </button>

        <button
          onClick={() => setModalCadastroAberto(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded cursor-pointer"
        >
          Cadastrar cartão
        </button>

      </div>

      <ListaCartoes
        listaCartoes={listaCartoes}
      /*onDetalhes={abrirDetalhes}*/
      />

      {modalCadastroAberto && (
        <ModalAdicionarCartao
          onClose={() => setModalCadastroAberto(false)}
          onCartaoAdicionado={buscarCartoes}
        />
      )}

      {modalConsultaEnderecoAberto && (
        <ModalConsultarEndereco
          onClose={() => setModalConsultaEnderecoAberto(false)}
          onEnderecoConsultado={consultaCep}
          endereco={enderecoConsultado}
        />
      )}

      {/* {modalDetalhesAberto && cartaoSelecionado && (
        <ModalDetalhesCartao
          cartao={cartaoSelecionado}
          onClose={fecharDetalhes}
        />
      )} */}

    </div>
  );
}

export default Cartoes;